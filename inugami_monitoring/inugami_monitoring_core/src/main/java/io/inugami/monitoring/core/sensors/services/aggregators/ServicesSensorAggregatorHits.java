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

import io.inugami.api.models.data.graphite.number.GraphiteNumber;
import io.inugami.api.models.data.graphite.number.LongNumber;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.monitoring.models.GenericMonitoringModelBuilder;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.spi.SpiPriority;
import io.inugami.monitoring.api.tools.GenericMonitoringModelTools;
import io.inugami.monitoring.core.sensors.services.ServiceValueTypes;
import io.inugami.monitoring.core.sensors.services.ServicesSensorAggregator;

/**
 * ServicesSensorAggregatorHits
 * 
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
@SpiPriority(0)
public class ServicesSensorAggregatorHits implements ServicesSensorAggregator {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public boolean accept(final GenericMonitoringModel data, final ConfigHandler<String, String> configuration) {
        return ServiceValueTypes.HITS.getKeywork().equals(data.getCounterType());
    }
    
    @Override
    public List<GenericMonitoringModel> compute(final GenericMonitoringModel data, final List<GraphiteNumber> values,
                                                final ConfigHandler<String, String> configuration) {
        final String timeUnit = configuration.grabOrDefault("timeUnit", "min");
        final GenericMonitoringModelBuilder builder = new GenericMonitoringModelBuilder(data);
        
        builder.setTimeUnit(timeUnit);
        builder.setValueType("count");
        builder.setValue(sum(values));
        
        return GenericMonitoringModelTools.buildSingleResult(builder);
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    private GraphiteNumber sum(final List<GraphiteNumber> values) {
        long result = 0;
        for (final GraphiteNumber value : Optional.ofNullable(values).orElse(new ArrayList<>())) {
            result += value.toLong();
        }
        return new LongNumber(result);
    }
}
