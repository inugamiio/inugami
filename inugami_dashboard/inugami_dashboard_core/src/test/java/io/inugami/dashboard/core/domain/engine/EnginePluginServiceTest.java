package io.inugami.dashboard.core.domain.engine;

import io.inugami.commons.test.UnitTestData;
import io.inugami.dashboard.api.domain.engine.EngineListener;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.core.domain.tools.DataUtils;
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.interfaces.exceptions.services.ProcessorException;
import io.inugami.framework.interfaces.exceptions.services.ProviderException;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.dashboard.core.domain.tools.DataUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class EnginePluginServiceTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private final static ThreadsExecutorService THREADS_EXECUTOR_SERVICE =
            new ThreadsExecutorService("EnginePluginServiceTest",
                                       10,
                                       false,
                                       65000L);
    @Mock
    private              Provider               provider;
    @Mock
    private              Processor              processor;
    @Mock
    private              EngineListener         listener;


    // =================================================================================================================
    // INIT
    // =================================================================================================================
    @AfterAll
    public static void shutdown() {
        THREADS_EXECUTOR_SERVICE.shutdown();
    }

    @BeforeEach
    public void init() throws ProviderException, ProcessorException {
        lenient().when(processor.getName()).thenReturn(PROCESSOR_NAME);
        lenient().when(processor.process(any(), any())).thenAnswer(answer -> answer.getArgument(1));
        lenient().when(provider.getName()).thenReturn(PROVIDER);
        lenient().when(provider.callEvent(any(), any(), any()))
                 .thenAnswer(answer -> buildProviderFutureResult(answer.getArgument(2)));
        lenient().when(provider.aggregate(any())).thenAnswer(answer -> {
            final List<ProviderFutureResult> input = answer.getArgument(0);
            return ProviderFutureResult.builder()
                                       .data(input.stream()
                                                  .map(item -> item.getData())
                                                  .flatMap(List::stream)
                                                  .toList())
                                       .build();
        });
    }

    // =================================================================================================================
    // TEST
    // =================================================================================================================

    @Test
    void isEnabled_nominal() {
        assertThat(service().isEnabled()).isTrue();
        assertThat(service(Plugin.builder()
                                 .config(PluginConfiguration.builder()
                                                            .enable(false)
                                                            .build())
                                 .gav(Gav.builder()
                                         .groupId("io.inugami.plugin")
                                         .artifactId("inu-test")
                                         .version("4.3.0")
                                         .qualifier("jar")
                                         .build())
                                 .build()).isEnabled()).isFalse();
    }

    @Test
    void hasEventsToRun_nominal() {
        final var service = service();
        assertThat(service.hasEventsToRun(UnitTestData.DATE_TIME)).isTrue();
        assertThat(service.hasEventsToRun(UnitTestData.DATE_TIME.plusMinutes(2))).isFalse();
    }

    @Disabled
    @Test
    void run_nominal() {
        final var                              service                     = service();
        final List<EnginePluginEventResultDTO> enginePluginEventResultDone = new ArrayList<>();

        final var engineListener = new EngineListener() {
            @Override
            public void onEventDone(final Plugin plugin,
                                    final GenericEvent<?> event,
                                    final EnginePluginEventResultDTO data) {
                enginePluginEventResultDone.add(data);
            }
        };
       final var result =  service.run(List.of(engineListener), UnitTestData.DATE_TIME);

        Collections.sort(enginePluginEventResultDone);
        assertText(enginePluginEventResultDone,
                   """
                           [ {
                             "data" : {
                               "alerts" : [ ],
                               "channel" : "SSE_inugami",
                               "data" : [ {
                                 "timestamp" : 1766064662604,
                                 "value" : 15.5
                               } ]
                             },
                             "name" : "event-name",
                             "status" : "SUCCESS"
                           }, {
                             "data" : {
                               "alerts" : [ ],
                               "data" : [ {
                                 "timestamp" : 1766064662604,
                                 "value" : 15.5
                               } ]
                             },
                             "name" : "event-name",
                             "status" : "SUCCESS"
                           } ]
                           """);
        assertText(result,
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
                               "name" : "event-name",
                               "status" : "SUCCESS"
                             }, {
                               "data" : {
                                 "alerts" : [ ],
                                 "data" : [ {
                                   "timestamp" : 1766064662604,
                                   "value" : 15.5
                                 } ]
                               },
                               "name" : "event-name",
                               "status" : "SUCCESS"
                             } ],
                             "gav" : {
                               "artifactId" : "inu-test",
                               "groupId" : "io.inugami.plugin",
                               "hash" : "io.inugami.plugin:inu-test:4.3.0:jar",
                               "qualifier" : "jar",
                               "version" : "4.3.0"
                             },
                             "status" : "SUCCESS"
                           }
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    EnginePluginService service() {
        return service(DataUtils.buildPlugin());
    }

    EnginePluginService service(Plugin plugin) {
        return EnginePluginService.builder()
                                  .plugin(plugin)
                                  .providers(List.of(provider))
                                  .processors(List.of(processor))
                                  .zoneOffset(ZoneOffset.UTC)
                                  .timeout(60000L)
                                  .threadsExecutorService(THREADS_EXECUTOR_SERVICE)
                                  .listeners(List.of(listener))
                                  .build();
    }
}