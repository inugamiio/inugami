package io.inugami.framework.interfaces.exceptions.asserts;

import io.inugami.framework.interfaces.commons.UnitTestHelper;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AssertHigherTest {
    private static final ErrorCode ERROR_CODE        = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE     = "error";
    public static final  String    DEFAULT_ERROR_MSG = "value must be higher";

    @Test
    void assertHigher_int() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigher(error, 10, 10));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(ERROR_MESSAGE, 10, 10));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(() -> ERROR_MESSAGE, 10, 10));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigher(ERROR_CODE, 10, 10));
    }

    @Test
    void assertHigher_integer() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigher(error, Integer.valueOf(10), Integer.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(() -> ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigher(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(10)));
    }

    @Test
    void assertHigher_long() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigher(error, 10L, 10L));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(ERROR_MESSAGE, 10L, 10L));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(() -> ERROR_MESSAGE, 10L, 10L));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigher(ERROR_CODE, 10L, 10L));
    }

    @Test
    void assertHigher_Long() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigher(error, Long.valueOf(10), Long.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(() -> ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigher(ERROR_CODE, Long.valueOf(10), Long.valueOf(10)));
    }


    @Test
    void assertHigher_float() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigher(error, 10f, 10f));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(ERROR_MESSAGE, 10f, 10f));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(() -> ERROR_MESSAGE, 10f, 10f));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigher(ERROR_CODE, 10f, 10f));
    }

    @Test
    void assertHigher_double() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigher(error, 10.0, 10.0));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(ERROR_MESSAGE, 10.0, 10.0));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(() -> ERROR_MESSAGE, 10.0, 10.0));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigher(ERROR_CODE, 10.0, 10.0));
    }

    @Test
    void assertHigher_Double() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigher(error, Double.valueOf(10), Double.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigher(ERROR_CODE, Double.valueOf(10), Double.valueOf(10)));
    }

    @Test
    void assertHigher_BigDecimal() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigher(error, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigher(() -> ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigher(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
    }
}