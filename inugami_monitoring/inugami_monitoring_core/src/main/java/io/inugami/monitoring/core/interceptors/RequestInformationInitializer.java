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
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.logger.MDCKeys;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.inugami.framework.api.loggers.mdc.mapper.MdcMapperUtils.convertToDouble;

@UtilityClass
public class RequestInformationInitializer {
    public static final List<String> HEADERS = List.of(Headers.X_CORRELATION_ID,
                                                       Headers.X_B_3_TRACEID,
                                                       Headers.X_CONVERSATION_ID,
                                                       Headers.X_AUTHORIZATION_TOKEN,
                                                       Headers.X_FRONT_VERSION,
                                                       Headers.X_DEVICE_IDENTIFIER,
                                                       Headers.X_DEVICE_TYPE,
                                                       Headers.X_DEVICE_CLASS,
                                                       Headers.X_DEVICE_CLASS,
                                                       Headers.X_DEVICE_OS_VERSION,
                                                       Headers.X_DEVICE_NETWORK_TYPE,
                                                       Headers.X_DEVICE_NETWORK_SPEED_DOWN,
                                                       Headers.X_DEVICE_NETWORK_SPEED_DOWN,
                                                       Headers.X_DEVICE_NETWORK_SPEED_LATENCY,
                                                       Headers.CLIENT_IP,
                                                       Headers.USER_AGENT,
                                                       Headers.ACCEPT_LANGUAGE,
                                                       Headers.COUNTRY
    );


    public static RequestData buildRequestInformation(final HttpServletRequest httpRequest) {
        final var builder = RequestData.builder();
        final var mdc     = MdcService.getInstance();
        builder.env(mdc.getMdc(MDCKeys.env));
        builder.asset(mdc.getMdc(MDCKeys.asset));
        builder.hostname(mdc.getMdc(MDCKeys.hostname));
        builder.instanceName(mdc.getMdc(MDCKeys.instanceName));
        builder.instanceNumber(mdc.getMdc(MDCKeys.instanceNumber));


        if (httpRequest == null) {
            builder.correlationId(mdc.correlationId());
            builder.traceId(mdc.traceId());
            return builder.build();
        }
        //REQUEST ------------------------------------------------------------------------------------------------------
        builder.uri(httpRequest.getRequestURI());
        builder.contextPath(httpRequest.getContextPath());
        builder.method(httpRequest.getMethod());
        builder.contentType(httpRequest.getContentType());
        builder.characterEncoding(httpRequest.getCharacterEncoding());
        builder.options(httpRequest.getParameterMap());

        //HEARDERS -----------------------------------------------------------------------------------------------------
        // tracking ----
        mdc.correlationId(httpRequest.getHeader(Headers.X_CORRELATION_ID));
        builder.correlationId(mdc.correlationId());

        mdc.traceId(httpRequest.getHeader(Headers.X_B_3_TRACEID));
        builder.traceId(mdc.traceId());

        mdc.conversationId(httpRequest.getHeader(Headers.X_CONVERSATION_ID));
        builder.traceId(mdc.conversationId());

        mdc.sessionId(httpRequest.getSession() == null ? null : httpRequest.getSession().getId());
        builder.sessionId(mdc.sessionId());

        builder.token(httpRequest.getHeader(Headers.X_AUTHORIZATION_TOKEN));


        // application ----
        mdc.applicationVersion(httpRequest.getHeader(Headers.X_FRONT_VERSION));
        builder.applicationVersion(mdc.applicationVersion());

        // service

        // devices ---
        mdc.deviceIdentifier(httpRequest.getHeader(Headers.X_DEVICE_IDENTIFIER));
        builder.applicationVersion(mdc.deviceIdentifier());

        mdc.deviceType(httpRequest.getHeader(Headers.X_DEVICE_TYPE));
        builder.deviceType(mdc.deviceType());

        mdc.deviceClass(httpRequest.getHeader(Headers.X_DEVICE_CLASS));
        builder.deviceClass(mdc.deviceClass());

        mdc.deviceClass(httpRequest.getHeader(Headers.X_DEVICE_CLASS));
        builder.deviceClass(mdc.deviceClass());

        mdc.osVersion(httpRequest.getHeader(Headers.X_DEVICE_OS_VERSION));
        builder.osVersion(mdc.osVersion());

        mdc.deviceNetworkType(httpRequest.getHeader(Headers.X_DEVICE_NETWORK_TYPE));
        builder.deviceNetworkType(mdc.deviceNetworkType());

        mdc.deviceNetworkSpeedDown(convertToDouble(httpRequest.getHeader(Headers.X_DEVICE_NETWORK_SPEED_DOWN)));
        builder.deviceNetworkSpeedDown(mdc.deviceNetworkSpeedDown());

        mdc.deviceNetworkSpeedUp(convertToDouble(httpRequest.getHeader(Headers.X_DEVICE_NETWORK_SPEED_DOWN)));
        builder.deviceNetworkSpeedUp(mdc.deviceNetworkSpeedUp());

        mdc.deviceNetworkSpeedLatency(convertToDouble(httpRequest.getHeader(Headers.X_DEVICE_NETWORK_SPEED_LATENCY)));
        builder.deviceNetworkSpeedLatency(mdc.deviceNetworkSpeedLatency());

        // IPs ---
        mdc.remoteAddress(httpRequest.getRemoteAddr());
        builder.remoteAddress(mdc.remoteAddress());

        // deviceIp
        mdc.deviceIp(httpRequest.getHeader(Headers.CLIENT_IP));
        builder.deviceIp(mdc.deviceIp());


        mdc.userAgent(httpRequest.getHeader(Headers.USER_AGENT));
        builder.userAgent(mdc.userAgent());

        // language ---
        mdc.language(httpRequest.getHeader(Headers.ACCEPT_LANGUAGE));
        builder.language(mdc.language());

        mdc.country(httpRequest.getHeader(Headers.COUNTRY));
        builder.language(mdc.country());

        // specific
        builder.headers(extractOtherHeaders(httpRequest));
        return builder
                .build();
    }

    private static Map<String, String> extractOtherHeaders(final HttpServletRequest httpRequest) {
        final Map<String, String> result = new LinkedHashMap<>();
        final var                 names  = httpRequest.getHeaderNames().asIterator();
        while (names.hasNext()) {
            final var name = names.next();
            if (HEADERS.contains(name)) {
                continue;
            }
            final String value = httpRequest.getHeader(name);
            result.put(name, value);
        }
        return result;
    }
}
