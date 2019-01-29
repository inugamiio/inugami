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
package org.inugami.monitoring.sensors.defaults;

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.models.data.graphite.number.FloatNumber;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.tools.Comparators;
import org.inugami.monitoring.api.data.GenericMonitoringModel;
import org.inugami.monitoring.api.data.GenericMonitoringModelBuilder;
import org.inugami.monitoring.api.sensors.MonitoringSensor;
import org.inugami.monitoring.api.tools.GenericMonitoringModelTools;
import org.inugami.monitoring.api.tools.IntervalValues;

/**
 * MemorySensor
 * 
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
public class MemorySensor implements MonitoringSensor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static long          MEGA = 1024 * 1024;
    private final long                 interval;
    
    private final double               percentil;
    
    private final IntervalValues<Long> values;
    
    private final String               timeUnit;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MemorySensor() {
        interval = -1;
        percentil = -1;
        values = null;
        timeUnit = null;
    }
    
    public MemorySensor(long interval, String query, ConfigHandler<String, String> configuration) {
        super();
        this.interval = interval;
        this.percentil = configuration.grab("percentil", 0.95);
        values = new IntervalValues<>(this::extractMemoryUsage, configuration.grab("intervalValuesDelais", 1000));
        timeUnit = configuration.grabOrDefault("timeUnit", "");
    }
    
    @Override
    public MonitoringSensor buildInstance(long interval, String query, ConfigHandler<String, String> configuration) {
        return new MemorySensor(interval, query, configuration);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    private Long extractMemoryUsage() {
        long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return used / MEGA;
    }
    
    @Override
    public List<GenericMonitoringModel> process() {
        final List<Long> data = values.poll();
        data.sort(Comparators.longComparator);
        return buildGenericMonitoringModel(data);
    }
    
    private List<GenericMonitoringModel> buildGenericMonitoringModel(List<Long> data) {
        List<GenericMonitoringModel> result = new ArrayList<>();
        final GenericMonitoringModelBuilder builder = GenericMonitoringModelTools.initResultBuilder();
        final Long resultValue = GenericMonitoringModelTools.getPercentilValues(data, percentil);
        
        builder.setCounterType("system");
        builder.setService("memory");
        
        builder.setTimeUnit(GenericMonitoringModelTools.buildTimeUnit(timeUnit, interval));
        
        builder.setValue(GenericMonitoringModelTools.getPercentilValues(data, 0));
        builder.setValueType("min");
        result.add(builder.build());
        
        builder.setValue(GenericMonitoringModelTools.getPercentilValues(data, 1));
        builder.setValueType("max");
        result.add(builder.build());
        
        builder.setValue(GenericMonitoringModelTools.getPercentilValues(data, 0.95));
        builder.setValueType("p95");
        result.add(builder.build());
        
        builder.setValue(GenericMonitoringModelTools.getPercentilValues(data, 0.90));
        builder.setValueType("p90");
        result.add(builder.build());
        
        builder.setValue(GenericMonitoringModelTools.getPercentilValues(data, 0.75));
        builder.setValueType("p75");
        result.add(builder.build());
        
        builder.setValue(GenericMonitoringModelTools.getPercentilValues(data, 0.5));
        builder.setValueType("p50");
        result.add(builder.build());
        
        builder.setValue(data.stream().mapToLong(item -> item).average().orElse(0));
        builder.setValueType("avg");
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
