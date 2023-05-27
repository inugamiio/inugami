/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.api.exceptions;

import io.inugami.api.exceptions.asserts.*;
import io.inugami.api.functionnals.ActionWithException;
import io.inugami.api.functionnals.IsEmptyFacet;
import io.inugami.api.functionnals.VoidFunction;
import io.inugami.api.functionnals.VoidFunctionWithException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Asserts
 *
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Asserts {

    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    private static final String ASSERT_TRUE_DEFAULT_MSG      = "this expression must be true";
    private static final String ASSERT_FALSE_DEFAULT_MSG     = "this expression must be false";
    private static final String ASSERT_NOT_EMPTY_DEFAULT_MSG = "the value mustn't be empty";

    private static final String ASSERT_EMPTY_DEFAULT_MSG = "the value should be empty";


    // -------------------------------------------------------------------------
    // IS TRUE
    // -------------------------------------------------------------------------

    public static void assertTrue(final boolean expression) {
        assertTrue(ASSERT_TRUE_DEFAULT_MSG, expression);
    }


    public static void assertTrue(final String message,
                                  final boolean expression) {
        if (!expression) {
            throwException(message == null ? ASSERT_TRUE_DEFAULT_MSG : message);
        }
    }


    public static void assertTrue(final ErrorCode errorCode,
                                  final boolean expression) {
        if (!expression) {
            throwException(errorCode);
        }
    }


    public static void assertTrue(final Supplier<String> messageProducer,
                                  final boolean expression) {
        if (!expression) {
            throwException(messageProducer == null ? ASSERT_TRUE_DEFAULT_MSG : messageProducer.get());
        }
    }

    // -------------------------------------------------------------------------
    // IS FALSE
    // -------------------------------------------------------------------------

    public static void assertFalse(final boolean expression) {
        assertFalse(ASSERT_FALSE_DEFAULT_MSG, expression);
    }


    public static void assertFalse(final String message,
                                   final boolean expression) {
        if (expression) {
            throwException(message == null ? ASSERT_FALSE_DEFAULT_MSG : message);
        }
    }


    public static void assertFalse(final Supplier<String> messageProducer,
                                   final boolean expression) {
        if (expression) {
            throwException(messageProducer == null ? ASSERT_FALSE_DEFAULT_MSG : messageProducer.get());
        }
    }


    public static void assertFalse(final ErrorCode errorCode,
                                   final boolean expression) {
        if (expression) {
            throwException(errorCode);
        }
    }

    // -------------------------------------------------------------------------
    // IS NULL
    // -------------------------------------------------------------------------
    public static void assertNull(final Object... objects) {
        AssertNull.assertNull(objects);
    }

    public static void assertNull(final String message,
                                  final Object... values) {
        AssertNull.assertNull(message, values);
    }

    public static void assertNull(final Supplier<String> messageProducer,
                                  final Object... values) {
        AssertNull.assertNull(messageProducer, values);
    }


    public static void assertNull(final ErrorCode errorCode,
                                  final Object... values) {
        AssertNull.assertNull(errorCode, values);
    }

    // -------------------------------------------------------------------------
    // NOT NULL
    // -------------------------------------------------------------------------


    public static void assertNotNull(final Object... objects) {
        AssertNull.assertNotNull(objects);
    }


    public static void assertNotNull(final String message,
                                     final Object... values) {
        AssertNull.assertNotNull(message, values);
    }


    public static void assertNotNull(final Supplier<String> messageProducer,
                                     final Object... values) {
        AssertNull.assertNotNull(messageProducer, values);
    }


    public static void assertNotNull(final ErrorCode errorCode,
                                     final Object... values) {
        AssertNull.assertNotNull(errorCode, values);
    }


    // -------------------------------------------------------------------------
    // NOT EMPTY
    // -------------------------------------------------------------------------
    public static void assertNotEmpty(final String value) {
        if (checkIsBlank(value)) {
            throwException(ASSERT_NOT_EMPTY_DEFAULT_MSG);
        }
    }


    public static void assertNotEmpty(final String message,
                                      final String value) {
        if (checkIsBlank(value)) {
            throwException(message == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : message);
        }
    }


    public static void assertNotEmpty(final Supplier<String> messageProducer,
                                      final String value) {
        if (checkIsBlank(value)) {
            throwException(messageProducer == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }


    public static void assertNotEmpty(final ErrorCode errorCode,
                                      final String value) {
        if (checkIsBlank(value)) {
            throwException(errorCode);
        }
    }

    public static boolean checkIsBlank(final String value) {

        boolean result = (value == null) || (value.length() == 0) || value.isEmpty();

        if (!result) {
            result = value.isBlank();
        }

        return result;
    }

    public static void assertNotEmpty(final Collection<?> value) {
        if (value == null || value.isEmpty()) {
            throwException(ASSERT_NOT_EMPTY_DEFAULT_MSG);
        }
    }

    public static void assertNotEmpty(final Supplier<String> messageProducer,
                                      final Collection<?> value) {
        if (value == null || value.isEmpty()) {
            throwException(messageProducer == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }


    public static void assertNotEmpty(final String message,
                                      final Collection<?> value) {
        if (value == null || value.isEmpty()) {
            throwException(message == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : message);
        }
    }


    public static void assertNotEmpty(final ErrorCode errorCode,
                                      final Collection<?> value) {
        if (value == null || value.isEmpty()) {
            throwException(errorCode);
        }
    }


    public static void assertNotEmpty(final IsEmptyFacet value) {
        if (value == null || value.isEmpty()) {
            throwException(ASSERT_NOT_EMPTY_DEFAULT_MSG);
        }
    }

    public static void assertNotEmpty(final String message,
                                      final IsEmptyFacet value) {
        if (value == null || value.isEmpty()) {
            throwException(message == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public static void assertNotEmpty(final Supplier<String> messageProducer,
                                      final IsEmptyFacet value) {
        if (value == null || value.isEmpty()) {
            throwException(messageProducer == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public static void assertNotEmpty(final ErrorCode errorCode,
                                      final IsEmptyFacet value) {
        if (value == null || value.isEmpty()) {
            throwException(errorCode);
        }
    }

    public static void assertNotEmpty(final Map<?, ?> value) {
        if (value == null || value.isEmpty()) {
            throwException(ASSERT_NOT_EMPTY_DEFAULT_MSG);
        }
    }


    public static void assertNotEmpty(final String message,
                                      final Map<?, ?> value) {
        if (value == null || value.isEmpty()) {
            throwException(message == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public static void assertNotEmpty(final Supplier<String> messageProducer,
                                      final Map<?, ?> value) {
        if (value == null || value.isEmpty()) {
            throwException(messageProducer == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }


    public static void assertNotEmpty(final ErrorCode errorCode,
                                      final Map<?, ?> value) {
        if (value == null || value.isEmpty()) {
            throwException(errorCode);
        }
    }


    // -------------------------------------------------------------------------
    // EMPTY
    // -------------------------------------------------------------------------
    public static void assertEmpty(final String value) {
        if (!checkIsBlank(value)) {
            throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public static void assertEmpty(final String message,
                                   final String value) {
        if (!checkIsBlank(value)) {
            throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public static void assertEmpty(final Supplier<String> messageProducer,
                                   final String value) {
        if (!checkIsBlank(value)) {
            throwException(messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public static void assertEmpty(final ErrorCode errorCode,
                                   final String value) {
        if (!checkIsBlank(value)) {
            throwException(errorCode);
        }
    }


    public static void assertEmpty(final Collection<?> value) {
        if (value == null || !value.isEmpty()) {
            throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public static void assertEmpty(final Supplier<String> messageProducer,
                                   final Collection<?> value) {
        if (value == null || !value.isEmpty()) {
            throwException(messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public static void assertEmpty(final String message,
                                   final Collection<?> value) {
        if (value == null || !value.isEmpty()) {
            throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public static void assertEmpty(final ErrorCode errorCode,
                                   final Collection<?> value) {
        if (value == null || !value.isEmpty()) {
            throwException(errorCode);
        }
    }

    public static void assertEmpty(final Map<?, ?> value) {
        if (value == null || !value.isEmpty()) {
            throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public static void assertEmpty(final String message,
                                   final Map<?, ?> value) {
        if (value == null || !value.isEmpty()) {
            throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public static void assertEmpty(final Supplier<String> messageProducer,
                                   final Map<?, ?> value) {
        if (value == null || !value.isEmpty()) {
            throwException(messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public static void assertEmpty(final ErrorCode errorCode,
                                   final Map<?, ?> value) {
        if (value == null || !value.isEmpty()) {
            throwException(errorCode);
        }
    }


    public static void assertEmpty(final IsEmptyFacet value) {
        if (value == null || !value.isEmpty()) {
            throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public static void assertEmpty(final Supplier<String> messageProducer,
                                   final IsEmptyFacet value) {
        if (value == null || !value.isEmpty()) {
            throwException(messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public static void assertEmpty(final String message,
                                   final IsEmptyFacet value) {
        if (value == null || !value.isEmpty()) {
            throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public static void assertEmpty(final ErrorCode errorCode,
                                   final IsEmptyFacet value) {
        if (value == null || !value.isEmpty()) {
            throwException(errorCode);
        }
    }


    // -------------------------------------------------------------------------
    // NULL OR EMPTY
    // -------------------------------------------------------------------------
    public static void assertNullOrEmpty(final String value) {
        if (value != null && !checkIsBlank(value)) {
            throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public static void assertNullOrEmpty(final String message,
                                         final String value) {
        if (value != null && !checkIsBlank(value)) {
            throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public static void assertNullOrEmpty(final Supplier<String> messageProducer,
                                         final String value) {
        if (value != null && !checkIsBlank(value)) {
            throwException(messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public static void assertNullOrEmpty(final ErrorCode errorCode,
                                         final String value) {
        if (value != null && !checkIsBlank(value)) {
            throwException(errorCode);
        }
    }


    public static void assertNullOrEmpty(final Collection<?> value) {
        if (value != null && !value.isEmpty()) {
            throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public static void assertNullOrEmpty(final Supplier<String> messageProducer,
                                         final Collection<?> value) {
        if (value != null && !value.isEmpty()) {
            throwException(messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public static void assertNullOrEmpty(final String message,
                                         final Collection<?> value) {
        if (value != null && !value.isEmpty()) {
            throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public static void assertNullOrEmpty(final ErrorCode errorCode,
                                         final Collection<?> value) {
        if (value != null && !value.isEmpty()) {
            throwException(errorCode);
        }
    }

    public static void assertNullOrEmpty(final Map<?, ?> value) {
        if (value != null && !value.isEmpty()) {
            throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public static void assertNullOrEmpty(final String message,
                                         final Map<?, ?> value) {
        if (value != null && !value.isEmpty()) {
            throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public static void assertNullOrEmpty(final Supplier<String> messageProducer,
                                         final Map<?, ?> value) {
        if (value != null && !value.isEmpty()) {
            throwException(messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public static void assertNullOrEmpty(final ErrorCode errorCode,
                                         final Map<?, ?> value) {
        if (value != null && !value.isEmpty()) {
            throwException(errorCode);
        }
    }


    public static void assertNullOrEmpty(final IsEmptyFacet value) {
        if (value != null && !value.isEmpty()) {
            throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public static void assertNullOrEmpty(final Supplier<String> messageProducer,
                                         final IsEmptyFacet value) {
        if (value != null && !value.isEmpty()) {
            throwException(messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public static void assertNullOrEmpty(final String message,
                                         final IsEmptyFacet value) {
        if (value != null && !value.isEmpty()) {
            throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public static void assertNullOrEmpty(final ErrorCode errorCode,
                                         final IsEmptyFacet value) {
        if (value != null && !value.isEmpty()) {
            throwException(errorCode);
        }
    }

    // -------------------------------------------------------------------------
    // EQUALS
    // -------------------------------------------------------------------------


    public static void assertEquals(final Object ref,
                                    final Object value) {
        AssertEquals.assertEquals(ref, value);
    }


    public static void assertEquals(final String message,
                                    final Object ref,
                                    final Object value) {
        AssertEquals.assertEquals(message, ref, value);
    }

    public static void assertEquals(final Supplier<String> messageProducer,
                                    final Object ref,
                                    final Object value) {
        AssertEquals.assertEquals(messageProducer, ref, value);
    }


    public static void assertEquals(final ErrorCode errorCode,
                                    final Object ref,
                                    final Object value) {
        AssertEquals.assertEquals(errorCode, ref, value);
    }


    public static void assertEquals(final int ref,
                                    final int value) {
        AssertEquals.assertEquals(ref, value);
    }

    public static void assertEquals(final String message,
                                    final int ref,
                                    final int value) {
        AssertEquals.assertEquals(message, ref, value);
    }

    public static void assertEquals(final Supplier<String> messageProducer,
                                    final int ref,
                                    final int value) {
        AssertEquals.assertEquals(messageProducer, ref, value);
    }

    public static void assertEquals(final ErrorCode errorCode,
                                    final int ref,
                                    final int value) {
        AssertEquals.assertEquals(errorCode, ref, value);
    }

    public static void assertEquals(final long ref,
                                    final long value) {
        AssertEquals.assertEquals(ref, value);
    }

    public static void assertEquals(final String message,
                                    final long ref,
                                    final long value) {
        AssertEquals.assertEquals(message, ref, value);
    }

    public static void assertEquals(final Supplier<String> messageProducer,
                                    final long ref,
                                    final long value) {
        AssertEquals.assertEquals(messageProducer, ref, value);
    }

    public static void assertEquals(final ErrorCode errorCode,
                                    final long ref,
                                    final long value) {
        AssertEquals.assertEquals(errorCode, ref, value);
    }

    public static void assertEquals(final float ref,
                                    final float value) {
        AssertEquals.assertEquals(ref, value);
    }

    public static void assertEquals(final String message,
                                    final float ref,
                                    final float value) {
        AssertEquals.assertEquals(message, ref, value);
    }

    public static void assertEquals(final Supplier<String> messageProducer,
                                    final float ref,
                                    final float value) {
        AssertEquals.assertEquals(messageProducer, ref, value);
    }

    public static void assertEquals(final ErrorCode errorCode,
                                    final float ref,
                                    final float value) {
        AssertEquals.assertEquals(errorCode, ref, value);
    }

    public static void assertEquals(final double ref,
                                    final double value) {
        AssertEquals.assertEquals(ref, value);
    }

    public static void assertEquals(final String message,
                                    final double ref,
                                    final double value) {
        AssertEquals.assertEquals(message, ref, value);
    }

    public static void assertEquals(final Supplier<String> messageProducer,
                                    final double ref,
                                    final double value) {
        AssertEquals.assertEquals(messageProducer, ref, value);
    }

    public static void assertEquals(final ErrorCode errorCode,
                                    final double ref,
                                    final double value) {
        AssertEquals.assertEquals(errorCode, ref, value);
    }


    // -------------------------------------------------------------------------
    // NOT EQUALS
    // -------------------------------------------------------------------------
    public static void assertNotEquals(final Object ref,
                                       final Object value) {
        AssertNotEquals.assertNotEquals(ref, value);
    }

    public static void assertNotEquals(final String message,
                                       final Object ref,
                                       final Object value) {
        AssertNotEquals.assertNotEquals(message, ref, value);
    }

    public static void assertNotEquals(final Supplier<String> messageProducer,
                                       final Object ref,
                                       final Object value) {
        AssertNotEquals.assertNotEquals(messageProducer, ref, value);
    }

    public static void assertNotEquals(final ErrorCode errorCode,
                                       final Object ref,
                                       final Object value) {
        AssertNotEquals.assertNotEquals(errorCode, ref, value);
    }

    public static void assertNotEquals(final int ref,
                                       final int value) {
        AssertNotEquals.assertNotEquals(ref, value);
    }

    public static void assertNotEquals(final String message,
                                       final int ref,
                                       final int value) {
        AssertNotEquals.assertNotEquals(message, ref, value);
    }


    public static void assertNotEquals(final Supplier<String> messageProducer,
                                       final int ref,
                                       final int value) {
        AssertNotEquals.assertNotEquals(messageProducer, ref, value);
    }


    public static void assertNotEquals(final ErrorCode errorCode,
                                       final int ref,
                                       final int value) {
        AssertNotEquals.assertNotEquals(errorCode, ref, value);
    }

    public static void assertNotEquals(final long ref,
                                       final long value) {
        AssertNotEquals.assertNotEquals(ref, value);
    }

    public static void assertNotEquals(final String message,
                                       final long ref,
                                       final long value) {
        AssertNotEquals.assertNotEquals(message, ref, value);
    }


    public static void assertNotEquals(final Supplier<String> messageProducer,
                                       final long ref,
                                       final long value) {
        AssertNotEquals.assertNotEquals(messageProducer, ref, value);
    }


    public static void assertNotEquals(final ErrorCode errorCode,
                                       final long ref,
                                       final long value) {
        AssertNotEquals.assertNotEquals(errorCode, ref, value);
    }

    public static void assertNotEquals(final float ref,
                                       final float value) {
        AssertNotEquals.assertNotEquals(ref, value);
    }

    public static void assertNotEquals(final String message,
                                       final float ref,
                                       final float value) {
        AssertNotEquals.assertNotEquals(message, ref, value);
    }


    public static void assertNotEquals(final Supplier<String> messageProducer,
                                       final float ref,
                                       final float value) {
        AssertNotEquals.assertNotEquals(messageProducer, ref, value);
    }


    public static void assertNotEquals(final ErrorCode errorCode,
                                       final float ref,
                                       final float value) {
        AssertNotEquals.assertNotEquals(errorCode, ref, value);
    }

    public static void assertNotEquals(final double ref,
                                       final double value) {
        AssertNotEquals.assertNotEquals(ref, value);
    }

    public static void assertNotEquals(final String message,
                                       final double ref,
                                       final double value) {
        AssertNotEquals.assertNotEquals(message, ref, value);
    }


    public static void assertNotEquals(final Supplier<String> messageProducer,
                                       final double ref,
                                       final double value) {
        AssertNotEquals.assertNotEquals(messageProducer, ref, value);
    }


    public static void assertNotEquals(final ErrorCode errorCode,
                                       final double ref,
                                       final double value) {
        AssertNotEquals.assertNotEquals(errorCode, ref, value);
    }


    // -------------------------------------------------------------------------
    // LOWER
    // -------------------------------------------------------------------------
    public static void assertLower(final int ref,
                                   final int value) {
        AssertLower.assertLower(ref, value);
    }

    public static void assertLower(final String message,
                                   final int ref,
                                   final int value) {
        AssertLower.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer,
                                   final int ref,
                                   final int value) {
        AssertLower.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode,
                                   final int ref,
                                   final int value) {
        AssertLower.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final long ref,
                                   final long value) {
        AssertLower.assertLower(ref, value);
    }

    public static void assertLower(final String message,
                                   final long ref,
                                   final long value) {
        AssertLower.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer,
                                   final long ref,
                                   final long value) {
        AssertLower.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode,
                                   final long ref,
                                   final long value) {
        AssertLower.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final float ref,
                                   final float value) {
        AssertLower.assertLower(ref, value);
    }

    public static void assertLower(final String message,
                                   final float ref,
                                   final float value) {
        AssertLower.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer,
                                   final float ref,
                                   final float value) {
        AssertLower.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode,
                                   final float ref,
                                   final float value) {
        AssertLower.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final double ref,
                                   final double value) {
        AssertLower.assertLower(ref, value);
    }

    public static void assertLower(final String message,
                                   final double ref,
                                   final double value) {
        AssertLower.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer,
                                   final double ref,
                                   final double value) {
        AssertLower.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode,
                                   final double ref,
                                   final double value) {
        AssertLower.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final Integer ref,
                                   final Integer value) {
        AssertLower.assertLower(ref, value);
    }

    public static void assertLower(final String message,
                                   final Integer ref,
                                   final Integer value) {

        AssertLower.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer,
                                   final Integer ref,
                                   final Integer value) {
        AssertLower.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode,
                                   final Integer ref,
                                   final Integer value) {
        AssertLower.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final Long ref,
                                   final Long value) {
        AssertLower.assertLower(ref, value);
    }

    public static void assertLower(final String message,
                                   final Long ref,
                                   final Long value) {
        AssertLower.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer,
                                   final Long ref,
                                   final Long value) {
        AssertLower.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode,
                                   final Long ref,
                                   final Long value) {
        AssertLower.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final Double ref,
                                   final Double value) {
        AssertLower.assertLower(ref, value);
    }

    public static void assertLower(final String message,
                                   final Double ref,
                                   final Double value) {
        AssertLower.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer,
                                   final Double ref,
                                   final Double value) {
        AssertLower.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode,
                                   final Double ref,
                                   final Double value) {
        AssertLower.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final BigDecimal ref,
                                   final BigDecimal value) {
        AssertLower.assertLower(ref, value);
    }

    public static void assertLower(final String message,
                                   final BigDecimal ref,
                                   final BigDecimal value) {
        AssertLower.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer,
                                   final BigDecimal ref,
                                   final BigDecimal value) {
        AssertLower.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode,
                                   final BigDecimal ref,
                                   final BigDecimal value) {
        AssertLower.assertLower(errorCode, ref, value);
    }


    // -------------------------------------------------------------------------
    // LOWER OR EQUALS
    // -------------------------------------------------------------------------
    public static void assertLowerOrEquals(final int ref,
                                           final int value) {
        AssertLowerOrEquals.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message,
                                           final int ref,
                                           final int value) {
        AssertLowerOrEquals.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final int ref,
                                           final int value) {
        AssertLowerOrEquals.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode,
                                           final int ref,
                                           final int value) {
        AssertLowerOrEquals.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final long ref,
                                           final long value) {
        AssertLowerOrEquals.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message,
                                           final long ref,
                                           final long value) {
        AssertLowerOrEquals.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final long ref,
                                           final long value) {
        AssertLowerOrEquals.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode,
                                           final long ref,
                                           final long value) {
        AssertLowerOrEquals.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final float ref,
                                           final float value) {
        AssertLowerOrEquals.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message,
                                           final float ref,
                                           final float value) {
        AssertLowerOrEquals.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final float ref,
                                           final float value) {
        AssertLowerOrEquals.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode,
                                           final float ref,
                                           final float value) {
        AssertLowerOrEquals.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final double ref,
                                           final double value) {
        AssertLowerOrEquals.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message,
                                           final double ref,
                                           final double value) {
        AssertLowerOrEquals.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final double ref,
                                           final double value) {
        AssertLowerOrEquals.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode,
                                           final double ref,
                                           final double value) {
        AssertLowerOrEquals.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final Integer ref,
                                           final Integer value) {
        AssertLowerOrEquals.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message,
                                           final Integer ref,
                                           final Integer value) {
        AssertLowerOrEquals.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final Integer ref,
                                           final Integer value) {
        AssertLowerOrEquals.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode,
                                           final Integer ref,
                                           final Integer value) {
        AssertLowerOrEquals.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final Long ref,
                                           final Long value) {
        AssertLowerOrEquals.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message,
                                           final Long ref,
                                           final Long value) {
        AssertLowerOrEquals.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final Long ref,
                                           final Long value) {
        AssertLowerOrEquals.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode,
                                           final Long ref,
                                           final Long value) {
        AssertLowerOrEquals.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final Double ref,
                                           final Double value) {
        AssertLowerOrEquals.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message,
                                           final Double ref,
                                           final Double value) {
        AssertLowerOrEquals.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final Double ref,
                                           final Double value) {
        AssertLowerOrEquals.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode,
                                           final Double ref,
                                           final Double value) {
        AssertLowerOrEquals.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final BigDecimal ref,
                                           final BigDecimal value) {
        AssertLowerOrEquals.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message,
                                           final BigDecimal ref,
                                           final BigDecimal value) {
        AssertLowerOrEquals.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final BigDecimal ref,
                                           final BigDecimal value) {
        AssertLowerOrEquals.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode,
                                           final BigDecimal ref,
                                           final BigDecimal value) {
        AssertLowerOrEquals.assertLowerOrEquals(errorCode, ref, value);
    }

    // -------------------------------------------------------------------------
    // HIGHER
    // -------------------------------------------------------------------------
    public static void assertHigher(final int ref,
                                    final int value) {
        AssertHigher.assertHigher(ref, value);
    }

    public static void assertHigher(final String message,
                                    final int ref,
                                    final int value) {
        AssertHigher.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer,
                                    final int ref,
                                    final int value) {
        AssertHigher.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode,
                                    final int ref,
                                    final int value) {
        AssertHigher.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final long ref,
                                    final long value) {
        AssertHigher.assertHigher(ref, value);
    }

    public static void assertHigher(final String message,
                                    final long ref,
                                    final long value) {
        AssertHigher.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer,
                                    final long ref,
                                    final long value) {
        AssertHigher.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode,
                                    final long ref,
                                    final long value) {
        AssertHigher.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final float ref,
                                    final float value) {
        AssertHigher.assertHigher(ref, value);
    }

    public static void assertHigher(final String message,
                                    final float ref,
                                    final float value) {
        AssertHigher.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer,
                                    final float ref,
                                    final float value) {
        AssertHigher.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode,
                                    final float ref,
                                    final float value) {
        AssertHigher.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final double ref,
                                    final double value) {
        AssertHigher.assertHigher(ref, value);
    }

    public static void assertHigher(final String message,
                                    final double ref,
                                    final double value) {
        AssertHigher.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer,
                                    final double ref,
                                    final double value) {
        AssertHigher.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode,
                                    final double ref,
                                    final double value) {
        AssertHigher.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final Integer ref,
                                    final Integer value) {
        AssertHigher.assertHigher(ref, value);
    }

    public static void assertHigher(final String message,
                                    final Integer ref,
                                    final Integer value) {
        AssertHigher.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer,
                                    final Integer ref,
                                    final Integer value) {
        AssertHigher.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode,
                                    final Integer ref,
                                    final Integer value) {
        AssertHigher.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final Long ref,
                                    final Long value) {
        AssertHigher.assertHigher(ref, value);
    }

    public static void assertHigher(final String message,
                                    final Long ref,
                                    final Long value) {
        AssertHigher.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer,
                                    final Long ref,
                                    final Long value) {
        AssertHigher.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode,
                                    final Long ref,
                                    final Long value) {
        AssertHigher.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final Double ref,
                                    final Double value) {
        AssertHigher.assertHigher(ref, value);
    }

    public static void assertHigher(final String message,
                                    final Double ref,
                                    final Double value) {
        AssertHigher.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer,
                                    final Double ref,
                                    final Double value) {
        AssertHigher.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode,
                                    final Double ref,
                                    final Double value) {
        AssertHigher.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final BigDecimal ref,
                                    final BigDecimal value) {
        AssertHigher.assertHigher(ref, value);
    }

    public static void assertHigher(final String message,
                                    final BigDecimal ref,
                                    final BigDecimal value) {
        AssertHigher.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer,
                                    final BigDecimal ref,
                                    final BigDecimal value) {
        AssertHigher.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode,
                                    final BigDecimal ref,
                                    final BigDecimal value) {
        AssertHigher.assertHigher(errorCode, ref, value);
    }


    // -------------------------------------------------------------------------
    // assertHigherOrEquals
    // -------------------------------------------------------------------------
    public static void assertHigherOrEquals(final int ref,
                                            final int value) {
        AssertHigherOrEquals.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message,
                                            final int ref,
                                            final int value) {
        AssertHigherOrEquals.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final int ref,
                                            final int value) {
        AssertHigherOrEquals.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode,
                                            final int ref,
                                            final int value) {
        AssertHigherOrEquals.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final long ref,
                                            final long value) {
        AssertHigherOrEquals.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message,
                                            final long ref,
                                            final long value) {
        AssertHigherOrEquals.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final long ref,
                                            final long value) {
        AssertHigherOrEquals.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode,
                                            final long ref,
                                            final long value) {
        AssertHigherOrEquals.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final float ref,
                                            final float value) {
        AssertHigherOrEquals.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message,
                                            final float ref,
                                            final float value) {
        AssertHigherOrEquals.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final float ref,
                                            final float value) {
        AssertHigherOrEquals.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode,
                                            final float ref,
                                            final float value) {
        AssertHigherOrEquals.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final double ref,
                                            final double value) {
        AssertHigherOrEquals.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message,
                                            final double ref,
                                            final double value) {
        AssertHigherOrEquals.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final double ref,
                                            final double value) {
        AssertHigherOrEquals.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode,
                                            final double ref,
                                            final double value) {
        AssertHigherOrEquals.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final Integer ref,
                                            final Integer value) {
        AssertHigherOrEquals.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final Integer ref,
                                            final Integer value) {
        AssertHigherOrEquals.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final String message,
                                            final Integer ref,
                                            final Integer value) {
        AssertHigherOrEquals.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode,
                                            final Integer ref,
                                            final Integer value) {
        AssertHigherOrEquals.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final Long ref,
                                            final Long value) {
        AssertHigherOrEquals.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message,
                                            final Long ref,
                                            final Long value) {
        AssertHigherOrEquals.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final Long ref,
                                            final Long value) {
        AssertHigherOrEquals.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode,
                                            final Long ref,
                                            final Long value) {
        AssertHigherOrEquals.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final Double ref,
                                            final Double value) {
        AssertHigherOrEquals.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message,
                                            final Double ref,
                                            final Double value) {
        AssertHigherOrEquals.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final Double ref,
                                            final Double value) {
        AssertHigherOrEquals.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode,
                                            final Double ref,
                                            final Double value) {
        AssertHigherOrEquals.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final BigDecimal ref,
                                            final BigDecimal value) {
        AssertHigherOrEquals.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message,
                                            final BigDecimal ref,
                                            final BigDecimal value) {
        AssertHigherOrEquals.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final BigDecimal ref,
                                            final BigDecimal value) {
        AssertHigherOrEquals.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode,
                                            final BigDecimal ref,
                                            final BigDecimal value) {
        AssertHigherOrEquals.assertHigherOrEquals(errorCode, ref, value);
    }


    // -------------------------------------------------------------------------
    // REGEX
    // -------------------------------------------------------------------------
    public static void assertRegexMatch(final Pattern regex,
                                        final String value) {
        AssertRegex.assertRegexMatch(regex, value);
    }

    public static void assertRegexMatch(final String message,
                                        final Pattern regex,
                                        final String value) {
        AssertRegex.assertRegexMatch(message, regex, value);
    }

    public static void assertRegexMatch(final Supplier<String> messageProducer,
                                        final Pattern regex,
                                        final String value) {
        AssertRegex.assertRegexMatch(messageProducer, regex, value);
    }

    public static void assertRegexMatch(final ErrorCode errorCode,
                                        final Pattern regex,
                                        final String value) {
        AssertRegex.assertRegexMatch(errorCode, regex, value);
    }


    public static void assertRegexMatch(final String regex,
                                        final String value) {
        AssertRegex.assertRegexMatch(regex, value);
    }

    public static void assertRegexMatch(final String message,
                                        final String regex,
                                        final String value) {
        AssertRegex.assertRegexMatch(message, regex, value);
    }


    public static void assertRegexMatch(final Supplier<String> messageProducer,
                                        final String regex,
                                        final String value) {
        AssertRegex.assertRegexMatch(messageProducer, regex, value);
    }

    public static void assertRegexMatch(final ErrorCode errorCode,
                                        final String regex,
                                        final String value) {
        AssertRegex.assertRegexMatch(errorCode, regex, value);
    }


    public static void assertRegexNotMatch(final Pattern regex,
                                           final String value) {
        AssertRegex.assertRegexNotMatch(regex, value);
    }

    public static void assertRegexNotMatch(final String message,
                                           final Pattern regex,
                                           final String value) {
        AssertRegex.assertRegexNotMatch(message, regex, value);
    }

    public static void assertRegexNotMatch(final Supplier<String> messageProducer,
                                           final Pattern regex,
                                           final String value) {
        AssertRegex.assertRegexNotMatch(messageProducer, regex, value);
    }

    public static void assertRegexNotMatch(final ErrorCode errorCode,
                                           final Pattern regex,
                                           final String value) {
        AssertRegex.assertRegexNotMatch(errorCode, regex, value);
    }


    public static void assertRegexNotMatch(final String regex,
                                           final String value) {
        AssertRegex.assertRegexNotMatch(regex, value);
    }

    public static void assertRegexNotMatch(final String message,
                                           final String regex,
                                           final String value) {
        AssertRegex.assertRegexNotMatch(message, regex, value);
    }


    public static void assertRegexNotMatch(final Supplier<String> messageProducer,
                                           final String regex,
                                           final String value) {
        AssertRegex.assertRegexNotMatch(messageProducer, regex, value);
    }

    public static void assertRegexNotMatch(final ErrorCode errorCode,
                                           final String regex,
                                           final String value) {
        AssertRegex.assertRegexNotMatch(errorCode, regex, value);
    }

    public static void assertRegexFind(final Pattern regex,
                                       final String value) {
        AssertRegex.assertRegexFind(regex, value);
    }

    public static void assertRegexFind(final String message,
                                       final Pattern regex,
                                       final String value) {
        AssertRegex.assertRegexFind(message, regex, value);
    }

    public static void assertRegexFind(final Supplier<String> messageProducer,
                                       final Pattern regex,
                                       final String value) {
        AssertRegex.assertRegexFind(messageProducer, regex, value);
    }

    public static void assertRegexFind(final ErrorCode errorCode,
                                       final Pattern regex,
                                       final String value) {
        AssertRegex.assertRegexFind(errorCode, regex, value);
    }


    public static void assertRegexFind(final String regex,
                                       final String value) {
        AssertRegex.assertRegexFind(regex, value);
    }

    public static void assertRegexFind(final String message,
                                       final String regex,
                                       final String value) {
        AssertRegex.assertRegexFind(message, regex, value);
    }


    public static void assertRegexFind(final Supplier<String> messageProducer,
                                       final String regex,
                                       final String value) {
        AssertRegex.assertRegexFind(messageProducer, regex, value);
    }

    public static void assertRegexFind(final ErrorCode errorCode,
                                       final String regex,
                                       final String value) {
        AssertRegex.assertRegexFind(errorCode, regex, value);
    }

    public static void assertRegexNotFind(final Pattern regex,
                                          final String value) {
        AssertRegex.assertRegexNotFind(regex, value);
    }

    public static void assertRegexNotFind(final String message,
                                          final Pattern regex,
                                          final String value) {
        AssertRegex.assertRegexNotFind(message, regex, value);
    }

    public static void assertRegexNotFind(final Supplier<String> messageProducer,
                                          final Pattern regex,
                                          final String value) {
        AssertRegex.assertRegexNotFind(messageProducer, regex, value);
    }

    public static void assertRegexNotFind(final ErrorCode errorCode,
                                          final Pattern regex,
                                          final String value) {
        AssertRegex.assertRegexNotFind(errorCode, regex, value);
    }


    public static void assertRegexNotFind(final String regex,
                                          final String value) {
        AssertRegex.assertRegexNotFind(regex, value);
    }

    public static void assertRegexNotFind(final String message,
                                          final String regex,
                                          final String value) {
        AssertRegex.assertRegexNotFind(message, regex, value);
    }


    public static void assertRegexNotFind(final Supplier<String> messageProducer,
                                          final String regex,
                                          final String value) {
        AssertRegex.assertRegexNotFind(messageProducer, regex, value);
    }

    public static void assertRegexNotFind(final ErrorCode errorCode,
                                          final String regex,
                                          final String value) {
        AssertRegex.assertRegexNotFind(errorCode, regex, value);
    }


    public static List<ErrorCode> checkModel(final VoidFunctionWithException... assertions) {
        if (assertions == null) {
            return new ArrayList<>();
        }
        return checkModel(Arrays.asList(assertions));
    }

    public static List<ErrorCode> checkModel(final List<VoidFunctionWithException> assertions) {
        final List<ErrorCode> result = new ArrayList<>();
        if (assertions != null) {
            for (final VoidFunctionWithException function : assertions) {
                try {
                    function.process();
                } catch (final Exception e) {

                    if (e instanceof ExceptionWithErrorCode) {
                        result.add(((ExceptionWithErrorCode) e).getErrorCode());
                    } else {
                        result.add(DefaultErrorCode.fromErrorCode(DefaultErrorCode.buildUndefineError()).message(e.getMessage()).build());
                    }
                }
            }
        }
        return result;
    }


    public static void assertModel(final VoidFunctionWithException... assertions) {
        if (assertions == null) {
            return;
        }
        assertModel(Arrays.asList(assertions));
    }

    public static void assertModel(final List<VoidFunctionWithException> assertions) {
        final List<ErrorCode> errors = checkModel(assertions);
        if (!errors.isEmpty()) {
            AssertCommons.throwException(errors);
        }
    }


    // -------------------------------------------------------------------------
    // Assert throws
    // -------------------------------------------------------------------------
    public static void assertThrow(final String message,
                                   final VoidFunctionWithException function) {
        try {
            function.process();
            throwException(message);
        } catch (final Exception error) {
        }
    }


    // -------------------------------------------------------------------------
    // Assert file
    // -------------------------------------------------------------------------
    public static void assertFileExists(final File path) {
        AssertFile.assertFileExists(path);
    }

    public static void assertFileExists(final String message, final File path) {
        AssertFile.assertFileExists(message, path);
    }

    public static void assertFileExists(final ErrorCode errorCode, final File path) {
        AssertFile.assertFileExists(errorCode, path);
    }

    public static void assertFileReadable(final File path) {
        AssertFile.assertFileReadable(path);
    }

    public static void assertFileReadable(final String message, final File path) {
        AssertFile.assertFileReadable(message, path);
    }

    public static void assertFileReadable(final ErrorCode errorCode, final File path) {
        AssertFile.assertFileReadable(errorCode, path);
    }

    public static void assertFileWrite(final File path) {
        AssertFile.assertFileWrite(path);
    }

    public static void assertFileWrite(final String message, final File path) {
        AssertFile.assertFileWrite(message, path);
    }

    public static void assertFileWrite(final ErrorCode errorCode, final File path) {
        AssertFile.assertFileWrite(errorCode, path);
    }

    public static void assertFileExecutable(final File path) {
        AssertFile.assertFileExecutable(path);
    }

    public static void assertFileExecutable(final String message, final File path) {
        AssertFile.assertFileExecutable(message, path);
    }

    public static void assertFileExecutable(final ErrorCode errorCode, final File path) {
        AssertFile.assertFileExecutable(errorCode, path);
    }

    // -------------------------------------------------------------------------
    // Assert folder
    // -------------------------------------------------------------------------
    public static void assertFolderExists(final File path) {
        AssertFile.assertFolderExists(path);
    }

    public static void assertFolderExists(final String message, final File path) {
        AssertFile.assertFolderExists(message, path);
    }

    public static void assertFolderExists(final ErrorCode errorCode, final File path) {
        AssertFile.assertFolderExists(errorCode, path);
    }

    public static <T> T wrapErrorForSupplierWithException(final ActionWithException action, final ErrorCode errorCode) {
        return wrapErrorForSupplierWithException(action, errorCode, null);
    }

    public static <T> T wrapErrorForSupplierWithException(final ActionWithException action, final ErrorCodeResolver resolver) {
        return wrapErrorForSupplierWithException(action, null, resolver);
    }


    public static <T> T wrapErrorForSupplierWithException(final ActionWithException action, final ErrorCode errorCode, final ErrorCodeResolver resolver) {
        assertNotNull(action);
        try {
            return action.process();
        } catch (final Throwable e) {
            throw handlerException(errorCode, resolver, e);
        }
    }


    public static <T> T wrapErrorForSupplier(final Supplier<T> action, final ErrorCode errorCode) {
        return wrapErrorForSupplier(action, errorCode, null);
    }

    public static <T> T wrapErrorForSupplier(final Supplier<T> action, final ErrorCodeResolver resolver) {
        return wrapErrorForSupplier(action, null, resolver);
    }


    public static <T> T wrapErrorForSupplier(final Supplier<T> action, final ErrorCode errorCode, final ErrorCodeResolver resolver) {
        assertNotNull(action);
        try {
            return action.get();
        } catch (final Throwable e) {
            throw handlerException(errorCode, resolver, e);
        }
    }


    public static void wrapErrorForVoidFunction(final VoidFunction action, final ErrorCode errorCode) {
        wrapErrorForVoidFunction(action, errorCode, null);
    }

    public static void wrapErrorForVoidFunction(final VoidFunction action, final ErrorCodeResolver resolver) {
        wrapErrorForVoidFunction(action, null, resolver);
    }


    public static void wrapErrorForVoidFunction(final VoidFunction action, final ErrorCode errorCode, final ErrorCodeResolver resolver) {
        assertNotNull(action);
        try {
            action.process();
        } catch (final Throwable e) {
            throw handlerException(errorCode, resolver, e);
        }
    }


    public static void wrapErrorForVoidFunctionWithException(final VoidFunctionWithException action, final ErrorCode errorCode) {
        wrapErrorForVoidFunctionWithException(action, errorCode, null);
    }

    public static void wrapErrorForVoidFunctionWithException(final VoidFunctionWithException action, final ErrorCodeResolver resolver) {
        wrapErrorForVoidFunctionWithException(action, null, resolver);
    }


    public static void wrapErrorForVoidFunctionWithException(final VoidFunctionWithException action, final ErrorCode errorCode, final ErrorCodeResolver resolver) {
        assertNotNull(action);
        try {
            action.process();
        } catch (final Throwable e) {
            throw handlerException(errorCode, resolver, e);
        }
    }

    private static ErrorCode resolveErrorCode(final Throwable exception, final ErrorCode errorCode, final ErrorCodeResolver resolver) {
        if (errorCode == null) {
            if (exception instanceof ExceptionWithErrorCode) {
                return ((ExceptionWithErrorCode) exception).getErrorCode();
            } else if (resolver != null) {
                final ErrorCode error = resolver.resolve(exception);
                return error == null ? DefaultErrorCode.buildUndefineError() : error;
            } else {
                return DefaultErrorCode.buildUndefineError();
            }
        } else {
            return errorCode;
        }
    }

    private static UncheckedException handlerException(final ErrorCode errorCode, final ErrorCodeResolver resolver, final Throwable e) {
        ErrorCode currentErrorCode = resolveErrorCode(e, errorCode, resolver);
        if (currentErrorCode.getMessage() == null) {
            currentErrorCode = DefaultErrorCode.fromErrorCode(currentErrorCode).message(e.getMessage()).build();
        }
        return new UncheckedException(currentErrorCode.addDetail(e.getMessage()), e);
    }

    // -------------------------------------------------------------------------
    // TOOLS
    // -------------------------------------------------------------------------
    public static void throwException(final String message) {
        AssertCommons.throwException(null, message);
    }

    public static void throwException(final ErrorCode errorCode) {
        AssertCommons.throwException(errorCode);
    }

    public static void throwException(final ErrorCode errorCode,
                                      final String message,
                                      final Serializable... args) {
        AssertCommons.throwException(errorCode, message);
    }


}
