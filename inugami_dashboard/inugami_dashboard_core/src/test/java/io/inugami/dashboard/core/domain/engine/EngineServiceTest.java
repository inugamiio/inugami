package io.inugami.dashboard.core.domain.engine;

import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.dashboard.api.domain.engine.EngineListener;
import io.inugami.dashboard.api.domain.engine.dto.EngineResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EventDoneDTO;
import io.inugami.dashboard.api.domain.event.IEventDataDao;
import io.inugami.dashboard.api.domain.sender.ISSESender;
import io.inugami.dashboard.core.domain.tools.DataUtils;
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.exceptions.services.ProcessorException;
import io.inugami.framework.interfaces.exceptions.services.ProviderException;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.dashboard.core.domain.tools.DataUtils.PROCESSOR_NAME;
import static io.inugami.dashboard.core.domain.tools.DataUtils.buildProviderFutureResult;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings({"java:S2925"})
@ExtendWith(MockitoExtension.class)
class EngineServiceTest {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final  String                             PROVIDER                  = "provider";
    private final static ThreadsExecutorService             MAIN_EXECUTOR_SERVICE     =
            new ThreadsExecutorService("EngineServiceMainExecutor",
                                       1,
                                       false,
                                       65000L);
    private final static ThreadsExecutorService             INTERNAL_EXECUTOR_SERVICE =
            new ThreadsExecutorService("EngineServiceInternalExecutor",
                                       1,
                                       false,
                                       60000L);
    public static final  String                             EVENT_DATA_DONE_NOMINAL   = """
            [ {
              "data" : {
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
              },
              "date" : "2025-12-19T21:51:00",
              "event" : {
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
              },
              "plugin" : {
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
              }
            }, {
              "data" : {
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
              },
              "date" : "2025-12-19T21:51:00",
              "event" : {
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
              },
              "plugin" : {
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
              }
            }, {
              "data" : {
                "data" : {
                  "alerts" : [ ],
                  "data" : [ {
                    "timestamp" : 1766064662604,
                    "value" : 15.5
                  } ]
                },
                "name" : "event-name",
                "status" : "SUCCESS"
              },
              "date" : "2025-12-19T21:51:00",
              "event" : {
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
              },
              "plugin" : {
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
              }
            }, {
              "data" : {
                "data" : {
                  "alerts" : [ ],
                  "data" : [ {
                    "timestamp" : 1766064662604,
                    "value" : 15.5
                  } ]
                },
                "name" : "event-name",
                "status" : "SUCCESS"
              },
              "date" : "2025-12-19T21:51:00",
              "event" : {
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
              },
              "plugin" : {
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
              }
            } ]
            """;
    @Mock
    private              Clock                              clock;
    @Mock
    private              Provider                           provider;
    @Mock
    private              Processor                          processor;
    @Mock
    private              IEventDataDao                      eventDataDao;
    @Mock
    private              ISSESender                         sseSender;
    @Captor
    private              ArgumentCaptor<List<EventDoneDTO>> updateEventsDataCaptor;
    @Captor
    private              ArgumentCaptor<List<EventDoneDTO>> sseSenderDataCaptor;

    @AfterAll
    public static void shutdown() {
        INTERNAL_EXECUTOR_SERVICE.shutdown();
        MAIN_EXECUTOR_SERVICE.shutdown();
    }

    @BeforeEach
    public void init() throws ProviderException, ProcessorException {
        lenient().when(clock.getZone()).thenReturn(ZoneOffset.UTC);
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

    //==================================================================================================================
    // RUN
    //==================================================================================================================
    @Test
    void run_nominal() throws InterruptedException {
        final List<EngineResultDTO> results = new ArrayList<>();
        final EngineListener listener = new EngineListener() {
            @Override
            public void onDone(final EngineResultDTO engineResult) {
                results.add(engineResult);
            }
        };

        final var           firstTime  = Instant.parse("2025-12-19T21:50:00.00Z");
        final var           secondTime = Instant.parse("2025-12-19T21:51:00.00Z");
        final AtomicBoolean firstClock = new AtomicBoolean(true);
        when(clock.instant()).thenAnswer(answer -> {
            if (firstClock.get()) {
                firstClock.set(false);
                return firstTime;
            }
            return secondTime;
        });


        final var service = service(listener);

        //--------------------------------------------------------------------------------------------------------------
        // first run to collect data
        //--------------------------------------------------------------------------------------------------------------
        service.run();
        Thread.sleep(500);
        assertText(results,
                   """
                           [ {
                                "end" : "2025-12-19T21:51:00",
                                "plugins" : [ {
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
                                } ],
                                "processId" : "7ee6ec58-6cd9-4cec-a1ed-78dc52592094",
                                "start" : "2025-12-19T21:50:00",
                                "status" : "SUCCESS",
                                "traceId" : "a9d26441-c793-4986-9d72-334a29a37167"
                              } ]
                           """,
                   SkipLineMatcher.of(34, 37));
        //--------------------------------------------------------------------------------------------------------------
        // second run to send data
        //--------------------------------------------------------------------------------------------------------------
        service.run();
        Thread.sleep(500);
        verify(eventDataDao).updateEventsData(updateEventsDataCaptor.capture());
        verify(sseSender).onEventDone(sseSenderDataCaptor.capture());

        assertText(updateEventsDataCaptor.getValue(), EVENT_DATA_DONE_NOMINAL);

        assertText(sseSenderDataCaptor.getValue(), EVENT_DATA_DONE_NOMINAL);
    }

    @Test
    void run_withError() throws InterruptedException {
        final List<EngineResultDTO> results = new ArrayList<>();
        final EngineListener listener = new EngineListener() {
            @Override
            public void onDone(final EngineResultDTO engineResult) {
                results.add(engineResult);
            }
        };
        when(provider.callEvent(any(), any(), any())).thenThrow(new UncheckedException(DefaultErrorCode.buildUndefineError()));
        when(clock.instant()).thenReturn(Instant.parse("2025-12-19T21:50:00.00Z"));


        final var service = service(listener);

        //--------------------------------------------------------------------------------------------------------------
        // first run to collect data
        //--------------------------------------------------------------------------------------------------------------
        service.run();
        Thread.sleep(500);
        assertText(clean(results.stream().findFirst().orElse(null)),
                   """
                           {
                                "end" : "2025-12-19T21:50:00",
                                "plugins" : [ {
                                  "events" : [ {
                                    "error" : {
                                      "message" : "",
                                      "errorCode" : {
                                        "statusCode" : 500,
                                        "errorCode" : "err-undefine",
                                        "errorType" : "technical",
                                        "exploitationError" : false,
                                        "rollbackRequire" : false,
                                        "retryable" : false
                                      }
                                    },
                                    "errorCode" : {
                                      "statusCode" : 500,
                                      "errorCode" : "err-undefine",
                                      "errorType" : "technical",
                                      "exploitationError" : false,
                                      "rollbackRequire" : false,
                                      "retryable" : false
                                    },
                                    "name" : "event-name",
                                    "status" : "ERROR"
                                  } ],
                                  "gav" : {
                                    "artifactId" : "inu-test",
                                    "groupId" : "io.inugami.plugin",
                                    "hash" : "io.inugami.plugin:inu-test:4.3.0:jar",
                                    "qualifier" : "jar",
                                    "version" : "4.3.0"
                                  },
                                  "status" : "ERROR"
                                } ],
                                "processId" : "fcfe1060-0ceb-4bd7-babc-9e8c5bcd37d4",
                                "start" : "2025-12-19T21:50:00",
                                "status" : "ERROR",
                                "traceId" : "571ebdab-819a-4df9-8991-0c8b8038ab5e"
                              }
                           """,
                   SkipLineMatcher.of(35, 38));

    }

    private EngineResultDTO clean(final EngineResultDTO value) {
        return value.toBuilder()
                    .clearPlugins()
                    .plugins(List.of(value.getPlugins().stream()
                                          .findFirst()
                                          .map(pluginResult -> pluginResult.toBuilder()
                                                                           .clearEvents()
                                                                           .events(pluginResult.getEvents()
                                                                                               .stream()
                                                                                               .filter(e -> e.getMessage() ==
                                                                                                            null ||
                                                                                                            e.getMessage()
                                                                                                             .isEmpty())
                                                                                               .toList())
                                                                           .build())
                                          .orElse(null)))
                    .build();
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    EngineService service(final EngineListener listener) {
        return EngineService.builder()
                            .clock(clock)
                            .zoneOffset(ZoneOffset.UTC)
                            .listeners(new ArrayList<>(List.of(listener)))
                            .plugins(List.of(DataUtils.buildPlugin()
                                                      .toBuilder()
                                                      .processors(processor)
                                                      .providers(provider)
                                                      .build()))
                            .eventDataDao(eventDataDao)
                            .sseSender(sseSender)
                            .threadsExecutorInternal(INTERNAL_EXECUTOR_SERVICE)
                            .threadsExecutor(MAIN_EXECUTOR_SERVICE)
                            .timeout(70000L)
                            .build()
                            .init();
    }
}