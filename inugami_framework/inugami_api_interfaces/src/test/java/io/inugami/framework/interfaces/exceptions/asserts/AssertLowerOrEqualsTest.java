package io.inugami.framework.interfaces.exceptions.asserts;


import io.inugami.framework.interfaces.commons.UnitTestHelper;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AssertLowerOrEqualsTest {
    private static final ErrorCode ERROR_CODE        = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE     = "error";
    public static final  String    DEFAULT_ERROR_MSG = "value must be lower or equals";

    @Test
    void assertLowerOrEquals_int() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertLowerOrEquals(error, 10, 11));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(ERROR_MESSAGE, 10, 11));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertLowerOrEquals(error, 10, 11));
    }

    @Test
    void assertLowerOrEquals_integer() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertLowerOrEquals(error, Integer.valueOf(10), Integer.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(() -> ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertLowerOrEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(11)));
    }

    @Test
    void assertLowerOrEquals_long() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertLowerOrEquals(error, 10L, 11L));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(ERROR_MESSAGE, 10L, 11L));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(() -> ERROR_MESSAGE, 10L, 11L));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertLowerOrEquals(ERROR_CODE, 10L, 11L));
    }

    @Test
    void assertLowerOrEquals_Long() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertLowerOrEquals(error, Long.valueOf(10), Long.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(() -> ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertLowerOrEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(11)));
    }


    @Test
    void assertLowerOrEquals_float() {
        String error = null;
        Asserts.assertLowerOrEquals(error, 10f, 10f);
        Asserts.assertLowerOrEquals(error, 10f, 9f);
        Asserts.assertLowerOrEquals(ERROR_MESSAGE, 10f, 10f);
        Asserts.assertLowerOrEquals(ERROR_MESSAGE, 10f, 9f);
        Asserts.assertLowerOrEquals(() -> ERROR_MESSAGE, 10f, 10f);
        Asserts.assertLowerOrEquals(() -> ERROR_MESSAGE, 10f, 9f);
        Asserts.assertLowerOrEquals(ERROR_CODE, 10f, 10f);
        Asserts.assertLowerOrEquals(ERROR_CODE, 10f, 9f);

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertLowerOrEquals(error, 10f, 11f));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(ERROR_MESSAGE, 10f, 11f));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(() -> ERROR_MESSAGE, 10f, 11f));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertLowerOrEquals(ERROR_CODE, 10f, 11f));
    }

    @Test
    void assertLowerOrEquals_double() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertLowerOrEquals(error, 10.0, 11.0));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(ERROR_MESSAGE, 10.0, 11.0));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(() -> ERROR_MESSAGE, 10.0, 11.0));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertLowerOrEquals(ERROR_CODE, 10.0, 11.0));
    }

    @Test
    void assertLowerOrEquals_Double() {
        String error = null;
        Asserts.assertLowerOrEquals(error, Double.valueOf(10), Double.valueOf(9));
        Asserts.assertLowerOrEquals(error, Double.valueOf(10), Double.valueOf(10));
        Asserts.assertLowerOrEquals(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(9));
        Asserts.assertLowerOrEquals(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10));
        Asserts.assertLowerOrEquals(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(9));
        Asserts.assertLowerOrEquals(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10));
        Asserts.assertLowerOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(9));
        Asserts.assertLowerOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(10));


        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertLowerOrEquals(error, Double.valueOf(10), Double.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertLowerOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(11)));
    }

    @Test
    void assertLowerOrEquals_BigDecimal() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertLowerOrEquals(error, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertLowerOrEquals(() -> ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertLowerOrEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
    }
}