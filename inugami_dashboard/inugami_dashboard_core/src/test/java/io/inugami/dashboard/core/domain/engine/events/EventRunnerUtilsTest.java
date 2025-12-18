package io.inugami.dashboard.core.domain.engine.events;

import io.inugami.commons.test.UnitTestHelper;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.exceptions.services.ProcessorException;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.dashboard.core.domain.engine.events.EventRunnerUtils.resolveErrorCode;
import static io.inugami.dashboard.core.domain.engine.events.EventRunnerUtils.selectProcessor;
import static org.assertj.core.api.Assertions.assertThat;

class EventRunnerUtilsTest {


    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================

    // =================================================================================================================
    // UTILITY CLASS
    // =================================================================================================================
    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(EventRunnerUtils.class);
    }

    // =================================================================================================================
    // SELECT PROCESSOR
    // =================================================================================================================
    @Test
    void selectProcessor_nominal() {
        MyProcessor myProcessor = new MyProcessor();
        assertThat(selectProcessor(List.of(ProcessorModel.builder()
                                                         .name("myProcessor")
                                                         .build()), List.of(myProcessor))).isNotEmpty();

        assertThat(selectProcessor(List.of(ProcessorModel.builder()
                                                         .name(MyProcessor.class.getName())
                                                         .build()), List.of(myProcessor))).isNotEmpty();

        assertThat(selectProcessor(List.of(ProcessorModel.builder()
                                                         .className(MyProcessor.class.getName())
                                                         .build()), List.of(myProcessor))).isNotEmpty();

        assertThat(selectProcessor(List.of(ProcessorModel.builder()
                                                         .name("other")
                                                         .build()), List.of(myProcessor))).isEmpty();

    }

    // =================================================================================================================
    // SELECT PROCESSOR
    // =================================================================================================================
    @Test
    void resolveErrorCode_nominal() {
        assertText(resolveErrorCode(new Exception("sorry")),
                   """
                           {
                             "statusCode" : 500,
                             "errorCode" : "EVENT-0_0",
                             "errorType" : "technical",
                             "message" : "undefined error",
                             "exploitationError" : false,
                             "rollbackRequire" : false,
                             "retryable" : false
                           }
                           """);

        assertText(resolveErrorCode(null),
                   """
                           {
                             "statusCode" : 500,
                             "errorCode" : "EVENT-0_0",
                             "errorType" : "technical",
                             "message" : "undefined error",
                             "exploitationError" : false,
                             "rollbackRequire" : false,
                             "retryable" : false
                           }
                           """);

        assertText(resolveErrorCode(new UncheckedException(DefaultErrorCode.buildUndefineError())),
                   """
                           {
                             "statusCode" : 500,
                             "errorCode" : "err-undefine",
                             "errorType" : "technical",
                             "exploitationError" : false,
                             "rollbackRequire" : false,
                             "retryable" : false
                           }
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private static class MyProcessor implements Processor {
        @Override
        public String getName() {
            return "myProcessor";
        }

        @Override
        public ProviderFutureResult process(final GenericEvent event,
                                            final ProviderFutureResult data) throws ProcessorException {
            return null;
        }
    }
}