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
package io.inugami.monitoring.core.context;

import io.inugami.framework.api.processors.DefaultConfigHandler;
import io.inugami.framework.interfaces.ctx.BootstrapContext;
import io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.framework.interfaces.monitoring.models.Monitoring;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSender;
import io.inugami.framework.interfaces.monitoring.sensors.MonitoringSensor;
import io.inugami.monitoring.core.context.sensors.SensorsIntervalManagerTask;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNull;

/**
 * MonitoringContext
 *
 * @author patrick_guillerm
 * @since 27 déc. 2018
 */
@Slf4j
@Builder
public class MonitoringContext implements BootstrapContext<Void> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private       Monitoring                            config;
    @Builder.Default
    private final Map<Long, SensorsIntervalManagerTask> managersTasks = new HashMap<>();

    // =========================================================================
    // BOOSTRAP
    // =========================================================================
    @Override
    public void initialize(final Void ctx) {
        if (config == null) {
            config = Monitoring.builder().build();
        }
        config.setProperties(applyIfNull(config.getProperties(), () -> new DefaultConfigHandler()));
        config.setSenders(applyIfNull(config.getSenders(), () -> new ArrayList<>()));
        config.setSensors(applyIfNull(config.getSensors(), () -> new ArrayList<>()));
        config.setInterceptors(applyIfNull(config.getInterceptors(), () -> new ArrayList<>()));

        if (config.isEnable()) {
            managersTasks.putAll(buildSensorsTasks(config.getSensors()));
        }

        managersTasks.entrySet().stream().map(Map.Entry::getValue).forEach(task -> task.initialize(this));

    }


    // =========================================================================
    // SHUTDOWN
    // =========================================================================
    @Override
    public void shutdown(final Void ctx) {
        //@formatter:off
        if(config != null) {
            managersTasks.entrySet()
                         .stream()
                         .map(Map.Entry::getValue)
                         .forEach(task -> task.shutdown(this));

            config.getSenders().forEach(MonitoringSender::shutdown);
        }
        //@formatter:on
    }


    // =========================================================================
    // BUILDER
    // =========================================================================
    private Map<Long, SensorsIntervalManagerTask> buildSensorsTasks(final List<MonitoringSensor> sensors) {
        final Map<Long, SensorsIntervalManagerTask> result = new HashMap<>();
        if (sensors == null) {
            return result;
        }

        for (final MonitoringSensor sensor : sensors) {
            SensorsIntervalManagerTask task = result.get(sensor.getInterval());
            if (task == null) {
                if (sensor.getInterval() > 100) {
                    task = new SensorsIntervalManagerTask(config.getMaxSensorsTasksThreads(), sensor.getInterval(), config.getSenders());
                    result.put(sensor.getInterval(), task);
                } else {
                    log.error("sensor interval must be higher or equals than 100ms!({})", sensor.getName());
                }

            }
            if (task != null) {
                task.add(sensor);
            }

        }
        return result;
    }

    // =========================================================================
    // BUILDER
    // =========================================================================
    public Monitoring getConfig() {
        return config;
    }
    public List<MonitoringFilterInterceptor> getInterceptors() {
        return Optional.ofNullable(config.getInterceptors()).orElse(List.of());
    }


    // =========================================================================
    // UTILS
    // =========================================================================
    public Map<String, String> getTrackingInformation() {
        return MonitoringContextUtils.getTrackingInformation(config);
    }
}
