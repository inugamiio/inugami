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
package org.inugami.monitoring.config.models;

import static org.inugami.monitoring.config.tools.ApplyStrategy.applyStrategy;
import static org.inugami.monitoring.config.tools.ApplyStrategy.applyStrategyBoolean;

import org.inugami.api.constants.JvmKeyValues;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.functionnals.ApplyIfNull;
import org.inugami.api.functionnals.PostProcessing;
import org.inugami.api.models.data.basic.JsonObject;
import org.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * MonitoringConfig
 * 
 * @author patrickguillerm
 * @since Jan 14, 2019
 */
@XStreamAlias("monitoring")
public class MonitoringConfig implements PostProcessing<ConfigHandler<String, String>>, JsonObject, ApplyIfNull {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long        serialVersionUID = -8578418466667776898L;
    
    @XStreamAsAttribute
    private Boolean                  enable;
    
    @XStreamAsAttribute
    private String                   env;
    
    @XStreamAsAttribute
    private String                   asset;
    
    @XStreamAsAttribute
    private String                   hostname;
    
    @XStreamAsAttribute
    private String                   instanceName;
    
    @XStreamAsAttribute
    private String                   instanceNumber;
    
    @XStreamAsAttribute
    private String                   maxSensorsTasksThreads;
    
    @XStreamAsAttribute
    private String                   applicationVersion;
    
    private HeaderInformationsConfig header;
    
    private PropertiesConfig         properties;
    
    private MonitoringSendersConfig  senders;
    
    private SensorsConfig            sensors;
    
    private InterceptorsConfig       interceptors;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public void postProcessing(final ConfigHandler<String, String> context) throws TechnicalException {
        
        if (header == null) {
            header = new HeaderInformationsConfig();
        }
        
        senders = senders == null ? new MonitoringSendersConfig() : senders;
        sensors = sensors == null ? new SensorsConfig() : sensors;
        
        //@formatter:off
        enable                 = applyStrategyBoolean(JvmKeyValues.MONITORING_ENABLE, enable, true);
        env                    = applyStrategy(JvmKeyValues.ENVIRONMENT,env,"prod");
        asset                  = applyStrategy(JvmKeyValues.ASSET,asset,"inugami");
        hostname               = applyStrategy(JvmKeyValues.APPLICATION_HOST_NAME,hostname);
        instanceName           = applyStrategy(JvmKeyValues.INSTANCE,instanceName);
        instanceNumber         = applyStrategy(JvmKeyValues.INSTANCE_NUMBER,instanceNumber); 
        maxSensorsTasksThreads = applyIfNull(maxSensorsTasksThreads , () -> "20");
        //@formatter:on
        
        header.postProcessing(context);
        
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        return convertToJson();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Boolean getEnable() {
        return enable;
    }
    
    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }
    
    public String getEnv() {
        return env;
    }
    
    public void setEnv(final String env) {
        this.env = env;
    }
    
    public String getAsset() {
        return asset;
    }
    
    public void setAsset(final String asset) {
        this.asset = asset;
    }
    
    public String getHostname() {
        return hostname;
    }
    
    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }
    
    public String getInstanceName() {
        return instanceName;
    }
    
    public void setInstanceName(final String instanceName) {
        this.instanceName = instanceName;
    }
    
    public String getInstanceNumber() {
        return instanceNumber;
    }
    
    public void setInstanceNumber(final String instanceNumber) {
        this.instanceNumber = instanceNumber;
    }
    
    public HeaderInformationsConfig getHeader() {
        return header;
    }
    
    public void setHeader(final HeaderInformationsConfig header) {
        this.header = header;
    }
    
    public PropertiesConfig getProperties() {
        return properties;
    }
    
    public void setProperties(final PropertiesConfig properties) {
        this.properties = properties;
    }
    
    public MonitoringSendersConfig getSenders() {
        return senders;
    }
    
    public void setSenders(final MonitoringSendersConfig senders) {
        this.senders = senders;
    }
    
    public SensorsConfig getSensors() {
        return sensors;
    }
    
    public void setSensors(final SensorsConfig sensors) {
        this.sensors = sensors;
    }
    
    public String getMaxSensorsTasksThreads() {
        return maxSensorsTasksThreads;
    }
    
    public void setMaxSensorsTasksThreads(final String maxSensorsTasksThreads) {
        this.maxSensorsTasksThreads = maxSensorsTasksThreads;
    }
    
    public String getApplicationVersion() {
        return applicationVersion;
    }
    
    public void setApplicationVersion(final String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }
    
    public InterceptorsConfig getInterceptors() {
        return interceptors;
    }
    
    public void setInterceptors(final InterceptorsConfig interceptors) {
        this.interceptors = interceptors;
    }
    
}
