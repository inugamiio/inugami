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
package io.inugami.dashboard.core.domain.plugin;

import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.event.IEventDataDao;
import io.inugami.dashboard.api.domain.plugin.IPluginLoaderService;
import io.inugami.dashboard.api.domain.plugin.IPluginService;
import io.inugami.framework.commons.messages.MessagesServices;
import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.configuration.models.app.ApplicationConfig;
import io.inugami.framework.configuration.models.front.PluginFrontConfig;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.models.plugins.PropertyModel;
import io.inugami.framework.configuration.services.resolver.ConfigurationResolver;
import io.inugami.framework.configuration.services.validators.PluginConfigurationValidator;
import io.inugami.framework.interfaces.alertings.AlertingProvider;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.handlers.Handler;
import io.inugami.framework.interfaces.listeners.EngineListener;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.spi.PropertiesProducerSpi;
import io.inugami.framework.interfaces.spi.SpiLoaderServiceSPI;
import jakarta.annotation.Priority;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

import static io.inugami.framework.api.tools.RunSafeUtils.onVoidError;
import static io.inugami.framework.api.tools.RunSafeUtils.runSafe;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.or;

@Slf4j
@RequiredArgsConstructor
@Service
@Builder
public class PluginService implements IPluginService {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final String                DEFAULT_SCHEDULER = "0 * * * * ?";
    private final       ApplicationConfig     applicationConfig;
    private final       ConfigurationResolver configurationResolver;
    private final       IPluginLoaderService  pluginLoaderService;
    private final       SpiLoaderServiceSPI   spiLoaderService;
    private final       IEventDataDao         eventDataDao;
    private final       List<Plugin>          plugins           = new ArrayList<>();

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Override
    public Collection<Plugin> findAllPlugin() {
        return Collections.unmodifiableList(plugins);
    }

    @Override
    public Map<String, EnginePluginEventResultDTO> findPluginDataByGav(final @NonNull String groupId,
                                                                       final @NonNull String artifactId) {
        return eventDataDao.findPluginDataByGav(Gav.builder()
                                                   .groupId(groupId)
                                                   .artifactId(artifactId)
                                                   .build());
    }


    //==================================================================================================================
    // LOAD PLUGINS
    //==================================================================================================================
    public List<Plugin> loadPlugins() {
        final var configuration = loadConfiguration();
        validateConfiguration(configuration);
        final List<Plugin> result = createPlugins(configuration);
        for (Plugin plugin : result) {
            log.info("using plugin : {}", plugin.getGav().getHash());
        }
        plugins.addAll(cleanPlugins(result));
        return result;
    }

    private Collection<Plugin> cleanPlugins(final List<Plugin> plugins) {
        final List<Plugin> result = new ArrayList<>();
        for (Plugin plugin : plugins) {
            result.add(Plugin.builder()
                             .gav(plugin.getGav().toBuilder().build())
                             .frontConfig(Optional.ofNullable(plugin.getFrontConfig())
                                                  .map(item -> item.toBuilder().build())
                                                  .orElse(null))
                             .events(Optional.ofNullable(plugin.getEvents())
                                             .orElse(List.of())
                                             .stream()
                                             .map(event -> EventConfig.builder()
                                                                      .name(event.getName())
                                                                      .enable(event.getEnable())
                                                                      .events(Optional.ofNullable(event.getEvents())
                                                                                      .orElse(List.of())
                                                                                      .stream()
                                                                                      .map(e -> Event.builder()
                                                                                                     .name(e.getName())
                                                                                                     .from(e.getFrom())
                                                                                                     .fromFirstTime(e.getFromFirstTime())
                                                                                                     .until(e.getUntil())
                                                                                                     .scheduler(e.getScheduler())
                                                                                                     .build())

                                                                                      .toList())
                                                                      .simpleEvents(Optional.ofNullable(event.getSimpleEvents())
                                                                                            .orElse(List.of())
                                                                                            .stream()
                                                                                            .map(e -> SimpleEvent.builder()
                                                                                                                 .name(e.getName())
                                                                                                                 .from(e.getFrom())
                                                                                                                 .fromFirstTime(e.getFromFirstTime())
                                                                                                                 .until(e.getUntil())
                                                                                                                 .scheduler(e.getScheduler())
                                                                                                                 .build())
                                                                                            .toList())
                                                                      .build())
                                             .toList()
                                    )
                             .build());
        }
        return result;
    }


    //==================================================================================================================
    // CONFIGURATION
    //==================================================================================================================
    protected List<PluginConfiguration> loadConfiguration() {
        final List<PluginConfiguration> pluginConfigurations = new ArrayList<>();
        try {
            pluginConfigurations.addAll(configurationResolver.resolvePluginsConfigurations().orElse(List.of()));
        } catch (TechnicalException e) {
            log.error(e.getMessage(), e);
            throw new UncheckedException(e.getMessage());
        }
        return pluginConfigurations;
    }

    //==================================================================================================================
    // VALIDATE
    //==================================================================================================================
    protected void validateConfiguration(final List<PluginConfiguration> configuration) {
        final Map<PluginConfiguration, Throwable> errors = new LinkedHashMap<>();
        for (PluginConfiguration config : configuration) {
            onVoidError(() -> new PluginConfigurationValidator(config, config.getConfigFile()).validate(),
                        error -> errors.put(config, error));
        }

        if (!errors.isEmpty()) {
            for (Map.Entry<PluginConfiguration, Throwable> error : errors.entrySet()) {
                log.error("[{}] invalid plugin configuration : {}", error.getKey().getConfigFile(), error.getValue()
                                                                                                         .getMessage());
            }
            throw new UncheckedException("invalid plugins configuration !");
        }
    }

    //==================================================================================================================
    // CREATE PLUGINS
    //==================================================================================================================
    protected List<Plugin> createPlugins(final List<PluginConfiguration> configurations) {
        final List<Plugin>        result           = new ArrayList<>();
        final Map<String, String> globalProperties = buildGlobalProperties();
        for (PluginConfiguration config : configurations) {
            Plugin plugin = runSafe(() -> createPlugin(config, globalProperties), log);
            applyIfNotNull(plugin, result::add);
        }
        return result;
    }


    protected Plugin createPlugin(final PluginConfiguration config,
                                  final Map<String, String> globalProperties) throws TechnicalException {

        final Optional<List<EventConfig>> eventsOpt = configurationResolver.resolvePluginEventConfig(config);
        final List<EventConfig> events =
                eventsOpt.isPresent() ? visiteEventFile(eventsOpt.get()) : List.of();
        final ManifestInfo                     manifest    = configurationResolver.resolvePluginManifest(config);
        final Map<String, Map<String, String>> properties  = new LinkedHashMap<>();
        final var                              frontConfig = registerFrontProperties(config);
        final List<AlertingProvider> alertings =
                pluginLoaderService.loadAlertings(config.getAlertings(), globalProperties, manifest);
        final List<EngineListener> listeners =
                pluginLoaderService.loadListeners(config.getListeners(), globalProperties, manifest);
        final List<Processor> processors =
                pluginLoaderService.loadProcessors(config.getProcessors(), globalProperties, manifest);
        final List<Provider> providers =
                pluginLoaderService.loadProviders(config.getProviders(), globalProperties, manifest);
        final List<Handler> handlers =
                pluginLoaderService.loadHandlers(config.getHandlers(), globalProperties, manifest);

        MessagesServices.register(properties);
        return Plugin.builder()
                     .config(config)
                     .events(events)
                     .eventConfigPresent(!events.isEmpty())
                     .manifest(manifest)
                     .properties(properties)
                     .frontConfig(frontConfig)
                     .listeners(listeners)
                     .processors(processors)
                     .providers(providers)
                     .handlers(handlers)
                     .alertingProviders(alertings)
                     .gav(config.getGav())
                     .build();
    }

    private PluginFrontConfig registerFrontProperties(final PluginConfiguration config) {
        final Map<String, String> frontProperties = new HashMap<>();
        for (final PropertyModel property : or(config.getFrontProperties(), () -> new ArrayList<PropertyModel>())) {
            frontProperties.put(property.getKey(), property.getValue());
        }
        MessagesServices.registerConfig(frontProperties);
        return PluginFrontConfig.builder()
                                .build();
    }

    private List<EventConfig> visiteEventFile(final List<EventConfig> config) {
        return config.stream()
                     .map(this::visiteEventFile)
                     .toList();
    }

    private EventConfig visiteEventFile(final EventConfig eventFileConfig) {
        eventFileConfig.setScheduler(or(eventFileConfig.getScheduler(), DEFAULT_SCHEDULER));

        for (SimpleEvent event : Optional.ofNullable(eventFileConfig.getSimpleEvents()).orElse(List.of())) {
            event.setScheduler(or(event.getScheduler(), DEFAULT_SCHEDULER));
        }

        for (Event event : Optional.ofNullable(eventFileConfig.getEvents()).orElse(List.of())) {
            event.setScheduler(or(event.getScheduler(), DEFAULT_SCHEDULER));
        }

        return eventFileConfig;
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private int comparePropertiesProducer(final PropertiesProducerSpi ref, final PropertiesProducerSpi value) {
        final Priority refPriority   = ref == null ? null : ref.getClass().getAnnotation(Priority.class);
        final Priority valuePriority = value == null ? null : value.getClass().getAnnotation(Priority.class);

        final int refWeigth   = refPriority == null ? 0 : refPriority.value();
        final int valueWeigth = valuePriority == null ? 0 : valuePriority.value();

        return Integer.compare(refWeigth, valueWeigth);

    }

    private Map<String, String> buildGlobalProperties() {
        final Map<String, String> result = new LinkedHashMap<>();

        for (PropertyModel property : or(applicationConfig.getProperties(), new ArrayList<PropertyModel>())) {
            result.put(property.getKey(), property.getValue());
        }

        final List<PropertiesProducerSpi> producers = spiLoaderService.loadServices(PropertiesProducerSpi.class);
        if (producers != null) {
            producers.sort(this::comparePropertiesProducer);
            producers.stream().map(PropertiesProducerSpi::produce).forEach(result::putAll);
        }

        return result;
    }
}
