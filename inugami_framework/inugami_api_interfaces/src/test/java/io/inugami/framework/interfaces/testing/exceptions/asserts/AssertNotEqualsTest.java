package io.inugami.framework.interfaces.testing.exceptions.asserts;


import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AssertNotEqualsTest {
    private static final ErrorCode ERROR_CODE        = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE     = "error";
    public static final  String    DEFAULT_ERROR_MSG = "values mustn't be equals";

    @Test
    void assertNotEquals_int() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertNotEquals(error, 10, 10));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(ERROR_MESSAGE, 10, 10));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(() -> ERROR_MESSAGE, 10, 10));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNotEquals(ERROR_CODE, 10, 10));
    }

    @Test
    void assertNotEquals_integer() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertNotEquals(error, Integer.valueOf(10), Integer.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(() -> ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNotEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(10)));
    }

    @Test
    void assertNotEquals_long() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertNotEquals(error, 10L, 10L));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(ERROR_MESSAGE, 10L, 10L));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(() -> ERROR_MESSAGE, 10L, 10L));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNotEquals(ERROR_CODE, 10L, 10L));
    }

    @Test
    void assertNotEquals_Long() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertNotEquals(error, Long.valueOf(10), Long.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(() -> ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNotEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(10)));
    }


    @Test
    void assertNotEquals_float() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertNotEquals(error, 10f, 10f));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(ERROR_MESSAGE, 10f, 10f));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(() -> ERROR_MESSAGE, 10f, 10f));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNotEquals(ERROR_CODE, 10f, 10f));
    }

    @Test
    void assertNotEquals_double() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertNotEquals(error, 10.0, 10.0));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(ERROR_MESSAGE, 10.0, 10.0));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(() -> ERROR_MESSAGE, 10.0, 10.0));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNotEquals(ERROR_CODE, 10.0, 10.0));
    }

    @Test
    void assertNotEquals_Double() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertNotEquals(error, Double.valueOf(10), Double.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNotEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(10)));
    }

    @Test
    void assertNotEquals_BigDecimal() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertNotEquals(error, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertNotEquals(() -> ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNotEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)));
    }
}