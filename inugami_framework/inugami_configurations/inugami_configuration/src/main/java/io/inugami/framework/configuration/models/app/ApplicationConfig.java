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
package io.inugami.framework.configuration.models.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.commons.connectors.config.HttpDefaultConfig;
import io.inugami.configuration.models.plugins.PropertyModel;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * ApplicationConfig
 *
 * @author patrick_guillerm
 * @since 15 déc. 2017
 */
@XStreamAlias("application")
public class ApplicationConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -3387499578573275619L;

    @XStreamAsAttribute
    private String applicationName;

    @XStreamAsAttribute
    private boolean enableTechnicalsUsers;

    @XStreamAsAttribute
    private long timeout;

    @XStreamAsAttribute
    private long scriptTimeout;

    @XStreamAsAttribute
    private int maxPluginRunning;

    @XStreamAsAttribute
    private int maxPluginRunningStandalone;

    @XStreamAsAttribute
    private int maxRunningEvents;

    @XStreamAsAttribute
    private int maxThreads = 1500;

    @XStreamAsAttribute
    private Integer alertingDynamicMaxThreads;

    private DataProviderModel dataStorage;

    private List<PropertyModel> properties;

    @XStreamImplicit
    private List<SecurityConfiguration> security;

    private HttpDefaultConfig httpDefaultConfig;

    private boolean alertingEnable;

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ApplicationConfig [properties=");
        builder.append(properties);
        builder.append(", security=");
        builder.append(security);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public void postProcessing(final ConfigHandler<String, String> ctx) throws TechnicalException {
        //@formatter:off
        applicationName             = ctx.applyProperties(JvmKeyValues.APPLICATION_NAME.or(applicationName));
        timeout                     = Long.parseLong(ctx.applyProperties(JvmKeyValues.APPLICATION_TIMEOUT.or(timeout)));
        scriptTimeout               = Long.parseLong(ctx.applyProperties(JvmKeyValues.APPLICATION_SCRIPT_TIMEOUT.or("5000")));
        maxPluginRunning            = Integer.parseInt(ctx.applyProperties(JvmKeyValues.APPLICATION_PLUGIN_RUNNING.or(maxPluginRunning)));
        maxPluginRunningStandalone  = Integer.parseInt(ctx.applyProperties(JvmKeyValues.APPLICATION_PLUGIN_RUNNING_STANDALONE.or(maxPluginRunningStandalone)));
        alertingEnable              = Boolean.parseBoolean(ctx.applyProperties(JvmKeyValues.ALERTING_ENABLE.or(String.valueOf(alertingEnable))));
        maxThreads                  = Integer.parseInt(ctx.applyProperties(JvmKeyValues.APPLICATION_MAX_THREADS.or("1500")));
        alertingDynamicMaxThreads   = Integer.parseInt(ctx.applyProperties(JvmKeyValues.ALERTING_DYNAMIC_MAX_THREADS.or(alertingDynamicMaxThreads==null?10:alertingDynamicMaxThreads)));
        
        if(maxThreads<=0) {
            maxThreads = 1500;
        }
        //@formatter:off        
        
        if(security==null) {
            security = new ArrayList<>();
        }

        if(dataStorage==null) {
            dataStorage = new DataProviderModel();
        }
        dataStorage.postProcessing(ctx);

        httpDefaultConfig.postProcessing(ctx);
        
        for(final SecurityConfiguration secu : security) {
           secu.postProcessing(ctx);
        }
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getApplicationName() {
        return applicationName;
    }
    
    public void setApplicationName(final String applicationName) {
        this.applicationName = applicationName;
    }
    
    public long getTimeout() {
        return timeout;
    }
    
    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }
    
    public int getMaxPluginRunning() {
        return maxPluginRunning;
    }
    
    public void setMaxPluginRunning(final int maxPluginRunning) {
        this.maxPluginRunning = maxPluginRunning;
    }
    
    public int getMaxPluginRunningStandalone() {
        return maxPluginRunningStandalone;
    }
    
    public void setMaxPluginRunningStandalone(final int maxPluginRunningStandalone) {
        this.maxPluginRunningStandalone = maxPluginRunningStandalone;
    }
    
    public int getMaxRunningEvents() {
        return maxRunningEvents;
    }
    
    public void setMaxRunningEvents(final int maxRunningEvents) {
        this.maxRunningEvents = maxRunningEvents;
    }
    
    public List<PropertyModel> getProperties() {
        return properties;
    }
    
    public void setProperties(final List<PropertyModel> properties) {
        this.properties = properties;
    }
    
    public List<SecurityConfiguration> getSecurity() {
        return security;
    }
    
    public Optional<SecurityConfiguration> getSecurity(final String name){
        Optional<SecurityConfiguration> result =Optional.empty();
        
        if(security!=null) {
            //@formatter:off
            result =security.stream()
                            .filter(secu-> secu.getName().equalsIgnoreCase(name))
                            .findFirst();
            //@formatter:on            
        }
        return result;
    }

    public ApplicationConfig addSecurityConfiguration(final SecurityConfiguration config) {
        if (security == null) {
            security = new ArrayList<>();
        }
        if (config != null) {
            security.add(config);
        }
        return this;
    }

    public ApplicationConfig addSecurityConfigurations(final List<SecurityConfiguration> config) {
        if (security == null) {
            security = new ArrayList<>();
        }
        if (config != null) {
            security.addAll(config);
        }
        return this;
    }

    public void setSecurity(final List<SecurityConfiguration> security) {
        this.security = security;
    }

    public HttpDefaultConfig getHttpDefaultConfig() {
        return httpDefaultConfig;
    }

    public void setHttpDefaultConfig(final HttpDefaultConfig httpDefaultConfig) {
        this.httpDefaultConfig = httpDefaultConfig;
    }

    public boolean isEnableTechnicalsUsers() {
        return enableTechnicalsUsers;
    }

    public void setEnableTechnicalsUsers(final boolean enableTechnicalsUsers) {
        this.enableTechnicalsUsers = enableTechnicalsUsers;
    }

    public long getScriptTimeout() {
        return scriptTimeout;
    }

    public void setScriptTimeout(final long scriptTimeout) {
        this.scriptTimeout = scriptTimeout;
    }

    public List<SecurityConfiguration> getSecurityTechnicalConfig() {
        final List<SecurityConfiguration> secuConfig = (security == null ? new ArrayList<>() : security);
        //@formatter:off
        return  secuConfig.stream()
                          .filter(secu-> "technical".equals(secu.getName()))
                          .collect(Collectors.toList());
        //@formatter:off                
    }

    public DataProviderModel getDataStorage() {
        return dataStorage;
    }

    public void setDataStorage(final DataProviderModel dataStorage) {
        this.dataStorage = dataStorage;
    }

    public boolean isAlertingEnable() {
        return alertingEnable;
    }

    public void setAlertingEnable(final boolean alertingEnable) {
        this.alertingEnable = alertingEnable;
    }

    public int getMaxThreads() {
        return maxThreads;
    }
    public void setMaxThreads(final int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getAlertingDynamicMaxThreads() {
        return alertingDynamicMaxThreads;
    }
    
    
}
