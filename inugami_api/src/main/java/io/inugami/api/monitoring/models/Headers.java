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

    private String      correlationId;
    private String      requestId;
    private String      traceId;
    private String      conversationId;
    private String      token;
    private String      deviceIdentifier;
    private String      deviceType;
    private String      deviceClass;
    private String      deviceVersion;
    private String      deviceOsVersion;
    private String      deviceNetworkType;
    private String      deviceNetworkSpeedDown;
    private String      deviceNetworkSpeedUp;
    private String      deviceNetworkSpeedLatency;
    private String      deviceIp;
    private String      userAgent;
    private String      language;
    private String      country;
    private Set<String> specificHeaders = new LinkedHashSet<>();

    public static Headers buildFromConfig(final ConfigHandler<String, String> configuration) {
        Headers result = new Headers();
        result.refreshConfig(configuration);
        return result;
    }

    public Headers refreshConfig() {
        return refreshConfig(new DefaultConfigHandler());
    }

    public Headers refreshConfig(final ConfigHandler<String, String> configuration) {
        correlationId = configuration.getOrDefault("inugami.monitoring.headers.correlationId", "x-correlation-id");
        requestId = configuration.getOrDefault("inugami.monitoring.headers.requestId", "x-b3-traceid");
        traceId = configuration.getOrDefault("inugami.monitoring.headers.traceId", "x-b3-traceid");
        conversationId = configuration.getOrDefault("inugami.monitoring.headers.conversationId", "x-conversation-id");
        token = configuration.getOrDefault("inugami.monitoring.headers.token", "Authorization");
        deviceIdentifier = configuration.getOrDefault("inugami.monitoring.headers.deviceIdentifier", "x-device-identifier");
        deviceType = configuration.getOrDefault("inugami.monitoring.headers.deviceType", "x-device-type");
        deviceClass = configuration.getOrDefault("inugami.monitoring.headers.deviceClass", "x-device-class");
        deviceVersion = configuration.getOrDefault("inugami.monitoring.headers.deviceVersion", "x-device-version");
        deviceOsVersion = configuration.getOrDefault("inugami.monitoring.headers.deviceOsVersion", "x-device-os-version");
        deviceNetworkType = configuration.getOrDefault("inugami.monitoring.headers.deviceNetworkType", "x-device-network-type");
        deviceNetworkSpeedDown = configuration.getOrDefault("inugami.monitoring.headers.deviceNetworkSpeedDown", "x-device-network-speed-down");
        deviceNetworkSpeedUp = configuration.getOrDefault("inugami.monitoring.headers.deviceNetworkSpeedUp", "x-device-network-speed-up");
        deviceNetworkSpeedLatency = configuration.getOrDefault("inugami.monitoring.headers.deviceNetworkSpeedLatency", "x-device-network-speed-latency");
        deviceIp = configuration.getOrDefault("inugami.monitoring.headers.deviceIp", "clientIp");
        userAgent = configuration.getOrDefault("inugami.monitoring.headers.userAgent", "User-Agent");
        language = configuration.getOrDefault("inugami.monitoring.headers.language", "Accept-Language");
        country = configuration.getOrDefault("inugami.monitoring.headers.country", "country");


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
