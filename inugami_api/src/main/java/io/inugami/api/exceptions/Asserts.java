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
import io.inugami.api.functionnals.IsEmptyFacet;
import io.inugami.api.functionnals.VoidFunctionWithException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

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
    private static final String ASSERT_NULL_DEFAULT_MSG      = "objects arguments must be null";
    private static final String ASSERT_NOT_NULL_DEFAULT_MSG  = "this argument is required; it must not be null";
    private static final String ASSERT_NOT_EMPTY_DEFAULT_MSG = "the value mustn't be empty";

    private static final String ASSERT_EMPTY_DEFAULT_MSG = "the value should be empty";


    // -------------------------------------------------------------------------
    // IS TRUE
    // -------------------------------------------------------------------------
    @Deprecated
    public static void isTrue(final boolean expression) {
        assertTrue(ASSERT_TRUE_DEFAULT_MSG, expression);
    }

    public static void assertTrue(final boolean expression) {
        assertTrue(ASSERT_TRUE_DEFAULT_MSG, expression);
    }

    @Deprecated
    public static void isTrue(final String message,
                              final boolean expression) {
        assertTrue(message, expression);
    }

    public static void assertTrue(final String message,
                                  final boolean expression) {
        if (!expression) {
            throwException(message == null ? ASSERT_TRUE_DEFAULT_MSG : message);
        }
    }

    @Deprecated
    public static void isTrue(final ErrorCode errorCode,
                              final boolean expression) {
        assertTrue(errorCode, expression);
    }

    public static void assertTrue(final ErrorCode errorCode,
                                  final boolean expression) {
        if (!expression) {
            throwException(errorCode);
        }
    }

    @Deprecated
    public static void isTrue(final Supplier<String> messageProducer,
                              final boolean expression) {
        assertTrue(messageProducer, expression);
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
    @Deprecated
    public static void isFalse(final boolean expression) {
        assertFalse(ASSERT_FALSE_DEFAULT_MSG, expression);
    }

    public static void assertFalse(final boolean expression) {
        assertFalse(ASSERT_FALSE_DEFAULT_MSG, expression);
    }

    @Deprecated
    public static void isFalse(final String message,
                               final boolean expression) {
        assertFalse(message, expression);
    }

    public static void assertFalse(final String message,
                                   final boolean expression) {
        if (expression) {
            throwException(message == null ? ASSERT_FALSE_DEFAULT_MSG : message);
        }
    }

    @Deprecated
    public static void isFalse(final Supplier<String> messageProducer,
                               final boolean expression) {
        assertFalse(messageProducer, expression);
    }

    public static void assertFalse(final Supplier<String> messageProducer,
                                   final boolean expression) {
        if (expression) {
            throwException(messageProducer == null ? ASSERT_FALSE_DEFAULT_MSG : messageProducer.get());
        }
    }

    @Deprecated
    public static void isFalse(final ErrorCode errorCode,
                               final boolean expression) {
        assertFalse(errorCode, expression);
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
    @Deprecated
    public static void isNull(final Object... objects) {
        assertNull(ASSERT_NULL_DEFAULT_MSG, objects);
    }

    public static void assertNull(final Object... objects) {
        assertNull(ASSERT_NULL_DEFAULT_MSG, objects);
    }

    @Deprecated
    public static void isNull(final String message,
                              final Object... values) {
        assertNull(message, values);
    }

    public static void assertNull(final String message,
                                  final Object... values) {
        if(values==null){
            return;
        }
        for (final Object item : values) {
            if (item != null) {
                throwException(message == null ? ASSERT_NULL_DEFAULT_MSG : message);
            }
        }
    }

    @Deprecated
    public static void isNull(final Supplier<String> messageProducer,
                              final Object... values) {
        assertNull(messageProducer, values);
    }

    public static void assertNull(final Supplier<String> messageProducer,
                                  final Object... values) {
        if(values==null){
            return;
        }
        for (final Object item : values) {
            if (item != null) {
                throwException(messageProducer == null ? ASSERT_NULL_DEFAULT_MSG : messageProducer.get());
            }
        }
    }

    @Deprecated
    public static void isNull(final ErrorCode errorCode,
                              final Object... values) {
        assertNull(errorCode, values);
    }

    public static void assertNull(final ErrorCode errorCode,
                                  final Object... values) {
        if(values==null){
            return;
        }
        for (final Object item : values) {
            if (item != null) {
                throwException(errorCode);
            }
        }
    }

    // -------------------------------------------------------------------------
    // NOT NULL
    // -------------------------------------------------------------------------
    @Deprecated
    public static void notNull(final Object... objects) {
        assertNotNull(ASSERT_NOT_NULL_DEFAULT_MSG,
                      objects);
    }

    public static void assertNotNull(final Object... objects) {
        notNull(ASSERT_NOT_NULL_DEFAULT_MSG,
                objects);
    }

    @Deprecated
    public static void notNull(final String message,
                               final Object... values) {

    }

    public static void assertNotNull(final String message,
                                     final Object... values) {
        if (checkIfHasNull(values)) {
            throwException(message == null ? ASSERT_NOT_NULL_DEFAULT_MSG : message);
        }
    }

    @Deprecated
    public static void notNull(final Supplier<String> messageProducer,
                               final Object... values) {
        assertNotNull(messageProducer, values);
    }

    public static void assertNotNull(final Supplier<String> messageProducer,
                                     final Object... values) {
        if (checkIfHasNull(values)) {
            throwException(messageProducer == null ? ASSERT_NULL_DEFAULT_MSG : messageProducer.get());
        }
    }

    @Deprecated
    public static void notNull(final ErrorCode errorCode,
                               final Object... values) {
        assertNotNull(errorCode, values);
    }

    public static void assertNotNull(final ErrorCode errorCode,
                                     final Object... values) {
        if (checkIfHasNull(values)) {
            throwException(errorCode);
        }
    }

    private static boolean checkIfHasNull(final Object... values) {
        if (values == null) {
            return true;
        }
        for (Object value : values) {
            if (value == null) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // NOT EMPTY
    // -------------------------------------------------------------------------
    public static void assertNotEmpty(final String value) {
        if (checkIsBlank(value)) {
            throwException(ASSERT_NOT_EMPTY_DEFAULT_MSG);
        }
    }

    @Deprecated
    public static void notEmpty(final String message,
                                final String value) {
        assertNotEmpty(message, value);
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

    @Deprecated
    public static void notEmpty(final ErrorCode errorCode,
                                final String value) {
        assertNotEmpty(errorCode, value);
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

    @Deprecated
    public static void notEmpty(final String message,
                                final Collection<?> value) {
        assertNotEmpty(message, value);
    }

    public static void assertNotEmpty(final String message,
                                      final Collection<?> value) {
        if (value == null || value.isEmpty()) {
            throwException(message == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : message);
        }
    }

    @Deprecated
    public static void notEmpty(final ErrorCode errorCode,
                                final Collection<?> value) {
        assertNotEmpty(errorCode, value);
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
            throwException(message == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG :message);
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

    @Deprecated
    public static void notEmpty(final Map<?, ?> value) {
        assertNotEmpty(value);
    }

    public static void assertNotEmpty(final Map<?, ?> value) {
        if (value == null || value.isEmpty()) {
            throwException(ASSERT_NOT_EMPTY_DEFAULT_MSG);
        }
    }


    @Deprecated
    public static void notEmpty(final String message,
                                final Map<?, ?> value) {
        assertNotEmpty(message, value);
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

    @Deprecated
    public static void notEmpty(final ErrorCode errorCode,
                                final Map<?, ?> value) {
        assertNotEmpty(errorCode, value);
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
    // EQUALS
    // -------------------------------------------------------------------------
    @Deprecated
    public static void equalsObj(final Object ref,
                                 final Object value) {
        AssertEquals.assertEquals(ref, value);
    }

    public static void assertEquals(final Object ref,
                                    final Object value) {
        AssertEquals.assertEquals(ref, value);
    }


    @Deprecated
    public static void equalsObj(final String message,
                                 final Object ref,
                                 final Object value) {
        AssertEquals.assertEquals(message, ref, value);
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


    @Deprecated
    public static void equalsObj(final ErrorCode errorCode,
                                 final Object ref,
                                 final Object value) {
        AssertEquals.assertEquals(errorCode, ref, value);
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
    // Assert throws
    // -------------------------------------------------------------------------
    public static void assertThrow(final String message,
                                   final VoidFunctionWithException function) {
        try {
            function.process();
            throwException(message);
        } catch (Exception error) {
        }
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
