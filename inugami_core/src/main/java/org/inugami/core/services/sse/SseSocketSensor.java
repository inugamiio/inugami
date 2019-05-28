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
package org.inugami.core.services.sse;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.inugami.api.monitoring.models.GenericMonitoringModel;
import org.inugami.api.monitoring.models.GenericMonitoringModelBuilder;
import org.inugami.api.monitoring.sensors.MonitoringSensor;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.monitoring.api.tools.GenericMonitoringModelTools;

/**
 * SseSocketSensor
 * 
 * @author patrick_guillerm
 * @since 21 janv. 2019
 */
public class SseSocketSensor implements MonitoringSensor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static AtomicInteger counter = new AtomicInteger();
    
    private final long                 interval;
    
    private final String               timeUnit;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SseSocketSensor() {
        interval = 60000;
        timeUnit = null;
    }
    
    public SseSocketSensor(final long interval, final String query, final ConfigHandler<String, String> configuration) {
        this.interval = interval;
        timeUnit = configuration.grabOrDefault("timeUnit", "");
    }
    
    @Override
    public MonitoringSensor buildInstance(final long interval, final String query,
                                          final ConfigHandler<String, String> configuration) {
        return new SseSocketSensor(interval, query, configuration);
    }
    
    @Override
    public long getInterval() {
        return interval;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public List<GenericMonitoringModel> process() {
        final GenericMonitoringModelBuilder builder = GenericMonitoringModelTools.initResultBuilder();
        builder.setCounterType("system");
        builder.setService("sseSockets");
        
        builder.setValue(counter.get());
        builder.setTimeUnit(GenericMonitoringModelTools.buildTimeUnit(timeUnit, interval));
        builder.setValueType("hits");
        
        return GenericMonitoringModelTools.buildSingleResult(builder);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    static int decrementAndGet() {
        int result = counter.decrementAndGet();
        if (result < 0) {
            result = 0;
            counter.set(0);
        }
        return result;
    }
    
    static int incrementAndGet() {
        return counter.incrementAndGet();
    }
    
    static int get() {
        return counter.get();
    }
}
