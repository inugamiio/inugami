package io.inugami.framework.api.tools;

import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.functionnals.GenericActionWithException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.inugami.framework.api.tools.RunSafeUtils.*;
import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class RunSafeUtilsTest {

    public static final String             HELLO               = "hello";
    public static final String             ERROR               = "error";
    public static final String             SUCCESS             = "success";
    public static final UncheckedException UNCHECKED_EXCEPTION = new UncheckedException("sorry");

    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(RunSafeUtils.class);
    }

    @Test
    void runSafeOrElse_nominal() {
        assertThat(runSafeOrElse(() -> HELLO, ERROR)).isEqualTo(HELLO);

        GenericActionWithException<String> action = () -> {
            throw new Exception("sorry");
        };
        assertThat(runSafeOrElse(action, ERROR)).isEqualTo(ERROR);
    }

    @Test
    void runSafe_nominal() {
        assertThat(runSafe(() -> HELLO)).isEqualTo(HELLO);
        GenericActionWithException<String> action = () -> {
            throw new Exception("sorry");
        };
        assertThat(runSafe(action)).isNull();
    }

    @Test
    void runSafeVoid_nominal() {
        final List<String> data = new ArrayList<>();
        runSafeVoid(() -> data.add("OK"));
        runSafeVoid(() -> {
            throw new Exception("sorry");
        });
        assertText(data, """
                [ "OK" ]
                """);
    }

    @Test
    void onVoidError_nominal() {
        final List<Throwable> errors = new ArrayList<>();
        onVoidError(() -> {
            log.info(SUCCESS);
        }, errors::add);

        onVoidError(() -> {
            throw UNCHECKED_EXCEPTION;
        }, errors::add);

        assertText(errors,
                   """
                           [ {
                             "message" : "sorry",
                             "errorCode" : {
                               "statusCode" : 500,
                               "errorCode" : "err-undefine",
                               "errorType" : "technical",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             }
                           } ]
                           """);
    }

    @Test
    void onError_nominal() {
        final List<Throwable> errors = new ArrayList<>();
        assertThat(onError(() -> SUCCESS, errors::add)).isEqualTo(SUCCESS);

        GenericActionWithException<String> actionErr = () -> {
            throw UNCHECKED_EXCEPTION;
        };

        assertThat(onError(actionErr, errors::add)).isNull();

        assertText(errors,
                   """
                           [ {
                             "message" : "sorry",
                             "errorCode" : {
                               "statusCode" : 500,
                               "errorCode" : "err-undefine",
                               "errorType" : "technical",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             }
                           } ]
                           """);
    }
}