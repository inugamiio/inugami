package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.inugami.api.exceptions.Asserts.assertHigher;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertThrows;

class AssertHigherTest {
    private static final ErrorCode ERROR_CODE        = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE     = "error";
    public static final  String    DEFAULT_ERROR_MSG = "value must be higher";

    @Test
    void assertHigher_int() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertHigher(error, 10, 10));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(ERROR_MESSAGE, 10, 10));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(() -> ERROR_MESSAGE, 10, 10));
        assertThrows(ERROR_CODE, () -> assertHigher(ERROR_CODE, 10, 10));
    }

    @Test
    void assertHigher_integer() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertHigher(error, Integer.valueOf(10), Integer.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(() -> ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(10)));
        assertThrows(ERROR_CODE, () -> assertHigher(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(10)));
    }

    @Test
    void assertHigher_long() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertHigher(error, 10L, 10L));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(ERROR_MESSAGE, 10L, 10L));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(() -> ERROR_MESSAGE, 10L, 10L));
        assertThrows(ERROR_CODE, () -> assertHigher(ERROR_CODE, 10L, 10L));
    }

    @Test
    void assertHigher_Long() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertHigher(error, Long.valueOf(10), Long.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(() -> ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(10)));
        assertThrows(ERROR_CODE, () -> assertHigher(ERROR_CODE, Long.valueOf(10), Long.valueOf(10)));
    }


    @Test
    void assertHigher_float() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertHigher(error, 10f, 10f));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(ERROR_MESSAGE, 10f, 10f));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(() -> ERROR_MESSAGE, 10f, 10f));
        assertThrows(ERROR_CODE, () -> assertHigher(ERROR_CODE, 10f, 10f));
    }

    @Test
    void assertHigher_double() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertHigher(error, 10.0, 10.0));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(ERROR_MESSAGE, 10.0, 10.0));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(() -> ERROR_MESSAGE, 10.0, 10.0));
        assertThrows(ERROR_CODE, () -> assertHigher(ERROR_CODE, 10.0, 10.0));
    }

    @Test
    void assertHigher_Double() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertHigher(error, Double.valueOf(10), Double.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10)));
        assertThrows(ERROR_CODE, () -> assertHigher(ERROR_CODE, Double.valueOf(10), Double.valueOf(10)));
    }

    @Test
    void assertHigher_BigDecimal() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertHigher(error, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertHigher(() -> ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        assertThrows(ERROR_CODE, () -> assertHigher(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
    }
}