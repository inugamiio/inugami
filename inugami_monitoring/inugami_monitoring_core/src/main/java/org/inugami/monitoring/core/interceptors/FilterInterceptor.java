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
package org.inugami.monitoring.core.interceptors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.tools.Chrono;
import org.inugami.api.tools.CalendarTools;
import org.inugami.commons.spi.SpiLoader;
import org.inugami.monitoring.api.data.ResponseData;
import org.inugami.monitoring.api.data.ResquestData;
import org.inugami.monitoring.api.data.ResquestDataBuilder;
import org.inugami.monitoring.api.exceptions.ErrorResult;
import org.inugami.monitoring.api.exceptions.ExceptionResolver;
import org.inugami.monitoring.api.interceptors.MonitoringFilterInterceptor;
import org.inugami.monitoring.api.interceptors.RequestContext;
import org.inugami.monitoring.api.interceptors.RequestInformationInitializer;
import org.inugami.monitoring.api.obfuscators.ObfuscatorTools;
import org.inugami.monitoring.api.resolvers.Interceptable;
import org.inugami.monitoring.core.context.MonitoringBootstrap;

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
    private final static List<ExceptionResolver>           EXCEPTION_RESOLVER     = SPI_LOADER.loadSpiServicesByPriority(ExceptionResolver.class,new FilterInterceptorErrorResolver());
    private final static List<Interceptable>               INTERCEPTABLE_RESOLVER = SPI_LOADER.loadSpiServicesByPriority(Interceptable.class);
    private final static Map<String, Boolean>              INTERCEPTABLE_RESOLVED = new HashMap<>();
    private final static int KILO                                                 = 1024;
    //@formatter:on
    
    // =========================================================================
    // LIFECYCLE
    // =========================================================================
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void destroy() {
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                              ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String currentPath = buildCurrentPath(httpRequest);
        if (mustIntercept(currentPath)) {
            try {
                processIntercepting(request, response, chain, httpRequest);
            }
            catch (Exception e) {
                throw new IOException(e.getMessage(), e);
            }
        }
        else {
            chain.doFilter(request, response);
        }
    }
    
    private void processIntercepting(ServletRequest request, ServletResponse response, FilterChain chain,
                                     HttpServletRequest httpRequest) throws Exception {
        byte[] data = null;
        String content = null;
        try {
            data = readInput(httpRequest.getInputStream());
            content = data == null ? null : ObfuscatorTools.applyObfuscators(new String(data));
        }
        catch (IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.METRICS.error(e.getMessage());
        }
        
        final Map<String, String> headers = RequestInformationInitializer.buildHeadersMap(httpRequest);
        RequestInformationInitializer.buildRequestInformation(httpRequest, headers);
        onBegin(httpRequest, headers, content);
        
        Exception error = null;
        
        final ResponseWrapper responseWrapper = new ResponseWrapper(response);
        final Chrono chrono = Chrono.startChrono();
        try {
            chain.doFilter(buildRequestProxy(request, data), responseWrapper);
        }
        catch (Exception e) {
            error = e;
            throw e;
        }
        finally {
            chrono.stop();
            final ErrorResult errorResult = error == null ? null : resolveError(error);
            onEnd(httpRequest, responseWrapper, errorResult, chrono.getDelaisInMillis(), content);
        }
    }
    
    private ServletRequest buildRequestProxy(ServletRequest request, final byte[] content) {
        final Class<?>[] types = { ServletRequest.class, HttpServletRequest.class };
        //@formatter:off
        final ServletRequest proxy = (ServletRequest) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                                             types,
                                                                             new RequestCallBackInterceptor(request,content));
        //@formatter:on
        
        return proxy;
    }
    
    private String buildCurrentPath(HttpServletRequest httpRequest) {
        return httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
    }
    
    private boolean mustIntercept(String currentPath) {
        Boolean result = INTERCEPTABLE_RESOLVED.get(currentPath);
        if (result == null) {
            for (Interceptable resolver : INTERCEPTABLE_RESOLVER) {
                result = resolver.isInterceptable(currentPath);
                if (result) {
                    break;
                }
            }
            INTERCEPTABLE_RESOLVED.put(currentPath, result);
        }
        
        return result;
    }
    
    // =========================================================================
    // LIFECYCLE
    // =========================================================================
    private void onBegin(HttpServletRequest httpRequest, Map<String, String> headers, String content) {
        
        final ResquestData requestData = convertToRequestData(httpRequest, headers, content);
        for (MonitoringFilterInterceptor interceptor : MonitoringBootstrap.getContext().getInterceptors()) {
            interceptor.onBegin(requestData);
        }
    }
    
    private void onEnd(HttpServletRequest httpRequest, ResponseWrapper httpResponse, ErrorResult error, long duration,
                       String content) {
        RequestInformationInitializer.appendResponseHeaderInformation(httpResponse);
        RequestContext.getInstance();
        final Map<String, String> headers = RequestInformationInitializer.buildHeadersMap(httpResponse);
        final ResquestData requestData = convertToRequestData(httpRequest, headers, content);
        final ResponseData responseData = convertToResponseData(httpResponse, duration);
        for (MonitoringFilterInterceptor interceptor : MonitoringBootstrap.getContext().getInterceptors()) {
            interceptor.onDone(requestData, responseData, error);
        }
    }
    
    // =========================================================================
    // CONVERTERS
    // =========================================================================
    private ResquestData convertToRequestData(HttpServletRequest httpRequest, Map<String, String> headers,
                                              String content) {
        final ResquestDataBuilder builder = new ResquestDataBuilder();
        
        builder.setMethod(httpRequest.getMethod());
        builder.setUri(httpRequest.getRequestURI().toString());
        builder.setContextPath(httpRequest.getContextPath());
        builder.setContentType(httpRequest.getContentType());
        builder.setHearder(headers);
        builder.setContent(content==null?null:content.trim());
        return builder.build();
    }
    
    private byte[] readInput(ServletInputStream inputStream) {
        
        final StringBuilder result = new StringBuilder();
        final ByteArrayOutputStream out = new ByteArrayOutputStream(64 * KILO);
        final int bufferSize = 16 * KILO;
        byte[] buffer = new byte[bufferSize];
        int cursor = 0;
        
        int hasNext = 0;
        do {
            try {
                hasNext = inputStream.read(buffer);
                if (hasNext != -1) {
                    result.append(new String(buffer));
                    out.write(buffer, cursor * bufferSize, bufferSize);
                }
                cursor++;
            }
            catch (IOException e) {
            }
            
        }
        while (hasNext != -1);
        
        return out.toByteArray();
    }
    
    private ResponseData convertToResponseData(ResponseWrapper httpResponse, long duration) {
        final String content = ObfuscatorTools.applyObfuscators(httpResponse.getData());
        return new ResponseData(httpResponse.getStatus(), content, httpResponse.getContentType(), duration,
                                CalendarTools.buildCalendar().getTimeInMillis());
    }
    
    // =========================================================================
    // ERROR RESOLVER
    // =========================================================================
    private ErrorResult resolveError(Exception error) {
        ErrorResult result = null;
        for (ExceptionResolver resolver : EXCEPTION_RESOLVER) {
            result = resolver.resolve(error);
            if (result != null) {
                break;
            }
        }
        return result;
    }
    
}
