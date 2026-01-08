/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.monitoring.core.interceptors;

import io.inugami.framework.api.exceptions.WarningContext;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.monitoring.RequestContext;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
import io.inugami.framework.interfaces.models.tools.Chrono;
import io.inugami.framework.interfaces.monitoring.ErrorResult;
import io.inugami.framework.interfaces.monitoring.FilterInterceptorCachePurgeStrategy;
import io.inugami.framework.interfaces.monitoring.Interceptable;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodDTO;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.data.ResponseData;
import io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.models.CurrentApplicationDTO;
import io.inugami.framework.interfaces.spi.SpiLoaderServiceSPI;
import io.inugami.monitoring.api.obfuscators.ObfuscatorTools;
import io.inugami.monitoring.core.interceptors.internal.FilterInterceptorContext;
import io.inugami.monitoring.core.interceptors.internal.FilterInterceptorErrorsUtils;
import io.inugami.monitoring.core.interceptors.internal.FilterInterceptorIOUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;
import static io.inugami.monitoring.core.interceptors.internal.FilterInterceptorErrorsUtils.defineStatusAndDuration;
import static io.inugami.monitoring.core.interceptors.internal.FilterInterceptorErrorsUtils.resolveError;
import static io.inugami.monitoring.core.interceptors.internal.FilterInterceptorIOUtils.convertToResponseData;
import static io.inugami.monitoring.core.interceptors.internal.FilterInterceptorTrackingMdcUtils.*;


/**
 * FilterInterceptor
 *
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@SuppressWarnings({"java:S112", "java:S1181", "java:S108", "java:S2589"})
@Slf4j
@RequiredArgsConstructor
@WebFilter(urlPatterns = "*", asyncSupported = true)
public class FilterInterceptor implements Filter, ApplicationLifecycleSPI {


    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String DEFAULT_ERROR_CODE = FilterInterceptorErrorsUtils.DEFAULT_ERROR_CODE;

    protected static final AtomicReference<FilterInterceptorContext> FILTER_INTERCEPTOR_CONTEXT =
            new AtomicReference<>();
    public static final    String                                    EMPTY                      = "";

    private final        SpiLoaderServiceSPI           spiLoaderServiceSPI;
    private final        CurrentApplicationDTO         currentApplication;
    private              ConfigHandler<String, String> configuration;
    private static final Map<String, Boolean>          INTERCEPTABLE_URI_RESOLVED = new ConcurrentHashMap<>();


    // =================================================================================================================
    // LIFECYCLE
    // =================================================================================================================

    @Override
    public void onApplicationStarted(final Object event) {
        log.info("initialize FilterInterceptor");
        initAttributes();
    }

    @Override
    public void destroy() {
        // nothing to do
    }

    public void initAttributes() {
        if (FILTER_INTERCEPTOR_CONTEXT.get() == null) {
            FILTER_INTERCEPTOR_CONTEXT.set(FilterInterceptorContext.builder()
                                                                   .spiLoaderServiceSPI(spiLoaderServiceSPI)
                                                                   .initSpi()
                                                                   .build());
        }
    }


    // =================================================================================================================
    // DO FILTER
    // =================================================================================================================
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        MdcService.getInstance().clear();
        initMDCCurrentApplication();
        WarningContext.getInstance().clear();

        if (FILTER_INTERCEPTOR_CONTEXT.get() == null) {
            initAttributes();
        }
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final RequestData        requestData = RequestInformationInitializer.buildRequestInformation(httpRequest);

        if (mustIntercept(requestData)) {
            try {
                processIntercepting(request, (HttpServletResponse) response, chain, httpRequest, requestData);
            } catch (final Exception e) {
                throw new IOException(e.getMessage(), e);
            } finally {
                MdcService.getInstance().clear();
            }
        } else {
            try {
                RequestContext.setInstance(requestData);
                chain.doFilter(request, response);
            } finally {
                MdcService.getInstance().clear();
            }
        }
    }

    private void initMDCCurrentApplication() {
        if (currentApplication == null) {
            return;
        }
        MdcService.getInstance()
                  .groupId(currentApplication.getGroupId())
                  .artifactId(currentApplication.getArtifactId())
                  .version(currentApplication.getVersion())
                  .commitId(currentApplication.getCommitId())
                  .commitDate(currentApplication.getCommitDate());
    }


    // =================================================================================================================
    // INTERCEPT
    // =================================================================================================================
    protected void processIntercepting(final ServletRequest request,
                                       final HttpServletResponse response,
                                       final FilterChain chain,
                                       final HttpServletRequest httpRequest,
                                       final RequestData requestData) throws Exception {
        final var ctx     = FILTER_INTERCEPTOR_CONTEXT.get();
        byte[]    data    = null;
        String    content = null;
        try {
            data    = FilterInterceptorIOUtils.readInput(httpRequest.getInputStream());
            content = data == null ? null : ObfuscatorTools.applyObfuscators(new String(data));
        } catch (final IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.METRICS.error(e.getMessage());
        }
        requestData.setContent(content);

        initCorrelationIdAndTraceId(requestData, request);
        final JavaRestMethodDTO javaRestMethod = resolveJavaRestMethod(request, ctx.getJavaRestMethodResolvers());
        addTrackingInformation(response, javaRestMethod, ctx.getJavaRestMethodTrackers());

        Exception                error           = null;
        final var                headers         = extractHeaders(httpRequest);
        final ResponseWrapper    responseWrapper = new ResponseWrapper(response, headers, ctx.getResponseListeners());
        final HttpServletRequest currentRequest  = buildRequestProxy((HttpServletRequest) request, data);
        requestData.setRequest(currentRequest);
        requestData.setResponse(responseWrapper);
        requestData.setService(resolveServiceName(javaRestMethod));
        RequestContext.setInstance(requestData);

        onBegin(currentRequest, requestData);
        final Chrono chrono = Chrono.startChrono();
        try {
            chain.doFilter(currentRequest, responseWrapper);
        } catch (final Exception e) {
            error = e;
            throw e;
        } finally {
            chrono.stop();
            final ErrorResult errorResult = resolveError(error, responseWrapper, ctx.getExceptionResolvers());
            onEnd(currentRequest, responseWrapper, errorResult, chrono.getDuration(), requestData);
        }
    }


    // =================================================================================================================
    // LIFECYCLE
    // =================================================================================================================
    protected void onBegin(final HttpServletRequest httpRequest,
                           final RequestData requestData) {

        onBeginInitMdcFields(requestData, httpRequest);

        for (final MonitoringFilterInterceptor interceptor : FILTER_INTERCEPTOR_CONTEXT.get()
                                                                                       .getMonitoringFilterInterceptors()) {
            try {
                interceptor.onBegin(requestData);
            } catch (final Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    protected void onEnd(final HttpServletRequest httpRequest,
                         final ResponseWrapper httpResponse,
                         final ErrorResult error,
                         final long duration,
                         final RequestData requestData) {
        RequestContext.getInstance();
        onEndInitMdcFields(error, duration, httpResponse);
        defineStatusAndDuration(httpResponse, duration);
        final ResponseData responseData = convertToResponseData(httpRequest, httpResponse, duration);

        for (final MonitoringFilterInterceptor interceptor : FILTER_INTERCEPTOR_CONTEXT.get()
                                                                                       .getMonitoringFilterInterceptors()) {
            try {
                interceptor.onDone(requestData, responseData, error);
            } catch (final Throwable e) {
                if (log.isDebugEnabled()) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }


    protected HttpServletRequest buildRequestProxy(final HttpServletRequest request, final byte[] content) {
        final Class<?>[] types = {ServletRequest.class, HttpServletRequest.class};
        return (HttpServletRequest) Proxy.newProxyInstance(this.getClass()
                                                               .getClassLoader(), types, new RequestCallBackInterceptor(request, content));
    }


    protected boolean mustIntercept(final RequestData requestData) {
        Boolean result = INTERCEPTABLE_URI_RESOLVED.get(Optional.ofNullable(requestData)
                                                                .map(RequestData::getUri)
                                                                .orElse(EMPTY));
        if (result == null) {
            for (final Interceptable resolver : FILTER_INTERCEPTOR_CONTEXT.get().getInterceptableResolvers()) {
                result = resolver.isInterceptable(requestData);
                if (!result) {
                    break;
                }
            }
            applyIfNotNull(result, r -> INTERCEPTABLE_URI_RESOLVED.put(requestData.getUri(), r));
        }
        purgeCacheIfRequired();

        return result;
    }


    // =================================================================================================================
    // PURGE CACHE
    // =================================================================================================================
    protected void purgeCacheIfRequired() {
        for (FilterInterceptorCachePurgeStrategy cachePurgeStrategy : FILTER_INTERCEPTOR_CONTEXT.get()
                                                                                                .getCachePurgeStrategies()) {
            if (cachePurgeStrategy.shouldPurge(INTERCEPTABLE_URI_RESOLVED)) {
                INTERCEPTABLE_URI_RESOLVED.clear();
                break;
            }
        }
    }

}
