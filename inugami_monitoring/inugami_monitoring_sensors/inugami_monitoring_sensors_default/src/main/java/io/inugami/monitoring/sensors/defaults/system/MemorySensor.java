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
package io.inugami.monitoring.sensors.defaults.system;

import io.inugami.framework.api.tools.Comparators;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.models.number.FloatNumber;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.monitoring.sensors.MonitoringSensor;
import io.inugami.monitoring.api.tools.GenericMonitoringModelTools;
import io.inugami.monitoring.api.tools.IntervalValues;

import java.util.ArrayList;
import java.util.List;

/**
 * MemorySensor
 *
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
@SuppressWarnings({"java:S1172"})
public class MemorySensor implements MonitoringSensor {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long                 MEGA = 1024L * 1024L;
    private final        long                 interval;
    private final        IntervalValues<Long> values;
    private final        String               timeUnit;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MemorySensor() {
        interval = -1;
        values = null;
        timeUnit = null;
    }

    public MemorySensor(final long interval, final String query, final ConfigHandler<String, String> configuration) {
        super();
        this.interval = interval;
        values = IntervalValues.<Long>builder()
                               .handler(this::extractMemoryUsage)
                               .interval(configuration.grab("intervalValuesDelais", 1000))
                               .build();

        timeUnit = configuration.grabOrDefault("timeUnit", "");
    }

    @Override
    public MonitoringSensor buildInstance(final long interval, final String query,
                                          final ConfigHandler<String, String> configuration) {
        return new MemorySensor(interval, query, configuration);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    private Long extractMemoryUsage() {
        final long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return used / MEGA;
    }

    @Override
    public List<GenericMonitoringModel> process() {
        final List<Long> data = values.poll();
        data.sort(Comparators.LONG_COMPARATOR);
        return buildGenericMonitoringModel(data);
    }

    private List<GenericMonitoringModel> buildGenericMonitoringModel(final List<Long> data) {
        final List<GenericMonitoringModel> result  = new ArrayList<>();
        final var                          builder = GenericMonitoringModelTools.initResultBuilder().toBuilder();

        builder.counterType("system");
        builder.service("memory");

        builder.timeUnit(GenericMonitoringModelTools.buildTimeUnit(timeUnit, interval));

        builder.value(FloatNumber.of(GenericMonitoringModelTools.getPercentilValues(data, 0)));
        builder.valueType("min");
        result.add(builder.build());

        builder.value(FloatNumber.of(GenericMonitoringModelTools.getPercentilValues(data, 1)));
        builder.valueType("max");
        result.add(builder.build());

        builder.value(FloatNumber.of(GenericMonitoringModelTools.getPercentilValues(data, 0.95)));
        builder.valueType("p95");
        result.add(builder.build());

        builder.value(FloatNumber.of(GenericMonitoringModelTools.getPercentilValues(data, 0.90)));
        builder.valueType("p90");
        result.add(builder.build());

        builder.value(FloatNumber.of(GenericMonitoringModelTools.getPercentilValues(data, 0.75)));
        builder.valueType("p75");
        result.add(builder.build());

        builder.value(FloatNumber.of(GenericMonitoringModelTools.getPercentilValues(data, 0.5)));
        builder.valueType("p50");
        result.add(builder.build());

        builder.value(FloatNumber.of(GenericMonitoringModelTools.getPercentilValues(data, 0)));
        builder.valueType("avg");
        result.add(builder.build());

        return result;
    }

    @Override
    public void shutdown() {
        values.shutdown(null);
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getName() {
        return "memory";
    }

    @Override
    public long getInterval() {
        return interval;
    }
}
