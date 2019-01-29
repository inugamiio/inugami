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
import org.inugami.monitoring.api.data.config.Monitoring;
import org.inugami.monitoring.api.interceptors.MonitoringFilterInterceptor;
import org.inugami.monitoring.api.interceptors.RequestContext;
import org.inugami.monitoring.api.senders.MonitoringSender;
import org.inugami.monitoring.api.sensors.MonitoringSensor;
import org.inugami.monitoring.config.loader.ConfigurationLoader;
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
    private final static Monitoring                     CONFIG        = ConfigurationLoader.CONFIGURATION;
    
    private final Map<Long, SensorsIntervalManagerTask> managersTasks = new HashMap<>();
    
    // =========================================================================
    // BOOSTRAP
    // =========================================================================
    public void bootrap(Void ctx) {
        RequestContext.initializeConfig(CONFIG);
        if (CONFIG.isEnable()) {
            managersTasks.putAll(buildSensorsTasks(CONFIG.getSensors()));
        }
        
        //@formatter:off
        managersTasks.entrySet()
                     .stream()
                     .map(Map.Entry::getValue)
                     .forEach(task -> task.bootrap(this));
        //@formatter:on
    };
    
    // =========================================================================
    // SHUTDOWN
    // =========================================================================
    public void shutdown(Void ctx) {
        //@formatter:off
        managersTasks.entrySet()
                     .stream()
                     .map(Map.Entry::getValue)
                     .forEach(task -> task.shutdown(this));
        
        CONFIG.getSenders().forEach(MonitoringSender::shutdown);
        //@formatter:on
    };
    
    // =========================================================================
    // BUILDER
    // =========================================================================
    private Map<Long, SensorsIntervalManagerTask> buildSensorsTasks(List<MonitoringSensor> sensors) {
        Map<Long, SensorsIntervalManagerTask> result = new HashMap<>();
        if (sensors == null) {
            return result;
        }
        
        for (MonitoringSensor sensor : sensors) {
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
