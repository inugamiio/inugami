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
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * CpuSensor
 *
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
@Slf4j
@SuppressWarnings({"java:S1172", "java:S3011"})
public class CpuSensor implements MonitoringSensor {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final long interval;

    private final double percentil;

    private final IntervalValues<Double> values;

    private final String timeUnit;

    private final OperatingSystemMXBean jmx = ManagementFactory.getOperatingSystemMXBean();

    private final Method getProcessCpuLoad;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CpuSensor() {
        interval = -1;
        percentil = -1;
        values = null;
        timeUnit = null;
        getProcessCpuLoad = null;
    }

    public CpuSensor(final long interval, final String query, final ConfigHandler<String, String> configuration) {
        super();
        this.interval = interval;
        this.percentil = configuration.grab("percentil", 0.95);
        values = IntervalValues.<Double>builder()
                               .handler(this::extractCpuUsage)
                               .interval(configuration.grab("intervalValuesDelais", 1000))
                               .build();
        timeUnit = configuration.grabOrDefault("timeUnit", "");

        Method cpuloadMethod = null;
        for (final Method method : jmx.getClass().getDeclaredMethods()) {
            if ("getProcessCpuLoad".equals(method.getName())) {
                cpuloadMethod = method;
                cpuloadMethod.setAccessible(true);
                break;
            }
        }
        getProcessCpuLoad = cpuloadMethod;

    }

    @Override
    public MonitoringSensor buildInstance(final long interval,
                                          final String query,
                                          final ConfigHandler<String, String> configuration) {
        return new CpuSensor(interval, query, configuration);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    private Double extractCpuUsage() {
        Double result = null;
        if (getProcessCpuLoad != null) {
            try {
                result = (Double) getProcessCpuLoad.invoke(jmx);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error(e.getMessage(), e);
            }
        }

        if (result == null) {
            result = jmx.getSystemLoadAverage();
        }
        return result * 100;
    }

    @Override
    public List<GenericMonitoringModel> process() {
        final List<Double> cpuValues   = values.poll();
        final Double       resultValue = GenericMonitoringModelTools.getPercentilValues(cpuValues, percentil, Comparators.DOUBLE_COMPARATOR);
        return resultValue == null ? null : buildGenericMonitoringModel(resultValue);
    }

    private List<GenericMonitoringModel> buildGenericMonitoringModel(final Double resultValue) {
        return List.of(GenericMonitoringModelTools.initResultBuilder()
                                                  .toBuilder()
                                                  .counterType("system")
                                                  .service("cpu")
                                                  .value(FloatNumber.of(resultValue))
                                                  .timeUnit(GenericMonitoringModelTools.buildTimeUnit(timeUnit, interval))
                                                  .valueType("percent")
                                                  .build());
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
        return "cpu";
    }

    @Override
    public long getInterval() {
        return interval;
    }

}
