package io.inugami.dashboard.core.domain.engine.events;

import io.inugami.commons.test.UnitTestData;
import io.inugami.dashboard.api.domain.engine.EngineListener;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.exceptions.services.ProcessorException;
import io.inugami.framework.interfaces.exceptions.services.ProviderException;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.TargetConfig;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.dashboard.core.domain.tools.DataUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventRunnerTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private final static ThreadsExecutorService                     THREADS_EXECUTOR_SERVICE =
            new ThreadsExecutorService("EventRunnerTestThreads",
                                       1,
                                       false,
                                       65000L);
    public static final  String                                     PROVIDER                 = "provider";
    public static final  String                                     PROVIDER_SECOND          = "providerSecond";
    public static final  String                                     NOMINAL                  = """
            {
              "data" : {
                "alerts" : [ ],
                "data" : [ {
                  "timestamp" : 1766064662604,
                  "value" : 15.5
                } ]
              },
              "name" : "nominal",
              "status" : "SUCCESS"
            }
            """;
    @Mock
    private              Provider                                   provider;
    @Mock
    private              Provider                                   providerSecond;
    @Mock
    private              EngineListener                             listener;
    @Mock
    private              Processor                                  processor;
    @Mock
    private              Processor                                  processorSecond;
    @Captor
    private              ArgumentCaptor<EnginePluginEventResultDTO> enginePluginEventResultDTOCaptor;


    @AfterAll
    public static void shutdown() {
        THREADS_EXECUTOR_SERVICE.shutdown();
    }

    @BeforeEach
    public void init() throws ProviderException, ProcessorException {
        lenient().when(processor.getName()).thenReturn(PROCESSOR_NAME);
        lenient().when(processor.process(any(), any())).thenAnswer(answer -> answer.getArgument(1));
        lenient().when(processorSecond.getName()).thenReturn("otherProcessor");
        lenient().when(provider.getName()).thenReturn(PROVIDER);
        lenient().when(providerSecond.getName()).thenReturn(PROVIDER_SECOND);
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
    // RUN EVENTS
    // =================================================================================================================
    @Disabled
    @Test
    void runEvent_nominal() throws Exception {
        final var runner = runner(Event.builder()
                                       .name("nominal")
                                       .provider(PROVIDER)
                                       .targets(TargetConfig.builder()
                                                            .name("nominal_target")
                                                            .processors(List.of(ProcessorModel.builder()
                                                                                              .name(PROCESSOR_NAME)
                                                                                              .build()))
                                                            .build())
                                       .build());
        final var callable = runner.run();
        final var result   = callable.call();
        assertText(result, NOMINAL);
    }

    @Disabled
    @Test
    void runEvent_nominal_withoutMainProvider() throws Exception {
        final var runner = runner(Event.builder()
                                       .name("nominal")
                                       .targets(TargetConfig.builder()
                                                            .name("nominal_target")
                                                            .provider(PROVIDER)
                                                            .processors(List.of(ProcessorModel.builder()
                                                                                              .name(PROCESSOR_NAME)
                                                                                              .build()))
                                                            .build())
                                       .build());
        final var callable = runner.run();
        final var result   = callable.call();
        assertText(result, NOMINAL);
    }

    @Disabled
    @Test
    void runEvent_withoutProvider() throws Exception {
        final var runner = runner(Event.builder()
                                       .name("nominal")
                                       .targets(TargetConfig.builder()
                                                            .name("nominal_target")
                                                            .processors(List.of(ProcessorModel.builder()
                                                                                              .name(PROCESSOR_NAME)
                                                                                              .build()))
                                                            .build())
                                       .build());
        final var callable = runner.run();
        final var result   = callable.call();
        assertText(result,
                   """
                           {
                             "message" : "no provider found",
                             "name" : "nominal",
                             "status" : "NOTHING_TO_DO"
                           }
                           """);
    }

    @Disabled
    @Test
    void runEvent_withProviderError() throws Exception {
        when(provider.callEvent(any(), any(), any())).thenThrow(new UncheckedException("sorry"));
        final var runner = runner(Event.builder()
                                       .name("nominal")
                                       .provider(PROVIDER)
                                       .targets(TargetConfig.builder()
                                                            .name("nominal_target")
                                                            .processors(List.of(ProcessorModel.builder()
                                                                                              .name(PROCESSOR_NAME)
                                                                                              .build()))
                                                            .build())
                                       .build());
        final var callable = runner.run();
        final var result   = callable.call();
        assertText(result,
                   """
                           {
                             "message" : "sorry",
                             "name" : "nominal",
                             "status" : "ERROR"
                           }
                           """);
    }

    @Disabled
    @Test
    void runEvent_withAggregatorError() throws Exception {
        when(providerSecond.callEvent(any(), any(), any()))
                .thenAnswer(answer -> buildProviderFutureResult(answer.getArgument(2)));
        when(providerSecond.aggregate(any())).thenThrow(new UncheckedException("sorry"));
        final var runner = runner(Event.builder()
                                       .name("nominal")
                                       .provider(PROVIDER_SECOND)
                                       .targets(TargetConfig.builder()
                                                            .name("nominal_target")
                                                            .processors(List.of(ProcessorModel.builder()
                                                                                              .name(PROCESSOR_NAME)
                                                                                              .build()))
                                                            .build())
                                       .build());
        final var callable = runner.run();
        final var result   = callable.call();
        assertText(result,
                   """
                           {
                             "error" : {
                               "message" : "sorry",
                               "errorCode" : {
                                 "statusCode" : 500,
                                 "errorCode" : "err-undefine",
                                 "errorType" : "technical",
                                 "exploitationError" : false,
                                 "rollbackRequire" : false,
                                 "retryable" : false
                               }
                             },
                             "message" : "sorry",
                             "name" : "nominal",
                             "status" : "ERROR"
                           }
                           """);
    }

    @Disabled
    @Test
    void runEvent_withoutEvent() throws Exception {
        final var callable = runner(null).run();
        final var result   = callable.call();
        assertText(result,
                   """
                           {
                             "name" : "undefined",
                             "status" : "NOTHING_TO_DO"
                           }
                           """);
    }

    @Disabled
    @Test
    void runEvent_withoutTarget() throws Exception {
        final var callable = runner(Event.builder()
                                         .name("withoutTargets")
                                         .build()).run();
        final var result = callable.call();
        assertText(result,
                   """
                           {
                             "name" : "withoutTargets",
                             "status" : "NOTHING_TO_DO"
                           }
                           """);

        final var callableEmpty = runner(Event.builder()
                                              .name("withoutTargets")
                                              .targets(List.of())
                                              .build()).run();
        final var resultEmpty = callableEmpty.call();
        assertText(resultEmpty,
                   """
                           {
                             "name" : "withoutTargets",
                             "status" : "NOTHING_TO_DO"
                           }
                           """);
    }

    @Disabled
    @Test
    void onError_nominal() {
        final EventRunner runner = runner(Event.builder().build());

        final List<EnginePluginEventResultDTO> status = new ArrayList<>();
        runner.onError(new UncheckedException(DefaultErrorCode.buildUndefineError()), null, status);

        assertText(status,
                   """
                           [ {
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
                             "status" : "ERROR"
                           } ]
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    EventRunner runner(final Event event) {
        return EventRunner.builder()
                          .event(event)
                          .now(UnitTestData.DATE_TIME)
                          .plugin(buildPlugin())
                          .processors(List.of(processor, processorSecond))
                          .zoneOffset(ZoneOffset.UTC)
                          .timeout(60000L)
                          .threadsExecutorService(THREADS_EXECUTOR_SERVICE)
                          .listeners(List.of(listener))
                          .providers(List.of(provider, providerSecond))
                          .build();
    }
}