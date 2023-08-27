package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;

import static io.inugami.api.exceptions.Asserts.assertNotNull;
import static io.inugami.api.exceptions.Asserts.assertNull;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertThrows;

class AssertNullTest {
    private static final ErrorCode ERROR_CODE    = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE = "error";
    public static final  String    VALUE         = "value";

    @Test
    void assertNull_withError() {
        String error = null;
        assertThrows("objects arguments must be null", () -> assertNull(error, VALUE));
        assertThrows(ERROR_MESSAGE, () -> assertNull(ERROR_MESSAGE, VALUE));
        assertThrows(ERROR_MESSAGE, () -> assertNull(() -> ERROR_MESSAGE, VALUE));
        assertThrows(ERROR_CODE, () -> assertNull(ERROR_CODE, VALUE));
    }

    @Test
    void assertNotNull_withError() {
        String error = null;
        assertThrows("this argument is required; it must not be null", () -> assertNotNull(null));
        assertThrows(ERROR_MESSAGE, () -> assertNotNull(ERROR_MESSAGE, VALUE, null));
        assertThrows(ERROR_MESSAGE, () -> assertNotNull(() -> ERROR_MESSAGE, VALUE, null));
        assertThrows(ERROR_CODE, () -> assertNotNull(ERROR_CODE, VALUE, null));
    }
}