package io.inugami.framework.interfaces.testing.exceptions.asserts;

import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.MultiUncheckedException;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.exceptions.asserts.AssertCommons;
import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

class AssertCommonsTest {

    @Test
    void throwException_withMessage() {
        UnitTestHelper.assertThrows("message", () -> AssertCommons.INSTANCE.throwException("message"));
    }

    @Test
    void throwException_withErrorCode() {
        UnitTestHelper.assertThrows(UncheckedException.class, () -> AssertCommons.INSTANCE.throwException(DefaultErrorCode.buildUndefineError()));
        ErrorCode errorCodeNull = null;
        UnitTestHelper.assertThrows(UncheckedException.class, () -> AssertCommons.INSTANCE.throwException(errorCodeNull));
    }

    @Test
    void throwException_withListErrorCode() {
        UnitTestHelper.assertThrows(MultiUncheckedException.class, () -> AssertCommons.INSTANCE.throwException(List.of(DefaultErrorCode.buildUndefineError())));
    }
}