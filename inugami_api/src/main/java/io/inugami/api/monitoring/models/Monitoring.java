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
package io.inugami.api.monitoring.models;

import java.util.ArrayList;
import java.util.List;

import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.senders.MonitoringSender;
import io.inugami.api.monitoring.sensors.MonitoringSensor;
import io.inugami.api.processors.ConfigHandler;

/**
 * Monitoring
 * 
 * @author patrickguillerm
 * @since Jan 16, 2019
 */

/**
 * Monitoring
 * 
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
public class Monitoring {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final boolean                           enable;
    
    private final String                            env;
    
    private final String                            asset;
    
    private final String                            hostname;
    
    private final String                            instanceName;
    
    private final String                            instanceNumber;
    
    private final String                            applicationVersion;
    
    private final Headers                           headers;
    
    private final int                               maxSensorsTasksThreads;
    
    private final ConfigHandler<String, String>     properties;
    
    private final List<MonitoringSender>            senders;
    
    private final List<MonitoringSensor>            sensors;
    
    private final List<MonitoringFilterInterceptor> interceptors;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Monitoring(final boolean enable, final String env, final String asset, final String hostname,
                      final String instanceName, final String instanceNumber,
                      final ConfigHandler<String, String> properties, final List<MonitoringSender> senders,
                      final List<MonitoringSensor> sensors, final Headers headers, final int maxSensorsTasksThreads,
                      final String applicationVersion, final List<MonitoringFilterInterceptor> interceptors) {
        super();
        this.enable = enable;
        this.env = env;
        this.asset = asset;
        this.hostname = hostname;
        this.instanceName = instanceName;
        this.instanceNumber = instanceNumber;
        this.properties = properties;
        this.senders = senders;
        this.sensors = sensors;
        this.headers = headers;
        this.maxSensorsTasksThreads = maxSensorsTasksThreads;
        this.applicationVersion = applicationVersion;
        this.interceptors = interceptors;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Monitoring [enable=");
        builder.append(enable);
        builder.append(", env=");
        builder.append(env);
        builder.append(", asset=");
        builder.append(asset);
        builder.append(", hostname=");
        builder.append(hostname);
        builder.append(", instanceName=");
        builder.append(instanceName);
        builder.append(", instanceNumber=");
        builder.append(instanceNumber);
        builder.append(", properties=");
        builder.append(properties);
        builder.append(", headers=");
        builder.append(headers);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public boolean isEnable() {
        return enable;
    }
    
    public String getEnv() {
        return env;
    }
    
    public String getAsset() {
        return asset;
    }
    
    public String getHostname() {
        return hostname;
    }
    
    public String getInstanceName() {
        return instanceName;
    }
    
    public String getInstanceNumber() {
        return instanceNumber;
    }
    
    public ConfigHandler<String, String> getProperties() {
        return properties;
    }
    
    public List<MonitoringSender> getSenders() {
        return senders == null ? new ArrayList<>() : senders;
    }
    
    public List<MonitoringSensor> getSensors() {
        return sensors == null ? new ArrayList<>() : sensors;
    }
    
    public Headers getHeaders() {
        return headers;
    }
    
    public int getMaxSensorsTasksThreads() {
        return maxSensorsTasksThreads;
    }
    
    public String getApplicationVersion() {
        return applicationVersion;
    }
    
    public List<MonitoringFilterInterceptor> getInterceptors() {
        return interceptors == null ? new ArrayList<>() : interceptors;
    }
    
}
