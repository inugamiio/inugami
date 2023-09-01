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
package io.inugami.api.monitoring.models;

import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.processors.DefaultConfigHandler;
import lombok.*;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Headers
 *
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Headers {
    public static final String X_DEVICE_IDENTIFIER            = "x-device-identifier";
    public static final String X_CORRELATION_ID               = "x-correlation-id";
    public static final String X_CONVERSATION_ID              = "x-conversation-id";
    public static final String X_B_3_TRACEID                  = "x-b3-traceid";
    public static final String AUTHORIZATION                  = "Authorization";
    public static final String X_DEVICE_TYPE                  = "x-device-type";
    public static final String X_DEVICE_CLASS                 = "x-device-class";
    public static final String X_DEVICE_SYSTEM                = "x-device-system";
    public static final String X_DEVICE_VERSION               = "x-device-version";
    public static final String X_DEVICE_OS_VERSION            = "x-device-os-version";
    public static final String X_DEVICE_NETWORK_TYPE          = "x-device-network-type";
    public static final String X_DEVICE_NETWORK_SPEED_DOWN    = "x-device-network-speed-down";
    public static final String X_DEVICE_NETWORK_SPEED_UP      = "x-device-network-speed-up";
    public static final String X_DEVICE_NETWORK_SPEED_LATENCY = "x-device-network-speed-latency";
    public static final String CLIENT_IP                      = "clientIp";
    public static final String USER_AGENT                     = "User-Agent";
    public static final String ACCEPT_LANGUAGE                = "Accept-Language";
    public static final String COUNTRY                        = "country";
    public static final String WARNING                        = "Warning";
    public static final String ERROR_CODE                     = "errorCode";
    public static final String ERROR_EXCEPTION                = "errorException";
    public static final String ERROR_MESSAGE                  = "errorMessage";
    public static final String ERROR_MESSAGE_DETAIL           = "errorMessageDetail";
    public static final String X_FRONT_VERSION                = "x-front-version";
    public static final String X_APPLICATION                  = "x-application";

    private String correlationId;
    private String requestId;
    private String traceId;
    private String conversationId;
    private String token;
    private String deviceIdentifier;
    private String deviceType;
    private String deviceSystem;

    private String deviceClass;
    private String deviceVersion;
    private String deviceOsVersion;
    private String deviceNetworkType;
    private String deviceNetworkSpeedDown;
    private String deviceNetworkSpeedUp;
    private String deviceNetworkSpeedLatency;
    private String deviceIp;
    private String userAgent;
    private String language;
    private String country;

    private String      warning;
    private String      errorCode;
    private String      errorException;
    private String      errorMessage;
    private String      errorMessageDetail;
    private String      frontVersion;
    private String      callFrom;
    private Set<String> specificHeaders = new LinkedHashSet<>();

    public static Headers buildFromConfig(final ConfigHandler<String, String> configuration) {
        final Headers result = new Headers();
        result.refreshConfig(configuration);
        return result;
    }

    public Headers refreshConfig() {
        return refreshConfig(new DefaultConfigHandler());
    }

    /**
     * @param configuration
     * @return
     */
    public Headers refreshConfig(final ConfigHandler<String, String> configuration) {
        correlationId = configuration.getOrDefault("inugami.monitoring.headers.correlationId", X_CORRELATION_ID);
        requestId = configuration.getOrDefault("inugami.monitoring.headers.requestId", X_B_3_TRACEID);
        traceId = configuration.getOrDefault("inugami.monitoring.headers.traceId", X_B_3_TRACEID);
        conversationId = configuration.getOrDefault("inugami.monitoring.headers.conversationId", X_CONVERSATION_ID);
        token = configuration.getOrDefault("inugami.monitoring.headers.token", AUTHORIZATION);
        deviceIdentifier = configuration.getOrDefault("inugami.monitoring.headers.deviceIdentifier", X_DEVICE_IDENTIFIER);
        deviceType = configuration.getOrDefault("inugami.monitoring.headers.deviceType", X_DEVICE_TYPE);
        deviceClass = configuration.getOrDefault("inugami.monitoring.headers.deviceClass", X_DEVICE_CLASS);
        deviceSystem = configuration.getOrDefault("inugami.monitoring.headers.deviceSystem", X_DEVICE_SYSTEM);
        deviceVersion = configuration.getOrDefault("inugami.monitoring.headers.deviceVersion", X_DEVICE_VERSION);
        deviceOsVersion = configuration.getOrDefault("inugami.monitoring.headers.deviceOsVersion", X_DEVICE_OS_VERSION);
        deviceNetworkType = configuration.getOrDefault("inugami.monitoring.headers.deviceNetworkType", X_DEVICE_NETWORK_TYPE);
        deviceNetworkSpeedDown = configuration.getOrDefault("inugami.monitoring.headers.deviceNetworkSpeedDown", X_DEVICE_NETWORK_SPEED_DOWN);
        deviceNetworkSpeedUp = configuration.getOrDefault("inugami.monitoring.headers.deviceNetworkSpeedUp", X_DEVICE_NETWORK_SPEED_UP);
        deviceNetworkSpeedLatency = configuration.getOrDefault("inugami.monitoring.headers.deviceNetworkSpeedLatency", X_DEVICE_NETWORK_SPEED_LATENCY);
        deviceIp = configuration.getOrDefault("inugami.monitoring.headers.deviceIp", CLIENT_IP);
        userAgent = configuration.getOrDefault("inugami.monitoring.headers.userAgent", USER_AGENT);
        language = configuration.getOrDefault("inugami.monitoring.headers.language", ACCEPT_LANGUAGE);
        country = configuration.getOrDefault("inugami.monitoring.headers.country", COUNTRY);
        warning = configuration.getOrDefault("inugami.monitoring.headers.warning", WARNING);
        errorCode = configuration.getOrDefault("inugami.monitoring.headers.error.code", ERROR_CODE);
        errorException = configuration.getOrDefault("inugami.monitoring.headers.error.exception", ERROR_EXCEPTION);
        errorMessage = configuration.getOrDefault("inugami.monitoring.headers.error.message", ERROR_MESSAGE);
        errorMessageDetail = configuration.getOrDefault("inugami.monitoring.headers.error.message.detail", ERROR_MESSAGE_DETAIL);
        frontVersion = configuration.getOrDefault("inugami.monitoring.headers.front.version", X_FRONT_VERSION);
        callFrom = configuration.getOrDefault("inugami.monitoring.headers.front.application", X_APPLICATION);


        final String specifics = configuration.getOrDefault("inugami.monitoring.headers.specifics", "");
        if (specifics != null && !specifics.isEmpty()) {
            specificHeaders.addAll(Arrays.asList(specifics.split(","))
                                         .stream()
                                         .map(String::trim)
                                         .collect(Collectors.toList()));
        }

        return this;
    }
}

