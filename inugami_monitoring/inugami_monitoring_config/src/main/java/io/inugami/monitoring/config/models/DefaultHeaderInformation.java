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
import lombok.*;
import java.io.Serializable;

/**
 * DefaultHeaderInformation
 *
 * @author patrickguillerm
 * @since Jan 14, 2019
 */
@Getter
@Setter
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class DefaultHeaderInformation implements Serializable {
    private static final long serialVersionUID = -6659187410328161928L;

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
    private String warning;
    private String errorCode;
    private String errorException;
    private String errorMessage;
    private String errorMessageDetail;
    private String frontVersion;
    private String callFrom;
}
