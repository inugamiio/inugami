package io.inugami.api.exceptions.asserts;

import io.inugami.interfaces.exceptions.DefaultErrorCode;
import io.inugami.interfaces.exceptions.ErrorCode;
import io.inugami.interfaces.exceptions.MultiUncheckedException;
import io.inugami.interfaces.exceptions.UncheckedException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.api.tools.unit.test.UnitTestHelper.assertThrows;

class AssertCommonsTest {

    @Test
    void throwException_withMessage() {
        assertThrows("message", () -> AssertCommons.INSTANCE.throwException("message"));
    }

    @Test
    void throwException_withErrorCode() {
        assertThrows(UncheckedException.class, () -> AssertCommons.INSTANCE.throwException(DefaultErrorCode.buildUndefineError()));
        ErrorCode errorCodeNull = null;
        assertThrows(UncheckedException.class, () -> AssertCommons.INSTANCE.throwException(errorCodeNull));
    }

    @Test
    void throwException_withListErrorCode() {
        assertThrows(MultiUncheckedException.class, () -> AssertCommons.INSTANCE.throwException(List.of(DefaultErrorCode.buildUndefineError())));
    }
}