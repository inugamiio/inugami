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
package org.inugami.monitoring.api.interceptors;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.inugami.api.loggers.Loggers;
import org.inugami.commons.spi.SpiLoader;
import org.inugami.monitoring.api.data.RequestInformation;
import org.inugami.monitoring.api.data.RequestInformationBuilder;
import org.inugami.monitoring.api.data.config.Monitoring;
import org.inugami.monitoring.api.interceptors.RequestContext;
import org.inugami.monitoring.api.resolvers.DefaultServiceNameResolver;
import org.inugami.monitoring.api.resolvers.ServiceNameResolver;
import org.inugami.monitoring.config.loader.ConfigurationLoader;

/**
 * RequestInformationInitializer
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
public final class RequestInformationInitializer {
    // =========================================================================
    // BUILDER
    // =========================================================================
    private final static Monitoring          CONFIG                = ConfigurationLoader.CONFIGURATION;
    
    private static final SpiLoader           SPI_LOADER            = new SpiLoader();
    
    private static final ServiceNameResolver SERVICE_NAME_RESOLVER = SPI_LOADER.loadSpiServiceByPriority(ServiceNameResolver.class,
                                                                                                         new DefaultServiceNameResolver());
    
    

    
    // =========================================================================
    // BUILDER
    // =========================================================================
    public static synchronized void buildRequestInformation(HttpServletRequest request, Map<String, String> headers) {
        final RequestInformation information = buildInformation(request, headers);
        RequestContext.setInstance(information);
    }
    
    private static RequestInformation buildInformation(HttpServletRequest request, final Map<String, String> headers) {
        
        RequestInformationBuilder builder = new RequestInformationBuilder();
        builder.setEnv(CONFIG.getEnv());
        builder.setAsset(CONFIG.getAsset());
        builder.setHostname(CONFIG.getHostname());
        builder.setInstanceName(CONFIG.getInstanceName());
        builder.setInstanceNumber(CONFIG.getInstanceNumber());
        builder.setApplicationVersion(CONFIG.getApplicationVersion());
        
        builder.setCorrelationId(buildUid(headers.get(CONFIG.getHeaders().getConversationId())));
        builder.setRequestId(buildUid(headers.get(CONFIG.getHeaders().getRequestId())));
        builder.setConversationId(headers.get(CONFIG.getHeaders().getConversationId()));
        builder.setSessionId(request.getRequestedSessionId());
        
        builder.setService(SERVICE_NAME_RESOLVER.resolve(buildUriPath(request)));
        
        builder.setDeviceIdentifier(headers.get(CONFIG.getHeaders().getDeviceIdentifier()));
        builder.setDeviceType(headers.get(CONFIG.getHeaders().getDeviceType()));
        builder.setDeviceClass(headers.get(CONFIG.getHeaders().getDeviceClass()));
        
        String clientVersion = headers.get(CONFIG.getHeaders().getDeviceVersion());
        builder.setVersion(clientVersion);
        builder.setMajorVersion(clientVersion == null ? null : clientVersion.split("[.]")[0]);
        builder.setOsVersion(headers.get(CONFIG.getHeaders().getDeviceOsVersion()));
        builder.setDeviceNetworkType(headers.get(CONFIG.getHeaders().getDeviceNetworkType()));
        builder.setDeviceNetworkSpeedDown(parseDouble(headers.get(CONFIG.getHeaders().getDeviceNetworkSpeedDown())));
        builder.setDeviceNetworkSpeedUp(parseDouble(headers.get(CONFIG.getHeaders().getDeviceNetworkSpeedUp())));
        builder.setDeviceNetworkSpeedLatency(parseDouble(headers.get(CONFIG.getHeaders().getDeviceNetworkSpeedLatency())));
        
        builder.setRemoteAddress(request.getRemoteAddr());
        builder.setDeviceIp(headers.get(CONFIG.getHeaders().getDeviceIp()));
        builder.setUserAgent(headers.get(CONFIG.getHeaders().getUserAgent()));
        
        builder.setLanguage(headers.get(CONFIG.getHeaders().getLanguage()));
        builder.setCountry(headers.get(CONFIG.getHeaders().getCountry()));
        
        Map<String, String> specific = new HashMap<>();
        if (CONFIG.getHeaders().getSpecificHeaders() != null && !CONFIG.getHeaders().getSpecificHeaders().isEmpty()) {
            for (String key : CONFIG.getHeaders().getSpecificHeaders()) {
                specific.put(key, headers.get(key));
            }
        }
        builder.setSpecific(specific);
        return builder.build();
    }
    
    private static String buildUriPath(HttpServletRequest request) {
        final String contextPath = request.getContextPath();
        final String path = request.getRequestURI().toString();
        return path.length() >= contextPath.length() ? path.substring(contextPath.length()) : path;
    }
    
    private static String buildUriPath(String string) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private static String buildUid(String uid) {
        return uid == null || uid.trim().isEmpty() ? UUID.randomUUID().toString() : uid;
    }
    
    public static Map<String, String> buildHeadersMap(HttpServletRequest request) {
        final Map<String, String> header = new HashMap<>();
        final Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            header.put(key.toLowerCase(), request.getHeader(key));
        }
        return header;
    }
    
    public static Map<String, String> buildHeadersMap(HttpServletResponse response) {
        final Map<String, String> header = new HashMap<>();
        final Iterator<String> names = response.getHeaderNames().iterator();
        while (names.hasNext()) {
            String key = names.next();
            header.put(key.toLowerCase(), response.getHeader(key));
        }
        header.put(CONFIG.getHeaders().getCorrelationId(), RequestContext.getInstance().getCorrelationId());
        return header;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    
    public static void appendResponseHeaderInformation(HttpServletResponse httpResponse) {
        final RequestInformation requestContext = RequestContext.getInstance();
        httpResponse.setHeader(CONFIG.getHeaders().getCorrelationId(), requestContext.getCorrelationId());
        httpResponse.setHeader(CONFIG.getHeaders().getRequestId(), requestContext.getRequestId());
    }
    
    private static Double parseDouble(String value) {
        Double result = null;
        if (value != null) {
            try {
                result = Double.parseDouble(value);
            }
            catch (Exception e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }
        return result;
    }
}
