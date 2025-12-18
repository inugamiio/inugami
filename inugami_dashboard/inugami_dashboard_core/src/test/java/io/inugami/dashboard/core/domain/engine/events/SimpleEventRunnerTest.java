package io.inugami.dashboard.core.domain.engine.events;

import io.inugami.commons.test.UnitTestData;
import io.inugami.dashboard.api.domain.engine.EngineListener;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.concurrent.FutureData;
import io.inugami.framework.interfaces.concurrent.FutureDataModel;
import io.inugami.framework.interfaces.concurrent.ImmediateFutureData;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.models.number.DataPoint;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleEventRunnerTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String                                     PROCESSOR_NAME = "processor_name";
    @Mock
    private             Provider                                   provider;
    @Mock
    private             EngineListener                             listener;
    @Mock
    private             Processor                                  processor;
    @Mock
    private             Processor                                  processorSecond;
    @Captor
    private             ArgumentCaptor<EnginePluginEventResultDTO> enginePluginEventResultDTOCaptor;

    @BeforeEach
    public void init() {
        lenient().when(processor.getName()).thenReturn(PROCESSOR_NAME);
        lenient().when(processorSecond.getName()).thenReturn("otherProcessor");
    }

    // =================================================================================================================
    // RUN EVENTS
    // =================================================================================================================
    @Test
    void run_nominal() throws Exception {
        when(provider.callEvent(any(), any(), any())).thenAnswer(answer -> buildProviderFutureResult(answer.getArgument(2)));
        when(processor.process(any(), any())).thenAnswer(answer -> answer.getArgument(1));
        final var callable = runner().run();

        final var result = callable.call();

        assertText(result,
                   """
                           {
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
                           }
                           """);
        verify(listener).onEventDone(any(), any(), enginePluginEventResultDTOCaptor.capture());
        assertText(enginePluginEventResultDTOCaptor.getValue(),
                   """
                           {
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
                           }
                           """);
    }

    @Test
    void run_withProcessorError() throws Exception {
        when(provider.callEvent(any(), any(), any())).thenAnswer(answer -> buildProviderFutureResult(answer.getArgument(2)));
        when(processor.process(any(), any())).thenThrow(new UncheckedException(DefaultErrorCode.buildUndefineError()));
        final var callable = runner().run();

        final var result = callable.call();

        assertText(result,
                   """
                           {
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
                           }
                           """);
        verify(listener).onEventDone(any(), any(), enginePluginEventResultDTOCaptor.capture());
        assertText(enginePluginEventResultDTOCaptor.getValue(),
                   """
                           {
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
                           }
                           """);
    }


    @Test
    void run_withProviderError() throws Exception {
        when(provider.callEvent(any(), any(), any())).thenThrow(new RuntimeException("sorry"));
        final var callable = runner().run();

        final var result = callable.call();

        assertText(result,
                   """
                           {
                             "error" : {
                               "message" : "sorry"
                             },
                             "errorCode" : {
                               "statusCode" : 500,
                               "errorCode" : "EVENT-0_0",
                               "errorType" : "technical",
                               "message" : "undefined error",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "message" : "sorry",
                             "name" : "event-name",
                             "status" : "ERROR"
                           }
                           """);
        verify(listener).onEventDone(any(), any(), enginePluginEventResultDTOCaptor.capture());
        assertText(enginePluginEventResultDTOCaptor.getValue(),
                   """
                           {
                             "error" : {
                               "message" : "sorry"
                             },
                             "errorCode" : {
                               "statusCode" : 500,
                               "errorCode" : "EVENT-0_0",
                               "errorType" : "technical",
                               "message" : "undefined error",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "message" : "sorry",
                             "name" : "event-name",
                             "status" : "ERROR"
                           }
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    SimpleEventRunner runner() {
        return SimpleEventRunner.builder()
                                .event(buildEvent())
                                .now(UnitTestData.DATE_TIME)
                                .plugin(buildPlugin())
                                .provider(provider)
                                .processors(List.of(processorSecond, processor))
                                .timeout(60000L)
                                .zoneOffset(ZoneOffset.UTC)
                                .listeners(List.of(listener))
                                .build();
    }


    private SimpleEvent buildEvent() {
        return SimpleEvent.builder()
                          .name("event-name")
                          .fromFirstTime("-10min")
                          .from("-5min")
                          .processors(List.of(ProcessorModel.builder()
                                                            .name(PROCESSOR_NAME)
                                                            .className("foo.bar.Processor")
                                                            .build()))
                          .query("query")
                          .scheduler("scheduler")
                          .mapper("mapper")
                          .provider("provider")
                          .build();
    }

    private Plugin buildPlugin() {
        return Plugin.builder()
                     .gav(Gav.builder()
                             .groupId("io.inugami")
                             .artifactId("inugami_api")
                             .version("3.3.0")
                             .qualifier("jar")
                             .build())
                     .build();
    }

    private FutureData<ProviderFutureResult> buildProviderFutureResult(final LocalDateTime date) {
        return FutureDataModel.<ProviderFutureResult>builder()
                              .future(ImmediateFutureData.<ProviderFutureResult>builder()
                                                         .data(ProviderFutureResult.builder()
                                                                                   .channel("SSE_inugami")
                                                                                   .data(DataPoint.builder()
                                                                                                  .timestamp(1766064662604L)
                                                                                                  .value(15.5)
                                                                                                  .build())
                                                                                   .build())
                                                         .build())
                              .build();
    }
}