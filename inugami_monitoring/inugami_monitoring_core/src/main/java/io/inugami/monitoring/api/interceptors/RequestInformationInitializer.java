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
package io.inugami.monitoring.api.interceptors;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.inugami.api.loggers.Loggers;
import io.inugami.api.monitoring.RequestContext;
import io.inugami.api.monitoring.RequestInformation;
import io.inugami.api.monitoring.RequestInformationBuilder;
import io.inugami.api.monitoring.models.Monitoring;
import io.inugami.api.spi.SpiLoader;
import io.inugami.monitoring.api.resolvers.DefaultServiceNameResolver;
import io.inugami.monitoring.api.resolvers.ServiceNameResolver;

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
    private final static Monitoring CONFIG = RequestContext.loadConfig();

    private static final SpiLoader SPI_LOADER = new SpiLoader();

    private static final ServiceNameResolver SERVICE_NAME_RESOLVER = SPI_LOADER.loadSpiServiceByPriority(
            ServiceNameResolver.class,
            new DefaultServiceNameResolver());
    private static final int                 MAX_SIZE              = 512;

    // =========================================================================
    // BUILDER
    // =========================================================================
    public static synchronized RequestInformation buildRequestInformation(final HttpServletRequest request,
                                                            final Map<String, String> headers) {
        final RequestInformation information = buildInformation(request, headers);
        RequestContext.setInstance(information);
        return information;
    }

    private static RequestInformation buildInformation(final HttpServletRequest request,
                                                       final Map<String, String> headers) {

        final RequestInformationBuilder builder = new RequestInformationBuilder();
        builder.setEnv(CONFIG.getEnv());
        builder.setAsset(CONFIG.getAsset());
        builder.setHostname(CONFIG.getHostname());
        builder.setInstanceName(CONFIG.getInstanceName());
        builder.setInstanceNumber(CONFIG.getInstanceNumber());
        builder.setApplicationVersion(CONFIG.getApplicationVersion());

        builder.setCorrelationId(cleanInput(buildUid(headers.get(CONFIG.getHeaders().getCorrelationId()))));
        builder.setRequestId(cleanInput(buildUid(headers.get(CONFIG.getHeaders().getRequestId()))));
        builder.setConversationId(cleanInput(headers.get(CONFIG.getHeaders().getConversationId())));
        builder.setSessionId(cleanInput(request.getRequestedSessionId()));

        builder.setService(SERVICE_NAME_RESOLVER.resolve(buildUriPath(request)));

        builder.setDeviceIdentifier(cleanInput(headers.get(CONFIG.getHeaders().getDeviceIdentifier())));
        builder.setDeviceType(cleanInput(headers.get(CONFIG.getHeaders().getDeviceType())));
        builder.setDeviceClass(cleanInput(headers.get(CONFIG.getHeaders().getDeviceClass())));

        final String clientVersion = headers.get(CONFIG.getHeaders().getDeviceVersion());
        builder.setVersion(cleanInput(clientVersion));
        builder.setMajorVersion(clientVersion == null ? null : cleanInput(clientVersion.split("[.]")[0]));
        builder.setOsVersion(cleanInput(headers.get(CONFIG.getHeaders().getDeviceOsVersion())));
        builder.setDeviceNetworkType(cleanInput(headers.get(CONFIG.getHeaders().getDeviceNetworkType())));
        builder.setDeviceNetworkSpeedDown(
                parseDouble(cleanInput(headers.get(CONFIG.getHeaders().getDeviceNetworkSpeedDown()))));
        builder.setDeviceNetworkSpeedUp(
                parseDouble(cleanInput(headers.get(CONFIG.getHeaders().getDeviceNetworkSpeedUp()))));
        builder.setDeviceNetworkSpeedLatency(
                parseDouble(cleanInput(headers.get(CONFIG.getHeaders().getDeviceNetworkSpeedLatency()))));

        builder.setRemoteAddress(request.getRemoteAddr());
        builder.setDeviceIp(cleanInput(headers.get(CONFIG.getHeaders().getDeviceIp())));
        builder.setUserAgent(cleanInput(headers.get(CONFIG.getHeaders().getUserAgent())));

        builder.setLanguage(cleanInput(headers.get(CONFIG.getHeaders().getLanguage())));
        builder.setCountry(cleanInput(headers.get(CONFIG.getHeaders().getCountry())));

        final Map<String, String> specific = new HashMap<>();
        if ((CONFIG.getHeaders().getSpecificHeaders() != null) && !CONFIG.getHeaders().getSpecificHeaders().isEmpty()) {
            for (final String key : CONFIG.getHeaders().getSpecificHeaders()) {
                final String value = cleanInput(headers.get(key));
                if (value != null) {
                    specific.put(key, value);
                }

            }
        }
        builder.setSpecific(specific);
        return builder.build();
    }


    private static String buildUriPath(final HttpServletRequest request) {
        final String contextPath = request.getContextPath();
        final String path        = request.getRequestURI().toString();
        return path.length() >= contextPath.length() ? path.substring(contextPath.length()) : path;
    }

    private static String buildUid(final String uid) {
        return (uid == null) || uid.trim().isEmpty() ? UUID.randomUUID().toString() : uid;
    }

    public static Map<String, String> buildHeadersMap(final HttpServletRequest request) {
        final Map<String, String> header = new HashMap<>();
        final Enumeration<String> names  = request.getHeaderNames();
        while (names.hasMoreElements()) {
            final String key = names.nextElement();
            header.put(key.toLowerCase(), request.getHeader(key));
        }
        return header;
    }

    public static Map<String, String> buildHeadersMap(final HttpServletResponse response) {
        final Map<String, String> header = new HashMap<>();
        final Iterator<String>    names  = response.getHeaderNames().iterator();
        while (names.hasNext()) {
            final String key = names.next();
            header.put(key.toLowerCase(), response.getHeader(key));
        }
        header.put(CONFIG.getHeaders().getCorrelationId(), RequestContext.getInstance().getCorrelationId());
        return header;
    }

    private static String cleanInput(final String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String currentValue = value.trim();
        if (currentValue.length() > MAX_SIZE) {
            currentValue = currentValue.substring(0, MAX_SIZE - 1);
        }
        return currentValue;
    }
    // =========================================================================
    // TOOLS
    // =========================================================================

    public static void appendResponseHeaderInformation(final HttpServletResponse httpResponse) {
        final RequestInformation requestContext = RequestContext.getInstance();
        httpResponse.setHeader(CONFIG.getHeaders().getCorrelationId(), requestContext.getCorrelationId());
        httpResponse.setHeader(CONFIG.getHeaders().getRequestId(), requestContext.getRequestId());
    }

    private static Double parseDouble(final String value) {
        Double result = null;
        if (value != null) {
            try {
                result = Double.parseDouble(value);
            }
            catch (final Exception e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }
        return result;
    }
}
