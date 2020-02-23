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
package org.inugami.monitoring.core.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.inugami.api.ctx.BootstrapContext;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.monitoring.MonitoringLoaderSpi;
import org.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import org.inugami.api.monitoring.models.Monitoring;
import org.inugami.api.monitoring.senders.MonitoringSender;
import org.inugami.api.monitoring.sensors.MonitoringSensor;
import org.inugami.api.spi.SpiLoader;
import org.inugami.monitoring.core.context.sensors.SensorsIntervalManagerTask;

/**
 * MonitoringContext
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2018
 */
public class MonitoringContext implements BootstrapContext<Void> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Monitoring                     CONFIG        = loadConfiguration();
    
    private final Map<Long, SensorsIntervalManagerTask> managersTasks = new HashMap<>();
    
    // =========================================================================
    // BOOSTRAP
    // =========================================================================
    @Override
    public void bootrap(final Void ctx) {
        if (CONFIG != null) {
            if (CONFIG.isEnable()) {
                managersTasks.putAll(buildSensorsTasks(CONFIG.getSensors()));
            }
            
            //@formatter:off
            managersTasks.entrySet()
                         .stream()
                         .map(Map.Entry::getValue)
                         .forEach(task -> task.bootrap(this));
            //@formatter:on
        }
    };
    
    private static Monitoring loadConfiguration() {
        final MonitoringLoaderSpi loader = new SpiLoader().loadSpiSingleService(MonitoringLoaderSpi.class);
        return loader == null ? null : loader.load();
    }
    
    // =========================================================================
    // SHUTDOWN
    // =========================================================================
    @Override
    public void shutdown(final Void ctx) {
        //@formatter:off
        if(CONFIG != null) {
            managersTasks.entrySet()
                         .stream()
                         .map(Map.Entry::getValue)
                         .forEach(task -> task.shutdown(this));

            CONFIG.getSenders().forEach(MonitoringSender::shutdown);
        }
        //@formatter:on
    };
    
    // =========================================================================
    // BUILDER
    // =========================================================================
    private Map<Long, SensorsIntervalManagerTask> buildSensorsTasks(final List<MonitoringSensor> sensors) {
        final Map<Long, SensorsIntervalManagerTask> result = new HashMap<>();
        if (sensors == null) {
            return result;
        }
        
        for (final MonitoringSensor sensor : sensors) {
            SensorsIntervalManagerTask task = result.get(sensor.getInterval());
            if (task == null) {
                if (sensor.getInterval() > 100) {
                    task = new SensorsIntervalManagerTask(CONFIG.getMaxSensorsTasksThreads(), sensor.getInterval(),
                                                          CONFIG.getSenders());
                    result.put(sensor.getInterval(), task);
                }
                else {
                    Loggers.CONFIG.error("sensor interval must be higher or equals than 100ms!({})", sensor.getName());
                }
                
            }
            if (task != null) {
                task.add(sensor);
            }
            
        }
        return result;
    }
    
    // =========================================================================
    // BUILDER
    // =========================================================================
    public Monitoring getConfig() {
        return CONFIG;
    }
    
    public List<MonitoringSender> getSenders() {
        return CONFIG.getSenders();
    }
    
    public List<MonitoringSensor> getSensors() {
        return CONFIG.getSensors();
    }
    
    public List<MonitoringFilterInterceptor> getInterceptors() {
        return CONFIG.getInterceptors();
    }
    
}
