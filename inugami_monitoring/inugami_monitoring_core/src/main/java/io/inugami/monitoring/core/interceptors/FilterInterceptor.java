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

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.listeners.ApplicationLifecycleSPI;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.tools.Chrono;
import io.inugami.api.monitoring.*;
import io.inugami.api.monitoring.data.ResponseData;
import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.exceptions.ErrorResult;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.Headers;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.spi.SpiLoader;
import io.inugami.api.tools.CalendarTools;
import io.inugami.monitoring.api.exceptions.ExceptionResolver;
import io.inugami.monitoring.api.interceptors.RequestInformationInitializer;
import io.inugami.monitoring.api.obfuscators.ObfuscatorTools;
import io.inugami.monitoring.api.resolvers.Interceptable;
import io.inugami.monitoring.core.context.MonitoringBootstrap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.inugami.api.functionnals.FunctionalUtils.applyIfNotNull;

/**
 * FilterInterceptor
 *
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@WebFilter(urlPatterns = "*", asyncSupported = true)
public class FilterInterceptor implements Filter, ApplicationLifecycleSPI {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    //@formatter:off
    private List<JavaRestMethodResolver>        javaRestMethodResolvers = null;
    private List<JavaRestMethodTracker>         javaRestMethodTrackers  = null;
    private List<Interceptable>                 interceptableResolver   = null;
    private FilterInterceptorCachePurgeStrategy purgeCacheStrategy      = null;

    private              List<ExceptionResolver>           exceptionResolver            = null;
    private              List<MonitoringFilterInterceptor> monitoringFilterInterceptors = new ArrayList<>();
    private              ConfigHandler<String, String>     configuration;
    private final static Map<String, Boolean>              INTERCEPTABLE_URI_RESOLVED   = new ConcurrentHashMap<>();
    private final static int                               KILO                         = 1024;


    //@formatter:on

    // =========================================================================
    // LIFECYCLE
    // =========================================================================
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        initAttributes();
    }

    @Override
    public void destroy() {
    }


    @Override
    public void onConfigurationReady(final ConfigHandler<String, String> configuration) {
        initAttributes();
    }

    public void initAttributes() {
        javaRestMethodResolvers = SpiLoader.getInstance().loadSpiServicesByPriority(JavaRestMethodResolver.class);
        javaRestMethodTrackers = SpiLoader.getInstance().loadSpiServicesByPriority(JavaRestMethodTracker.class);
        interceptableResolver = SpiLoader.getInstance().loadSpiServicesByPriority(Interceptable.class);
        exceptionResolver = SpiLoader.getInstance().loadSpiServicesByPriority(ExceptionResolver.class, new FilterInterceptorErrorResolver());
        purgeCacheStrategy = SpiLoader.getInstance().loadSpiServiceByPriority(FilterInterceptorCachePurgeStrategy.class, new DefaultFilterInterceptorCachePurgeStrategy());

        monitoringFilterInterceptors = new ArrayList<>();
        for (final MonitoringFilterInterceptor interceptor : MonitoringBootstrap.getContext().getInterceptors()) {
            monitoringFilterInterceptors.add(interceptor);
        }

        final List<MonitoringFilterInterceptor> monitoringFilterInterceptorsSpi = SpiLoader.getInstance().loadSpiServicesByPriority(MonitoringFilterInterceptor.class);
        for (final MonitoringFilterInterceptor interceptor : monitoringFilterInterceptorsSpi) {
            if (isNotContains(interceptor, MonitoringBootstrap.getContext().getInterceptors())) {
                monitoringFilterInterceptors.add(interceptor);
            }
        }
    }

    private boolean isNotContains(final MonitoringFilterInterceptor interceptor, final List<MonitoringFilterInterceptor> monitoringFilterInterceptors) {
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

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {

        if (isAttributesNotInitialized()) {
            initAttributes();
        }
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String             currentPath = buildCurrentPath(httpRequest);

        if (mustIntercept(currentPath)) {
            try {
                processIntercepting(request, (HttpServletResponse) response, chain, httpRequest);
            } catch (final Exception e) {
                throw new IOException(e.getMessage(), e);
            }
        } else {
            chain.doFilter(request, response);
        }


    }

    private boolean isAttributesNotInitialized() {
        return javaRestMethodResolvers == null || javaRestMethodTrackers == null || interceptableResolver == null || exceptionResolver == null;
    }

    private void processIntercepting(final ServletRequest request, final HttpServletResponse response,
                                     final FilterChain chain, final HttpServletRequest httpRequest) throws Exception {
        byte[] data    = null;
        String content = null;
        try {
            data = readInput(httpRequest.getInputStream());
            content = data == null ? null : ObfuscatorTools.applyObfuscators(new String(data));
        } catch (final IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.METRICS.error(e.getMessage());
        }

        final Map<String, String> headers = RequestInformationInitializer.buildHeadersMap(httpRequest);
        final RequestInformation requestInfo = RequestInformationInitializer.buildRequestInformation(
                httpRequest, headers);

        initCorrelationIdAndTraceId(requestInfo, request);
        final JavaRestMethodDTO javaRestMethod = resolveJavaRestMethod(request);
        addTrackingInformation(response, requestInfo, javaRestMethod);

        onBegin(httpRequest, response, headers, content);

        Exception error = null;

        final ResponseWrapper responseWrapper = new ResponseWrapper(response);
        final Chrono          chrono          = Chrono.startChrono();
        try {
            chain.doFilter(buildRequestProxy(request, data), responseWrapper);
        } catch (final Exception e) {
            error = e;
            throw e;
        } finally {
            chrono.stop();
            final ErrorResult errorResult = resolveError(error);
            onEnd(httpRequest, responseWrapper, errorResult, chrono.getDelaisInMillis(), content);
        }
    }

    private void initCorrelationIdAndTraceId(final RequestInformation requestInfo, final ServletRequest request) {
        MdcService.getInstance()
                  .correlationId(requestInfo.getCorrelationId())
                  .traceId(requestInfo.getTraceId());

        HttpServletRequest httpServletRequest = null;
        if (request instanceof HttpServletRequest) {
            httpServletRequest = (HttpServletRequest) request;
        }

        if (httpServletRequest != null) {
            try {
                MdcService.getInstance()
                          .verb(httpServletRequest.getMethod())
                          .url(httpServletRequest.getRequestURI());
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
                                        final RequestInformation requestInfo,
                                        final JavaRestMethodDTO javaRestMethod) {
        final Headers headers = MonitoringBootstrap.CONTEXT.getConfig().getHeaders();

        applyIfNotNull(requestInfo.getDeviceIdentifier(),
                       value -> response.setHeader(headers.getDeviceIdentifier(), value));
        applyIfNotNull(requestInfo.getCorrelationId(), value -> response.setHeader(headers.getCorrelationId(), value));

        response.setHeader(headers.getConversationId(), MdcService.getInstance().correlationId());
        response.setHeader(headers.getRequestId(), MdcService.getInstance().traceId());


        if (javaRestMethod != null) {
            for (final JavaRestMethodTracker tracker : javaRestMethodTrackers) {
                if (tracker.accept(javaRestMethod)) {
                    tracker.track(javaRestMethod);
                }
            }
        }
    }

    private ServletRequest buildRequestProxy(final ServletRequest request, final byte[] content) {
        final Class<?>[] types = {ServletRequest.class, HttpServletRequest.class};
        //@formatter:off
        final ServletRequest proxy = (ServletRequest) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                                             types,
                                                                             new RequestCallBackInterceptor(request, content));
        //@formatter:on

        return proxy;
    }

    private String buildCurrentPath(final HttpServletRequest httpRequest) {
        return httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
    }

    private boolean mustIntercept(final String currentPath) {
        Boolean result = INTERCEPTABLE_URI_RESOLVED.get(currentPath);
        if (result == null) {
            for (final Interceptable resolver : interceptableResolver) {
                result = resolver.isInterceptable(currentPath);
                if (!result) {
                    break;
                }
            }
            INTERCEPTABLE_URI_RESOLVED.put(currentPath, result);
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
    // LIFECYCLE
    // =========================================================================
    private void onBegin(final HttpServletRequest httpRequest, final HttpServletResponse response, final Map<String, String> headers,
                         final String content) {

        final ResquestData requestData = convertToRequestData(httpRequest, response, content);
        onBeginInitMdcFields(requestData, httpRequest);

        for (final MonitoringFilterInterceptor interceptor : monitoringFilterInterceptors) {
            try {
                interceptor.onBegin(requestData);
            } catch (final Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    private void onEnd(final HttpServletRequest httpRequest, final ResponseWrapper httpResponse,
                       final ErrorResult error, final long duration, final String content) {
        RequestInformationInitializer.appendResponseHeaderInformation(httpResponse);
        RequestContext.getInstance();
        onEndInitMdcFields(error, duration, httpResponse);
        MdcService.getInstance().duration(duration)
                  .status(httpResponse.getStatus());

        final ResquestData requestData  = convertToRequestData(httpRequest, httpResponse, content);
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

    // =========================================================================
    // MDC
    // =========================================================================
    private void onBeginInitMdcFields(final ResquestData requestData, final HttpServletRequest httpRequest) {
        final MdcService mdc = MdcService.getInstance();
        try {
            mdc.setMdc(MdcService.MDCKeys.callType, MdcService.CALL_TYPE_REST);
            mdc.setMdc(MdcService.MDCKeys.uri, requestData.getUri());
            mdc.setMdc(MdcService.MDCKeys.verb, httpRequest.getMethod());
            mdc.setMdc(MdcService.MDCKeys.sessionId, httpRequest.getRequestedSessionId());
            mdc.setMdc(MdcService.MDCKeys.authProtocol, httpRequest.getAuthType());

            if (httpRequest.getUserPrincipal() != null) {
                mdc.setMdc(MdcService.MDCKeys.principal, httpRequest.getUserPrincipal().getName());
            }
            mdc.setMdc(MdcService.MDCKeys.url, httpRequest.getRequestURL().toString());
        } catch (final Throwable e) {
        }

    }

    private void onEndInitMdcFields(final ErrorResult error, final long duration, final ResponseWrapper httpResponse) {
        final MdcService mdc = MdcService.getInstance();
        try {
            mdc.duration(duration);
            mdc.setMdc(MdcService.MDCKeys.httpStatus, httpResponse.getStatus());

            if (error != null && error.getCurrentErrorCode() != null) {
                mdc.errorCode(error.getCurrentErrorCode());
            }
        } catch (final Throwable e) {
        }
    }


    // =========================================================================
    // CONVERTERS
    // =========================================================================
    private ResquestData convertToRequestData(final HttpServletRequest httpRequest,
                                              final HttpServletResponse response,
                                              final String content) {
        final ResquestData.ResquestDataBuilder builder = ResquestData.builder()
                                                                     .httpRequest(httpRequest)
                                                                     .httpResponse(response)
                                                                     .method(httpRequest.getMethod())
                                                                     .uri(httpRequest.getRequestURI().toString())
                                                                     .contextPath(httpRequest.getContextPath())
                                                                     .contentType(httpRequest.getContentType())
                                                                     .content(content == null ? null : content.trim());


        final Map<String, String> headers  = new LinkedHashMap<>();
        final Iterator<String>    iterator = httpRequest.getHeaderNames().asIterator();
        while (iterator.hasNext()) {
            final String headerName = iterator.next();
            headers.put(headerName, httpRequest.getHeader(headerName));
        }
        builder.hearder(headers);

        return builder.build();
    }

    private byte[] readInput(final ServletInputStream inputStream) {

        final StringBuilder         result = new StringBuilder();
        final ByteArrayOutputStream out    = new ByteArrayOutputStream(64 * KILO);

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

    private ResponseData convertToResponseData(final HttpServletRequest httpRequest, final ResponseWrapper httpResponse, final long duration) {
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
    protected ErrorResult resolveError(final Exception error) {
        ErrorResult result = null;

        if (error != null) {
            for (final ExceptionResolver resolver : exceptionResolver) {
                try {
                    result = resolver.resolve(error);
                } catch (final Throwable e) {
                    log.error(e.getMessage(), e);
                }

                if (result != null) {
                    break;
                }
            }
        }

        ErrorCode currentErrorCode = null;
        if (result == null) {
            currentErrorCode = MdcService.getInstance().errorCode();
        }

        if (currentErrorCode != null) {
            result = ErrorResult.builder()
                                .httpCode(currentErrorCode.getStatusCode())
                                .errorCode(currentErrorCode.getErrorCode())
                                .errorType(currentErrorCode.getErrorType())
                                .message(currentErrorCode.getMessage())
                                .exploitationError(currentErrorCode.isExploitationError())
                                .currentErrorCode(currentErrorCode)
                                .build();
        }

        if (result != null && error != null) {
            result = result.toBuilder().exception(error).build();
        }
        return result;
    }


}
