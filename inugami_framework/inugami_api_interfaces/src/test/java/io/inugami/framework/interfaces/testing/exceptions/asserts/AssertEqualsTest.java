package io.inugami.framework.interfaces.testing.exceptions.asserts;

import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AssertEqualsTest {
    private static final ErrorCode ERROR_CODE        = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE     = "error";
    public static final  String    DEFAULT_ERROR_MSG = "object must be equals";

    @Test
    void assertEquals_int() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertEquals(error, 10, 11));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(ERROR_MESSAGE, 10, 11));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(() -> ERROR_MESSAGE, 10, 11));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertEquals(ERROR_CODE, 10, 11));
    }

    @Test
    void assertEquals_integer() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertEquals(error, Integer.valueOf(10), Integer.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(() -> ERROR_MESSAGE, Integer.valueOf(10), Integer.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(11)));
    }

    @Test
    void assertEquals_long() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertEquals(error, 10L, 11L));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(ERROR_MESSAGE, 10L, 11L));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(() -> ERROR_MESSAGE, 10L, 11L));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertEquals(ERROR_CODE, 10L, 11L));
    }

    @Test
    void assertEquals_Long() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertEquals(error, Long.valueOf(10), Long.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(() -> ERROR_MESSAGE, Long.valueOf(10), Long.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(11)));
    }


    @Test
    void assertEquals_float() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertEquals(error, 10f, 11f));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(ERROR_MESSAGE, 10f, 11f));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(() -> ERROR_MESSAGE, 10f, 11f));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertEquals(ERROR_CODE, 10f, 11f));
    }

    @Test
    void assertEquals_double() {
        String error = null;
        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertEquals(error, 10.0, 11.0));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(ERROR_MESSAGE, 10.0, 11.0));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(() -> ERROR_MESSAGE, 10.0, 11.0));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertEquals(ERROR_CODE, 10.0, 11.0));
    }

    @Test
    void assertEquals_Double() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertEquals(error, Double.valueOf(10), Double.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(() -> ERROR_MESSAGE, Double.valueOf(10), Double.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(11)));
    }

    @Test
    void assertEquals_BigDecimal() {
        String error = null;

        UnitTestHelper.assertThrows(DEFAULT_ERROR_MSG, () -> Asserts.assertEquals(error, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertEquals(() -> ERROR_MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)));
    }
}