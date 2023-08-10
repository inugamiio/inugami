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
package io.inugami.core.context.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import io.inugami.api.alertings.AlertingProvider;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.handlers.Handler;
import io.inugami.api.listeners.EngineListener;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.plugins.ManifestInfo;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.processors.Processor;
import io.inugami.api.providers.Provider;
import io.inugami.commons.messages.MessagesServices;
import io.inugami.configuration.models.EventConfig;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.configuration.models.plugins.PluginConfiguration;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import io.inugami.configuration.services.resolver.ConfigurationResolver;
import io.inugami.core.context.GenericContext;
import io.inugami.core.context.Loader;

/**
 * PluginBuilder
 * 
 * @author patrickguillerm
 * @since 3 sept. 2018
 */
public class PluginBuilder {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ConfigurationResolver configurationResolver = new ConfigurationResolver();
    
    private final GenericContext<?>     ctx;
    
    private final PluginEventsVisitor   eventsValidator;
    
    private final Loader                loader;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public PluginBuilder(final GenericContext<?> ctx) {
        this.ctx = ctx;
        this.eventsValidator = new PluginEventsVisitor();
        loader = new Loader();
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public List<Plugin> buildsPlugins() throws TechnicalException {
        List<Plugin> result = null;
        final List<PluginConfiguration> pluginsConfigurations = configurationResolver.resolvePluginsConfigurations().orElse(new ArrayList<>());
        if (pluginsConfigurations.isEmpty()) {
            Loggers.INIT.info("no plugin detected");
        }
        else {
            result = processBuildsPlugins(pluginsConfigurations);
        }
        return result;
    }
    
    // =========================================================================
    // PROCESS BUILD PLUGINS
    // =========================================================================
    private List<Plugin> processBuildsPlugins(final List<PluginConfiguration> pluginsConfigurations) {
        final Map<String, String> properties = ctx.getInitialProperties();
        return buildPlugins(pluginsConfigurations, properties);
    }
    
    protected List<Plugin> buildPlugins(final List<PluginConfiguration> pluginsConfigurations,
                                        final Map<String, String> properties) {
        final Set<Plugin> result = new LinkedHashSet<>();
        
        if (pluginsConfigurations != null) {
            for (final PluginConfiguration config : pluginsConfigurations) {
                try {
                    final Plugin plugin = buildPlugin(config, properties);
                    result.add(plugin);
                }
                catch (final TechnicalException e) {
                    Loggers.INIT.error("unable to create plugin : {} - {}", e.getMessage(), config.getGav());
                    Loggers.DEBUG.error(e.getMessage(), e);
                }
            }
        }
        
        return new ArrayList<>(result);
    }
    
    // =========================================================================
    // BUILD PLUGIN
    // =========================================================================
    private Plugin buildPlugin(final PluginConfiguration config,
                               final Map<String, String> properties) throws TechnicalException {
        final ConfigHandler<String, String> configHandler = new ConfigHandlerHashMap(properties);
        
        final Optional<List<EventConfig>> eventsOpt = configurationResolver.resolvePluginEventConfig(config);
        final List<EventConfig> eventsConfigs = eventsValidator.visite(eventsOpt, ctx::registerCronExpression);
        
        final ManifestInfo manifest = configurationResolver.resolvePluginManifest(config);
        
        //@formatter:off
        final List<EngineListener> listeners                    = loader.loadListeners(config.getListeners(), configHandler,manifest);
        final List<Processor> processors                        = loader.loadProcessors(config.getProcessors(), configHandler, manifest);
        final List<Provider> providers                          = loader.loadProvider(config.getProviders(), configHandler, manifest);
        final List<Handler> handlers                            = loader.loadHandlers(config.getHandlers(), configHandler);
        final List<AlertingProvider> alertings                  = loader.loadAlertings(config.getAlertings(), configHandler);
        final Map<String, Map<String, String>> propertiesLabels = loader.loadPropertiesLables(manifest);
        final Map<String, String> frontProperties               = loader.loadFrontProperties(config);
        //@formatter:on
        
        loader.loadAlertingResources(manifest);
        
        ctx.registerListeners(listeners);
        ctx.registerNamedComponents(processors);
        ctx.registerNamedComponents(providers);
        ctx.registerNamedComponents(handlers);
        ctx.registerPropertiesLabels(propertiesLabels);
        ctx.registerFrontProperties(frontProperties);
        
        if (alertings != null) {
            alertings.forEach(AlertingProvider::postConstruct);
        }
        
        final Map<String, Map<String, String>> labels = meldProperties(propertiesLabels, frontProperties);
        
        //@formatter:off
        return new Plugin(config,
                          eventsConfigs,
                          listeners,
                          processors,
                          handlers,
                          providers,
                          alertings,
                          manifest,
                          labels);
         //@formatter:on
    }
    
    private Map<String, Map<String, String>> meldProperties(final Map<String, Map<String, String>> propertiesLabels,
                                                            final Map<String, String> frontProperties) {
        Map<String, Map<String, String>> result = null;
        if ((propertiesLabels != null) && (frontProperties != null)) {
            result = new HashMap<>(propertiesLabels);
            
            Map<String, String> savedKeys = result.get(MessagesServices.initLocale());
            if (savedKeys == null) {
                savedKeys = new HashMap<>();
            }
            savedKeys.putAll(frontProperties);
            result.put(MessagesServices.initLocale(), savedKeys);
        }
        return result;
    }
    
}
