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

import io.inugami.api.loggers.Loggers;
import io.inugami.api.monitoring.RequestContext;
import io.inugami.api.monitoring.RequestInformation;
import io.inugami.api.monitoring.models.Monitoring;
import io.inugami.api.spi.SpiLoader;
import io.inugami.monitoring.api.resolvers.DefaultServiceNameResolver;
import io.inugami.monitoring.api.resolvers.ServiceNameResolver;
import io.inugami.monitoring.core.context.MonitoringBootstrap;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * RequestInformationInitializer
 *
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@UtilityClass
public final class RequestInformationInitializer {
    // =========================================================================
    // BUILDER
    // =========================================================================
    private static Monitoring config = null;


    private static final ServiceNameResolver SERVICE_NAME_RESOLVER = SpiLoader.getInstance().loadSpiServiceByPriority(
            ServiceNameResolver.class,
            new DefaultServiceNameResolver());
    private static final int                 MAX_SIZE              = 512;

    // =========================================================================
    // BUILDER
    // =========================================================================
    public static synchronized RequestInformation buildRequestInformation(final HttpServletRequest request,
                                                                          final Map<String, String> headers) {
        if (config == null) {
            config = MonitoringBootstrap.getContext().getConfig();
        }
        final RequestInformation information = buildInformation(request, headers);
        RequestContext.setInstance(information);
        return information;
    }

    private static RequestInformation buildInformation(final HttpServletRequest request,
                                                       final Map<String, String> headers) {

        final RequestInformation.RequestInformationBuilder builder = RequestInformation.builder();
        builder.env(config.getEnv());
        builder.asset(config.getAsset());
        builder.hostname(config.getHostname());
        builder.instanceName(config.getInstanceName());
        builder.instanceNumber(config.getInstanceNumber());
        builder.applicationVersion(config.getApplicationVersion());

        builder.correlationId(cleanInput(buildUid(headers.get(config.getHeaders().getCorrelationId()))));
        builder.requestId(cleanInput(buildUid(headers.get(config.getHeaders().getRequestId()))));
        builder.traceId(cleanInput(buildUid(headers.get(config.getHeaders().getTraceId()))));
        builder.conversationId(cleanInput(headers.get(config.getHeaders().getConversationId())));

        builder.service(SERVICE_NAME_RESOLVER.resolve(buildUriPath(request)));
        builder.callFrom(cleanInput(headers.get(config.getHeaders().getCallFrom())));
        builder.deviceIdentifier(cleanInput(headers.get(config.getHeaders().getDeviceIdentifier())));
        builder.deviceType(cleanInput(headers.get(config.getHeaders().getDeviceType())));
        builder.deviceClass(cleanInput(headers.get(config.getHeaders().getDeviceClass())));

        final String clientVersion = headers.get(config.getHeaders().getDeviceVersion());
        builder.version(cleanInput(clientVersion));
        builder.majorVersion(clientVersion == null ? null : cleanInput(clientVersion.split("[.]")[0]));
        builder.osVersion(cleanInput(headers.get(config.getHeaders().getDeviceOsVersion())));
        builder.deviceNetworkType(cleanInput(headers.get(config.getHeaders().getDeviceNetworkType())));
        builder.deviceNetworkSpeedDown(parseDouble(cleanInput(headers.get(config.getHeaders()
                                                                                .getDeviceNetworkSpeedDown()))));
        builder.deviceNetworkSpeedUp(parseDouble(cleanInput(headers.get(config.getHeaders()
                                                                              .getDeviceNetworkSpeedUp()))));
        builder.deviceNetworkSpeedLatency(parseDouble(cleanInput(headers.get(config.getHeaders()
                                                                                   .getDeviceNetworkSpeedLatency()))));

        builder.remoteAddress(request.getRemoteAddr());
        builder.deviceIp(cleanInput(headers.get(config.getHeaders().getDeviceIp())));
        builder.userAgent(cleanInput(headers.get(config.getHeaders().getUserAgent())));

        builder.language(cleanInput(headers.get(config.getHeaders().getLanguage())));
        builder.country(cleanInput(headers.get(config.getHeaders().getCountry())));

        final Map<String, String> specific = new HashMap<>();
        if ((config.getHeaders().getSpecificHeaders() != null) && !config.getHeaders().getSpecificHeaders().isEmpty()) {
            for (final String key : config.getHeaders().getSpecificHeaders()) {
                final String value = cleanInput(headers.get(key));
                if (value != null) {
                    specific.put(key, value);
                }

            }
        }
        builder.specific(specific);
        return builder.build();
    }


    private static String buildUriPath(final HttpServletRequest request) {
        final String contextPath = request.getContextPath();
        final String path        = request.getRequestURI();
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
        header.put(config.getHeaders().getCorrelationId(), RequestContext.getInstance().getCorrelationId());
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
        httpResponse.setHeader(config.getHeaders().getCorrelationId(), requestContext.getCorrelationId());
        httpResponse.setHeader(config.getHeaders().getRequestId(), requestContext.getRequestId());
    }

    private static Double parseDouble(final String value) {
        Double result = null;
        if (value != null) {
            try {
                result = Double.parseDouble(value);
            } catch (final Exception e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }
        return result;
    }
}
