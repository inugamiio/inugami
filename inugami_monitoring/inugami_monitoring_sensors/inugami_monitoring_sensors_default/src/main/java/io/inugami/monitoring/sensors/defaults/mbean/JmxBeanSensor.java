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
package io.inugami.monitoring.sensors.defaults.mbean;

import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.monitoring.models.GenericMonitoringModelDTO;
import io.inugami.api.monitoring.sensors.MonitoringSensor;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.monitoring.api.tools.GenericMonitoringModelTools;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S5361"})
public class JmxBeanSensor implements MonitoringSensor {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final long interval;

    private final String query;

    private final MBeanServer jmx = ManagementFactory.getPlatformMBeanServer();

    private final String timeUnit;

    private final ConfigHandler<String, String> configuration;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public JmxBeanSensor() {
        super();
        interval = 60000L;
        query = null;
        timeUnit = null;
        configuration = null;
    }

    public JmxBeanSensor(final long interval, final String query,
                         final ConfigHandler<String, String> configuration) {
        this.interval = interval;
        this.query = query;
        this.timeUnit = null;
        this.configuration = configuration;

    }

    @Override
    public MonitoringSensor buildInstance(final long interval, final String query,
                                          final ConfigHandler<String, String> configuration) {

        return new JmxBeanSensor(interval, query, configuration);
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public List<GenericMonitoringModel> process() {
        final List<GenericMonitoringModel> result = new ArrayList<>();
        if (query != null) {

            final String localQuery = configuration == null ? query : configuration.applyProperties(query);
            final JmxBeanSensorQuery queryData = new JmxBeanSensorQuery().convertToObject(localQuery.getBytes(),
                                                                                          StandardCharsets.UTF_8);

            //@formatter:off
            final String normalizePath = queryData.getPath()
                                                  .replaceAll("[\\W]", "_")
                                                  .replaceAll("\"", "")
                                                  .replaceAll("'", "");
            //@formatter:on

            Loggers.METRICS.debug("processing mbean sensor for : {}", normalizePath);
            if (queryData.getMbeanAttibute() != null) {
                result.addAll(processAttribute(normalizePath, queryData));
            }

        }

        return result;
    }

    private List<GenericMonitoringModel> processAttribute(final String normalizePath,
                                                          final JmxBeanSensorQuery queryData) {
        Object value = null;
        try {
            value = jmx.getAttribute(new ObjectName(queryData.getPath()), queryData.getMbeanAttibute());
        } catch (InstanceNotFoundException | AttributeNotFoundException | MalformedObjectNameException
                 | ReflectionException | MBeanException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }

        return value == null ? new ArrayList<>()
                : convertToMonitoringModel(normalizePath, value, queryData.getMbeanAttibute());
    }

    // =========================================================================
    // Convertor
    // =========================================================================
    private List<GenericMonitoringModel> convertToMonitoringModel(final String normalizePath, final Object value,
                                                                  final String valueComeFrom) {

        final GenericMonitoringModelDTO.GenericMonitoringModelDTOBuilder builder = GenericMonitoringModelTools.initResultBuilder();

        builder.counterType("system");
        builder.service("jmx");
        builder.subService(String.join("@", normalizePath, valueComeFrom));
        builder.timeUnit(GenericMonitoringModelTools.buildTimeUnit(timeUnit, interval));
        builder.valueType("mbean");

        if ((value instanceof Byte) || (value instanceof Short) || (value instanceof Integer)
            || (value instanceof Long)) {
            builder.addValue((long) value);
        } else if ((value instanceof Float) || (value instanceof Double)) {
            builder.addValue((double) value);
        } else if (value instanceof JsonObject) {
            builder.data(((JsonObject) value).convertToJson());
        } else {
            builder.data(String.valueOf(value));
        }
        return List.of(builder.build());
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public long getInterval() {
        return interval;
    }
}
