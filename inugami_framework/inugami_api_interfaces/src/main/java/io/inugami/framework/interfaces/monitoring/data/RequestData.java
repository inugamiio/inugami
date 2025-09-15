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
package io.inugami.framework.interfaces.monitoring.data;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * RequestInformation
 *
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@Getter
@Setter
public class RequestData implements Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long                  serialVersionUID = -7805638130853062376L;
    private              String                uri;
    private              String                contextPath;
    private              String                contentType;
    private              String                method;
    private              String                characterEncoding;
    private              String                content;
    private              Map<String, String[]> options;
    private              HttpServletRequest    request;
    private              HttpServletResponse   response;
    // server
    private              String                env;

    private String asset;

    private String hostname;

    private String instanceName;

    private String instanceNumber;

    // tracking
    private String correlationId;

    private String requestId;
    private String traceId;

    private String conversationId;

    private String sessionId;

    private String token;

    // application
    private String applicationVersion;

    private String service;

    // devices
    private String deviceIdentifier;

    private String deviceType;

    private String deviceClass;

    private String version;

    private String majorVersion;

    private String osVersion;

    private String deviceNetworkType;

    private Double deviceNetworkSpeedDown;

    private Double deviceNetworkSpeedUp;

    private Double deviceNetworkSpeedLatency;

    // IPs
    private String remoteAddress;

    private String deviceIp;

    private String userAgent;

    // language
    private String              language;
    private String              country;
    private Map<String, String> headers;
    private String              callFrom;

    public static class RequestDataBuilder {

        public RequestData.RequestDataBuilder addHeader(final String key,
                                                        final String value) {
            if (headers == null) {
                headers = new LinkedHashMap<>();
            }
            if (key != null && value != null) {
                headers.put(key.trim(), value);
            }
            return this;
        }

        public RequestData.RequestDataBuilder addOption(final String key,
                                                        final String... value) {
            if (options == null) {
                options = new LinkedHashMap<>();
            }
            if (key != null && value != null) {
                options.put(key.trim(), value);
            }
            return this;
        }
    }
}
