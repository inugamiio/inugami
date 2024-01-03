package io.inugami.api.exceptions.asserts;

import io.inugami.interfaces.exceptions.DefaultErrorCode;
import io.inugami.interfaces.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.inugami.api.exceptions.Asserts.assertLowerOrEquals;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertThrows;

class AssertLowerOrEqualsTest {
    private static final ErrorCode ERROR_CODE        = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE     = "error";
    public static final  String    DEFAULT_ERROR_MSG = "value must be lower or equals";

    @Test
    void assertLowerOrEquals_int() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertLowerOrEquals(error, 10, 11));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(ERROR_MESSAGE, 10, 11));
        assertThrows(ERROR_CODE, () -> assertLowerOrEquals(error, 10, 11));
    }

    @Test
    void assertLowerOrEquals_integer() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertLowerOrEquals(error, Integer.valueOf(10), Integer.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(() -> ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(11)));
        assertThrows(ERROR_CODE, () -> assertLowerOrEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(11)));
    }

    @Test
    void assertLowerOrEquals_long() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertLowerOrEquals(error, 10L, 11L));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(ERROR_MESSAGE, 10L, 11L));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(() -> ERROR_MESSAGE, 10L, 11L));
        assertThrows(ERROR_CODE, () -> assertLowerOrEquals(ERROR_CODE, 10L, 11L));
    }

    @Test
    void assertLowerOrEquals_Long() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertLowerOrEquals(error, Long.valueOf(10), Long.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(() -> ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(11)));
        assertThrows(ERROR_CODE, () -> assertLowerOrEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(11)));
    }


    @Test
    void assertLowerOrEquals_float() {
        String error = null;
        assertLowerOrEquals(error, 10f, 10f);
        assertLowerOrEquals(error, 10f, 9f);
        assertLowerOrEquals(ERROR_MESSAGE, 10f, 10f);
        assertLowerOrEquals(ERROR_MESSAGE, 10f, 9f);
        assertLowerOrEquals(() -> ERROR_MESSAGE, 10f, 10f);
        assertLowerOrEquals(() -> ERROR_MESSAGE, 10f, 9f);
        assertLowerOrEquals(ERROR_CODE, 10f, 10f);
        assertLowerOrEquals(ERROR_CODE, 10f, 9f);

        assertThrows(DEFAULT_ERROR_MSG, () -> assertLowerOrEquals(error, 10f, 11f));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(ERROR_MESSAGE, 10f, 11f));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(() -> ERROR_MESSAGE, 10f, 11f));
        assertThrows(ERROR_CODE, () -> assertLowerOrEquals(ERROR_CODE, 10f, 11f));
    }

    @Test
    void assertLowerOrEquals_double() {
        String error = null;
        assertThrows(DEFAULT_ERROR_MSG, () -> assertLowerOrEquals(error, 10.0, 11.0));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(ERROR_MESSAGE, 10.0, 11.0));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(() -> ERROR_MESSAGE, 10.0, 11.0));
        assertThrows(ERROR_CODE, () -> assertLowerOrEquals(ERROR_CODE, 10.0, 11.0));
    }

    @Test
    void assertLowerOrEquals_Double() {
        String error = null;
        assertLowerOrEquals(error, Double.valueOf(10), Double.valueOf(9));
        assertLowerOrEquals(error, Double.valueOf(10), Double.valueOf(10));
        assertLowerOrEquals(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(9));
        assertLowerOrEquals(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10));
        assertLowerOrEquals(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(9));
        assertLowerOrEquals(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10));
        assertLowerOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(9));
        assertLowerOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(10));


        assertThrows(DEFAULT_ERROR_MSG, () -> assertLowerOrEquals(error, Double.valueOf(10), Double.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(11)));
        assertThrows(ERROR_CODE, () -> assertLowerOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(11)));
    }

    @Test
    void assertLowerOrEquals_BigDecimal() {
        String error = null;

        assertThrows(DEFAULT_ERROR_MSG, () -> assertLowerOrEquals(error, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        assertThrows(ERROR_MESSAGE, () -> assertLowerOrEquals(() -> ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        assertThrows(ERROR_CODE, () -> assertLowerOrEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
    }
}