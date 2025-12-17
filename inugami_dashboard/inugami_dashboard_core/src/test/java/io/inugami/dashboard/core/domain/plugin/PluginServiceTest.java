package io.inugami.dashboard.core.domain.plugin;

import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.event.IEventDataDao;
import io.inugami.dashboard.api.domain.plugin.IPluginLoaderService;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.commons.messages.MessagesServices;
import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.configuration.models.app.ApplicationConfig;
import io.inugami.framework.configuration.models.front.PluginFrontConfig;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.services.resolver.ConfigurationResolver;
import io.inugami.framework.interfaces.alertings.AlertingProvider;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.handlers.Handler;
import io.inugami.framework.interfaces.listeners.EngineListener;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.event.TargetConfig;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.spi.SpiLoaderServiceSPI;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.inugami.commons.test.UnitTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PluginServiceTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Mock
    private ApplicationConfig     applicationConfig;
    @Mock
    private ConfigurationResolver configurationResolver;
    @Mock
    private IPluginLoaderService  pluginLoaderService;
    @Mock
    private SpiLoaderServiceSPI   spiLoaderService;
    @Mock
    private IEventDataDao         eventDataDao;
    @Mock
    private Plugin                plugin;
    @Mock
    private AlertingProvider      alertingProvider;
    @Mock
    private EngineListener        engineListener;
    @Mock
    private Processor             processor;
    @Mock
    private Provider              provider;
    @Mock
    private Handler               handler;

    //==================================================================================================================
    // INIT
    //==================================================================================================================

    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
        MessagesServices.clean();
        lenient().when(configurationResolver.resolvePluginManifest(any())).thenReturn(ManifestInfo.builder()
                                                                                                  .workspace(new File("/home/me/dev/inugami"))
                                                                                                  .build());
        lenient().when(pluginLoaderService.loadAlertings(any(), any(), any())).thenReturn(List.of(alertingProvider));
        lenient().when(pluginLoaderService.loadListeners(any(), any(), any())).thenReturn(List.of(engineListener));
        lenient().when(pluginLoaderService.loadProcessors(any(), any(), any())).thenReturn(List.of(processor));
        lenient().when(pluginLoaderService.loadProviders(any(), any(), any())).thenReturn(List.of(provider));
        lenient().when(pluginLoaderService.loadHandlers(any(), any(), any())).thenReturn(List.of(handler));
    }

    @AfterEach

    public void clean() {
        MdcService.getInstance().clear();
        MessagesServices.clean();
    }

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Test
    void findPluginDataByGav_nominal() {
        when(eventDataDao.findPluginDataByGav(any()))
                .thenReturn(Map.of("inu-test",
                                   EnginePluginEventResultDTO.builder()
                                                             .status(Status.SUCCESS)
                                                             .data(ProviderFutureResult.builder()
                                                                                       .build())
                                                             .build()));
        final var gav = buildGav();
        assertText(service().findPluginDataByGav(gav.getGroupId(), gav.getArtifactId()),
                   """
                           {
                             "inu-test" : {
                               "data" : {
                                 "alerts" : [ ],
                                 "data" : [ ]
                               },
                               "status" : "SUCCESS"
                             }
                           }
                           """);
    }

    //==================================================================================================================
    // LOAD PLUGINS
    //==================================================================================================================
    @Test
    void loadPlugins_nominal() throws TechnicalException {
        when(configurationResolver.resolvePluginsConfigurations()).thenReturn(buildPluginConfigurations());
        when(configurationResolver.resolvePluginEventConfig(any())).thenReturn(buildEventConfigs());
        final var    service = service();
        final Plugin plugin  = service.loadPlugins().get(0);
        assertThat(plugin.getConfig()).isNotNull();
        assertThat(plugin.getListeners()).size().isOne();
        assertThat(plugin.getProcessors()).size().isOne();
        assertThat(plugin.getProviders()).size().isOne();
        assertThat(plugin.getHandlers()).size().isOne();
        assertThat(plugin.getAlertingProviders()).size().isOne();

        assertText(cleanPlugin(plugin),
                   """
                           {
                              "alertingProviders" : [ ],
                              "config" : {
                                "alertings" : [ ],
                                "components" : [ ],
                                "dependencies" : [ ],
                                "eventsFiles" : [ ],
                                "frontConfig" : {
                                  "commonsCss" : "inu_test.css",
                                  "menuLinks" : [ ],
                                  "pluginBaseName" : "inu_test",
                                  "router" : [ ]
                                },
                                "frontProperties" : [ ],
                                "gav" : {
                                  "artifactId" : "inu-test",
                                  "groupId" : "io.inugami",
                                  "hash" : "io.inugami:inu-test:4.3.0",
                                  "version" : "4.3.0"
                                },
                                "handlers" : [ ],
                                "listeners" : [ ],
                                "processors" : [ ],
                                "properties" : {
                                  "debug" : "true",
                                  "timeout" : "60000"
                                },
                                "providers" : [ ],
                                "resources" : [ ],
                                "security" : [ ]
                              },
                              "enabled" : false,
                              "eventConfigPresent" : true,
                              "events" : [ {
                                "events" : [ {
                                  "type" : "Event",
                                  "name" : "composite-event",
                                  "fromFirstTime" : null,
                                  "until" : null,
                                  "provider" : null,
                                  "mapper" : null,
                                  "processors" : [ ],
                                  "alertings" : [ ],
                                  "scheduler" : "0 * * * * ?",
                                  "targets" : [ {
                                    "name" : "target",
                                    "fromFirstTime" : null,
                                    "until" : null,
                                    "provider" : "ela",
                                    "mapper" : null,
                                    "processors" : [ ],
                                    "alertings" : [ ],
                                    "query" : "ummarize(asPercent(sumSeries(org.foo.bar.jmx.gravida.sessions),sumSeries(org.foo.bar.jmx.*.session)), \\"24h\\", \\"avg\\",true)",
                                    "parent" : null,
                                    "scheduler" : "0 0 * * * ?"
                                  } ]
                                } ],
                                "gav" : {
                                  "artifactId" : "inu-test",
                                  "groupId" : "io.inugami",
                                  "hash" : "io.inugami:inu-test:4.3.0",
                                  "version" : "4.3.0"
                                },
                                "name" : "inu-events",
                                "scheduler" : "0 * * * * ?",
                                "simpleEvents" : [ {
                                  "type" : "SimpleEvent",
                                  "name" : "simple-event",
                                  "fromFirstTime" : null,
                                  "until" : null,
                                  "provider" : "graphite",
                                  "mapper" : null,
                                  "processors" : [ ],
                                  "alertings" : [ ],
                                  "query" : "ummarize(asPercent(sumSeries(org.foo.bar.jmx.gravida.sessions),sumSeries(org.foo.bar.jmx.*.session)), \\"24h\\", \\"avg\\",true)",
                                  "parent" : null,
                                  "scheduler" : "0 0/5 * * * ?"
                                } ]
                              } ],
                              "frontConfig" : {
                                "menuLinks" : [ ],
                                "router" : [ ]
                              },
                              "gav" : {
                                "artifactId" : "inu-test",
                                "groupId" : "io.inugami",
                                "hash" : "io.inugami:inu-test:4.3.0",
                                "version" : "4.3.0"
                              },
                              "handlers" : [ ],
                              "listeners" : [ ],
                              "manifest" : {
                                "workspace" : "/home/me/dev/inugami"
                              },
                              "processors" : [ ],
                              "properties" : { },
                              "providers" : [ ]
                            }
                           """);
        assertText(service.findAllPlugin().stream().map(this::cleanPlugin).toList(),
                   """
                           [ {
                                "alertingProviders" : [ ],
                                "enabled" : false,
                                "eventConfigPresent" : false,
                                "events" : [ {
                                  "events" : [ {
                                    "type" : "Event",
                                    "name" : "composite-event",
                                    "fromFirstTime" : null,
                                    "until" : null,
                                    "provider" : null,
                                    "mapper" : null,
                                    "processors" : [ ],
                                    "alertings" : [ ],
                                    "scheduler" : "0 * * * * ?",
                                    "targets" : [ ]
                                  } ],
                                  "name" : "inu-events",
                                  "simpleEvents" : [ {
                                    "type" : "SimpleEvent",
                                    "name" : "simple-event",
                                    "fromFirstTime" : null,
                                    "until" : null,
                                    "provider" : null,
                                    "mapper" : null,
                                    "processors" : [ ],
                                    "alertings" : [ ],
                                    "query" : null,
                                    "parent" : null,
                                    "scheduler" : "0 0/5 * * * ?"
                                  } ]
                                } ],
                                "frontConfig" : {
                                  "menuLinks" : [ ],
                                  "router" : [ ]
                                },
                                "gav" : {
                                  "artifactId" : "inu-test",
                                  "groupId" : "io.inugami",
                                  "hash" : "io.inugami:inu-test:4.3.0",
                                  "version" : "4.3.0"
                                },
                                "handlers" : [ ],
                                "listeners" : [ ],
                                "processors" : [ ],
                                "providers" : [ ]
                              } ]
                           """);

    }


    @Test
    void validateConfiguration_withError() {
        assertLogs(() -> {
                       assertThrows(UncheckedException.class, () -> {
                           service().validateConfiguration(List.of(buildPluginConfiguration().toBuilder()
                                                                                             .gav(null)
                                                                                             .build()));
                       });
                   },
                   PluginService.class,
                   """
                           [
                               {
                                   "loggerName":"io.inugami.dashboard.core.domain.plugin.PluginService",
                                   "level":"ERROR",
                                   "mdc":{}
                                   "message":"[null] invalid plugin configuration : Cannot invoke \\"io.inugami.framework.interfaces.models.maven.Gav.getGroupId()\\" because the return value of \\"io.inugami.framework.configuration.models.plugins.PluginConfiguration.getGav()\\" is null"
                               }
                           ]
                           """);
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    PluginService service() {
        return PluginService.builder()
                            .applicationConfig(applicationConfig)
                            .configurationResolver(configurationResolver)
                            .pluginLoaderService(pluginLoaderService)
                            .spiLoaderService(spiLoaderService)
                            .eventDataDao(eventDataDao)
                            .build();
    }

    private Optional<List<PluginConfiguration>> buildPluginConfigurations() {
        return Optional.of(List.of(buildPluginConfiguration()));
    }


    private PluginConfiguration buildPluginConfiguration() {
        final Map<String, String> properties = new LinkedHashMap<>();
        properties.put("debug", "true");
        properties.put("timeout", "60000");

        return PluginConfiguration.builder()
                                  .gav(buildGav())
                                  .properties(properties)
                                  .frontConfig(PluginFrontConfig.builder()
                                                                .pluginBaseName("inu_test")
                                                                .commonsCss("inu_test.css")
                                                                .build())
                                  .build();
    }

    private Optional<List<EventConfig>> buildEventConfigs() {
        return Optional.of(List.of(
                EventConfig.builder()
                           .gav(buildGav())
                           .name("inu-events")
                           .simpleEvents(List.of(
                                   SimpleEvent.builder()
                                              .name("simple-event")
                                              .provider("graphite")
                                              .scheduler("0 0/5 * * * ?")
                                              .query("ummarize(asPercent(sumSeries(org.foo.bar.jmx.gravida.sessions),sumSeries(org.foo.bar.jmx.*.session)), \"24h\", \"avg\",true)")
                                              .build()
                                                ))
                           .events(Event.builder()
                                        .name("composite-event")
                                        .targets(TargetConfig.builder()
                                                             .name("target")
                                                             .provider("ela")
                                                             .scheduler("0 0 * * * ?")
                                                             .query("ummarize(asPercent(sumSeries(org.foo.bar.jmx.gravida.sessions),sumSeries(org.foo.bar.jmx.*.session)), \"24h\", \"avg\",true)")
                                                             .build())
                                        .build())
                           .build()
                                  ));
    }


    private Gav buildGav() {
        return Gav.builder()
                  .groupId("io.inugami")
                  .artifactId("inu-test")
                  .version("4.3.0")
                  .build();
    }

    private Plugin cleanPlugin(final Plugin plugin) {
        return plugin.toBuilder()
                     .clearListeners()
                     .clearProcessors()
                     .clearProviders()
                     .clearHandlers()
                     .clearAlertingProviders()
                     .build();
    }
}