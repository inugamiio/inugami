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
package org.inugami.monitoring.sensors.defaults.system;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.inugami.api.loggers.Loggers;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.tools.Comparators;
import org.inugami.monitoring.api.data.GenericMonitoringModel;
import org.inugami.monitoring.api.data.GenericMonitoringModelBuilder;
import org.inugami.monitoring.api.sensors.MonitoringSensor;
import org.inugami.monitoring.api.tools.GenericMonitoringModelTools;
import org.inugami.monitoring.api.tools.IntervalValues;

/**
 * CpuSensor
 * 
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
public class CpuSensor implements MonitoringSensor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final long                   interval;
    
    private final double                 percentil;
    
    private final IntervalValues<Double> values;
    
    private final String                 timeUnit;
    
    private final OperatingSystemMXBean  jmx = ManagementFactory.getOperatingSystemMXBean();
    
    private final Method                 getProcessCpuLoad;
    
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
        values = new IntervalValues<>(this::extractCpuUsage, configuration.grab("intervalValuesDelais", 1000));
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
    public MonitoringSensor buildInstance(final long interval, final String query,
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
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }
        
        if (result == null) {
            result = jmx.getSystemLoadAverage();
        }
        return result * 100;
    }
    
    @Override
    public List<GenericMonitoringModel> process() {
        final List<Double> cpuValues = values.poll();
        final Double resultValue = GenericMonitoringModelTools.getPercentilValues(cpuValues, percentil,
                                                                                  Comparators.doubleComparator);
        return resultValue == null ? null : buildGenericMonitoringModel(resultValue);
    }
    
    private List<GenericMonitoringModel> buildGenericMonitoringModel(final Double resultValue) {
        final GenericMonitoringModelBuilder builder = GenericMonitoringModelTools.initResultBuilder();
        
        builder.setCounterType("system");
        builder.setService("cpu");
        
        builder.setValue(resultValue);
        builder.setTimeUnit(GenericMonitoringModelTools.buildTimeUnit(timeUnit, interval));
        builder.setValueType("percent");
        
        return GenericMonitoringModelTools.buildSingleResult(builder);
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
