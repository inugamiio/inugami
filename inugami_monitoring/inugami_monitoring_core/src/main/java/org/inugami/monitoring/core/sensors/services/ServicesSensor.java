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
package org.inugami.monitoring.core.sensors.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.inugami.api.models.Tuple;
import org.inugami.api.models.data.graphite.number.GraphiteNumber;
import org.inugami.api.models.tools.BlockingQueue;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.commons.spi.SpiLoader;
import org.inugami.monitoring.api.data.GenericMonitoringModel;
import org.inugami.monitoring.api.sensors.MonitoringSensor;

/**
 * ServicesSensor
 * 
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
public class ServicesSensor implements MonitoringSensor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static BlockingQueue<GenericMonitoringModel> BUFFER      = new BlockingQueue<>();
    
    private static final List<ServicesSensorAggregator>        AGGREGATORS = new SpiLoader().loadSpiServicesByPriority(ServicesSensorAggregator.class);
    
    private static long                                        interval;
    
    private ConfigHandler<String, String>                      configuration;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public MonitoringSensor buildInstance(long interval, String query, ConfigHandler<String, String> configuration) {
        ServicesSensor.interval = interval;
        this.configuration = configuration;
        return this;
    }
    
    //
    @Override
    public long getInterval() {
        return interval;
    }
    
    @Override
    public String getName() {
        return "servicesSensor";
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public List<GenericMonitoringModel> process() {
        final List<GenericMonitoringModel> result = new ArrayList<>();
        final List<GenericMonitoringModel> data = BUFFER.pollAll();
        final Map<GenericMonitoringModel, List<GraphiteNumber>> reducedValues = reduceData(data);
        
        for (Map.Entry<GenericMonitoringModel, List<GraphiteNumber>> entry : reducedValues.entrySet()) {
            result.addAll(computeValue(entry.getKey(), entry.getValue()));
        }
        
        return result;
    }
    
    private Map<GenericMonitoringModel, List<GraphiteNumber>> reduceData(List<GenericMonitoringModel> data) {
        Map<String, Tuple<GenericMonitoringModel, List<GraphiteNumber>>> localBuffer = new HashMap<>();
        
        for (GenericMonitoringModel item : data) {
            Tuple<GenericMonitoringModel, List<GraphiteNumber>> saved = localBuffer.get(item.getNonTemporalHash());
            if (saved == null) {
                List<GraphiteNumber> values = new ArrayList<>();
                values.add(item.getValue());
                saved = new Tuple<>(item, values);
                localBuffer.put(item.getNonTemporalHash(), saved);
            }
            else if (item.getValue() != null) {
                saved.getValue().add(item.getValue());
            }
        }
        
        Map<GenericMonitoringModel, List<GraphiteNumber>> result = new HashMap<>();
        for (Map.Entry<String, Tuple<GenericMonitoringModel, List<GraphiteNumber>>> entry : localBuffer.entrySet()) {
            result.put(entry.getValue().getKey(), entry.getValue().getValue());
        }
        return result;
    }
    
    private List<GenericMonitoringModel> computeValue(GenericMonitoringModel data, List<GraphiteNumber> values) {
        final List<GenericMonitoringModel> result = new ArrayList<>();
        for (ServicesSensorAggregator aggregator : AGGREGATORS) {
            if (aggregator.accept(data, configuration)) {
                final List<GenericMonitoringModel> aggregated = aggregator.compute(data, values, configuration);
                if (aggregated != null) {
                    result.addAll(aggregated);
                }
            }
        }
        return result;
    }
    
    // =========================================================================
    // ADD DATA
    // =========================================================================
    public static synchronized void addData(List<GenericMonitoringModel> data) {
        if (data != null) {
            BUFFER.addAll(data);
        }
    }
    
}
