package io.inugami.api.exceptions.asserts;

import io.inugami.interfaces.exceptions.DefaultErrorCode;
import io.inugami.interfaces.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.inugami.api.exceptions.Asserts.assertEquals;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertThrows;

class AssertEqualsTest {
    private static final ErrorCode ERROR_CODE        = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE     = "error";
    public static final  String    DEFAULT_ERROR_MSG = "object must be equals";

    @Test
    void assertEquals_int() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertEquals(error, 10, 11));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(ERROR_MESSAGE, 10, 11));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(() -> ERROR_MESSAGE, 10, 11));
        assertThrows(ERROR_CODE, () -> assertEquals(ERROR_CODE, 10, 11));
    }

    @Test
    void assertEquals_integer() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertEquals(error, Integer.valueOf(10), Integer.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(() -> ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(11)));
        assertThrows(ERROR_CODE, () -> assertEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(11)));
    }

    @Test
    void assertEquals_long() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertEquals(error, 10L, 11L));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(ERROR_MESSAGE, 10L, 11L));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(() -> ERROR_MESSAGE, 10L, 11L));
        assertThrows(ERROR_CODE, () -> assertEquals(ERROR_CODE, 10L, 11L));
    }

    @Test
    void assertEquals_Long() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertEquals(error, Long.valueOf(10), Long.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(() -> ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(11)));
        assertThrows(ERROR_CODE, () -> assertEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(11)));
    }


    @Test
    void assertEquals_float() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertEquals(error, 10f, 11f));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(ERROR_MESSAGE, 10f, 11f));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(() -> ERROR_MESSAGE, 10f, 11f));
        assertThrows(ERROR_CODE, () -> assertEquals(ERROR_CODE, 10f, 11f));
    }

    @Test
    void assertEquals_double() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertEquals(error, 10.0, 11.0));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(ERROR_MESSAGE, 10.0, 11.0));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(() -> ERROR_MESSAGE, 10.0, 11.0));
        assertThrows(ERROR_CODE, () -> assertEquals(ERROR_CODE, 10.0, 11.0));
    }

    @Test
    void assertEquals_Double() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertEquals(error, Double.valueOf(10), Double.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(11)));
        assertThrows(ERROR_CODE, () -> assertEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(11)));
    }

    @Test
    void assertEquals_BigDecimal() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertEquals(error, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertEquals(() -> ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        assertThrows(ERROR_CODE, () -> assertEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
    }
}