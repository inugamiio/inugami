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

import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.monitoring.RequestContext;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
import io.inugami.framework.interfaces.models.tools.Chrono;
import io.inugami.framework.interfaces.monitoring.*;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.data.ResponseData;
import io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.logger.MDCKeys;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.inugami.framework.interfaces.spi.SpiLoader;
import io.inugami.framework.interfaces.tools.CalendarTools;
import io.inugami.monitoring.api.exceptions.ExceptionResolver;
import io.inugami.monitoring.api.obfuscators.ObfuscatorTools;
import io.inugami.monitoring.api.resolvers.Interceptable;
import io.inugami.monitoring.core.context.MonitoringBootstrapService;
import io.inugami.monitoring.core.interceptors.mdc.DefaultMdcCleaner;
import io.inugami.monitoring.core.interceptors.mdc.MdcCleaner;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;


/**
 * FilterInterceptor
 *
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@SuppressWarnings({"java:S112", "java:S1181", "java:S108"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@WebFilter(urlPatterns = "*", asyncSupported = true)
public class FilterInterceptor implements Filter, ApplicationLifecycleSPI {

    public static final String DEFAULT_ERROR_CODE = "ERR-0000";
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    private              List<JavaRestMethodResolver>        javaRestMethodResolvers      = null;
    private              List<JavaRestMethodTracker>         javaRestMethodTrackers       = null;
    private              List<Interceptable>                 interceptableResolver        = null;
    private              FilterInterceptorCachePurgeStrategy purgeCacheStrategy           = null;
    private              MdcCleaner                          mdcCleaner                   = null;
    private              List<ExceptionResolver>             exceptionResolver            = null;
    private              List<ResponseListener>              responseListeners            = null;
    private              List<MonitoringFilterInterceptor>   monitoringFilterInterceptors = new ArrayList<>();
    private              ConfigHandler<String, String>       configuration;
    private static final Map<String, Boolean>                INTERCEPTABLE_URI_RESOLVED   = new ConcurrentHashMap<>();
    private static final int                                 KILO                         = 1024;

    // =================================================================================================================
    // LIFECYCLE
    // =================================================================================================================
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        initAttributes();
    }

    @Override
    public void destroy() {
        // nothing to do
    }


    @Override
    public void onConfigurationReady(final ConfigHandler<String, String> configuration) {
        initAttributes();
    }

    public void initAttributes() {
        javaRestMethodResolvers = SpiLoader.getInstance().loadSpiServicesByPriority(JavaRestMethodResolver.class);
        javaRestMethodTrackers = SpiLoader.getInstance().loadSpiServicesByPriority(JavaRestMethodTracker.class);
        interceptableResolver = SpiLoader.getInstance().loadSpiServicesByPriority(Interceptable.class);
        exceptionResolver = SpiLoader.getInstance()
                                     .loadSpiServicesByPriority(ExceptionResolver.class, new FilterInterceptorErrorResolver());
        purgeCacheStrategy = SpiLoader.getInstance()
                                      .loadSpiServiceByPriority(FilterInterceptorCachePurgeStrategy.class, new DefaultFilterInterceptorCachePurgeStrategy());
        mdcCleaner = new MdcCleaner(SpiLoader.getInstance()
                                             .loadSpiServicesWithDefault(MdcCleanerSPI.class, new DefaultMdcCleaner()));
        responseListeners = SpiLoader.getInstance().loadSpiServicesByPriority(ResponseListener.class);
        monitoringFilterInterceptors = new ArrayList<>();
        for (final MonitoringFilterInterceptor interceptor : MonitoringBootstrapService.getContext()
                                                                                       .getInterceptors()) {
            monitoringFilterInterceptors.add(interceptor);
        }

        final List<MonitoringFilterInterceptor> monitoringFilterInterceptorsSpi = SpiLoader.getInstance()
                                                                                           .loadSpiServicesByPriority(MonitoringFilterInterceptor.class);
        for (final MonitoringFilterInterceptor interceptor : monitoringFilterInterceptorsSpi) {
            if (isNotContains(interceptor, MonitoringBootstrapService.getContext().getInterceptors())) {
                monitoringFilterInterceptors.add(interceptor);
            }
        }
    }

    private boolean isNotContains(final MonitoringFilterInterceptor interceptor,
                                  final List<MonitoringFilterInterceptor> monitoringFilterInterceptors) {
        final boolean result = true;
        if (monitoringFilterInterceptors == null) {
            return result;
        }
        for (final MonitoringFilterInterceptor monitoringInterceptor : monitoringFilterInterceptors) {
            if (interceptor.getClass() == monitoringInterceptor.getClass()) {
                return false;
            }
        }
        return result;
    }

    // =================================================================================================================
    // DO FILTER
    // =================================================================================================================
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        MdcService.getInstance().clear();

        if (isAttributesNotInitialized()) {
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
                chain.doFilter(request, response);
            } finally {
                MdcService.getInstance().clear();
            }
        }
    }


    // =================================================================================================================
    // INTERCEPT
    // =================================================================================================================


    private void processIntercepting(final ServletRequest request,
                                     final HttpServletResponse response,
                                     final FilterChain chain,
                                     final HttpServletRequest httpRequest,
                                     final RequestData requestData) throws Exception {
        byte[] data    = null;
        String content = null;
        try {
            data = readInput(httpRequest.getInputStream());
            content = data == null ? null : ObfuscatorTools.applyObfuscators(new String(data));
        } catch (final IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.METRICS.error(e.getMessage());
        }
        requestData.setContent(content);


        initCorrelationIdAndTraceId(requestData, request);
        final JavaRestMethodDTO javaRestMethod = resolveJavaRestMethod(request);
        addTrackingInformation(response, javaRestMethod);

        Exception                error           = null;
        final ResponseWrapper    responseWrapper = new ResponseWrapper(response, extractHeaders(httpRequest), responseListeners);
        final HttpServletRequest currentRequest  = buildRequestProxy((HttpServletRequest) request, data);
        requestData.setRequest(currentRequest);
        requestData.setResponse(responseWrapper);

        onBegin(currentRequest, requestData);
        final Chrono chrono = Chrono.startChrono();
        try {
            chain.doFilter(currentRequest, responseWrapper);
        } catch (final Exception e) {
            error = e;
            throw e;
        } finally {
            chrono.stop();
            final ErrorResult errorResult = resolveError(error, responseWrapper);
            onEnd(currentRequest, responseWrapper, errorResult, chrono.getDuration(), requestData);
        }
    }

    // =========================================================================
    // LIFECYCLE
    // =========================================================================
    private void onBegin(final HttpServletRequest httpRequest,
                         final RequestData requestData) {

        onBeginInitMdcFields(requestData, httpRequest);

        for (final MonitoringFilterInterceptor interceptor : monitoringFilterInterceptors) {
            try {
                interceptor.onBegin(requestData);
            } catch (final Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    private void onEnd(final HttpServletRequest httpRequest,
                       final ResponseWrapper httpResponse,
                       final ErrorResult error,
                       final long duration,
                       final RequestData requestData) {
        RequestContext.getInstance();
        onEndInitMdcFields(error, duration, httpResponse);
        MdcService.getInstance().duration(duration).status(httpResponse.getStatus());

        if (httpResponse.getStatus() >= 400) {
            MdcService.getInstance().globalStatusError();
        } else {
            MdcService.getInstance().globalStatusSuccess();
        }

        final ResponseData responseData = convertToResponseData(httpRequest, httpResponse, duration);

        for (final MonitoringFilterInterceptor interceptor : monitoringFilterInterceptors) {
            try {
                interceptor.onDone(requestData, responseData, error);
            } catch (final Throwable e) {
                if (log.isDebugEnabled()) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    protected Map<String, List<String>> extractHeaders(final HttpServletRequest httpRequest) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        if (httpRequest == null) {
            return result;
        }
        final Iterator<String> names = httpRequest.getHeaderNames().asIterator();
        while (names.hasNext()) {
            final var name = names.next();
            result.put(name, List.of(httpRequest.getHeader(name)));
        }
        return result;
    }

    private void initCorrelationIdAndTraceId(final RequestData requestInfo, final ServletRequest request) {
        MdcService.getInstance().correlationId(requestInfo.getCorrelationId()).traceId(requestInfo.getTraceId());

        HttpServletRequest httpServletRequest = null;
        if (request instanceof HttpServletRequest) {
            httpServletRequest = (HttpServletRequest) request;
        }

        if (httpServletRequest != null) {
            try {
                MdcService.getInstance().verb(httpServletRequest.getMethod()).url(httpServletRequest.getRequestURI());
            } catch (final Throwable e) {
            }
        }
    }

    private JavaRestMethodDTO resolveJavaRestMethod(final ServletRequest request) {
        JavaRestMethodDTO        result      = null;
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        for (final JavaRestMethodResolver resolver : javaRestMethodResolvers) {
            try {
                result = resolver.resolve(httpRequest);
                if (result != null) {
                    break;
                }
            } catch (final Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }


    private void addTrackingInformation(final HttpServletResponse response,
                                        final JavaRestMethodDTO javaRestMethod) {
        if (javaRestMethod != null) {
            for (final JavaRestMethodTracker tracker : javaRestMethodTrackers) {
                if (tracker.accept(javaRestMethod)) {
                    tracker.track(javaRestMethod);
                }
            }
        }
        final var mdc = MdcService.getInstance();
        response.setHeader(Headers.X_CORRELATION_ID, mdc.correlationId());
        response.setHeader(Headers.X_B_3_TRACEID, mdc.traceId());
    }

    private HttpServletRequest buildRequestProxy(final HttpServletRequest request, final byte[] content) {
        final Class<?>[] types = {ServletRequest.class, HttpServletRequest.class};
        return (HttpServletRequest) Proxy.newProxyInstance(this.getClass()
                                                               .getClassLoader(), types, new RequestCallBackInterceptor(request, content));
    }


    private boolean mustIntercept(final RequestData requestData) {
        Boolean result = INTERCEPTABLE_URI_RESOLVED.get(requestData.getUri());
        if (result == null) {
            for (final Interceptable resolver : interceptableResolver) {
                result = resolver.isInterceptable(requestData);
                if (!result) {
                    break;
                }
            }
            INTERCEPTABLE_URI_RESOLVED.put(requestData.getUri(), result);
        }
        purgeCacheIfRequired();

        return result;
    }


    private void purgeCacheIfRequired() {
        if (purgeCacheStrategy != null && purgeCacheStrategy.shouldPurge(INTERCEPTABLE_URI_RESOLVED)) {
            INTERCEPTABLE_URI_RESOLVED.clear();
        }
    }


    // =========================================================================
    // MDC
    // =========================================================================
    private void onBeginInitMdcFields(final RequestData requestData, final HttpServletRequest httpRequest) {
        final MdcService mdc = MdcService.getInstance();
        try {
            mdc.setMdc(MDCKeys.callType, MdcService.CALL_TYPE_REST);
            mdc.setMdc(MDCKeys.uri, requestData.getUri());
            mdc.setMdc(MDCKeys.verb, httpRequest.getMethod());
            mdc.setMdc(MDCKeys.authProtocol, httpRequest.getAuthType());

            if (httpRequest.getUserPrincipal() != null) {
                mdc.setMdc(MDCKeys.principal, httpRequest.getUserPrincipal().getName());
            }
            mdc.setMdc(MDCKeys.url, httpRequest.getRequestURL().toString());
        } catch (final Throwable e) {
        }

    }

    private void onEndInitMdcFields(final ErrorResult error, final long duration, final ResponseWrapper httpResponse) {
        final MdcService mdc = MdcService.getInstance();
        try {
            mdc.duration(duration);
            mdc.setMdc(MDCKeys.httpStatus, httpResponse.getStatus());

            if (error != null && error.getCurrentErrorCode() != null) {
                mdc.errorCode(error.getCurrentErrorCode());
            }
        } catch (final Throwable e) {
        }
    }


    // =========================================================================
    // CONVERTERS
    // =========================================================================

    private byte[] readInput(final ServletInputStream inputStream) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream(64 * KILO);

        final int    bufferSize = 16 * KILO;
        final byte[] buffer     = new byte[bufferSize];

        int bytesLeft;
        try {
            while (-1 != (bytesLeft = inputStream.read(buffer))) {
                out.write(buffer, 0, bytesLeft);
            }
        } catch (final IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }

        return out.toByteArray();
    }

    private ResponseData convertToResponseData(final HttpServletRequest httpRequest,
                                               final ResponseWrapper httpResponse,
                                               final long duration) {
        final String content = ObfuscatorTools.applyObfuscators(httpResponse.getData());

        final Map<String, String> hearders = new LinkedHashMap<>();

        final Collection<String> headerNames = httpResponse.getHeaderNames();
        for (final String key : headerNames) {
            hearders.put(key, httpResponse.getHeader(key));
        }
        return ResponseData.builder()
                           .httpRequest(httpRequest)
                           .httpResponse(httpResponse)
                           .code(httpResponse.getStatus())
                           .content(content)
                           .contentType(httpResponse.getContentType())
                           .duration(duration)
                           .datetime(CalendarTools.buildCalendar().getTimeInMillis())
                           .hearder(hearders)
                           .build();
    }

    // =========================================================================
    // ERROR RESOLVER
    // =========================================================================
    protected ErrorResult resolveError(final Exception currentError, final ResponseWrapper responseWrapper) {
        Exception error            = currentError;
        ErrorCode currentErrorCode = MdcService.getInstance().errorCode();

        if (error == null && currentErrorCode != null) {
            error = new UncheckedException(buildDefaultErrorCode(responseWrapper));
        } else if (error == null) {
            return null;
        }

        ErrorResult.ErrorResultBuilder result = null;

        ErrorResult resolvedError = null;
        for (final ExceptionResolver resolver : exceptionResolver) {
            try {
                resolvedError = resolver.resolve(error);
                if (resolvedError != null) {
                    break;
                }
            } catch (final Throwable e) {
                log.error(e.getMessage(), e);
            }
        }

        if (resolvedError != null) {
            result = resolvedError.toBuilder();
        }


        if (currentErrorCode != null) {
            result = ErrorResult.builder()
                                .httpCode(currentErrorCode.getStatusCode())
                                .errorCode(currentErrorCode.getErrorCode())
                                .errorType(currentErrorCode.getErrorType())
                                .message(currentErrorCode.getMessage())
                                .exploitationError(currentErrorCode.isExploitationError())
                                .currentErrorCode(currentErrorCode);
        }

        if (result == null) {
            result = ErrorResult.builder().currentErrorCode(buildDefaultErrorCode(responseWrapper));
        }

        return result.exception(error).build();
    }

    private static DefaultErrorCode buildDefaultErrorCode(final ResponseWrapper responseWrapper) {
        return DefaultErrorCode.buildUndefineErrorCode()
                               .errorCode(DEFAULT_ERROR_CODE)
                               .statusCode(responseWrapper == null ? 500 : responseWrapper.getStatus())
                               .errorTypeTechnical()
                               .build();
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private boolean isAttributesNotInitialized() {
        return javaRestMethodResolvers == null || javaRestMethodTrackers == null || interceptableResolver == null ||
               exceptionResolver == null;
    }
}
