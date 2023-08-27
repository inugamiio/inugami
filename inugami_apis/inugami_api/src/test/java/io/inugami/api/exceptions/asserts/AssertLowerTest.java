package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.inugami.api.exceptions.Asserts.assertLower;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertThrows;

class AssertLowerTest {
    private static final ErrorCode ERROR_CODE        = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE     = "error";
    public static final  String    DEFAULT_ERROR_MSG = "value must be lower";

    @Test
    void assertLower_int() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertLower(error, 10, 10));
        assertThrows(ERROR_MESSAGE, () -> assertLower(ERROR_MESSAGE, 10, 10));
        assertThrows(ERROR_MESSAGE, () -> assertLower(() -> ERROR_MESSAGE, 10, 10));
        assertThrows(ERROR_CODE, () -> assertLower(ERROR_CODE, 10, 10));
    }

    @Test
    void assertLower_integer() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertLower(error, Integer.valueOf(10), Integer.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertLower(ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertLower(() -> ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(10)));
        assertThrows(ERROR_CODE, () -> assertLower(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(10)));
    }

    @Test
    void assertLower_long() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertLower(error, 10L, 10L));
        assertThrows(ERROR_MESSAGE, () -> assertLower(ERROR_MESSAGE, 10L, 10L));
        assertThrows(ERROR_MESSAGE, () -> assertLower(() -> ERROR_MESSAGE, 10L, 10L));
        assertThrows(ERROR_CODE, () -> assertLower(ERROR_CODE, 10L, 10L));
    }

    @Test
    void assertLower_Long() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertLower(error, Long.valueOf(10), Long.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertLower(ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertLower(() -> ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(10)));
        assertThrows(ERROR_CODE, () -> assertLower(ERROR_CODE, Long.valueOf(10), Long.valueOf(10)));
    }


    @Test
    void assertLower_float() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertLower(error, 10f, 10f));
        assertThrows(ERROR_MESSAGE, () -> assertLower(ERROR_MESSAGE, 10f, 10f));
        assertThrows(ERROR_MESSAGE, () -> assertLower(() -> ERROR_MESSAGE, 10f, 10f));
        assertThrows(ERROR_CODE, () -> assertLower(ERROR_CODE, 10f, 10f));
    }

    @Test
    void assertLower_double() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertLower(error, 10.0, 10.0));
        assertThrows(ERROR_MESSAGE, () -> assertLower(ERROR_MESSAGE, 10.0, 10.0));
        assertThrows(ERROR_MESSAGE, () -> assertLower(() -> ERROR_MESSAGE, 10.0, 10.0));
        assertThrows(ERROR_CODE, () -> assertLower(ERROR_CODE, 10.0, 10.0));
    }

    @Test
    void assertLower_Double() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertLower(error, Double.valueOf(10), Double.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertLower(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertLower(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10)));
        assertThrows(ERROR_CODE, () -> assertLower(ERROR_CODE, Double.valueOf(10), Double.valueOf(10)));
    }

    @Test
    void assertLower_BigDecimal() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertLower(error, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertLower(ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        assertThrows(ERROR_MESSAGE, () -> assertLower(() -> ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        assertThrows(ERROR_CODE, () -> assertLower(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
    }
}