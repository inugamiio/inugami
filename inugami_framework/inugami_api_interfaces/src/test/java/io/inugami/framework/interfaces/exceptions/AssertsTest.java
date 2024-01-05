package io.inugami.framework.interfaces.exceptions;

import io.inugami.framework.interfaces.commons.UnitTestHelper;
import io.inugami.framework.interfaces.functionnals.VoidFunctionWithException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"java:S5961"})
class AssertsTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String           MESSAGE          = "some error";
    private static final Supplier<String> MESSAGE_PRODUCER = () -> MESSAGE;

    private static final ErrorCode ERROR_CODE = DefaultErrorCode.buildUndefineError();
    private static final String    VALUE      = "VALUE";
    private static final String    REF        = "REF";


    private static final String NULL_MSG     = null;
    public static final  String SOME_MESSAGE = "some message";

    // =========================================================================
    // assertTrue
    // =========================================================================
    @Test
    void asserts() {
        UnitTestHelper.assertUtilityClassLombok(Asserts.class);
    }

    @Test
    void assertTrue_shouldPass() {
        Asserts.assertTrue(true);
        Asserts.assertTrue(VALUE, true);
        Asserts.assertTrue(MESSAGE, true);
        Asserts.assertTrue(MESSAGE_PRODUCER, true);
        Asserts.assertTrue(ERROR_CODE, true);

        UnitTestHelper.assertThrows("this expression must be true", () -> Asserts.assertTrue(NULL_MSG, false));
        UnitTestHelper.assertThrows("this expression must be true", () -> Asserts.assertTrue(false));
        UnitTestHelper.assertThrows(MESSAGE, () -> Asserts.assertTrue(MESSAGE, false));
        UnitTestHelper.assertThrows(MESSAGE, () -> Asserts.assertTrue(MESSAGE_PRODUCER, false));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertTrue(ERROR_CODE, false));
    }

    @Test
    void assertTrue__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(() -> Asserts.assertTrue(false), () -> Asserts.assertTrue(MESSAGE, false), () -> Asserts.assertTrue(MESSAGE_PRODUCER, false), () -> Asserts.assertTrue(ERROR_CODE, false)

        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertFalse__shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    @Test
    void assertTrue_withErrorCode() {
        Asserts.assertTrue(ERROR_CODE, true);
        try {
            Asserts.assertTrue(ERROR_CODE, false);
        } catch (UncheckedException e) {
        }

    }

    // =========================================================================
    // assertFalse
    // =========================================================================
    @Test
    void assertFalse_shouldPass() {
        UnitTestHelper.assertThrows("this expression must be false", () -> Asserts.assertFalse(true));
        UnitTestHelper.assertThrows(MESSAGE, () -> Asserts.assertFalse(MESSAGE, true));
        UnitTestHelper.assertThrows(MESSAGE_PRODUCER.get(), () -> Asserts.assertFalse(MESSAGE_PRODUCER, true));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertFalse(ERROR_CODE, true));

        Asserts.assertFalse(false);
        Asserts.assertFalse(VALUE, false);
        Asserts.assertFalse(MESSAGE, false);
        Asserts.assertFalse(MESSAGE_PRODUCER, false);
        Asserts.assertFalse(ERROR_CODE, false);

        UnitTestHelper.assertThrows("this expression must be false", () -> Asserts.assertFalse(true));
        UnitTestHelper.assertThrows(MESSAGE, () -> Asserts.assertFalse(MESSAGE, true));
        UnitTestHelper.assertThrows(MESSAGE, () -> Asserts.assertFalse(MESSAGE_PRODUCER, true));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertFalse(ERROR_CODE, true));


    }


    // =========================================================================
    // assertNull
    // =========================================================================
    @Test
    void assertNull_shouldPass() {
        Asserts.assertNull(MESSAGE, null);
        Asserts.assertNull(MESSAGE_PRODUCER, null);
        Asserts.assertNull(ERROR_CODE, null);

        UnitTestHelper.assertThrows(MESSAGE, () -> Asserts.assertNull(MESSAGE, VALUE));
        UnitTestHelper.assertThrows(MESSAGE_PRODUCER.get(), () -> Asserts.assertNull(MESSAGE_PRODUCER, VALUE));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNull(ERROR_CODE, VALUE));
    }


    // =========================================================================
    // assertNotNull
    // =========================================================================
    @Test
    void assertNotNull_shouldPass() {
        Asserts.assertNotNull(VALUE, VALUE);
        Asserts.assertNotNull(MESSAGE, VALUE, VALUE);
        Asserts.assertNotNull(MESSAGE_PRODUCER, VALUE, VALUE);
        Asserts.assertNotNull(ERROR_CODE, VALUE, VALUE);

        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotNull(null));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotNull(VALUE, null));
        UnitTestHelper.assertThrows(MESSAGE, () -> Asserts.assertNotNull(MESSAGE, VALUE, null));
        UnitTestHelper.assertThrows(MESSAGE_PRODUCER.get(), () -> Asserts.assertNotNull(MESSAGE_PRODUCER, VALUE, null));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertNotNull(ERROR_CODE, VALUE, null));
    }


    // =========================================================================
    // assertEquals
    // =========================================================================
    @Test
    void assertEquals_shouldPass() {
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
    void assertEquals__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(() -> Asserts.assertEquals(REF, VALUE), () -> Asserts.assertEquals(MESSAGE, REF, VALUE), () -> Asserts.assertEquals(MESSAGE_PRODUCER, REF, VALUE), () -> Asserts.assertEquals(ERROR_CODE, REF, VALUE), () -> Asserts.assertEquals(10, 11), () -> Asserts.assertEquals(MESSAGE, 10, 11), () -> Asserts.assertEquals(MESSAGE_PRODUCER, 10, 11), () -> Asserts.assertEquals(ERROR_CODE, 10, 11), () -> Asserts.assertEquals(10L, 11L), () -> Asserts.assertEquals(MESSAGE, 10L, 11L), () -> Asserts.assertEquals(MESSAGE_PRODUCER, 10L, 11L), () -> Asserts.assertEquals(ERROR_CODE, 10L, 11L), () -> Asserts.assertEquals(10.0f, 11.0f), () -> Asserts.assertEquals(MESSAGE, 10.0f, 11.0f), () -> Asserts.assertEquals(MESSAGE_PRODUCER, 10.0f, 11.0f), () -> Asserts.assertEquals(ERROR_CODE, 10.0f, 11.0f), () -> Asserts.assertEquals(10.0, 11.0), () -> Asserts.assertEquals(MESSAGE, 10.0, 11.0), () -> Asserts.assertEquals(MESSAGE_PRODUCER, 10.0, 11.0), () -> Asserts.assertEquals(ERROR_CODE, 10.0, 11.0)

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
    void assertNotEquals_shouldPass() {
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
    void assertNotEquals__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(() -> Asserts.assertNotEquals(VALUE, VALUE), () -> Asserts.assertNotEquals(MESSAGE, VALUE, VALUE), () -> Asserts.assertNotEquals(MESSAGE_PRODUCER, VALUE, VALUE), () -> Asserts.assertNotEquals(ERROR_CODE, VALUE, VALUE), () -> Asserts.assertNotEquals(10, 10), () -> Asserts.assertNotEquals(MESSAGE, 10, 10), () -> Asserts.assertNotEquals(MESSAGE_PRODUCER, 10, 10), () -> Asserts.assertNotEquals(ERROR_CODE, 10, 10), () -> Asserts.assertNotEquals(10L, 10L), () -> Asserts.assertNotEquals(MESSAGE, 10L, 10L), () -> Asserts.assertNotEquals(MESSAGE_PRODUCER, 10L, 10L), () -> Asserts.assertNotEquals(ERROR_CODE, 10L, 10L), () -> Asserts.assertNotEquals(10.0f, 10.0f), () -> Asserts.assertNotEquals(MESSAGE, 10.0f, 10.0f), () -> Asserts.assertNotEquals(MESSAGE_PRODUCER, 10.0f, 10.0f), () -> Asserts.assertNotEquals(ERROR_CODE, 10.0f, 10.0f), () -> Asserts.assertNotEquals(10.0, 10.0), () -> Asserts.assertNotEquals(MESSAGE, 10.0, 10.0), () -> Asserts.assertNotEquals(MESSAGE_PRODUCER, 10.0, 10.0), () -> Asserts.assertNotEquals(ERROR_CODE, 10.0, 10.0)

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
    void assertLower_shouldPass() {
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
    void assertLower__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(() -> Asserts.assertLower(10, 11), () -> Asserts.assertLower(10, 10), () -> Asserts.assertLower(MESSAGE, 10, 11), () -> Asserts.assertLower(MESSAGE, 10, 10), () -> Asserts.assertLower(MESSAGE_PRODUCER, 10, 11), () -> Asserts.assertLower(MESSAGE_PRODUCER, 10, 10), () -> Asserts.assertLower(ERROR_CODE, 10, 11), () -> Asserts.assertLower(ERROR_CODE, 10, 10),

                                                                  () -> Asserts.assertLower(10L, 11L), () -> Asserts.assertLower(10L, 10L), () -> Asserts.assertLower(MESSAGE, 10L, 11L), () -> Asserts.assertLower(MESSAGE, 10L, 10L), () -> Asserts.assertLower(MESSAGE_PRODUCER, 10L, 11L), () -> Asserts.assertLower(ERROR_CODE, 10L, 11L), () -> Asserts.assertLower(ERROR_CODE, 10L, 10L),

                                                                  () -> Asserts.assertLower(10.0f, 11.0f), () -> Asserts.assertLower(10.0f, 10.0f), () -> Asserts.assertLower(MESSAGE, 10.0f, 11.0f), () -> Asserts.assertLower(MESSAGE, 10.0f, 10.0f), () -> Asserts.assertLower(MESSAGE_PRODUCER, 10.0f, 11.0f), () -> Asserts.assertLower(MESSAGE_PRODUCER, 10.0f, 10.0f), () -> Asserts.assertLower(ERROR_CODE, 10.0f, 11.0f), () -> Asserts.assertLower(ERROR_CODE, 10.0f, 10.0f),

                                                                  () -> Asserts.assertLower(10.0, 11.0), () -> Asserts.assertLower(10.0, 10.0), () -> Asserts.assertLower(MESSAGE, 10.0, 11.0), () -> Asserts.assertLower(MESSAGE, 10.0, 10.0), () -> Asserts.assertLower(MESSAGE_PRODUCER, 10.0, 11.0), () -> Asserts.assertLower(MESSAGE_PRODUCER, 10.0, 10.0), () -> Asserts.assertLower(ERROR_CODE, 10.0, 11.0), () -> Asserts.assertLower(ERROR_CODE, 10.0, 10.0),

                                                                  () -> Asserts.assertLower(Integer.valueOf(10), Integer.valueOf(11)), () -> Asserts.assertLower(Integer.valueOf(10), Integer.valueOf(10)), () -> Asserts.assertLower(MESSAGE, Integer.valueOf(10), Integer.valueOf(11)), () -> Asserts.assertLower(MESSAGE, Integer.valueOf(10), Integer.valueOf(10)), () -> Asserts.assertLower(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(11)), () -> Asserts.assertLower(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(10)), () -> Asserts.assertLower(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(11)), () -> Asserts.assertLower(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(10)),

                                                                  () -> Asserts.assertLower(Long.valueOf(10), Long.valueOf(11)), () -> Asserts.assertLower(Long.valueOf(10), Long.valueOf(10)), () -> Asserts.assertLower(MESSAGE, Long.valueOf(10), Long.valueOf(11)), () -> Asserts.assertLower(MESSAGE, Long.valueOf(10), Long.valueOf(10)), () -> Asserts.assertLower(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(11)), () -> Asserts.assertLower(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(10)), () -> Asserts.assertLower(ERROR_CODE, Long.valueOf(10), Long.valueOf(11)), () -> Asserts.assertLower(ERROR_CODE, Long.valueOf(10), Long.valueOf(10)),

                                                                  () -> Asserts.assertLower(Double.valueOf(10), Double.valueOf(11)), () -> Asserts.assertLower(Double.valueOf(10), Double.valueOf(10)), () -> Asserts.assertLower(MESSAGE, Double.valueOf(10), Double.valueOf(11)), () -> Asserts.assertLower(MESSAGE, Double.valueOf(10), Double.valueOf(10)), () -> Asserts.assertLower(MESSAGE_PRODUCER, Long.valueOf(10), Double.valueOf(11)), () -> Asserts.assertLower(MESSAGE_PRODUCER, Long.valueOf(10), Double.valueOf(10)), () -> Asserts.assertLower(ERROR_CODE, Double.valueOf(10), Double.valueOf(11)), () -> Asserts.assertLower(ERROR_CODE, Double.valueOf(10), Double.valueOf(10)), () -> Asserts.assertLower(BigDecimal.valueOf(10), BigDecimal.valueOf(11)), () -> Asserts.assertLower(BigDecimal.valueOf(10), BigDecimal.valueOf(10)), () -> Asserts.assertLower(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)), () -> Asserts.assertLower(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)), () -> Asserts.assertLower(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(11)), () -> Asserts.assertLower(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(10)), () -> Asserts.assertLower(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)), () -> Asserts.assertLower(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(10))


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
    void assertLowerOrEquals_shouldPass() {
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
    void assertLowerOrEquals_shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(() -> Asserts.assertLowerOrEquals(10, 11), () -> Asserts.assertLowerOrEquals(MESSAGE, 10, 11), () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10, 11), () -> Asserts.assertLowerOrEquals(ERROR_CODE, 10, 11),

                                                                  () -> Asserts.assertLowerOrEquals(10L, 11L), () -> Asserts.assertLowerOrEquals(MESSAGE, 10L, 11L), () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10L, 11L), () -> Asserts.assertLowerOrEquals(ERROR_CODE, 10L, 11L),

                                                                  () -> Asserts.assertLowerOrEquals(10.0f, 11.0f), () -> Asserts.assertLowerOrEquals(MESSAGE, 10.0f, 11.0f), () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10.0f, 11.0f), () -> Asserts.assertLowerOrEquals(ERROR_CODE, 10.0f, 11.0f),

                                                                  () -> Asserts.assertLowerOrEquals(10.0, 11.0), () -> Asserts.assertLowerOrEquals(MESSAGE, 10.0, 11.0), () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, 10.0, 11.0), () -> Asserts.assertLowerOrEquals(ERROR_CODE, 10.0, 11.0),

                                                                  () -> Asserts.assertLowerOrEquals(Integer.valueOf(10), Integer.valueOf(11)), () -> Asserts.assertLowerOrEquals(MESSAGE, Integer.valueOf(10), Integer.valueOf(11)), () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(11)), () -> Asserts.assertLowerOrEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(11)),

                                                                  () -> Asserts.assertLowerOrEquals(Long.valueOf(10), Long.valueOf(11)), () -> Asserts.assertLowerOrEquals(MESSAGE, Long.valueOf(10), Long.valueOf(11)), () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(11)), () -> Asserts.assertLowerOrEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(11)),

                                                                  () -> Asserts.assertLowerOrEquals(Double.valueOf(10), Double.valueOf(11)), () -> Asserts.assertLowerOrEquals(MESSAGE, Double.valueOf(10), Double.valueOf(11)), () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Double.valueOf(11)), () -> Asserts.assertLowerOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(11)), () -> Asserts.assertLowerOrEquals(BigDecimal.valueOf(10), BigDecimal.valueOf(11)), () -> Asserts.assertLowerOrEquals(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(11)), () -> Asserts.assertLowerOrEquals(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(11)), () -> Asserts.assertLowerOrEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(11))

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
    void assertHigher_shouldPass() {
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
    void assertHigher_shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(() -> Asserts.assertHigher(10, 9), () -> Asserts.assertHigher(10, 10), () -> Asserts.assertHigher(MESSAGE, 10, 9), () -> Asserts.assertHigher(MESSAGE, 10, 10), () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10, 9), () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10, 10), () -> Asserts.assertHigher(ERROR_CODE, 10, 9), () -> Asserts.assertHigher(ERROR_CODE, 10, 10),

                                                                  () -> Asserts.assertHigher(10L, 9L), () -> Asserts.assertHigher(10L, 10L), () -> Asserts.assertHigher(MESSAGE, 10L, 9L), () -> Asserts.assertHigher(MESSAGE, 10L, 10L), () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10L, 9L), () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10L, 10L), () -> Asserts.assertHigher(ERROR_CODE, 10L, 9L), () -> Asserts.assertHigher(ERROR_CODE, 10L, 10L),

                                                                  () -> Asserts.assertHigher(10.0f, 9.0f), () -> Asserts.assertHigher(10.0f, 10.0f), () -> Asserts.assertHigher(MESSAGE, 10.0f, 9.0f), () -> Asserts.assertHigher(MESSAGE, 10.0f, 10.0f), () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10.0f, 9.0f), () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10.0f, 10.0f), () -> Asserts.assertHigher(ERROR_CODE, 10.0f, 9.0f), () -> Asserts.assertHigher(ERROR_CODE, 10.0f, 10.0f),

                                                                  () -> Asserts.assertHigher(10.0, 9.0), () -> Asserts.assertHigher(10.0, 10.0), () -> Asserts.assertHigher(MESSAGE, 10.0, 9.0), () -> Asserts.assertHigher(MESSAGE, 10.0, 10.0), () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10.0, 9.0), () -> Asserts.assertHigher(MESSAGE_PRODUCER, 10.0, 10.0), () -> Asserts.assertHigher(ERROR_CODE, 10.0, 9.0), () -> Asserts.assertHigher(ERROR_CODE, 10.0, 10.0),

                                                                  () -> Asserts.assertHigher(Integer.valueOf(10), Integer.valueOf(9)), () -> Asserts.assertHigher(Integer.valueOf(10), Integer.valueOf(10)), () -> Asserts.assertHigher(MESSAGE, Integer.valueOf(10), Integer.valueOf(9)), () -> Asserts.assertHigher(MESSAGE, Integer.valueOf(10), Integer.valueOf(10)), () -> Asserts.assertHigher(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(9)), () -> Asserts.assertHigher(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(10)), () -> Asserts.assertHigher(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(9)), () -> Asserts.assertHigher(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(10)),

                                                                  () -> Asserts.assertHigher(Long.valueOf(10), Long.valueOf(9)), () -> Asserts.assertHigher(Long.valueOf(10), Long.valueOf(10)), () -> Asserts.assertHigher(MESSAGE, Long.valueOf(10), Long.valueOf(9)), () -> Asserts.assertHigher(MESSAGE, Long.valueOf(10), Long.valueOf(10)), () -> Asserts.assertHigher(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(9)), () -> Asserts.assertHigher(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(10)), () -> Asserts.assertHigher(ERROR_CODE, Long.valueOf(10), Long.valueOf(9)), () -> Asserts.assertHigher(ERROR_CODE, Long.valueOf(10), Long.valueOf(10)),

                                                                  () -> Asserts.assertHigher(Double.valueOf(10), Double.valueOf(9)), () -> Asserts.assertHigher(Double.valueOf(10), Double.valueOf(10)), () -> Asserts.assertHigher(MESSAGE, Double.valueOf(10), Double.valueOf(9)), () -> Asserts.assertHigher(MESSAGE, Double.valueOf(10), Double.valueOf(10)), () -> Asserts.assertHigher(MESSAGE_PRODUCER, Double.valueOf(10), Double.valueOf(9)), () -> Asserts.assertHigher(MESSAGE_PRODUCER, Double.valueOf(10), Double.valueOf(10)), () -> Asserts.assertHigher(ERROR_CODE, Double.valueOf(10), Double.valueOf(9)), () -> Asserts.assertHigher(ERROR_CODE, Double.valueOf(10), Double.valueOf(10)),

                                                                  () -> Asserts.assertHigher(BigDecimal.valueOf(10), BigDecimal.valueOf(9)), () -> Asserts.assertHigher(BigDecimal.valueOf(10), BigDecimal.valueOf(10)), () -> Asserts.assertHigher(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(9)), () -> Asserts.assertHigher(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(10)), () -> Asserts.assertHigher(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(9)), () -> Asserts.assertHigher(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(10)), () -> Asserts.assertHigher(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(9)), () -> Asserts.assertHigher(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(10))

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
    void assertHigherOrEquals_shouldPass() {
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
    void assertHigherOrEquals_shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(() -> Asserts.assertHigherOrEquals(10, 9), () -> Asserts.assertHigherOrEquals(MESSAGE, 10, 9), () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10, 9), () -> Asserts.assertHigherOrEquals(ERROR_CODE, 10, 9),

                                                                  () -> Asserts.assertHigherOrEquals(10L, 9L), () -> Asserts.assertHigherOrEquals(MESSAGE, 10L, 9L), () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10L, 9L), () -> Asserts.assertHigherOrEquals(ERROR_CODE, 10L, 9L),

                                                                  () -> Asserts.assertHigherOrEquals(10.0f, 9.0f), () -> Asserts.assertHigherOrEquals(MESSAGE, 10.0f, 9.0f), () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10.0f, 9.0f), () -> Asserts.assertHigherOrEquals(ERROR_CODE, 10.0f, 9.0f),

                                                                  () -> Asserts.assertHigherOrEquals(10.0, 9.0), () -> Asserts.assertHigherOrEquals(MESSAGE, 10.0, 9.0), () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, 10.0, 9.0), () -> Asserts.assertHigherOrEquals(ERROR_CODE, 10.0, 9.0),

                                                                  () -> Asserts.assertHigherOrEquals(Integer.valueOf(10), Integer.valueOf(9)), () -> Asserts.assertHigherOrEquals(MESSAGE, Integer.valueOf(10), Integer.valueOf(9)), () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Integer.valueOf(10), Integer.valueOf(9)), () -> Asserts.assertHigherOrEquals(ERROR_CODE, Integer.valueOf(10), Integer.valueOf(9)),

                                                                  () -> Asserts.assertHigherOrEquals(Long.valueOf(10), Long.valueOf(9)), () -> Asserts.assertHigherOrEquals(MESSAGE, Long.valueOf(10), Long.valueOf(9)), () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Long.valueOf(10), Long.valueOf(9)), () -> Asserts.assertHigherOrEquals(ERROR_CODE, Long.valueOf(10), Long.valueOf(9)),

                                                                  () -> Asserts.assertHigherOrEquals(Double.valueOf(10), Double.valueOf(9)), () -> Asserts.assertHigherOrEquals(MESSAGE, Double.valueOf(10), Double.valueOf(9)), () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, Double.valueOf(10), Double.valueOf(9)), () -> Asserts.assertHigherOrEquals(ERROR_CODE, Double.valueOf(10), Double.valueOf(9)),

                                                                  () -> Asserts.assertHigherOrEquals(BigDecimal.valueOf(10), BigDecimal.valueOf(9)), () -> Asserts.assertHigherOrEquals(MESSAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(9)), () -> Asserts.assertHigherOrEquals(MESSAGE_PRODUCER, BigDecimal.valueOf(10), BigDecimal.valueOf(9)), () -> Asserts.assertHigherOrEquals(ERROR_CODE, BigDecimal.valueOf(10), BigDecimal.valueOf(9)));
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertHigherOrEquals_shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    @Test
    void assertBefore_nominal() {
        final LocalDateTime localDateTime = LocalDateTime.of(2023, 11, 12, 10, 0);
        final LocalDate     localDate     = LocalDate.of(2023, 11, 12);

        UnitTestHelper.assertThrows(() -> Asserts.assertBefore(localDate, localDate.plusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertBefore(SOME_MESSAGE, localDate, localDate.plusDays(2)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertBefore(ERROR_CODE, localDate, localDate.plusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertBefore(() -> SOME_MESSAGE, localDate, localDate.plusDays(2)));
        Asserts.assertBefore(localDate, localDate.minusDays(2));

        UnitTestHelper.assertThrows(() -> Asserts.assertBefore(localDateTime, localDateTime.plusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertBefore(SOME_MESSAGE, localDateTime, localDateTime.plusDays(2)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertBefore(ERROR_CODE, localDateTime, localDateTime.plusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertBefore(() -> SOME_MESSAGE, localDateTime, localDateTime.plusDays(2)));
        Asserts.assertBefore(localDateTime, localDateTime.minusDays(2));
    }

    @Test
    void assertAfter_nominal() {
        final LocalDateTime localDateTime = LocalDateTime.of(2023, 11, 12, 10, 0);
        final LocalDate     localDate     = LocalDate.of(2023, 11, 12);

        UnitTestHelper.assertThrows(() -> Asserts.assertAfter(localDate, localDate.minusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertAfter(SOME_MESSAGE, localDate, localDate.minusDays(2)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertAfter(ERROR_CODE, localDate, localDate.minusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertAfter(() -> SOME_MESSAGE, localDate, localDate.minusDays(2)));
        Asserts.assertAfter(localDate, localDate.plusDays(2));

        UnitTestHelper.assertThrows(() -> Asserts.assertAfter(localDateTime, localDateTime.minusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertAfter(SOME_MESSAGE, localDateTime, localDateTime.minusDays(2)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertAfter(ERROR_CODE, localDateTime, localDateTime.minusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertAfter(() -> SOME_MESSAGE, localDateTime, localDateTime.minusDays(2)));
        Asserts.assertAfter(localDateTime, localDateTime.plusDays(2));
    }

    @Test
    void assertEquals_nominal() {
        final LocalDateTime localDateTime = LocalDateTime.of(2023, 11, 12, 10, 0);
        final LocalDate     localDate     = LocalDate.of(2023, 11, 12);

        UnitTestHelper.assertThrows(() -> Asserts.assertEquals(localDate, localDate.minusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertEquals(SOME_MESSAGE, localDate, localDate.minusDays(2)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertEquals(ERROR_CODE, localDate, localDate.minusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertEquals(() -> SOME_MESSAGE, localDate, localDate.minusDays(2)));
        Asserts.assertAfter(localDate, localDate);

        UnitTestHelper.assertThrows(() -> Asserts.assertEquals(localDateTime, localDateTime.minusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertEquals(SOME_MESSAGE, localDateTime, localDateTime.minusDays(2)));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertEquals(ERROR_CODE, localDateTime, localDateTime.minusDays(2)));
        UnitTestHelper.assertThrows(SOME_MESSAGE, () -> Asserts.assertEquals(() -> SOME_MESSAGE, localDateTime, localDateTime.minusDays(2)));
        Asserts.assertAfter(localDateTime, localDateTime);
    }

    // =========================================================================
    // assertThrow
    // =========================================================================
    @Test
    void assertThrow_shouldThrow() {
        try {
            Asserts.assertThrow("should throw", () -> {
            });
            throw new RuntimeException("Asserts.assertThrow should throw if no error occurs");
        } catch (final Exception error) {
        }
        Asserts.assertThrow("should throw", () -> {
            throw new UncheckedException("error");
        });
    }

    // =========================================================================
    // throwErrorCodeOnError
    // =========================================================================
    @Test
    void throwErrorCodeOnError_withVoidFunction() {
        try {
            Asserts.wrapErrorForVoidFunction(AssertsTest::throwErrorCodeOnErrorWithVoidFunction, ERROR_CODE);
        } catch (final Exception e) {
            assertThat(e).isNotNull().isInstanceOf(ExceptionWithErrorCode.class).hasMessage(SOME_MESSAGE);
        }

    }

    private static void throwErrorCodeOnErrorWithVoidFunction() {
        throw new RuntimeException(SOME_MESSAGE);
    }
}
