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
package io.inugami.monitoring.springboot.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 2026-01-07
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ConfigurationProperties(prefix = "inugami.monitoring", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class InugamiMonitoringProperties {
    private InugamiMonitoringPropertiesInternal     internal     = new InugamiMonitoringPropertiesInternal();
    private InugamiMonitoringPropertiesInterceptors interceptors = new InugamiMonitoringPropertiesInterceptors();
    @Builder.Default
    private Map<String, Map<String, String>>        sensors      = new LinkedHashMap<>();
    @Builder.Default
    private Map<String, Map<String, String>>        senders      = new LinkedHashMap<>();

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class InugamiMonitoringPropertiesInternal {
        @Builder.Default
        private Long interval = 60000L;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class InugamiMonitoringPropertiesInterceptors {
        @Builder.Default
        private KpiInterceptors kpi = KpiInterceptors.builder().build();
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class KpiInterceptors {
        @Builder.Default
        private boolean enabled = false;
        private String  skipUrl;
    }

}
