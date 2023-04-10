package io.inugami.api.exceptions;

import io.inugami.api.functionnals.IsEmptyFacet;
import io.inugami.api.functionnals.VoidFunctionWithException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AssertsTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String           MESSAGE          = "some error";
    private static final Supplier<String> MESSAGE_PRODUCER = () -> MESSAGE;

    private static final ErrorCode ERROR_CODE  = DefaultErrorCode.buildUndefineError();
    private static final String    EMPTY_VALUE = "";
    private static final String    BLANK_VALUE = "   ";
    private static final String    VALUE       = "VALUE";
    private static final String    REF         = "REF";

    private static final List<String> EMPTY_LIST = List.of();
    private static final List<String> LIST       = List.of(VALUE);

    private static final Map<String, String> EMPTY_MAP = Map.of();
    private static final Map<String, String> MAP       = Map.of("key", VALUE);

    private static final IsEmptyFacet IS_EMPTY     = () -> true;
    private static final IsEmptyFacet IS_NOT_EMPTY = () -> false;

    // =========================================================================
    // assertTrue
    // =========================================================================
    @Test
    public void assertTrue_shouldPass() {
        Asserts.assertTrue(VALUE, true);
        Asserts.assertTrue(MESSAGE, true);
        Asserts.assertTrue(MESSAGE_PRODUCER, true);
        Asserts.assertTrue(ERROR_CODE, true);
    }

    @Test
    public void assertTrue__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertTrue(false),
                () -> Asserts.assertTrue(MESSAGE, false),
                () -> Asserts.assertTrue(MESSAGE_PRODUCER, false),
                () -> Asserts.assertTrue(ERROR_CODE, false)

        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertFalse__shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    // =========================================================================
    // assertFalse
    // =========================================================================
    @Test
    public void assertFalse_shouldPass() {
        Asserts.assertFalse(VALUE, false);
        Asserts.assertFalse(MESSAGE, false);
        Asserts.assertFalse(MESSAGE_PRODUCER, false);
        Asserts.assertFalse(ERROR_CODE, false);
    }

    @Test
    public void assertFalse__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertFalse(true),
                () -> Asserts.assertFalse(MESSAGE, true),
                () -> Asserts.assertFalse(MESSAGE_PRODUCER, true),
                () -> Asserts.assertFalse(ERROR_CODE, true)

        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertFalse__shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    // =========================================================================
    // assertNull
    // =========================================================================
    @Test
    public void assertNull_shouldPass() {
        Asserts.assertNull(null);
        Asserts.assertNull(MESSAGE, null);
        Asserts.assertNull(MESSAGE_PRODUCER, null);
        Asserts.assertNull(ERROR_CODE, null);
    }

    @Test
    public void assertNull__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertNull(VALUE),
                () -> Asserts.assertNull(MESSAGE, VALUE),
                () -> Asserts.assertNull(MESSAGE_PRODUCER, VALUE),
                () -> Asserts.assertNull(ERROR_CODE, VALUE)

        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertNotNull__shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    // =========================================================================
    // assertNotNull
    // =========================================================================
    @Test
    public void assertNotNull_shouldPass() {
        Asserts.assertNotNull(VALUE, VALUE);
        Asserts.assertNotNull(MESSAGE, VALUE, VALUE);
        Asserts.assertNotNull(MESSAGE_PRODUCER, VALUE, VALUE);
        Asserts.assertNotNull(ERROR_CODE, VALUE, VALUE);
    }

    @Test
    public void assertNotNull__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertNotNull(VALUE, null),
                () -> Asserts.assertNotNull(MESSAGE, VALUE, null),
                () -> Asserts.assertNotNull(MESSAGE_PRODUCER, VALUE, null),
                () -> Asserts.assertNotNull(ERROR_CODE, VALUE, null)

        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertNotNull__shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    // =========================================================================
    // assertNotEmpty
    // =========================================================================
    @Test
    public void assertNotEmpty_shouldPass() {
        Asserts.assertNotEmpty(VALUE);
        Asserts.assertNotEmpty(MESSAGE, VALUE);
        Asserts.assertNotEmpty(MESSAGE_PRODUCER, VALUE);
        Asserts.assertNotEmpty(ERROR_CODE, VALUE);

        Asserts.assertNotEmpty(LIST);
        Asserts.assertNotEmpty(MESSAGE, LIST);
        Asserts.assertNotEmpty(MESSAGE_PRODUCER, LIST);
        Asserts.assertNotEmpty(ERROR_CODE, LIST);

        Asserts.assertNotEmpty(MAP);
        Asserts.assertNotEmpty(MESSAGE, MAP);
        Asserts.assertNotEmpty(MESSAGE_PRODUCER, MAP);
        Asserts.assertNotEmpty(ERROR_CODE, MAP);

        Asserts.assertNotEmpty(IS_NOT_EMPTY);
        Asserts.assertNotEmpty(MESSAGE, IS_NOT_EMPTY);
        Asserts.assertNotEmpty(MESSAGE_PRODUCER, IS_NOT_EMPTY);
        Asserts.assertNotEmpty(ERROR_CODE, IS_NOT_EMPTY);
    }

    @Test
    public void assertNotEmpty__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertNotEmpty(EMPTY_VALUE),
                () -> Asserts.assertNotEmpty(MESSAGE, EMPTY_VALUE),
                () -> Asserts.assertNotEmpty(MESSAGE_PRODUCER, EMPTY_VALUE),
                () -> Asserts.assertNotEmpty(ERROR_CODE, EMPTY_VALUE),

                () -> Asserts.assertNotEmpty(BLANK_VALUE),
                () -> Asserts.assertNotEmpty(MESSAGE, BLANK_VALUE),
                () -> Asserts.assertNotEmpty(MESSAGE_PRODUCER, BLANK_VALUE),
                () -> Asserts.assertNotEmpty(ERROR_CODE, BLANK_VALUE),


                () -> Asserts.assertNotEmpty(EMPTY_LIST),
                () -> Asserts.assertNotEmpty(MESSAGE, EMPTY_LIST),
                () -> Asserts.assertNotEmpty(MESSAGE_PRODUCER, EMPTY_LIST),
                () -> Asserts.assertNotEmpty(ERROR_CODE, EMPTY_LIST),

                () -> Asserts.assertNotEmpty(EMPTY_MAP),
                () -> Asserts.assertNotEmpty(MESSAGE, EMPTY_MAP),
                () -> Asserts.assertNotEmpty(MESSAGE_PRODUCER, EMPTY_MAP),
                () -> Asserts.assertNotEmpty(ERROR_CODE, EMPTY_MAP),

                () -> Asserts.assertNotEmpty(IS_EMPTY),
                () -> Asserts.assertNotEmpty(MESSAGE, IS_EMPTY),
                () -> Asserts.assertNotEmpty(MESSAGE_PRODUCER, IS_EMPTY),
                () -> Asserts.assertNotEmpty(ERROR_CODE, IS_EMPTY)

        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertNotEmpty__shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    // =========================================================================
    // assertEmpty
    // =========================================================================
    @Test
    public void assertEmpty_shouldPass() {
        Asserts.assertEmpty("");
        Asserts.assertEmpty(MESSAGE, "");
        Asserts.assertEmpty(MESSAGE_PRODUCER, "");
        Asserts.assertEmpty(ERROR_CODE, "");
        Asserts.assertEmpty("    ");
        Asserts.assertEmpty(MESSAGE, "    ");
        Asserts.assertEmpty(MESSAGE_PRODUCER, "    ");
        Asserts.assertEmpty(ERROR_CODE, "    ");

        Asserts.assertEmpty(EMPTY_LIST);
        Asserts.assertEmpty(MESSAGE, EMPTY_LIST);
        Asserts.assertEmpty(MESSAGE_PRODUCER, EMPTY_LIST);
        Asserts.assertEmpty(ERROR_CODE, EMPTY_LIST);

        Asserts.assertEmpty(EMPTY_MAP);
        Asserts.assertEmpty(MESSAGE, EMPTY_MAP);
        Asserts.assertEmpty(MESSAGE_PRODUCER, EMPTY_MAP);
        Asserts.assertEmpty(ERROR_CODE, EMPTY_MAP);

        Asserts.assertEmpty(IS_EMPTY);
        Asserts.assertEmpty(MESSAGE, IS_EMPTY);
        Asserts.assertEmpty(MESSAGE_PRODUCER, IS_EMPTY);
        Asserts.assertEmpty(ERROR_CODE, IS_EMPTY);
    }

    @Test
    public void assertEmpty__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertEmpty(VALUE),
                () -> Asserts.assertEmpty(MESSAGE, VALUE),
                () -> Asserts.assertEmpty(MESSAGE_PRODUCER, VALUE),
                () -> Asserts.assertEmpty(ERROR_CODE, VALUE),

                () -> Asserts.assertEmpty(LIST),
                () -> Asserts.assertEmpty(MESSAGE, LIST),
                () -> Asserts.assertEmpty(MESSAGE_PRODUCER, LIST),
                () -> Asserts.assertEmpty(ERROR_CODE, LIST),

                () -> Asserts.assertEmpty(MAP),
                () -> Asserts.assertEmpty(MESSAGE, MAP),
                () -> Asserts.assertEmpty(MESSAGE_PRODUCER, MAP),
                () -> Asserts.assertEmpty(ERROR_CODE, MAP),

                () -> Asserts.assertEmpty(IS_NOT_EMPTY),
                () -> Asserts.assertEmpty(MESSAGE, IS_NOT_EMPTY),
                () -> Asserts.assertEmpty(MESSAGE_PRODUCER, IS_NOT_EMPTY),
                () -> Asserts.assertEmpty(ERROR_CODE, IS_NOT_EMPTY)
        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertEmpty__shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    // =========================================================================
    // assertEquals
    // =========================================================================
    @Test
    public void assertEquals_shouldPass() {
        Asserts.assertEquals(VALUE, VALUE);
        Asserts.assertEquals(VALUE, VALUE);
        Asserts.assertEquals(MESSAGE, VALUE, VALUE);
        Asserts.assertEquals(MESSAGE_PRODUCER, VALUE, VALUE);
        Asserts.assertEquals(ERROR_CODE, VALUE, VALUE);

        Asserts.assertEquals(10, 10);
        Asserts.assertEquals(MESSAGE, 10, 10);
        Asserts.assertEquals(MESSAGE_PRODUCER, 10, 10);
        Asserts.assertEquals(ERROR_CODE, 10, 10);

        Asserts.assertEquals(10L, 10L);
        Asserts.assertEquals(MESSAGE, 10L, 10L);
        Asserts.assertEquals(MESSAGE_PRODUCER, 10L, 10L);
        Asserts.assertEquals(ERROR_CODE, 10L, 10L);

        Asserts.assertEquals(10.0f, 10.0f);
        Asserts.assertEquals(MESSAGE, 10.0f, 10.0f);
        Asserts.assertEquals(MESSAGE_PRODUCER, 10.0f, 10.0f);
        Asserts.assertEquals(ERROR_CODE, 10.0f, 10.0f);

        Asserts.assertEquals(10.0, 10.0);
        Asserts.assertEquals(MESSAGE, 10.0, 10.0);
        Asserts.assertEquals(MESSAGE_PRODUCER, 10.0, 10.0);
        Asserts.assertEquals(ERROR_CODE, 10.0, 10.0);
    }

    @Test
    public void assertEquals__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertEquals(REF, VALUE),
                () -> Asserts.assertEquals(MESSAGE, REF, VALUE),
                () -> Asserts.assertEquals(MESSAGE_PRODUCER, REF, VALUE),
                () -> Asserts.assertEquals(ERROR_CODE, REF, VALUE),
                () -> Asserts.assertEquals(10, 11),
                () -> Asserts.assertEquals(MESSAGE, 10, 11),
                () -> Asserts.assertEquals(MESSAGE_PRODUCER, 10, 11),
                () -> Asserts.assertEquals(ERROR_CODE, 10, 11),
                () -> Asserts.assertEquals(10L, 11L),
                () -> Asserts.assertEquals(MESSAGE, 10L, 11L),
                () -> Asserts.assertEquals(MESSAGE_PRODUCER, 10L, 11L),
                () -> Asserts.assertEquals(ERROR_CODE, 10L, 11L),
                () -> Asserts.assertEquals(10.0f, 11.0f),
                () -> Asserts.assertEquals(MESSAGE, 10.0f, 11.0f),
                () -> Asserts.assertEquals(MESSAGE_PRODUCER, 10.0f, 11.0f),
                () -> Asserts.assertEquals(ERROR_CODE, 10.0f, 11.0f),
                () -> Asserts.assertEquals(10.0, 11.0),
                () -> Asserts.assertEquals(MESSAGE, 10.0, 11.0),
                () -> Asserts.assertEquals(MESSAGE_PRODUCER, 10.0, 11.0),
                () -> Asserts.assertEquals(ERROR_CODE, 10.0, 11.0)

        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertEquals__shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    // =========================================================================
    // assertNotEquals
    // =========================================================================
    @Test
    public void assertNotEquals_shouldPass() {
        Asserts.assertNotEquals(REF, VALUE);
        Asserts.assertNotEquals(MESSAGE, REF, VALUE);
        Asserts.assertNotEquals(MESSAGE_PRODUCER, REF, VALUE);
        Asserts.assertNotEquals(ERROR_CODE, REF, VALUE);

        Asserts.assertNotEquals(10, 11);
        Asserts.assertNotEquals(MESSAGE, 10, 11);
        Asserts.assertNotEquals(MESSAGE_PRODUCER, 10, 11);
        Asserts.assertNotEquals(ERROR_CODE, 10, 11);

        Asserts.assertNotEquals(10L, 11L);
        Asserts.assertNotEquals(MESSAGE, 10L, 11L);
        Asserts.assertNotEquals(MESSAGE_PRODUCER, 10L, 11L);
        Asserts.assertNotEquals(ERROR_CODE, 10L, 11L);

        Asserts.assertNotEquals(10.0f, 11.0f);
        Asserts.assertNotEquals(MESSAGE, 10.0f, 11.0f);
        Asserts.assertNotEquals(MESSAGE_PRODUCER, 10.0f, 11.0f);
        Asserts.assertNotEquals(ERROR_CODE, 10.0f, 11.0f);

        Asserts.assertNotEquals(10.0, 11.0);
        Asserts.assertNotEquals(MESSAGE, 10.0, 11.0);
        Asserts.assertNotEquals(MESSAGE_PRODUCER, 10.0, 11.0);
        Asserts.assertNotEquals(ERROR_CODE, 10.0, 11.0);
    }

    @Test
    public void assertNotEquals__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertNotEquals(VALUE, VALUE),
                () -> Asserts.assertNotEquals(MESSAGE, VALUE, VALUE),
                () -> Asserts.assertNotEquals(MESSAGE_PRODUCER, VALUE, VALUE),
                () -> Asserts.assertNotEquals(ERROR_CODE, VALUE, VALUE),
                () -> Asserts.assertNotEquals(10, 10),
                () -> Asserts.assertNotEquals(MESSAGE, 10, 10),
                () -> Asserts.assertNotEquals(MESSAGE_PRODUCER, 10, 10),
                () -> Asserts.assertNotEquals(ERROR_CODE, 10, 10),
                () -> Asserts.assertNotEquals(10L, 10L),
                () -> Asserts.assertNotEquals(MESSAGE, 10L, 10L),
                () -> Asserts.assertNotEquals(MESSAGE_PRODUCER, 10L, 10L),
                () -> Asserts.assertNotEquals(ERROR_CODE, 10L, 10L),
                () -> Asserts.assertNotEquals(10.0f, 10.0f),
                () -> Asserts.assertNotEquals(MESSAGE, 10.0f, 10.0f),
                () -> Asserts.assertNotEquals(MESSAGE_PRODUCER, 10.0f, 10.0f),
                () -> Asserts.assertNotEquals(ERROR_CODE, 10.0f, 10.0f),
                () -> Asserts.assertNotEquals(10.0, 10.0),
                () -> Asserts.assertNotEquals(MESSAGE, 10.0, 10.0),
                () -> Asserts.assertNotEquals(MESSAGE_PRODUCER, 10.0, 10.0),
                () -> Asserts.assertNotEquals(ERROR_CODE, 10.0, 10.0)

        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertNotEquals__shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    // =========================================================================
    // assertLower
    // =========================================================================
    @Test
    public void assertLower_shouldPass() {
        Asserts.assertLower(10, 9);
        Asserts.assertLower(MESSAGE, 10, 9);
        Asserts.assertLower(MESSAGE_PRODUCER, 10, 9);
        Asserts.assertLower(ERROR_CODE, 10, 9);

        Asserts.assertLower(10L, 9L);
        Asserts.assertLower(MESSAGE, 10L, 9L);
        Asserts.assertLower(MESSAGE_PRODUCER, 10L, 9L);
        Asserts.assertLower(ERROR_CODE, 10L, 9L);

        Asserts.assertLower(10.0f, 9.0f);
        Asserts.assertLower(MESSAGE, 10.0f, 9.0f);
        Asserts.assertLower(MESSAGE_PRODUCER, 10.0f, 9.0f);
        Asserts.assertLower(ERROR_CODE, 10.0f, 9.0f);

        Asserts.assertLower(10.0, 9.0);
        Asserts.assertLower(MESSAGE, 10.0, 9.0);
        Asserts.assertLower(MESSAGE_PRODUCER, 10.0, 9.0);
        Asserts.assertLower(ERROR_CODE, 10.0, 9.0);

        Asserts.assertLower(Integer.valueOf(10), Integer.valueOf(9));
        Asserts.assertLower(MESSAGE, Integer.valueOf(10), Integer.valueOf(9));
        Asserts.assertLower(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(9));
        Asserts.assertLower(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(9));

        Asserts.assertLower(Long.valueOf(10), Long.valueOf(9));
        Asserts.assertLower(MESSAGE, Long.valueOf(10), Long.valueOf(9));
        Asserts.assertLower(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(9));
        Asserts.assertLower(ERROR_CODE, Long.valueOf(10), Long.valueOf(9));

        Asserts.assertLower(Double.valueOf(10), Double.valueOf(9));
        Asserts.assertLower(MESSAGE, Double.valueOf(10), Double.valueOf(9));
        Asserts.assertLower(MESSAGE_PRODUCER, Long.valueOf(10), Double.valueOf(9));
        Asserts.assertLower(ERROR_CODE, Double.valueOf(10), Double.valueOf(9));

        Asserts.assertLower(BigDecimal.valueOf(10), BigDecimal.valueOf(9));
        Asserts.assertLower(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(9));
        Asserts.assertLower(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(9));
        Asserts.assertLower(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(9));
    }

    @Test
    public void assertLower__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertLower(10, 11),
                () -> Asserts.assertLower(10, 10),
                () -> Asserts.assertLower(MESSAGE, 10, 11),
                () -> Asserts.assertLower(MESSAGE, 10, 10),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, 10, 11),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, 10, 10),
                () -> Asserts.assertLower(ERROR_CODE, 10, 11),
                () -> Asserts.assertLower(ERROR_CODE, 10, 10),

                () -> Asserts.assertLower(10L, 11L),
                () -> Asserts.assertLower(10L, 10L),
                () -> Asserts.assertLower(MESSAGE, 10L, 11L),
                () -> Asserts.assertLower(MESSAGE, 10L, 10L),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, 10L, 11L),
                () -> Asserts.assertLower(ERROR_CODE, 10L, 11L),
                () -> Asserts.assertLower(ERROR_CODE, 10L, 10L),

                () -> Asserts.assertLower(10.0f, 11.0f),
                () -> Asserts.assertLower(10.0f, 10.0f),
                () -> Asserts.assertLower(MESSAGE, 10.0f, 11.0f),
                () -> Asserts.assertLower(MESSAGE, 10.0f, 10.0f),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, 10.0f, 11.0f),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, 10.0f, 10.0f),
                () -> Asserts.assertLower(ERROR_CODE, 10.0f, 11.0f),
                () -> Asserts.assertLower(ERROR_CODE, 10.0f, 10.0f),

                () -> Asserts.assertLower(10.0, 11.0),
                () -> Asserts.assertLower(10.0, 10.0),
                () -> Asserts.assertLower(MESSAGE, 10.0, 11.0),
                () -> Asserts.assertLower(MESSAGE, 10.0, 10.0),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, 10.0, 11.0),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, 10.0, 10.0),
                () -> Asserts.assertLower(ERROR_CODE, 10.0, 11.0),
                () -> Asserts.assertLower(ERROR_CODE, 10.0, 10.0),

                () -> Asserts.assertLower(Integer.valueOf(10), Integer.valueOf(11)),
                () -> Asserts.assertLower(Integer.valueOf(10), Integer.valueOf(10)),
                () -> Asserts.assertLower(MESSAGE, Integer.valueOf(10), Integer.valueOf(11)),
                () -> Asserts.assertLower(MESSAGE, Integer.valueOf(10), Integer.valueOf(10)),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(11)),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(10)),
                () -> Asserts.assertLower(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(11)),
                () -> Asserts.assertLower(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(10)),

                () -> Asserts.assertLower(Long.valueOf(10), Long.valueOf(11)),
                () -> Asserts.assertLower(Long.valueOf(10), Long.valueOf(10)),
                () -> Asserts.assertLower(MESSAGE, Long.valueOf(10), Long.valueOf(11)),
                () -> Asserts.assertLower(MESSAGE, Long.valueOf(10), Long.valueOf(10)),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(11)),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(10)),
                () -> Asserts.assertLower(ERROR_CODE, Long.valueOf(10), Long.valueOf(11)),
                () -> Asserts.assertLower(ERROR_CODE, Long.valueOf(10), Long.valueOf(10)),

                () -> Asserts.assertLower(Double.valueOf(10), Double.valueOf(11)),
                () -> Asserts.assertLower(Double.valueOf(10), Double.valueOf(10)),
                () -> Asserts.assertLower(MESSAGE, Double.valueOf(10), Double.valueOf(11)),
                () -> Asserts.assertLower(MESSAGE, Double.valueOf(10), Double.valueOf(10)),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, Long.valueOf(10), Double.valueOf(11)),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, Long.valueOf(10), Double.valueOf(10)),
                () -> Asserts.assertLower(ERROR_CODE, Double.valueOf(10), Double.valueOf(11)),
                () -> Asserts.assertLower(ERROR_CODE, Double.valueOf(10), Double.valueOf(10)),
                () -> Asserts.assertLower(BigDecimal.valueOf(10), BigDecimal.valueOf(11)),
                () -> Asserts.assertLower(BigDecimal.valueOf(10), BigDecimal.valueOf(10)),
                () -> Asserts.assertLower(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)),
                () -> Asserts.assertLower(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(11)),
                () -> Asserts.assertLower(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(10)),
                () -> Asserts.assertLower(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)),
                () -> Asserts.assertLower(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(10))


        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertLower__shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    // =========================================================================
    // assertLowerOrEquals
    // =========================================================================
    @Test
    public void assertLowerOrEquals_shouldPass() {
        Asserts.assertLowerOrEquals(10, 9);
        Asserts.assertLowerOrEquals(10, 10);
        Asserts.assertLowerOrEquals(MESSAGE, 10, 9);
        Asserts.assertLowerOrEquals(MESSAGE, 10, 10);
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10, 9);
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10, 10);
        Asserts.assertLowerOrEquals(ERROR_CODE, 10, 9);
        Asserts.assertLowerOrEquals(ERROR_CODE, 10, 10);

        Asserts.assertLowerOrEquals(10L, 9L);
        Asserts.assertLowerOrEquals(10L, 10L);
        Asserts.assertLowerOrEquals(MESSAGE, 10L, 9L);
        Asserts.assertLowerOrEquals(MESSAGE, 10L, 10L);
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10L, 9L);
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10L, 10L);
        Asserts.assertLowerOrEquals(ERROR_CODE, 10L, 9L);
        Asserts.assertLowerOrEquals(ERROR_CODE, 10L, 10L);

        Asserts.assertLowerOrEquals(10.0f, 9.0f);
        Asserts.assertLowerOrEquals(10.0f, 10.0f);
        Asserts.assertLowerOrEquals(MESSAGE, 10.0f, 9.0f);
        Asserts.assertLowerOrEquals(MESSAGE, 10.0f, 10.0f);
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10.0f, 9.0f);
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10.0f, 10.0f);
        Asserts.assertLowerOrEquals(ERROR_CODE, 10.0f, 9.0f);
        Asserts.assertLowerOrEquals(ERROR_CODE, 10.0f, 10.0f);

        Asserts.assertLowerOrEquals(10.0, 9.0);
        Asserts.assertLowerOrEquals(10.0, 10.0);
        Asserts.assertLowerOrEquals(MESSAGE, 10.0, 9.0);
        Asserts.assertLowerOrEquals(MESSAGE, 10.0, 10.0);
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10.0, 9.0);
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10.0, 10.0);
        Asserts.assertLowerOrEquals(ERROR_CODE, 10.0, 9.0);
        Asserts.assertLowerOrEquals(ERROR_CODE, 10.0, 10.0);

        Asserts.assertLowerOrEquals(Integer.valueOf(10), Integer.valueOf(9));
        Asserts.assertLowerOrEquals(Integer.valueOf(10), Integer.valueOf(10));
        Asserts.assertLowerOrEquals(MESSAGE, Integer.valueOf(10), Integer.valueOf(9));
        Asserts.assertLowerOrEquals(MESSAGE, Integer.valueOf(10), Integer.valueOf(10));
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(9));
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(10));
        Asserts.assertLowerOrEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(9));
        Asserts.assertLowerOrEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(10));

        Asserts.assertLowerOrEquals(Long.valueOf(10), Long.valueOf(9));
        Asserts.assertLowerOrEquals(Long.valueOf(10), Long.valueOf(10));
        Asserts.assertLowerOrEquals(MESSAGE, Long.valueOf(10), Long.valueOf(9));
        Asserts.assertLowerOrEquals(MESSAGE, Long.valueOf(10), Long.valueOf(10));
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(9));
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(10));
        Asserts.assertLowerOrEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(9));
        Asserts.assertLowerOrEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(10));

        Asserts.assertLowerOrEquals(Double.valueOf(10), Double.valueOf(9));
        Asserts.assertLowerOrEquals(Double.valueOf(10), Double.valueOf(10));
        Asserts.assertLowerOrEquals(MESSAGE, Double.valueOf(10), Double.valueOf(9));
        Asserts.assertLowerOrEquals(MESSAGE, Double.valueOf(10), Double.valueOf(10));
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Double.valueOf(9));
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Double.valueOf(10));
        Asserts.assertLowerOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(9));
        Asserts.assertLowerOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(10));

        Asserts.assertLowerOrEquals(BigDecimal.valueOf(10), BigDecimal.valueOf(9));
        Asserts.assertLowerOrEquals(BigDecimal.valueOf(10), BigDecimal.valueOf(10));
        Asserts.assertLowerOrEquals(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(9));
        Asserts.assertLowerOrEquals(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10));
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(9));
        Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(10));
        Asserts.assertLowerOrEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(9));
        Asserts.assertLowerOrEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(10));
    }

    @Test
    public void assertLowerOrEquals_shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertLowerOrEquals(10, 11),
                () -> Asserts.assertLowerOrEquals(MESSAGE, 10, 11),
                () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10, 11),
                () -> Asserts.assertLowerOrEquals(ERROR_CODE, 10, 11),

                () -> Asserts.assertLowerOrEquals(10L, 11L),
                () -> Asserts.assertLowerOrEquals(MESSAGE, 10L, 11L),
                () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10L, 11L),
                () -> Asserts.assertLowerOrEquals(ERROR_CODE, 10L, 11L),

                () -> Asserts.assertLowerOrEquals(10.0f, 11.0f),
                () -> Asserts.assertLowerOrEquals(MESSAGE, 10.0f, 11.0f),
                () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10.0f, 11.0f),
                () -> Asserts.assertLowerOrEquals(ERROR_CODE, 10.0f, 11.0f),

                () -> Asserts.assertLowerOrEquals(10.0, 11.0),
                () -> Asserts.assertLowerOrEquals(MESSAGE, 10.0, 11.0),
                () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10.0, 11.0),
                () -> Asserts.assertLowerOrEquals(ERROR_CODE, 10.0, 11.0),

                () -> Asserts.assertLowerOrEquals(Integer.valueOf(10), Integer.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(MESSAGE, Integer.valueOf(10), Integer.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(11)),

                () -> Asserts.assertLowerOrEquals(Long.valueOf(10), Long.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(MESSAGE, Long.valueOf(10), Long.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(11)),

                () -> Asserts.assertLowerOrEquals(Double.valueOf(10), Double.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(MESSAGE, Double.valueOf(10), Double.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Double.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(BigDecimal.valueOf(10), BigDecimal.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(11)),
                () -> Asserts.assertLowerOrEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(11))

        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertLowerOrEquals_shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    // =========================================================================
    // assertHigher
    // =========================================================================
    @Test
    public void assertHigher_shouldPass() {
        Asserts.assertHigher(10, 11);
        Asserts.assertHigher(MESSAGE, 10, 11);
        Asserts.assertHigher(MESSAGE_PRODUCER, 10, 11);
        Asserts.assertHigher(ERROR_CODE, 10, 11);

        Asserts.assertHigher(10L, 11L);
        Asserts.assertHigher(MESSAGE, 10L, 11L);
        Asserts.assertHigher(MESSAGE_PRODUCER, 10L, 11L);
        Asserts.assertHigher(ERROR_CODE, 10L, 11L);

        Asserts.assertHigher(10.0f, 11.0f);
        Asserts.assertHigher(MESSAGE, 10.0f, 11.0f);
        Asserts.assertHigher(MESSAGE_PRODUCER, 10.0f, 11.0f);
        Asserts.assertHigher(ERROR_CODE, 10.0f, 11.0f);

        Asserts.assertHigher(10.0, 11.0);
        Asserts.assertHigher(MESSAGE, 10.0, 11.0);
        Asserts.assertHigher(MESSAGE_PRODUCER, 10.0, 11.0);
        Asserts.assertHigher(ERROR_CODE, 10.0, 11.0);

        Asserts.assertHigher(Integer.valueOf(10), Integer.valueOf(11));
        Asserts.assertHigher(MESSAGE, Integer.valueOf(10), Integer.valueOf(11));
        Asserts.assertHigher(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(11));
        Asserts.assertHigher(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(11));

        Asserts.assertHigher(Long.valueOf(10), Long.valueOf(11));
        Asserts.assertHigher(MESSAGE, Long.valueOf(10), Long.valueOf(11));
        Asserts.assertHigher(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(11));
        Asserts.assertHigher(ERROR_CODE, Long.valueOf(10), Long.valueOf(11));

        Asserts.assertHigher(Double.valueOf(10), Double.valueOf(11));
        Asserts.assertHigher(MESSAGE, Double.valueOf(10), Double.valueOf(11));
        Asserts.assertHigher(MESSAGE_PRODUCER, Double.valueOf(10), Double.valueOf(11));
        Asserts.assertHigher(ERROR_CODE, Double.valueOf(10), Double.valueOf(11));

        Asserts.assertHigher(BigDecimal.valueOf(10), BigDecimal.valueOf(11));
        Asserts.assertHigher(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11));
        Asserts.assertHigher(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(11));
        Asserts.assertHigher(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(11));
    }

    @Test
    public void assertHigher_shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertHigher(10, 9),
                () -> Asserts.assertHigher(10, 10),
                () -> Asserts.assertHigher(MESSAGE, 10, 9),
                () -> Asserts.assertHigher(MESSAGE, 10, 10),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10, 9),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10, 10),
                () -> Asserts.assertHigher(ERROR_CODE, 10, 9),
                () -> Asserts.assertHigher(ERROR_CODE, 10, 10),

                () -> Asserts.assertHigher(10L, 9L),
                () -> Asserts.assertHigher(10L, 10L),
                () -> Asserts.assertHigher(MESSAGE, 10L, 9L),
                () -> Asserts.assertHigher(MESSAGE, 10L, 10L),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10L, 9L),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10L, 10L),
                () -> Asserts.assertHigher(ERROR_CODE, 10L, 9L),
                () -> Asserts.assertHigher(ERROR_CODE, 10L, 10L),

                () -> Asserts.assertHigher(10.0f, 9.0f),
                () -> Asserts.assertHigher(10.0f, 10.0f),
                () -> Asserts.assertHigher(MESSAGE, 10.0f, 9.0f),
                () -> Asserts.assertHigher(MESSAGE, 10.0f, 10.0f),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10.0f, 9.0f),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10.0f, 10.0f),
                () -> Asserts.assertHigher(ERROR_CODE, 10.0f, 9.0f),
                () -> Asserts.assertHigher(ERROR_CODE, 10.0f, 10.0f),

                () -> Asserts.assertHigher(10.0, 9.0),
                () -> Asserts.assertHigher(10.0, 10.0),
                () -> Asserts.assertHigher(MESSAGE, 10.0, 9.0),
                () -> Asserts.assertHigher(MESSAGE, 10.0, 10.0),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10.0, 9.0),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10.0, 10.0),
                () -> Asserts.assertHigher(ERROR_CODE, 10.0, 9.0),
                () -> Asserts.assertHigher(ERROR_CODE, 10.0, 10.0),

                () -> Asserts.assertHigher(Integer.valueOf(10), Integer.valueOf(9)),
                () -> Asserts.assertHigher(Integer.valueOf(10), Integer.valueOf(10)),
                () -> Asserts.assertHigher(MESSAGE, Integer.valueOf(10), Integer.valueOf(9)),
                () -> Asserts.assertHigher(MESSAGE, Integer.valueOf(10), Integer.valueOf(10)),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(9)),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(10)),
                () -> Asserts.assertHigher(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(9)),
                () -> Asserts.assertHigher(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(10)),

                () -> Asserts.assertHigher(Long.valueOf(10), Long.valueOf(9)),
                () -> Asserts.assertHigher(Long.valueOf(10), Long.valueOf(10)),
                () -> Asserts.assertHigher(MESSAGE, Long.valueOf(10), Long.valueOf(9)),
                () -> Asserts.assertHigher(MESSAGE, Long.valueOf(10), Long.valueOf(10)),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(9)),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(10)),
                () -> Asserts.assertHigher(ERROR_CODE, Long.valueOf(10), Long.valueOf(9)),
                () -> Asserts.assertHigher(ERROR_CODE, Long.valueOf(10), Long.valueOf(10)),

                () -> Asserts.assertHigher(Double.valueOf(10), Double.valueOf(9)),
                () -> Asserts.assertHigher(Double.valueOf(10), Double.valueOf(10)),
                () -> Asserts.assertHigher(MESSAGE, Double.valueOf(10), Double.valueOf(9)),
                () -> Asserts.assertHigher(MESSAGE, Double.valueOf(10), Double.valueOf(10)),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, Double.valueOf(10), Double.valueOf(9)),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, Double.valueOf(10), Double.valueOf(10)),
                () -> Asserts.assertHigher(ERROR_CODE, Double.valueOf(10), Double.valueOf(9)),
                () -> Asserts.assertHigher(ERROR_CODE, Double.valueOf(10), Double.valueOf(10)),

                () -> Asserts.assertHigher(BigDecimal.valueOf(10), BigDecimal.valueOf(9)),
                () -> Asserts.assertHigher(BigDecimal.valueOf(10), BigDecimal.valueOf(10)),
                () -> Asserts.assertHigher(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(9)),
                () -> Asserts.assertHigher(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(9)),
                () -> Asserts.assertHigher(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(10)),
                () -> Asserts.assertHigher(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(9)),
                () -> Asserts.assertHigher(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(10))

        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertHigher_shouldThrow  " + i, () -> functions.get(index).process());
        }
    }


    // =========================================================================
    // assertHigherOrEquals
    // =========================================================================
    @Test
    public void assertHigherOrEquals_shouldPass() {
        Asserts.assertHigherOrEquals(10, 10);
        Asserts.assertHigherOrEquals(10, 11);
        Asserts.assertHigherOrEquals(MESSAGE, 10, 10);
        Asserts.assertHigherOrEquals(MESSAGE, 10, 11);
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10, 10);
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10, 11);
        Asserts.assertHigherOrEquals(ERROR_CODE, 10, 10);
        Asserts.assertHigherOrEquals(ERROR_CODE, 10, 11);

        Asserts.assertHigherOrEquals(10L, 10L);
        Asserts.assertHigherOrEquals(10L, 11L);
        Asserts.assertHigherOrEquals(MESSAGE, 10L, 10L);
        Asserts.assertHigherOrEquals(MESSAGE, 10L, 11L);
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10L, 10L);
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10L, 11L);
        Asserts.assertHigherOrEquals(ERROR_CODE, 10L, 10L);
        Asserts.assertHigherOrEquals(ERROR_CODE, 10L, 11L);

        Asserts.assertHigherOrEquals(10.0f, 10.0f);
        Asserts.assertHigherOrEquals(10.0f, 11.0f);
        Asserts.assertHigherOrEquals(MESSAGE, 10.0f, 10.0f);
        Asserts.assertHigherOrEquals(MESSAGE, 10.0f, 11.0f);
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10.0f, 10.0f);
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10.0f, 11.0f);
        Asserts.assertHigherOrEquals(ERROR_CODE, 10.0f, 10.0f);
        Asserts.assertHigherOrEquals(ERROR_CODE, 10.0f, 11.0f);

        Asserts.assertHigherOrEquals(10.0, 10.0);
        Asserts.assertHigherOrEquals(10.0, 11.0);
        Asserts.assertHigherOrEquals(MESSAGE, 10.0, 10.0);
        Asserts.assertHigherOrEquals(MESSAGE, 10.0, 11.0);
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10.0, 10.0);
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10.0, 11.0);
        Asserts.assertHigherOrEquals(ERROR_CODE, 10.0, 10.0);
        Asserts.assertHigherOrEquals(ERROR_CODE, 10.0, 11.0);

        Asserts.assertHigherOrEquals(Integer.valueOf(10), Integer.valueOf(10));
        Asserts.assertHigherOrEquals(Integer.valueOf(10), Integer.valueOf(11));
        Asserts.assertHigherOrEquals(MESSAGE, Integer.valueOf(10), Integer.valueOf(10));
        Asserts.assertHigherOrEquals(MESSAGE, Integer.valueOf(10), Integer.valueOf(11));
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(10));
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(11));
        Asserts.assertHigherOrEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(10));
        Asserts.assertHigherOrEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(11));

        Asserts.assertHigherOrEquals(Long.valueOf(10), Long.valueOf(10));
        Asserts.assertHigherOrEquals(Long.valueOf(10), Long.valueOf(11));
        Asserts.assertHigherOrEquals(MESSAGE, Long.valueOf(10), Long.valueOf(10));
        Asserts.assertHigherOrEquals(MESSAGE, Long.valueOf(10), Long.valueOf(11));
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(10));
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(11));
        Asserts.assertHigherOrEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(10));
        Asserts.assertHigherOrEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(11));

        Asserts.assertHigherOrEquals(Double.valueOf(10), Double.valueOf(10));
        Asserts.assertHigherOrEquals(Double.valueOf(10), Double.valueOf(11));
        Asserts.assertHigherOrEquals(MESSAGE, Double.valueOf(10), Double.valueOf(10));
        Asserts.assertHigherOrEquals(MESSAGE, Double.valueOf(10), Double.valueOf(11));
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Double.valueOf(10), Double.valueOf(10));
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Double.valueOf(10), Double.valueOf(11));
        Asserts.assertHigherOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(10));
        Asserts.assertHigherOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(11));

        Asserts.assertHigherOrEquals(BigDecimal.valueOf(10L), BigDecimal.valueOf(10L));
        Asserts.assertHigherOrEquals(BigDecimal.valueOf(10L), BigDecimal.valueOf(11L));
        Asserts.assertHigherOrEquals(MESSAGE, BigDecimal.valueOf(10L), BigDecimal.valueOf(10L));
        Asserts.assertHigherOrEquals(MESSAGE, BigDecimal.valueOf(10L), BigDecimal.valueOf(11L));
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, BigDecimal.valueOf(10L), BigDecimal.valueOf(10L));
        Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, BigDecimal.valueOf(10L), BigDecimal.valueOf(11L));
        Asserts.assertHigherOrEquals(ERROR_CODE, BigDecimal.valueOf(10L), BigDecimal.valueOf(10L));
        Asserts.assertHigherOrEquals(ERROR_CODE, BigDecimal.valueOf(10L), BigDecimal.valueOf(11L));
    }

    @Test
    public void assertHigherOrEquals_shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertHigherOrEquals(10, 9),
                () -> Asserts.assertHigherOrEquals(MESSAGE, 10, 9),
                () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10, 9),
                () -> Asserts.assertHigherOrEquals(ERROR_CODE, 10, 9),

                () -> Asserts.assertHigherOrEquals(10L, 9L),
                () -> Asserts.assertHigherOrEquals(MESSAGE, 10L, 9L),
                () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10L, 9L),
                () -> Asserts.assertHigherOrEquals(ERROR_CODE, 10L, 9L),

                () -> Asserts.assertHigherOrEquals(10.0f, 9.0f),
                () -> Asserts.assertHigherOrEquals(MESSAGE, 10.0f, 9.0f),
                () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10.0f, 9.0f),
                () -> Asserts.assertHigherOrEquals(ERROR_CODE, 10.0f, 9.0f),

                () -> Asserts.assertHigherOrEquals(10.0, 9.0),
                () -> Asserts.assertHigherOrEquals(MESSAGE, 10.0, 9.0),
                () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10.0, 9.0),
                () -> Asserts.assertHigherOrEquals(ERROR_CODE, 10.0, 9.0),

                () -> Asserts.assertHigherOrEquals(Integer.valueOf(10), Integer.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(MESSAGE, Integer.valueOf(10), Integer.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(9)),

                () -> Asserts.assertHigherOrEquals(Long.valueOf(10), Long.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(MESSAGE, Long.valueOf(10), Long.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(9)),

                () -> Asserts.assertHigherOrEquals(Double.valueOf(10), Double.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(MESSAGE, Double.valueOf(10), Double.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Double.valueOf(10), Double.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(9)),

                () -> Asserts.assertHigherOrEquals(BigDecimal.valueOf(10), BigDecimal.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(9)),
                () -> Asserts.assertHigherOrEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(9))
        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertHigherOrEquals_shouldThrow  " + i, () -> functions.get(index).process());
        }
    }


    // =========================================================================
    // assertThrow
    // =========================================================================
    @Test
    public void assertThrow_shouldThrow() {
        try {
            Asserts.assertThrow("should throw", () -> {
            });
            throw new RuntimeException("Asserts.assertThrow should throw if no error occurs");
        } catch (final Exception error) {
        }

    }
}
