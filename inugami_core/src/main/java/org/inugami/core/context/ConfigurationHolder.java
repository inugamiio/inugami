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
package org.inugami.core.context;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.inugami.api.alertings.AlertingProvider;
import org.inugami.api.exceptions.FatalException;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.handlers.Handler;
import org.inugami.api.listeners.EngineListener;
import org.inugami.api.listeners.EngineListenerProcessor;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.Gav;
import org.inugami.api.models.plugins.ManifestInfo;
import org.inugami.api.processors.Config;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.processors.Processor;
import org.inugami.api.providers.Provider;
import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.messages.MessagesServices;
import org.inugami.configuration.models.EventConfig;
import org.inugami.configuration.models.app.ApplicationConfig;
import org.inugami.configuration.models.app.SecurityConfiguration;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.configuration.models.plugins.PluginConfiguration;
import org.inugami.configuration.models.plugins.PropertyModel;
import org.inugami.configuration.services.ConfigHandlerHashMap;
import org.inugami.configuration.services.resolver.ConfigurationResolver;
import org.inugami.core.context.loading.AlertsResourcesLoader;
import org.inugami.core.context.loading.AlertsResourcesLoaderFileSystem;
import org.inugami.core.context.loading.AlertsResourcesLoaderZip;
import org.inugami.core.context.loading.PropertiesResourcesLoader;
import org.inugami.core.context.loading.PropertiesResourcesLoaderZip;

/**
 * ConfigurationHolder
 * 
 * @author patrick_guillerm
 * @since 4 oct. 2016
 * @see org.inugami.core.context.plugins.PluginBuilder
 */
@Deprecated
public class ConfigurationHolder {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ConfigurationResolver          configurationResolver           = new ConfigurationResolver();
    
    private final GenericContext<Context>        genericContext;
    
    private final Loader                         loader                          = new Loader();
    
    private final Map<String, String>            globaleProperties;
    
    private final ConfigHandler<String, String>  globalConfiguration             = new ConfigHandlerHashMap("globalConfiguration");
    
    private final Map<Gav, Plugin>               plugins                         = new HashMap<>();
    
    private final List<Plugin>                   pluginsRefs                     = new ArrayList<>();
    
    private final Optional<List<EngineListener>> coreListeners;
    
    private final List<String>                   SCHEDULERS                      = new ArrayList<>();
    
    public final static String                   DEFAULT_SCHEDULER               = "0 * * * * ?";
    
    private ApplicationConfig                    applicationConfig;
    
    private final AlertsResourcesLoader          alertsResourcesLoaderZip        = new AlertsResourcesLoaderZip();
    
    private final AlertsResourcesLoader          alertsResourcesLoaderFileSystem = new AlertsResourcesLoaderFileSystem();
    
    private final PropertiesResourcesLoader      propertiesResourcesLoaderZip    = new PropertiesResourcesLoaderZip();
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ConfigurationHolder(final GenericContext<Context> genericContext, final EngineListener... listeners) {
        super();
        this.genericContext = genericContext;
        globaleProperties = this.genericContext.getInitialProperties();
        coreListeners = listeners == null ? Optional.empty() : Optional.of(Arrays.asList(listeners));
        SCHEDULERS.add(DEFAULT_SCHEDULER);
    }
    
    // =========================================================================
    // INITIALIZE
    // =========================================================================
    public void initialize() {
        try {
            applicationConfig = initializeApplicationConfig();
            
            Optional<List<PluginConfiguration>> pluginsConfigs;
            pluginsConfigs = resolvePlugins();
            if (pluginsConfigs.isPresent()) {
                loadGlobalProperties(pluginsConfigs.get());
                buildGlobalConfiguration();
                buildPlugins(pluginsConfigs.get());
            }
            else {
                Loggers.PLUGINS.warn("no plugin found!");
            }
            postProcessApplicationConfiguration();
            
        }
        catch (final TechnicalException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }
    
    private void buildGlobalConfiguration() {
        if (globaleProperties != null) {
            
            // @formatter:off
            if (applicationConfig.getProperties() != null) {
                applicationConfig.getProperties().forEach(
                        property -> globaleProperties.put(property.getKey(), property.getValue()));
            }

            globaleProperties.entrySet().forEach(item -> {
                globalConfiguration.put(item.getKey(), item.getValue());
                globalConfiguration.optionnal().put(item.getKey(), item.getValue());
            });
            // @formatter:on
        }
    }
    
    // =========================================================================
    // APPLICATION CONFIGURATION
    // =========================================================================
    
    /**
     * Allow to grab scheduler cron expressions
     * 
     * @return return all cron expressions define in events-files and events
     */
    public List<String> getSchedulers() {
        return SCHEDULERS;
    }
    
    private ApplicationConfig initializeApplicationConfig() {
        final File applicationConfigFile = FilesUtils.buildFile(ConfigurationResolver.HOME_PATH,
                                                                "application-configuration.xml");
        ApplicationConfig result = null;
        if (applicationConfigFile.exists()) {
            result = configurationResolver.loadApplicationConfig(applicationConfigFile);
        }
        else {
            final URL url = this.getClass().getClassLoader().getResource("META-INF/application-configuration.xml");
            result = configurationResolver.loadApplicationConfig(url);
        }
        
        return result;
    }
    
    private void postProcessApplicationConfiguration() {
        
        final List<Plugin> localPlugins = getPlugins().orElse(new ArrayList<>());
        for (final Plugin plugin : localPlugins) {
            if (plugin.getConfig().getSecurity() != null) {
                applicationConfig.addSecurityConfigurations(plugin.getConfig().getSecurity());
                
                appendAllConfig(plugin.getConfig().getSecurity());
                
            }
        }
        
        try {
            applicationConfig.postProcessing(getGlobalProperties());
        }
        catch (final TechnicalException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.XLLOG.error(e.getMessage());
            throw new FatalException(e.getMessage());
        }
    }
    
    private void appendAllConfig(final List<SecurityConfiguration> security) {
        for (final SecurityConfiguration secu : security) {
            if (secu.getConfigs() != null) {
                appendAllSecuConfig(secu.getConfigs());
            }
        }
    }
    
    private void appendAllSecuConfig(final List<Config> configs) {
        for (final Config conf : configs) {
            globalConfiguration.put(conf.getKey(), conf.getValue());
        }
    }
    
    // =========================================================================
    // BUILD PLUGIN
    // =========================================================================
    protected Optional<List<PluginConfiguration>> resolvePlugins() throws TechnicalException {
        return configurationResolver.resolvePluginsConfigurations();
    }
    
    protected void buildPlugins(final List<PluginConfiguration> pluginsConfigs) throws TechnicalException {
        
        if (pluginsConfigs != null) {
            for (final PluginConfiguration config : pluginsConfigs) {
                final Plugin plugin = buildPlugin(config);
                plugins.put(plugin.getGav(), plugin);
            }
        }
        
        if (!plugins.isEmpty()) {
            final List<Plugin> refs = new ArrayList<>(plugins.size());
            plugins.keySet().forEach((key) -> refs.add(plugins.get(key)));
            pluginsRefs.addAll(refs);
        }
    }
    
    protected Plugin getPlugin(final Gav gav) {
        Plugin result = null;
        if (gav != null) {
            result = plugins.get(gav);
        }
        return result;
    }
    
    protected Plugin buildPlugin(final PluginConfiguration config) throws TechnicalException {
        final Optional<List<EventConfig>> eventsOpt = configurationResolver.resolvePluginEventConfig(config);
        final List<EventConfig> eventsConfigs = eventsOpt.isPresent() ? visiteEventFile(eventsOpt.get()) : null;
        final ManifestInfo manifest = configurationResolver.resolvePluginManifest(config);
        
        final List<AlertingProvider> alertings = loader.loadAlertings(config.getAlertings(), globalConfiguration);
        Map<String, Map<String, String>> properties = null;
        if (manifest != null) {
            if (alertsResourcesLoaderZip.isJarResources(manifest.getManifestUrl())) {
                final File zip = FilesUtils.resolveJarFile(manifest.getManifestUrl());
                alertsResourcesLoaderZip.loadAlertingsResources(manifest.getManifestUrl());
                properties = propertiesResourcesLoaderZip.loadResources(zip);
            }
            else {
                alertsResourcesLoaderFileSystem.loadAlertingsResources(manifest.getManifestUrl());
            }
        }
        
        final List<EngineListener> listeners = loader.loadListeners(config.getListeners(), globalConfiguration,
                                                                    manifest);
        final List<Processor> processors = loader.loadProcessors(config.getProcessors(), globalConfiguration, manifest);
        final List<Provider> providers = loader.loadProvider(config.getProviders(), globalConfiguration, manifest);
        genericContext.registerNamedComponents(providers);
        final List<Handler> handlers = loader.loadHandlers(config.getHandlers(), globalConfiguration);
        
        if (alertings != null) {
            alertings.forEach(AlertingProvider::postConstruct);
        }
        
        if (properties != null) {
            MessagesServices.register(properties);
        }
        if (config.getFrontProperties().isPresent()) {
            final Map<String, String> frontProperties = new HashMap<>();
            for (final PropertyModel property : config.getFrontProperties().get()) {
                frontProperties.put(property.getKey(), property.getValue());
            }
            MessagesServices.registerConfig(frontProperties);
        }
        return new Plugin(config, eventsConfigs, listeners, processors, handlers, providers, alertings, manifest,
                          properties);
    }
    
    // =========================================================================
    // LOAD GLOBAL PROPERTIES
    // =========================================================================
    
    private void loadGlobalProperties(final List<PluginConfiguration> pluginConfigurations) {
        for (final PluginConfiguration pluginConfig : pluginConfigurations) {
            if (pluginConfig.getProperties() != null) {
                globaleProperties.putAll(appendProperties(pluginConfig.getProperties()));
            }
        }
    }
    
    private Map<String, String> appendProperties(final List<PropertyModel> properties) {
        final Map<String, String> result = new HashMap<>();
        if (properties != null) {
            //@formatter:off
            properties.stream()
                      .filter(property -> (property.getKey() != null) && !property.getKey().trim().isEmpty())
                      .forEach(property -> result.put(property.getKey(), property.getValue()));
            //@formatter:on
        }
        return result;
    }
    
    // =========================================================================
    // LOAD GLOBAL PROPERTIES
    // =========================================================================
    private List<EventConfig> visiteEventFile(final List<EventConfig> config) {
        return config.stream().map(this::visiteEventFile).collect(Collectors.toList());
    }
    
    private EventConfig visiteEventFile(final EventConfig eventFileConfig) {
        
        manageCron(eventFileConfig.getScheduler(), DEFAULT_SCHEDULER, eventFileConfig::setScheduler);
        
        //@formatter:off
        if (eventFileConfig.getSimpleEvents() != null) {
            eventFileConfig.getSimpleEvents()
                           .forEach(simpleEvent -> manageCron(simpleEvent.getScheduler(),
                                                   eventFileConfig.getScheduler(),
                                                   simpleEvent::setScheduler));
        }

        if (eventFileConfig.getEvents() != null) {
            eventFileConfig.getEvents()
                           .forEach(event -> manageCron(event.getScheduler(),
                                                        eventFileConfig.getScheduler(),
                                                        event::setScheduler));
        }

        // @formatter:on
        
        return eventFileConfig;
    }
    
    // =========================================================================
    // LOAD ALERTING RESOURCES
    // =========================================================================
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private void manageCron(final String cronExpr, final String defaultCron, final Consumer<String> function) {
        if (cronExpr != null) {
            addNewCronExpression(cronExpr);
        }
        else {
            function.accept(defaultCron);
        }
    }
    
    private void addNewCronExpression(final String cronExpression) {
        if (!SCHEDULERS.contains(cronExpression)) {
            SCHEDULERS.add(cronExpression);
        }
        
    }
    
    // =========================================================================
    // PUBLIC API
    // =========================================================================
    /**
     * Gets the plugins.
     *
     * @return the plugins
     * @Allow to grab all plugins
     */
    public Optional<List<Plugin>> getPlugins() {
        return pluginsRefs == null ? Optional.empty() : Optional.of(pluginsRefs);
    }
    
    /**
     * Allow to call all enable listeners with Functional function.
     *
     * @param listenerProcessor the specific function to execute on all
     *            listeners
     */
    public void callAllListener(final EngineListenerProcessor listenerProcessor) {
        coreListeners.ifPresent(engineListeners -> engineListeners.forEach(listenerProcessor::process));
        
        // @formatter:off
        if (getPlugins().isPresent()) {
            pluginsRefs.stream().filter(plugin -> plugin.isEnabled()).filter(
                    plugin -> plugin.getListeners().isPresent()).forEach(plugin -> {
                        plugin.getListeners().get().forEach(listenerProcessor::process);
                    });
        }
        // @formatter:on
    }
    
    public Optional<List<Provider>> getProviders() {
        final List<Provider> result = new ArrayList<>();
        // @formatter:off
        if (getPlugins().isPresent()) {
            pluginsRefs.stream().filter(plugin -> plugin.isEnabled()).filter(
                    plugin -> plugin.getProviders().isPresent()).forEach(
                            plugin -> result.addAll(plugin.getProviders().get()));
        }
        // @formatter:on
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }
    
    public ConfigHandler<String, String> getGlobalProperties() {
        return globalConfiguration;
    }
    
    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }
    
    // =========================================================================
    // StartDayTime & SchedulerCron
    // =========================================================================
    @Deprecated
    public String getStartDayTime() {
        return null;// configs.getGlobalConfig().getStartDayTime();
    }
    
    @Deprecated
    public long getStartDayTimeValue() {
        return 0L;
    }
    
    @Deprecated
    public Calendar getStartDayTimeCalendar() {
        return null;
    }
    
    @Deprecated
    public void setStartDayTime(final String startDayTime) {
    }
    
}
