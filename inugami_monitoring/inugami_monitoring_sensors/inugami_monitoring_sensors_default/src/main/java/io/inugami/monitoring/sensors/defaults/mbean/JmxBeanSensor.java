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

import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.models.number.FloatNumber;
import io.inugami.framework.interfaces.models.number.LongNumber;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.monitoring.sensors.MonitoringSensor;
import io.inugami.monitoring.api.tools.GenericMonitoringModelTools;
import lombok.extern.slf4j.Slf4j;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

            final String       localQuery = configuration == null ? query : configuration.applyProperties(query);
            JmxBeanSensorQuery queryData  = convertFromJson(new String(localQuery.getBytes(), StandardCharsets.UTF_8));


            //@formatter:off
            final String normalizePath = queryData.getPath()
                                                  .replaceAll("[\\W]", "_")
                                                  .replaceAll("\"", "")
                                                  .replaceAll("'", "");
            //@formatter:on

            Loggers.METRICS.debug("processing mbean sensor for : {}", normalizePath);
            if (queryData.getAttribute() != null) {
                result.addAll(processAttribute(normalizePath, queryData));
            }

        }

        return result;
    }

    private JmxBeanSensorQuery convertFromJson(final String jsonValue) {
        try {
            return JsonMarshaller.getInstance().getDefaultObjectMapper().readValue(jsonValue, JmxBeanSensorQuery.class);
        } catch (Throwable e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
            return null;
        }
    }

    private List<GenericMonitoringModel> processAttribute(final String normalizePath,
                                                          final JmxBeanSensorQuery queryData) {
        Object value = null;
        try {
            value = jmx.getAttribute(new ObjectName(queryData.getPath()), queryData.getAttribute());
        } catch (Throwable e) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }

        return value == null ? new ArrayList<>()
                : convertToMonitoringModel(normalizePath, value, queryData.getAttribute());
    }

    // =========================================================================
    // Convertor
    // =========================================================================
    private List<GenericMonitoringModel> convertToMonitoringModel(final String normalizePath, final Object value,
                                                                  final String valueComeFrom) {

        final var builder = GenericMonitoringModelTools.initResultBuilder().toBuilder();

        builder.counterType("system");
        builder.service("jmx");
        builder.subService(String.join("@", normalizePath, valueComeFrom));
        builder.timeUnit(GenericMonitoringModelTools.buildTimeUnit(timeUnit, interval));
        builder.valueType("mbean");

        if ((value instanceof Byte) || (value instanceof Short) || (value instanceof Integer)
            || (value instanceof Long)) {
            builder.value(LongNumber.of((long) value));
        } else if ((value instanceof Float) || (value instanceof Double)) {
            builder.value(FloatNumber.of((long) value));
        } else if (value instanceof String valueStr) {
            builder.data(valueStr);
        } else {
            final var json = convertToJson(value);
            if (json != null) {
                builder.data(json);
            }
        }
        return List.of(builder.build());
    }

    private String convertToJson(final Object value) {
        try {
            return JsonMarshaller.getInstance().getDefaultObjectMapper().writeValueAsString(value);
        } catch (Throwable e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
            return null;
        }

    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public long getInterval() {
        return interval;
    }
}
