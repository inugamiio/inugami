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

import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.senders.MonitoringSender;
import io.inugami.api.monitoring.sensors.MonitoringSensor;
import io.inugami.api.processors.ConfigHandler;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Monitoring
 *
 * @author patrickguillerm
 * @since Jan 16, 2019
 */

/**
 * Monitoring
 *
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@Setter
@Getter
public class Monitoring {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private boolean enable;

    private String env;

    private String asset;

    private String hostname;

    private String instanceName;

    private String instanceNumber;

    private String applicationVersion;

    private Headers headers;

    private int maxSensorsTasksThreads;

    private ConfigHandler<String, String> properties;

    private List<MonitoringSender> senders;

    private List<MonitoringSensor> sensors;

    private List<MonitoringFilterInterceptor> interceptors;

    public static MonitoringBuilder builder() {
        return new MonitoringBuilder();
    }

    // =========================================================================
    // API
    // =========================================================================
    public List<MonitoringFilterInterceptor> getInterceptors() {
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        return interceptors;
    }

    public void refreshConfig(final ConfigHandler<String, String> configuration) {
        enable = configuration.grabBoolean("inugami.monitoring.enabled", true);
        env = configuration.grabOrDefault("env", "dev");
        hostname = configuration.grabOrDefault("hostname", null);
        instanceName = configuration.grabOrDefault("instanceName", null);
        instanceNumber = configuration.grabOrDefault("instanceNumber", null);
        applicationVersion = configuration.grabOrDefault("version", null);
        maxSensorsTasksThreads = configuration.grabInt("env", 10);
        asset = configuration.grabOrDefault("application.application", null);
        properties = configuration;

        if (headers == null) {
            headers = Headers.buildFromConfig(configuration);
        } else {
            headers.refreshConfig(configuration);
        }
    }


    public static class MonitoringBuilder {
        public Monitoring build() {
            return new Monitoring(enable, env, asset, hostname, instanceName, instanceNumber, applicationVersion, headers, maxSensorsTasksThreads, properties,
                                  senders == null ? new ArrayList<>() : senders,
                                  sensors == null ? new ArrayList<>() : sensors,
                                  interceptors == null ? new ArrayList<>() : interceptors);
        }

    }
}
