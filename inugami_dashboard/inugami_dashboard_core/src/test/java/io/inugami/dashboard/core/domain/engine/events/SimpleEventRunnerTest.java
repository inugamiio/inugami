package io.inugami.dashboard.core.domain.engine.events;

import io.inugami.commons.test.UnitTestData;
import io.inugami.dashboard.api.domain.engine.EngineListener;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.providers.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneOffset;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.dashboard.core.domain.tools.DataUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleEventRunnerTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================

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
                                .event(buildSimpleEvent())
                                .now(UnitTestData.DATE_TIME)
                                .plugin(buildPlugin())
                                .provider(provider)
                                .processors(List.of(processorSecond, processor))
                                .timeout(60000L)
                                .zoneOffset(ZoneOffset.UTC)
                                .listeners(List.of(listener))
                                .build();
    }


}