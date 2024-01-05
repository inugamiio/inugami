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
package io.inugami.framework.interfaces.monitoring.models;

import lombok.experimental.UtilityClass;

/**
 * Headers
 *
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
@UtilityClass
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
}

