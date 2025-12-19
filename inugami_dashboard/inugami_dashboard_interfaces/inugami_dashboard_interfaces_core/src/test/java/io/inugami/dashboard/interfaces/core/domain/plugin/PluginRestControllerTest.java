package io.inugami.dashboard.interfaces.core.domain.plugin;

import io.inugami.commons.test.mock.MockContext;
import io.inugami.commons.test.mock.MockGenerator;
import io.inugami.commons.test.mock.MockOpenApiContext;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.engine.exception.EngineErrors;
import io.inugami.dashboard.api.domain.plugin.IPluginService;
import io.inugami.dashboard.interfaces.core.domain.plugin.mapper.InugamiInterfacePluginMapperConfiguration;
import io.inugami.dashboard.interfaces.domain.plugin.PluginRestClient;
import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.event.TargetConfig;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.models.number.DataPoint;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PluginRestControllerTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private static final String         BASE_FOLDER             =
            "io/inugami/dashboard/interfaces/domain/plugin/pluginRestClient/";
    private static final String         FIND_ALL_PLUGIN         = BASE_FOLDER + "findAllPlugin";
    private static final String         FIND_PLUGIN_DATA_BY_GAV = BASE_FOLDER + "findPluginDataByGav";
    public static final  String         GROUP_ID                = "io.inugami.plugin";
    public static final  String         ARTIFACT_ID             = "inu-test";
    @Mock
    private              IPluginService pluginService;

    @AfterAll
    public static void generateOpenApi() {
        MockGenerator.generateOpenApiDocumentation(MockOpenApiContext.builder()
                                                                     .restClientClass(PluginRestClient.class)
                                                                     .folders(List.of(FIND_ALL_PLUGIN, FIND_PLUGIN_DATA_BY_GAV))
                                                                     .build());
    }

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Test
    void findAllPlugin_nominal() {
        when(pluginService.findAllPlugin()).thenReturn(List.of(buildPlugin()));
        final var response = controller().findAllPlugin();
        assertText(response,
                   """
                           [ {
                             "config" : {
                               "alertings" : [ ],
                               "components" : [ ],
                               "dependencies" : [ ],
                               "enable" : true,
                               "eventsFiles" : [ ],
                               "frontProperties" : [ ],
                               "handlers" : [ ],
                               "listeners" : [ ],
                               "processors" : [ ],
                               "properties" : { },
                               "providers" : [ ],
                               "resources" : [ ],
                               "security" : [ ]
                             },
                             "enabled" : false,
                             "eventConfigPresent" : false,
                             "events" : [ {
                               "enable" : true,
                               "events" : [ {
                                 "type" : "Event",
                                 "name" : "event-name",
                                 "fromFirstTime" : "-10min",
                                 "until" : null,
                                 "provider" : "provider",
                                 "mapper" : "mapper",
                                 "processors" : [ {
                                   "configs" : { },
                                   "name" : "processor_name"
                                 } ],
                                 "alertings" : [ ],
                                 "scheduler" : "0 0/5 * * * ?",
                                 "targets" : [ {
                                   "name" : null,
                                   "fromFirstTime" : null,
                                   "until" : null,
                                   "provider" : null,
                                   "mapper" : null,
                                   "processors" : [ ],
                                   "alertings" : [ ],
                                   "query" : null,
                                   "parent" : null,
                                   "scheduler" : null
                                 } ]
                               } ],
                               "gav" : {
                                 "artifactId" : "inu-test",
                                 "groupId" : "io.inugami.plugin",
                                 "hash" : "io.inugami.plugin:inu-test:4.3.0:jar",
                                 "qualifier" : "jar",
                                 "version" : "4.3.0"
                               },
                               "name" : "events",
                               "simpleEvents" : [ {
                                 "type" : "SimpleEvent",
                                 "name" : "event-name",
                                 "fromFirstTime" : "-10min",
                                 "until" : null,
                                 "provider" : "provider",
                                 "mapper" : "mapper",
                                 "processors" : [ {
                                   "configs" : { },
                                   "name" : "processor_name"
                                 } ],
                                 "alertings" : [ ],
                                 "query" : "query",
                                 "parent" : null,
                                 "scheduler" : "0 0/5 * * * ?"
                               } ]
                             } ],
                             "gav" : {
                               "artifactId" : "inu-test",
                               "groupId" : "io.inugami.plugin",
                               "hash" : "io.inugami.plugin:inu-test:4.3.0:jar",
                               "qualifier" : "jar",
                               "version" : "4.3.0"
                             }
                           } ]
                           """);

        MockGenerator.generate(MockContext.builder()
                                          .folder(FIND_ALL_PLUGIN)
                                          .get("/")
                                          .addRequestHeaderTracking()
                                          .addResponseHeaderTracking()
                                          .statusSuccess()
                                          .responsePayload(response)
                                          .build());
    }


    @Test
    void findPluginDataByGav_nominal() {
        when(pluginService.findPluginDataByGav(GROUP_ID, ARTIFACT_ID)).thenReturn(Map.of(
                GROUP_ID + ":" + ARTIFACT_ID, buildEnginePluginEventResultDTO()));
        final var response = controller().findPluginDataByGav(GROUP_ID, ARTIFACT_ID);

        assertText(response,
                   """
                           {
                             "events" : [ {
                               "data" : {
                                 "alerts" : [ ],
                                 "channel" : "SSE_inugami",
                                 "data" : [ {
                                   "timestamp" : 1766064662604,
                                   "value" : 15.5
                                 } ]
                               },
                               "errorCode" : {
                                 "statusCode" : 500,
                                 "errorCode" : "ENGINE-0_6",
                                 "errorType" : "technical",
                                 "message" : "error on read application configuration",
                                 "exploitationError" : false,
                                 "rollbackRequire" : false,
                                 "retryable" : false
                               },
                               "name" : "simple-event",
                               "status" : "SUCCESS"
                             } ],
                             "gav" : {
                               "artifactId" : "inu-test",
                               "groupId" : "io.inugami.plugin",
                               "hash" : "io.inugami.plugin:inu-test:null"
                             }
                           }
                           """);
        MockGenerator.generate(MockContext.builder()
                                          .folder(FIND_PLUGIN_DATA_BY_GAV)
                                          .get("/{groupId}/{artifactId}/data")
                                          .addRequestParam("groupId", GROUP_ID)
                                          .addRequestParam("artifactId", ARTIFACT_ID)
                                          .addRequestHeaderTracking()
                                          .addResponseHeaderTracking()
                                          .statusSuccess()
                                          .responsePayload(response)
                                          .build());
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    PluginRestController controller() {
        return PluginRestController.builder()
                                   .pluginService(pluginService)
                                   .enginePluginEventResultAPIMapper(new InugamiInterfacePluginMapperConfiguration().enginePluginEventResultAPIMapper())
                                   .build();
    }

    private static Plugin buildPlugin() {
        return Plugin.builder()
                     .config(PluginConfiguration.builder()
                                                .enable(true)
                                                .build())
                     .gav(buildGav())
                     .events(EventConfig.builder()
                                        .gav(buildGav())
                                        .name("events")
                                        .enable(true)
                                        .events(buildEvent())
                                        .simpleEvents(buildSimpleEvent())
                                        .build())
                     .build();
    }

    private static Gav buildGav() {
        return Gav.builder()
                  .groupId(GROUP_ID)
                  .artifactId(ARTIFACT_ID)
                  .version("4.3.0")
                  .qualifier("jar")
                  .build();
    }

    private static Event buildEvent() {
        return Event.builder()
                    .name("event-name")
                    .fromFirstTime("-10min")
                    .from("-5min")
                    .processors(List.of(ProcessorModel.builder()
                                                      .name("processor_name")
                                                      .build()))

                    .targets(List.of(TargetConfig.builder()
                                                 .build()))
                    .scheduler("0 0/5 * * * ?")
                    .mapper("mapper")
                    .provider("provider")
                    .build();
    }

    private static SimpleEvent buildSimpleEvent() {
        return SimpleEvent.builder()
                          .name("event-name")
                          .fromFirstTime("-10min")
                          .from("-5min")
                          .processors(List.of(ProcessorModel.builder()
                                                            .name("processor_name")
                                                            .build()))
                          .query("query")
                          .scheduler("0 0/5 * * * ?")
                          .mapper("mapper")
                          .provider("provider")
                          .build();
    }

    private EnginePluginEventResultDTO buildEnginePluginEventResultDTO() {
        return EnginePluginEventResultDTO.builder()
                                         .name("simple-event")
                                         .errorCode(EngineErrors.APPLICATION_CONFIG_ERROR)
                                         .status(Status.SUCCESS)
                                         .data(ProviderFutureResult.builder()
                                                                   .channel("SSE_inugami")
                                                                   .data(DataPoint.builder()
                                                                                  .timestamp(1766064662604L)
                                                                                  .value(15.5)
                                                                                  .build())
                                                                   .build())
                                         .build()
                                         .toBuilder()
                                         .build();
    }
}