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
package io.inugami.monitoring.config.models;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.ApplyIfNull;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;
import lombok.*;

import static io.inugami.monitoring.config.tools.ApplyStrategy.applyStrategy;

/**
 * DefaultHeaderInformation
 *
 * @author patrickguillerm
 * @since Jan 14, 2019
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
@Setter
@Getter
public class DefaultHeaderInformation implements PostProcessing<ConfigHandler<String, String>>, ApplyIfNull {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @XStreamAsAttribute
    private String correlationId;

    @XStreamAsAttribute
    private String requestId;

    @XStreamAsAttribute
    private String traceId;

    @XStreamAsAttribute
    private String conversationId;

    @XStreamAsAttribute
    private String token;

    @XStreamAsAttribute
    private String deviceIdentifier;

    @XStreamAsAttribute
    private String deviceType;

    @XStreamAsAttribute
    private String deviceSystem;

    @XStreamAsAttribute
    private String deviceClass;

    @XStreamAsAttribute
    private String deviceVersion;

    @XStreamAsAttribute
    private String deviceOsVersion;

    @XStreamAsAttribute
    private String deviceNetworkType;

    @XStreamAsAttribute
    private String deviceNetworkSpeedDown;

    @XStreamAsAttribute
    private String deviceNetworkSpeedUp;

    @XStreamAsAttribute
    private String deviceNetworkSpeedLatency;

    @XStreamAsAttribute
    private String deviceIp;

    @XStreamAsAttribute
    private String userAgent;

    @XStreamAsAttribute
    private String language;

    @XStreamAsAttribute
    private String country;

    @XStreamAsAttribute
    private String warning;

    @XStreamAsAttribute
    private String errorCode;

    @XStreamAsAttribute
    private String errorException;

    @XStreamAsAttribute
    private String errorMessage;

    @XStreamAsAttribute
    private String errorMessageDetail;

    @XStreamAsAttribute
    private String frontVersion;

    @XStreamAsAttribute
    private String callFrom;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public void postProcessing(final ConfigHandler<String, String> context) throws TechnicalException {

        //@formatter:off
        correlationId = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_CORRELATION_ID, correlationId, "x-correlation-id"));
        requestId = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_REQUEST_ID, requestId, "x-request-id"));
        traceId = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_REQUEST_ID, requestId, "x-b3-traceid"));
        conversationId = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_CONVERSATION_ID, conversationId, "x-conversation-id"));
        token = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_TOKEN, token, "Authorization"));
        deviceIdentifier = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_IDENTIFIER, deviceIdentifier, "x-device-identifier"));
        deviceType = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_TYPE, deviceType, "x-device-type"));
        deviceClass = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_CLASS, deviceClass, "x-device-class"));
        deviceVersion = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_VERSION, deviceVersion, "x-device-version"));
        deviceOsVersion = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_OS_VERSION, deviceOsVersion, "x-device-os-version"));
        deviceNetworkType = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_NETWORK_TYPE, deviceNetworkType, "x-device-network-type"));
        deviceNetworkSpeedDown = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_NETWORK_SPEED_DOWN, deviceNetworkSpeedDown, "x-device-network-speed-down"));
        deviceNetworkSpeedUp = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_NETWORK_SPEED_UP, deviceNetworkSpeedUp, "x-device-network-speed-up"));
        deviceNetworkSpeedLatency = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_NETWORK_SPEED_LATENCY, deviceNetworkSpeedLatency, "x-device-network-speed-latency"));
        deviceIp = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_IP, deviceIp, "x-device-ip"));
        userAgent = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_USER_AGENT, userAgent, "user-agent"));
        language = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_LANGUAGE, language, "accept-language"));

        warning = context.applyProperties(applyStrategy("inugami.monitoring.headers.warning", warning, "Warning"));
        errorCode = context.applyProperties(applyStrategy("inugami.monitoring.headers.error.code", errorCode, "errorCode"));
        errorException = context.applyProperties(applyStrategy("inugami.monitoring.headers.error.exception", errorException, "errorException"));
        errorMessage = context.applyProperties(applyStrategy("inugami.monitoring.headers.error.message", errorMessage, "errorMessage"));
        errorMessageDetail = context.applyProperties(applyStrategy("inugami.monitoring.headers.error.message.detail", errorMessageDetail, "errorMessageDetail"));
        frontVersion = context.applyProperties(applyStrategy("inugami.monitoring.headers.front.version", frontVersion, "x-front-version"));
        callFrom = context.applyProperties(applyStrategy("inugami.monitoring.headers.front.application", callFrom, "x-application"));
        //@formatter:on
    }


}
