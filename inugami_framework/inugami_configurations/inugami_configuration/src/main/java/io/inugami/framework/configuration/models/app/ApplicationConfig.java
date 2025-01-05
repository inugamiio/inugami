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

import io.inugami.framework.configuration.models.plugins.PropertyModel;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.configurtation.JvmKeyValues;
import io.inugami.framework.interfaces.connectors.config.HttpDefaultConfig;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.functionnals.PostProcessing;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * ApplicationConfig
 *
 * @author patrick_guillerm
 * @since 15 déc. 2017
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long                        serialVersionUID = -3387499578573275619L;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String                      applicationName;
    private              boolean                     enableTechnicalsUsers;
    private              long                        timeout;
    private              long                        scriptTimeout;
    private              int                         maxPluginRunning;
    private              int                         maxPluginRunningStandalone;
    private              int                         maxRunningEvents;
    private              int                         maxThreads       = 1500;
    private              Integer                     alertingDynamicMaxThreads;
    private              DataProviderModel           dataStorage;
    @Singular("properties")
    private              List<PropertyModel>         properties;
    @Singular("security")
    private              List<SecurityConfiguration> security;
    private              HttpDefaultConfig           httpDefaultConfig;
    private              boolean                     alertingEnable;

    // =================================================================================================================
    // OVERRIDES
    // =================================================================================================================
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

    
}
