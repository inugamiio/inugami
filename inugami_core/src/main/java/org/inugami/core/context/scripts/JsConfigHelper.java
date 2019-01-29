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
package org.inugami.core.context.scripts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.inugami.api.processors.ConfigHandler;
import org.inugami.configuration.models.ProviderConfig;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.configuration.services.ConfigHandlerHashMap;
import org.inugami.core.context.Context;

/**
 * JsConfigHelper
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2017
 */
public final class JsConfigHelper {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    static String global(final String configName) {
        final ConfigHandler<String, String> config = Context.getInstance().getGlobalConfiguration();
        final String rawValue = config.grabOrDefault(configName, "");
        final String value = config.applyProperties(rawValue);
        return value.isEmpty() ? null : value;
    }
    
    static String providerConfig(final String providerName, final String configName) {
        final Optional<List<Plugin>> plugins = Context.getInstance().getPlugins();
        Optional<ProviderConfig> providerOpt = Optional.empty();
        
        if (plugins.isPresent()) {
            //@formatter:off
            providerOpt = plugins.get()
                                 .stream()
                                 .filter(plugin->plugin.getProviders().isPresent())
                                 .flatMap(plugin->plugin.getConfig().getProviders().stream())
                                 .filter(providerConf->providerConf.getName().equals(providerName))
                                 .findFirst();
            //@formatter:on
        }
        
        String result = null;
        if (providerOpt.isPresent()) {
            result = providerOpt.get().getConfig(configName).orElse(null);
            
        }
        
        if (result != null) {
            //@formatter:off
            final Map<String, String> aggregateConfig = new HashMap<>();
            providerOpt.get()
                       .getConfigs()
                       .stream().forEach(item -> aggregateConfig.put(item.getKey(),item.getValue()));
            
            Context.getInstance()
                   .getGlobalConfiguration()
                   .entrySet()
                   .stream()
                   .forEach(item -> aggregateConfig.put(item.getKey(),item.getValue()));
            //@formatter:on
            
            final ConfigHandler<String, String> config = new ConfigHandlerHashMap(aggregateConfig);
            
            result = config.applyProperties(result);
        }
        
        return result;
    }
    
}
