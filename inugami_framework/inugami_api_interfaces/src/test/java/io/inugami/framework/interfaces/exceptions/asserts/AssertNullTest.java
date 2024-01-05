package io.inugami.framework.interfaces.exceptions.asserts;

import io.inugami.framework.interfaces.commons.UnitTestHelper;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;


class AssertNullTest {
    private static final ErrorCode ERROR_CODE    = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE = "error";
    public static final  String    VALUE         = "value";

    @Test
    void assertNull_withError() {
        String error = null;
        UnitTestHelper.assertThrows("objects arguments must be null", () -> Asserts.assertNull(error, VALUE));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNull(ERROR_MESSAGE, VALUE));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNull(() -> ERROR_MESSAGE, VALUE));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNull(ERROR_CODE, VALUE));
    }

    @Test
    void assertNotNull_withError() {
        String error = null;
        UnitTestHelper.assertThrows("this argument is required; it must not be null", () -> Asserts.assertNotNull(null));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotNull(ERROR_MESSAGE, VALUE, null));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotNull(() -> ERROR_MESSAGE, VALUE, null));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNotNull(ERROR_CODE, VALUE, null));
    }
}