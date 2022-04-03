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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.tools.Chrono;
import io.inugami.api.monitoring.RequestContext;
import io.inugami.api.monitoring.RequestInformation;
import io.inugami.api.monitoring.data.ResponseData;
import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.data.ResquestDataBuilder;
import io.inugami.api.monitoring.exceptions.ErrorResult;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.Headers;
import io.inugami.api.spi.SpiLoader;
import io.inugami.api.tools.CalendarTools;
import io.inugami.monitoring.api.exceptions.ExceptionResolver;
import io.inugami.monitoring.api.interceptors.RequestInformationInitializer;
import io.inugami.monitoring.api.obfuscators.ObfuscatorTools;
import io.inugami.monitoring.api.resolvers.Interceptable;
import io.inugami.monitoring.core.context.MonitoringBootstrap;
import io.inugami.monitoring.core.context.MonitoringContext;

import static io.inugami.api.functionnals.FunctionalUtils.applyIfNotNull;

/**
 * FilterInterceptor
 *
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@WebFilter(urlPatterns = "*", asyncSupported = true)
public class FilterInterceptor implements Filter {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    //@formatter:off
    private final static SpiLoader SPI_LOADER = new SpiLoader();
    private final static List<Interceptable>               INTERCEPTABLE_RESOLVER     = SPI_LOADER.loadSpiServicesByPriority(Interceptable.class);
    private final static List<ExceptionResolver>           EXCEPTION_RESOLVER         = SPI_LOADER.loadSpiServicesByPriority(ExceptionResolver.class,new FilterInterceptorErrorResolver());
    private final static Map<String, Boolean>              INTERCEPTABLE_URI_RESOLVED = new ConcurrentHashMap<>();
    private final static int KILO                                                     = 1024;
    //@formatter:on

    // =========================================================================
    // LIFECYCLE
    // =========================================================================
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String             currentPath = buildCurrentPath(httpRequest);
        if (mustIntercept(currentPath)) {
            try {
                processIntercepting(request, (HttpServletResponse)response, chain, httpRequest);
            }
            catch (final Exception e) {
                throw new IOException(e.getMessage(), e);
            }
        }
        else {
            chain.doFilter(request, response);
        }
    }

    private void processIntercepting(final ServletRequest request, final HttpServletResponse response,
                                     final FilterChain chain, final HttpServletRequest httpRequest) throws Exception {
        byte[] data    = null;
        String content = null;
        try {
            data    = readInput(httpRequest.getInputStream());
            content = data == null ? null : ObfuscatorTools.applyObfuscators(new String(data));
        }
        catch (final IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.METRICS.error(e.getMessage());
        }

        final Map<String, String> headers = RequestInformationInitializer.buildHeadersMap(httpRequest);
        final RequestInformation requestInfo = RequestInformationInitializer.buildRequestInformation(
                httpRequest, headers);
        addTrackingInformation(response,requestInfo );

        onBegin(httpRequest, headers, content);

        Exception error = null;

        final ResponseWrapper responseWrapper = new ResponseWrapper(response);
        final Chrono          chrono          = Chrono.startChrono();
        try {
            chain.doFilter(buildRequestProxy(request, data), responseWrapper);
        }
        catch (final Exception e) {
            error = e;
            throw e;
        }
        finally {
            chrono.stop();
            final ErrorResult errorResult = error == null ? null : resolveError(error);
            onEnd(httpRequest, responseWrapper, errorResult, chrono.getDelaisInMillis(), content);
        }
    }

    private void addTrackingInformation(final HttpServletResponse response, final RequestInformation requestInfo) {
        final Headers headers = MonitoringBootstrap.CONTEXT.getConfig().getHeaders();

        applyIfNotNull(requestInfo.getDeviceIdentifier(), value->  response.setHeader(headers.getDeviceIdentifier(), value));
        applyIfNotNull(requestInfo.getCorrelationId(), value->  response.setHeader(headers.getCorrelationId(), value));
        applyIfNotNull(requestInfo.getConversationId(), value->  response.setHeader(headers.getConversationId(), value));
        applyIfNotNull(requestInfo.getRequestId(), value->  response.setHeader(headers.getRequestId(), value));

    }

    private ServletRequest buildRequestProxy(final ServletRequest request, final byte[] content) {
        final Class<?>[] types = {ServletRequest.class, HttpServletRequest.class};
        //@formatter:off
        final ServletRequest proxy = (ServletRequest) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                                             types,
                                                                             new RequestCallBackInterceptor(request,content));
        //@formatter:on

        return proxy;
    }

    private String buildCurrentPath(final HttpServletRequest httpRequest) {
        return httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
    }

    private boolean mustIntercept(final String currentPath) {
        Boolean result = INTERCEPTABLE_URI_RESOLVED.get(currentPath);
        if (result == null) {
            for (final Interceptable resolver : INTERCEPTABLE_RESOLVER) {
                result = resolver.isInterceptable(currentPath);
                if (!result) {
                    break;
                }
            }
            INTERCEPTABLE_URI_RESOLVED.put(currentPath, result);
        }

        return result;
    }

    // =========================================================================
    // LIFECYCLE
    // =========================================================================
    private void onBegin(final HttpServletRequest httpRequest, final Map<String, String> headers,
                         final String content) {

        final ResquestData requestData = convertToRequestData(httpRequest, content);
        for (final MonitoringFilterInterceptor interceptor : MonitoringBootstrap.getContext().getInterceptors()) {
            interceptor.onBegin(requestData);
        }
    }

    private void onEnd(final HttpServletRequest httpRequest, final ResponseWrapper httpResponse,
                       final ErrorResult error, final long duration, final String content) {
        RequestInformationInitializer.appendResponseHeaderInformation(httpResponse);
        RequestContext.getInstance();
        final ResquestData requestData  = convertToRequestData(httpRequest, content);
        final ResponseData responseData = convertToResponseData(httpResponse, duration);
        for (final MonitoringFilterInterceptor interceptor : MonitoringBootstrap.getContext().getInterceptors()) {
            interceptor.onDone(requestData, responseData, error);
        }
    }

    // =========================================================================
    // CONVERTERS
    // =========================================================================
    private ResquestData convertToRequestData(final HttpServletRequest httpRequest,
                                              final String content) {
        final ResquestDataBuilder builder = new ResquestDataBuilder();

        builder.setMethod(httpRequest.getMethod());
        builder.setUri(httpRequest.getRequestURI().toString());
        builder.setContextPath(httpRequest.getContextPath());
        builder.setContentType(httpRequest.getContentType());

        final Map<String, String> headers  = new LinkedHashMap<>();
        final Iterator<String>    iterator = httpRequest.getHeaderNames().asIterator();
        while (iterator.hasNext()) {
            final String headerName = iterator.next();
            headers.put(headerName, httpRequest.getHeader(headerName));
        }
        builder.setHearder(headers);
        builder.setContent(content == null ? null : content.trim());
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
        }
        catch (final IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }

        return out.toByteArray();
    }

    private ResponseData convertToResponseData(final ResponseWrapper httpResponse, final long duration) {
        final String content = ObfuscatorTools.applyObfuscators(httpResponse.getData());

        Map<String, String> hearders = new LinkedHashMap<>();

        final Collection<String> headerNames = httpResponse.getHeaderNames();
        for(String key : headerNames){
            hearders.put(key, httpResponse.getHeader(key));
        }
        return ResponseData.builder()
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
    private ErrorResult resolveError(final Exception error) {
        ErrorResult result = null;
        for (final ExceptionResolver resolver : EXCEPTION_RESOLVER) {
            result = resolver.resolve(error);
            if (result != null) {
                break;
            }
        }
        return result;
    }

}
