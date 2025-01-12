package io.inugami.framework.interfaces.testing.exceptions.asserts;

import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AssertHigherOrEqualsTest {
    private static final ErrorCode ERROR_CODE        = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE     = "error";
    public static final  String    DEFAULT_ERROR_MSG = "value must be higher or equals";

    @Test
    void assertHigherOrEquals_int() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigherOrEquals(error, 10, 9));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(ERROR_MESSAGE, 10, 9));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigherOrEquals(error, 10, 9));
    }

    @Test
    void assertHigherOrEquals_integer() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigherOrEquals(error, Integer.valueOf(10), Integer.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(() -> ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigherOrEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(9)));
    }

    @Test
    void assertHigherOrEquals_long() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigherOrEquals(error, 10L, 9L));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(ERROR_MESSAGE, 10L, 9L));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(() -> ERROR_MESSAGE, 10L, 9L));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigherOrEquals(ERROR_CODE, 10L, 9L));
    }

    @Test
    void assertHigherOrEquals_Long() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigherOrEquals(error, Long.valueOf(10), Long.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(() -> ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigherOrEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(9)));
    }


    @Test
    void assertHigherOrEquals_float() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigherOrEquals(error, 10f, 9f));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(ERROR_MESSAGE, 10f, 9f));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(() -> ERROR_MESSAGE, 10f, 9f));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigherOrEquals(ERROR_CODE, 10f, 9f));
    }

    @Test
    void assertHigherOrEquals_double() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigherOrEquals(error, 10.0, 9.0));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(ERROR_MESSAGE, 10.0, 9.0));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(() -> ERROR_MESSAGE, 10.0, 9.0));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigherOrEquals(ERROR_CODE, 10.0, 9.0));
    }

    @Test
    void assertHigherOrEquals_Double() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigherOrEquals(error, Double.valueOf(10), Double.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigherOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(9)));
    }

    @Test
    void assertHigherOrEquals_BigDecimal() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertHigherOrEquals(error, BigDecimal.valueOf(10), BigDecimal.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertHigherOrEquals(() -> ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(9)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertHigherOrEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(9)));
    }
}