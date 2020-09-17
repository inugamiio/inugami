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
package io.inugami.monitoring.core.sensors.services.aggregators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.inugami.api.models.data.graphite.number.FloatNumber;
import io.inugami.api.models.data.graphite.number.GraphiteNumber;
import io.inugami.api.models.data.graphite.number.LongNumber;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.monitoring.models.GenericMonitoringModelBuilder;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.spi.SpiPriority;
import io.inugami.monitoring.core.sensors.services.ServiceValueTypes;
import io.inugami.monitoring.core.sensors.services.ServicesSensorAggregator;

/**
 * ServicesSensorAggregatorResponseTime
 * 
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
@SpiPriority(0)
public class ServicesSensorAggregatorResponseTime implements ServicesSensorAggregator {
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public boolean accept(final GenericMonitoringModel data, final ConfigHandler<String, String> configuration) {
        return ServiceValueTypes.RESPONSE_TIME.getKeywork().equals(data.getCounterType());
    }
    
    @Override
    public List<GenericMonitoringModel> compute(final GenericMonitoringModel data, final List<GraphiteNumber> values,
                                                final ConfigHandler<String, String> configuration) {
        final String timeUnit = configuration.grabOrDefault("timeUnit", "min");
        final List<GenericMonitoringModel> result = new ArrayList<>();
        final GenericMonitoringModelBuilder builder = new GenericMonitoringModelBuilder(data);
        
        final List<Long> sortedValues = sortsValues(values);
        
        if (!sortedValues.isEmpty()) {
            final int size = sortedValues.size();
            builder.setTimeUnit(timeUnit);
            
            builder.setValueType("min");
            builder.setValue(new LongNumber(values.get(percentil(0, size)).toLong()));
            result.add(builder.build());
            
            builder.setValueType("max");
            builder.setValue(new LongNumber(values.get(percentil(1, size)).toLong()));
            result.add(builder.build());
            
            builder.setValueType("p99");
            builder.setValue(new LongNumber(values.get(percentil(0.99, size)).toLong()));
            result.add(builder.build());
            
            builder.setValueType("p95");
            builder.setValue(new LongNumber(values.get(percentil(0.95, size)).toLong()));
            result.add(builder.build());
            
            builder.setValueType("p90");
            builder.setValue(new LongNumber(values.get(percentil(0.90, size)).toLong()));
            result.add(builder.build());
            
            builder.setValueType("p75");
            builder.setValue(new LongNumber(values.get(percentil(0.75, size)).toLong()));
            result.add(builder.build());
            
            builder.setValueType("p50");
            builder.setValue(new LongNumber(values.get(percentil(0.5, size)).toLong()));
            result.add(builder.build());
            
            builder.setValueType("avg");
            builder.setValue(new FloatNumber(average(sortedValues)));
            result.add(builder.build());
        }
        
        return result;
    }
    
    private List<Long> sortsValues(final List<GraphiteNumber> values) {
        final List<Long> result = new ArrayList<>();
        for (final GraphiteNumber value : Optional.ofNullable(values).orElse(new ArrayList<>())) {
            if (value != null) {
                result.add(value.toLong());
            }
        }
        
        result.sort((ref, value) -> new Long(ref).compareTo(value));
        return result;
    }
    
    private int percentil(final double percentil, final int size) {
        int result = 0;
        if (percentil < 0.000001) {
            result = 0;
        }
        else if (percentil >= 1) {
            result = size - 1;
        }
        else {
            result = (int) (size * percentil);
        }
        
        if (result >= size) {
            result = size - 1;
        }
        return result;
    }
    
    private double average(final List<Long> sortedValues) {
        long sumValues = 0;
        for (final Long item : sortedValues) {
            sumValues += item;
        }
        
        final double size = 0.0 + sortedValues.size();
        return sumValues / size;
    }
    
}
