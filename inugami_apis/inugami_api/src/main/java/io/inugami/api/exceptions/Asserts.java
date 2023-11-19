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
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Asserts
 *
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */
@UtilityClass
public class Asserts {

    // ========================================================================
    // ATTRIBUTES
    // ========================================================================


    // -------------------------------------------------------------------------
    // IS TRUE
    // -------------------------------------------------------------------------

    public static void assertTrue(final boolean expression) {
        AssertBoolean.INSTANCE.assertTrue(expression);
    }


    public static void assertTrue(final String message, final boolean expression) {
        AssertBoolean.INSTANCE.assertTrue(message, expression);
    }


    public static void assertTrue(final ErrorCode errorCode, final boolean expression) {
        AssertBoolean.INSTANCE.assertTrue(errorCode, expression);
    }


    public static void assertTrue(final Supplier<String> messageProducer, final boolean expression) {
        AssertBoolean.INSTANCE.assertTrue(messageProducer, expression);
    }

    // -------------------------------------------------------------------------
    // IS FALSE
    // -------------------------------------------------------------------------

    public static void assertFalse(final boolean expression) {
        AssertBoolean.INSTANCE.assertFalse(expression);
    }


    public static void assertFalse(final String message, final boolean expression) {
        AssertBoolean.INSTANCE.assertFalse(message, expression);
    }


    public static void assertFalse(final Supplier<String> messageProducer, final boolean expression) {
        AssertBoolean.INSTANCE.assertFalse(messageProducer, expression);
    }


    public static void assertFalse(final ErrorCode errorCode, final boolean expression) {
        AssertBoolean.INSTANCE.assertFalse(errorCode, expression);
    }

    // -------------------------------------------------------------------------
    // IS NULL
    // -------------------------------------------------------------------------
    public static void assertNull(final String message, final Object... values) {
        AssertNull.INSTANCE.assertNull(message, values);
    }

    public static void assertNull(final Supplier<String> messageProducer, final Object... values) {
        AssertNull.INSTANCE.assertNull(messageProducer, values);
    }


    public static void assertNull(final ErrorCode errorCode, final Object... values) {
        AssertNull.INSTANCE.assertNull(errorCode, values);
    }

    // -------------------------------------------------------------------------
    // NOT NULL
    // -------------------------------------------------------------------------


    public static void assertNotNull(final Object... objects) {
        AssertNull.INSTANCE.assertNotNull(objects);
    }


    public static void assertNotNull(final String message, final Object... values) {
        AssertNull.INSTANCE.assertNotNull(message, values);
    }


    public static void assertNotNull(final Supplier<String> messageProducer, final Object... values) {
        AssertNull.INSTANCE.assertNotNull(messageProducer, values);
    }


    public static void assertNotNull(final ErrorCode errorCode, final Object... values) {
        AssertNull.INSTANCE.assertNotNull(errorCode, values);
    }


    // -------------------------------------------------------------------------
    // NOT EMPTY
    // -------------------------------------------------------------------------
    public static void assertNotEmpty(final String value) {
        AssertEmpty.INSTANCE.assertNotEmpty(value);
    }


    public static void assertNotEmpty(final String message, final String value) {
        AssertEmpty.INSTANCE.assertNotEmpty(message, value);
    }


    public static void assertNotEmpty(final Supplier<String> messageProducer, final String value) {
        AssertEmpty.INSTANCE.assertNotEmpty(messageProducer, value);
    }


    public static void assertNotEmpty(final ErrorCode errorCode, final String value) {
        AssertEmpty.INSTANCE.assertNotEmpty(errorCode, value);
    }

    public static boolean checkIsBlank(final String value) {
        return AssertEmpty.INSTANCE.checkIsBlank(value);
    }

    public static void assertNotEmpty(final Collection<?> value) {
        AssertEmpty.INSTANCE.assertNotEmpty(value);
    }

    public static void assertNotEmpty(final Supplier<String> messageProducer, final Collection<?> value) {
        AssertEmpty.INSTANCE.assertNotEmpty(messageProducer, value);
    }


    public static void assertNotEmpty(final String message, final Collection<?> value) {
        AssertEmpty.INSTANCE.assertNotEmpty(message, value);
    }


    public static void assertNotEmpty(final ErrorCode errorCode, final Collection<?> value) {
        AssertEmpty.INSTANCE.assertNotEmpty(errorCode, value);
    }


    public static void assertNotEmpty(final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertNotEmpty(value);
    }

    public static void assertNotEmpty(final String message, final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertNotEmpty(message, value);
    }

    public static void assertNotEmpty(final Supplier<String> messageProducer, final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertNotEmpty(messageProducer, value);
    }

    public static void assertNotEmpty(final ErrorCode errorCode, final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertNotEmpty(errorCode, value);
    }

    public static void assertNotEmpty(final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertNotEmpty(value);
    }


    public static void assertNotEmpty(final String message, final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertNotEmpty(message, value);
    }

    public static void assertNotEmpty(final Supplier<String> messageProducer, final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertNotEmpty(messageProducer, value);
    }


    public static void assertNotEmpty(final ErrorCode errorCode, final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertNotEmpty(errorCode, value);
    }


    // -------------------------------------------------------------------------
    // EMPTY
    // -------------------------------------------------------------------------
    public static void assertEmpty(final String value) {
        AssertEmpty.INSTANCE.assertEmpty(value);
    }

    public static void assertEmpty(final String message, final String value) {
        AssertEmpty.INSTANCE.assertEmpty(message, value);
    }

    public static void assertEmpty(final Supplier<String> messageProducer, final String value) {
        AssertEmpty.INSTANCE.assertEmpty(messageProducer, value);
    }

    public static void assertEmpty(final ErrorCode errorCode, final String value) {
        AssertEmpty.INSTANCE.assertEmpty(errorCode, value);
    }


    public static void assertEmpty(final Collection<?> value) {
        AssertEmpty.INSTANCE.assertEmpty(value);
    }

    public static void assertEmpty(final Supplier<String> messageProducer, final Collection<?> value) {
        AssertEmpty.INSTANCE.assertEmpty(messageProducer, value);
    }

    public static void assertEmpty(final String message, final Collection<?> value) {
        AssertEmpty.INSTANCE.assertEmpty(message, value);
    }

    public static void assertEmpty(final ErrorCode errorCode, final Collection<?> value) {
        AssertEmpty.INSTANCE.assertEmpty(errorCode, value);
    }

    public static void assertEmpty(final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertEmpty(value);
    }

    public static void assertEmpty(final String message, final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertEmpty(message, value);
    }

    public static void assertEmpty(final Supplier<String> messageProducer, final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertEmpty(messageProducer, value);
    }

    public static void assertEmpty(final ErrorCode errorCode, final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertEmpty(errorCode, value);
    }


    public static void assertEmpty(final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertEmpty(value);
    }

    public static void assertEmpty(final Supplier<String> messageProducer, final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertEmpty(messageProducer, value);
    }

    public static void assertEmpty(final String message, final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertEmpty(message, value);
    }

    public static void assertEmpty(final ErrorCode errorCode, final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertEmpty(errorCode, value);
    }


    // -------------------------------------------------------------------------
    // NULL OR EMPTY
    // -------------------------------------------------------------------------
    public static void assertNullOrEmpty(final String value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(value);
    }

    public static void assertNullOrEmpty(final String message, final String value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(message, value);
    }

    public static void assertNullOrEmpty(final Supplier<String> messageProducer, final String value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(messageProducer, value);
    }

    public static void assertNullOrEmpty(final ErrorCode errorCode, final String value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(errorCode, value);
    }


    public static void assertNullOrEmpty(final Collection<?> value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(value);
    }

    public static void assertNullOrEmpty(final Supplier<String> messageProducer, final Collection<?> value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(messageProducer, value);
    }

    public static void assertNullOrEmpty(final String message, final Collection<?> value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(message, value);
    }

    public static void assertNullOrEmpty(final ErrorCode errorCode, final Collection<?> value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(errorCode, value);
    }

    public static void assertNullOrEmpty(final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(value);
    }

    public static void assertNullOrEmpty(final String message, final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(message, value);
    }

    public static void assertNullOrEmpty(final Supplier<String> messageProducer, final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(messageProducer, value);
    }

    public static void assertNullOrEmpty(final ErrorCode errorCode, final Map<?, ?> value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(errorCode, value);
    }


    public static void assertNullOrEmpty(final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(value);
    }

    public static void assertNullOrEmpty(final Supplier<String> messageProducer, final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(messageProducer, value);
    }

    public static void assertNullOrEmpty(final String message, final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(message, value);
    }

    public static void assertNullOrEmpty(final ErrorCode errorCode, final IsEmptyFacet value) {
        AssertEmpty.INSTANCE.assertNullOrEmpty(errorCode, value);
    }

    // -------------------------------------------------------------------------
    // EQUALS
    // -------------------------------------------------------------------------


    public static void assertEquals(final Object ref, final Object value) {
        AssertEquals.INSTANCE.assertEquals(ref, value);
    }


    public static void assertEquals(final String message, final Object ref, final Object value) {
        AssertEquals.INSTANCE.assertEquals(message, ref, value);
    }

    public static void assertEquals(final Supplier<String> messageProducer, final Object ref, final Object value) {
        AssertEquals.INSTANCE.assertEquals(messageProducer, ref, value);
    }


    public static void assertEquals(final ErrorCode errorCode, final Object ref, final Object value) {
        AssertEquals.INSTANCE.assertEquals(errorCode, ref, value);
    }


    public static void assertEquals(final int ref, final int value) {
        AssertEquals.INSTANCE.assertEquals(ref, value);
    }

    public static void assertEquals(final String message, final int ref, final int value) {
        AssertEquals.INSTANCE.assertEquals(message, ref, value);
    }

    public static void assertEquals(final Supplier<String> messageProducer, final int ref, final int value) {
        AssertEquals.INSTANCE.assertEquals(messageProducer, ref, value);
    }

    public static void assertEquals(final ErrorCode errorCode, final int ref, final int value) {
        AssertEquals.INSTANCE.assertEquals(errorCode, ref, value);
    }

    public static void assertEquals(final long ref, final long value) {
        AssertEquals.INSTANCE.assertEquals(ref, value);
    }

    public static void assertEquals(final String message, final long ref, final long value) {
        AssertEquals.INSTANCE.assertEquals(message, ref, value);
    }

    public static void assertEquals(final Supplier<String> messageProducer, final long ref, final long value) {
        AssertEquals.INSTANCE.assertEquals(messageProducer, ref, value);
    }

    public static void assertEquals(final ErrorCode errorCode, final long ref, final long value) {
        AssertEquals.INSTANCE.assertEquals(errorCode, ref, value);
    }

    public static void assertEquals(final float ref, final float value) {
        AssertEquals.INSTANCE.assertEquals(ref, value);
    }

    public static void assertEquals(final String message, final float ref, final float value) {
        AssertEquals.INSTANCE.assertEquals(message, ref, value);
    }

    public static void assertEquals(final Supplier<String> messageProducer, final float ref, final float value) {
        AssertEquals.INSTANCE.assertEquals(messageProducer, ref, value);
    }

    public static void assertEquals(final ErrorCode errorCode, final float ref, final float value) {
        AssertEquals.INSTANCE.assertEquals(errorCode, ref, value);
    }

    public static void assertEquals(final double ref, final double value) {
        AssertEquals.INSTANCE.assertEquals(ref, value);
    }

    public static void assertEquals(final String message, final double ref, final double value) {
        AssertEquals.INSTANCE.assertEquals(message, ref, value);
    }

    public static void assertEquals(final Supplier<String> messageProducer, final double ref, final double value) {
        AssertEquals.INSTANCE.assertEquals(messageProducer, ref, value);
    }

    public static void assertEquals(final ErrorCode errorCode, final double ref, final double value) {
        AssertEquals.INSTANCE.assertEquals(errorCode, ref, value);
    }


    // -------------------------------------------------------------------------
    // NOT EQUALS
    // -------------------------------------------------------------------------
    public static void assertNotEquals(final Object ref, final Object value) {
        AssertNotEquals.INSTANCE.assertNotEquals(ref, value);
    }

    public static void assertNotEquals(final String message, final Object ref, final Object value) {
        AssertNotEquals.INSTANCE.assertNotEquals(message, ref, value);
    }

    public static void assertNotEquals(final Supplier<String> messageProducer, final Object ref, final Object value) {
        AssertNotEquals.INSTANCE.assertNotEquals(messageProducer, ref, value);
    }

    public static void assertNotEquals(final ErrorCode errorCode, final Object ref, final Object value) {
        AssertNotEquals.INSTANCE.assertNotEquals(errorCode, ref, value);
    }

    public static void assertNotEquals(final int ref, final int value) {
        AssertNotEquals.INSTANCE.assertNotEquals(ref, value);
    }

    public static void assertNotEquals(final String message, final int ref, final int value) {
        AssertNotEquals.INSTANCE.assertNotEquals(message, ref, value);
    }


    public static void assertNotEquals(final Supplier<String> messageProducer, final int ref, final int value) {
        AssertNotEquals.INSTANCE.assertNotEquals(messageProducer, ref, value);
    }


    public static void assertNotEquals(final ErrorCode errorCode, final int ref, final int value) {
        AssertNotEquals.INSTANCE.assertNotEquals(errorCode, ref, value);
    }

    public static void assertNotEquals(final long ref, final long value) {
        AssertNotEquals.INSTANCE.assertNotEquals(ref, value);
    }

    public static void assertNotEquals(final String message, final long ref, final long value) {
        AssertNotEquals.INSTANCE.assertNotEquals(message, ref, value);
    }


    public static void assertNotEquals(final Supplier<String> messageProducer, final long ref, final long value) {
        AssertNotEquals.INSTANCE.assertNotEquals(messageProducer, ref, value);
    }


    public static void assertNotEquals(final ErrorCode errorCode, final long ref, final long value) {
        AssertNotEquals.INSTANCE.assertNotEquals(errorCode, ref, value);
    }

    public static void assertNotEquals(final float ref, final float value) {
        AssertNotEquals.INSTANCE.assertNotEquals(ref, value);
    }

    public static void assertNotEquals(final String message, final float ref, final float value) {
        AssertNotEquals.INSTANCE.assertNotEquals(message, ref, value);
    }


    public static void assertNotEquals(final Supplier<String> messageProducer, final float ref, final float value) {
        AssertNotEquals.INSTANCE.assertNotEquals(messageProducer, ref, value);
    }


    public static void assertNotEquals(final ErrorCode errorCode, final float ref, final float value) {
        AssertNotEquals.INSTANCE.assertNotEquals(errorCode, ref, value);
    }

    public static void assertNotEquals(final double ref, final double value) {
        AssertNotEquals.INSTANCE.assertNotEquals(ref, value);
    }

    public static void assertNotEquals(final String message, final double ref, final double value) {
        AssertNotEquals.INSTANCE.assertNotEquals(message, ref, value);
    }


    public static void assertNotEquals(final Supplier<String> messageProducer, final double ref, final double value) {
        AssertNotEquals.INSTANCE.assertNotEquals(messageProducer, ref, value);
    }


    public static void assertNotEquals(final ErrorCode errorCode, final double ref, final double value) {
        AssertNotEquals.INSTANCE.assertNotEquals(errorCode, ref, value);
    }


    // -------------------------------------------------------------------------
    // LOWER
    // -------------------------------------------------------------------------
    public static void assertLower(final int ref, final int value) {
        AssertLower.INSTANCE.assertLower(ref, value);
    }

    public static void assertLower(final String message, final int ref, final int value) {
        AssertLower.INSTANCE.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer, final int ref, final int value) {
        AssertLower.INSTANCE.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode, final int ref, final int value) {
        AssertLower.INSTANCE.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final long ref, final long value) {
        AssertLower.INSTANCE.assertLower(ref, value);
    }

    public static void assertLower(final String message, final long ref, final long value) {
        AssertLower.INSTANCE.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer, final long ref, final long value) {
        AssertLower.INSTANCE.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode, final long ref, final long value) {
        AssertLower.INSTANCE.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final float ref, final float value) {
        AssertLower.INSTANCE.assertLower(ref, value);
    }

    public static void assertLower(final String message, final float ref, final float value) {
        AssertLower.INSTANCE.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer, final float ref, final float value) {
        AssertLower.INSTANCE.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode, final float ref, final float value) {
        AssertLower.INSTANCE.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final double ref, final double value) {
        AssertLower.INSTANCE.assertLower(ref, value);
    }

    public static void assertLower(final String message, final double ref, final double value) {
        AssertLower.INSTANCE.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer, final double ref, final double value) {
        AssertLower.INSTANCE.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode, final double ref, final double value) {
        AssertLower.INSTANCE.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final Integer ref, final Integer value) {
        AssertLower.INSTANCE.assertLower(ref, value);
    }

    public static void assertLower(final String message, final Integer ref, final Integer value) {

        AssertLower.INSTANCE.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer, final Integer ref, final Integer value) {
        AssertLower.INSTANCE.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode, final Integer ref, final Integer value) {
        AssertLower.INSTANCE.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final Long ref, final Long value) {
        AssertLower.INSTANCE.assertLower(ref, value);
    }

    public static void assertLower(final String message, final Long ref, final Long value) {
        AssertLower.INSTANCE.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer, final Long ref, final Long value) {
        AssertLower.INSTANCE.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode, final Long ref, final Long value) {
        AssertLower.INSTANCE.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final Double ref, final Double value) {
        AssertLower.INSTANCE.assertLower(ref, value);
    }

    public static void assertLower(final String message, final Double ref, final Double value) {
        AssertLower.INSTANCE.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer, final Double ref, final Double value) {
        AssertLower.INSTANCE.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode, final Double ref, final Double value) {
        AssertLower.INSTANCE.assertLower(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLower(final BigDecimal ref, final BigDecimal value) {
        AssertLower.INSTANCE.assertLower(ref, value);
    }

    public static void assertLower(final String message, final BigDecimal ref, final BigDecimal value) {
        AssertLower.INSTANCE.assertLower(message, ref, value);
    }

    public static void assertLower(final Supplier<String> messageProducer,
                                   final BigDecimal ref,
                                   final BigDecimal value) {
        AssertLower.INSTANCE.assertLower(messageProducer, ref, value);
    }

    public static void assertLower(final ErrorCode errorCode, final BigDecimal ref, final BigDecimal value) {
        AssertLower.INSTANCE.assertLower(errorCode, ref, value);
    }


    // -------------------------------------------------------------------------
    // LOWER OR EQUALS
    // -------------------------------------------------------------------------
    public static void assertLowerOrEquals(final int ref, final int value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message, final int ref, final int value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer, final int ref, final int value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode, final int ref, final int value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final long ref, final long value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message, final long ref, final long value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer, final long ref, final long value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode, final long ref, final long value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final float ref, final float value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message, final float ref, final float value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer, final float ref, final float value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode, final float ref, final float value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final double ref, final double value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message, final double ref, final double value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final double ref,
                                           final double value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode, final double ref, final double value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final Integer ref, final Integer value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message, final Integer ref, final Integer value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final Integer ref,
                                           final Integer value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode, final Integer ref, final Integer value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final Long ref, final Long value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message, final Long ref, final Long value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer, final Long ref, final Long value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode, final Long ref, final Long value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final Double ref, final Double value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message, final Double ref, final Double value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final Double ref,
                                           final Double value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode, final Double ref, final Double value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertLowerOrEquals(final BigDecimal ref, final BigDecimal value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(ref, value);
    }

    public static void assertLowerOrEquals(final String message, final BigDecimal ref, final BigDecimal value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(message, ref, value);
    }

    public static void assertLowerOrEquals(final Supplier<String> messageProducer,
                                           final BigDecimal ref,
                                           final BigDecimal value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(messageProducer, ref, value);
    }

    public static void assertLowerOrEquals(final ErrorCode errorCode, final BigDecimal ref, final BigDecimal value) {
        AssertLowerOrEquals.INSTANCE.assertLowerOrEquals(errorCode, ref, value);
    }

    // -------------------------------------------------------------------------
    // HIGHER
    // -------------------------------------------------------------------------
    public static void assertHigher(final int ref, final int value) {
        AssertHigher.INSTANCE.assertHigher(ref, value);
    }

    public static void assertHigher(final String message, final int ref, final int value) {
        AssertHigher.INSTANCE.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer, final int ref, final int value) {
        AssertHigher.INSTANCE.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode, final int ref, final int value) {
        AssertHigher.INSTANCE.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final long ref, final long value) {
        AssertHigher.INSTANCE.assertHigher(ref, value);
    }

    public static void assertHigher(final String message, final long ref, final long value) {
        AssertHigher.INSTANCE.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer, final long ref, final long value) {
        AssertHigher.INSTANCE.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode, final long ref, final long value) {
        AssertHigher.INSTANCE.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final float ref, final float value) {
        AssertHigher.INSTANCE.assertHigher(ref, value);
    }

    public static void assertHigher(final String message, final float ref, final float value) {
        AssertHigher.INSTANCE.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer, final float ref, final float value) {
        AssertHigher.INSTANCE.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode, final float ref, final float value) {
        AssertHigher.INSTANCE.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final double ref, final double value) {
        AssertHigher.INSTANCE.assertHigher(ref, value);
    }

    public static void assertHigher(final String message, final double ref, final double value) {
        AssertHigher.INSTANCE.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer, final double ref, final double value) {
        AssertHigher.INSTANCE.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode, final double ref, final double value) {
        AssertHigher.INSTANCE.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final Integer ref, final Integer value) {
        AssertHigher.INSTANCE.assertHigher(ref, value);
    }

    public static void assertHigher(final String message, final Integer ref, final Integer value) {
        AssertHigher.INSTANCE.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer, final Integer ref, final Integer value) {
        AssertHigher.INSTANCE.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode, final Integer ref, final Integer value) {
        AssertHigher.INSTANCE.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final Long ref, final Long value) {
        AssertHigher.INSTANCE.assertHigher(ref, value);
    }

    public static void assertHigher(final String message, final Long ref, final Long value) {
        AssertHigher.INSTANCE.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer, final Long ref, final Long value) {
        AssertHigher.INSTANCE.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode, final Long ref, final Long value) {
        AssertHigher.INSTANCE.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final Double ref, final Double value) {
        AssertHigher.INSTANCE.assertHigher(ref, value);
    }

    public static void assertHigher(final String message, final Double ref, final Double value) {
        AssertHigher.INSTANCE.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer, final Double ref, final Double value) {
        AssertHigher.INSTANCE.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode, final Double ref, final Double value) {
        AssertHigher.INSTANCE.assertHigher(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final BigDecimal ref, final BigDecimal value) {
        AssertHigher.INSTANCE.assertHigher(ref, value);
    }

    public static void assertHigher(final String message, final BigDecimal ref, final BigDecimal value) {
        AssertHigher.INSTANCE.assertHigher(message, ref, value);
    }

    public static void assertHigher(final Supplier<String> messageProducer,
                                    final BigDecimal ref,
                                    final BigDecimal value) {
        AssertHigher.INSTANCE.assertHigher(messageProducer, ref, value);
    }

    public static void assertHigher(final ErrorCode errorCode, final BigDecimal ref, final BigDecimal value) {
        AssertHigher.INSTANCE.assertHigher(errorCode, ref, value);
    }


    // -------------------------------------------------------------------------
    // assertHigherOrEquals
    // -------------------------------------------------------------------------
    public static void assertHigherOrEquals(final int ref, final int value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message, final int ref, final int value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer, final int ref, final int value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode, final int ref, final int value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final long ref, final long value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message, final long ref, final long value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer, final long ref, final long value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode, final long ref, final long value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final float ref, final float value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message, final float ref, final float value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final float ref,
                                            final float value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode, final float ref, final float value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final double ref, final double value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message, final double ref, final double value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final double ref,
                                            final double value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode, final double ref, final double value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final Integer ref, final Integer value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final Integer ref,
                                            final Integer value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final String message, final Integer ref, final Integer value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode, final Integer ref, final Integer value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final Long ref, final Long value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message, final Long ref, final Long value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer, final Long ref, final Long value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode, final Long ref, final Long value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final Double ref, final Double value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message, final Double ref, final Double value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final Double ref,
                                            final Double value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode, final Double ref, final Double value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(errorCode, ref, value);
    }

    // ------------------------------------------------------------------------
    public static void assertHigherOrEquals(final BigDecimal ref, final BigDecimal value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(ref, value);
    }

    public static void assertHigherOrEquals(final String message, final BigDecimal ref, final BigDecimal value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(message, ref, value);
    }

    public static void assertHigherOrEquals(final Supplier<String> messageProducer,
                                            final BigDecimal ref,
                                            final BigDecimal value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(messageProducer, ref, value);
    }

    public static void assertHigherOrEquals(final ErrorCode errorCode, final BigDecimal ref, final BigDecimal value) {
        AssertHigherOrEquals.INSTANCE.assertHigherOrEquals(errorCode, ref, value);
    }


    // -------------------------------------------------------------------------
    // REGEX
    // -------------------------------------------------------------------------
    public static void assertRegexMatch(final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexMatch(regex, value);
    }

    public static void assertRegexMatch(final String message, final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexMatch(message, regex, value);
    }

    public static void assertRegexMatch(final Supplier<String> messageProducer,
                                        final Pattern regex,
                                        final String value) {
        AssertRegex.INSTANCE.assertRegexMatch(messageProducer, regex, value);
    }

    public static void assertRegexMatch(final ErrorCode errorCode, final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexMatch(errorCode, regex, value);
    }


    public static void assertRegexMatch(final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexMatch(regex, value);
    }

    public static void assertRegexMatch(final String message, final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexMatch(message, regex, value);
    }


    public static void assertRegexMatch(final Supplier<String> messageProducer,
                                        final String regex,
                                        final String value) {
        AssertRegex.INSTANCE.assertRegexMatch(messageProducer, regex, value);
    }

    public static void assertRegexMatch(final ErrorCode errorCode, final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexMatch(errorCode, regex, value);
    }


    public static void assertRegexNotMatch(final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotMatch(regex, value);
    }

    public static void assertRegexNotMatch(final String message, final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotMatch(message, regex, value);
    }

    public static void assertRegexNotMatch(final Supplier<String> messageProducer,
                                           final Pattern regex,
                                           final String value) {
        AssertRegex.INSTANCE.assertRegexNotMatch(messageProducer, regex, value);
    }

    public static void assertRegexNotMatch(final ErrorCode errorCode, final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotMatch(errorCode, regex, value);
    }


    public static void assertRegexNotMatch(final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotMatch(regex, value);
    }

    public static void assertRegexNotMatch(final String message, final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotMatch(message, regex, value);
    }


    public static void assertRegexNotMatch(final Supplier<String> messageProducer,
                                           final String regex,
                                           final String value) {
        AssertRegex.INSTANCE.assertRegexNotMatch(messageProducer, regex, value);
    }

    public static void assertRegexNotMatch(final ErrorCode errorCode, final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotMatch(errorCode, regex, value);
    }

    public static void assertRegexFind(final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexFind(regex, value);
    }

    public static void assertRegexFind(final String message, final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexFind(message, regex, value);
    }

    public static void assertRegexFind(final Supplier<String> messageProducer,
                                       final Pattern regex,
                                       final String value) {
        AssertRegex.INSTANCE.assertRegexFind(messageProducer, regex, value);
    }

    public static void assertRegexFind(final ErrorCode errorCode, final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexFind(errorCode, regex, value);
    }


    public static void assertRegexFind(final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexFind(regex, value);
    }

    public static void assertRegexFind(final String message, final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexFind(message, regex, value);
    }


    public static void assertRegexFind(final Supplier<String> messageProducer, final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexFind(messageProducer, regex, value);
    }

    public static void assertRegexFind(final ErrorCode errorCode, final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexFind(errorCode, regex, value);
    }

    public static void assertRegexNotFind(final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotFind(regex, value);
    }

    public static void assertRegexNotFind(final String message, final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotFind(message, regex, value);
    }

    public static void assertRegexNotFind(final Supplier<String> messageProducer,
                                          final Pattern regex,
                                          final String value) {
        AssertRegex.INSTANCE.assertRegexNotFind(messageProducer, regex, value);
    }

    public static void assertRegexNotFind(final ErrorCode errorCode, final Pattern regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotFind(errorCode, regex, value);
    }


    public static void assertRegexNotFind(final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotFind(regex, value);
    }

    public static void assertRegexNotFind(final String message, final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotFind(message, regex, value);
    }


    public static void assertRegexNotFind(final Supplier<String> messageProducer,
                                          final String regex,
                                          final String value) {
        AssertRegex.INSTANCE.assertRegexNotFind(messageProducer, regex, value);
    }

    public static void assertRegexNotFind(final ErrorCode errorCode, final String regex, final String value) {
        AssertRegex.INSTANCE.assertRegexNotFind(errorCode, regex, value);
    }


    public static List<ErrorCode> checkModel(final VoidFunctionWithException... assertions) {
        return AssertModel.INSTANCE.checkModel(assertions);
    }

    public static List<ErrorCode> checkModel(final List<VoidFunctionWithException> assertions) {
        return AssertModel.INSTANCE.checkModel(assertions);
    }


    public static void assertModel(final VoidFunctionWithException... assertions) {
        AssertModel.INSTANCE.assertModel(assertions);
    }

    public static void assertModel(final List<VoidFunctionWithException> assertions) {
        AssertModel.INSTANCE.assertModel(assertions);
    }


    // -------------------------------------------------------------------------
    // Assert throws
    // -------------------------------------------------------------------------
    @SuppressWarnings({"java:S108"})
    public static void assertThrow(final String message, final VoidFunctionWithException function) {
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
        AssertFile.INSTANCE.assertFileExists(path);
    }

    public static void assertFileExists(final String message, final File path) {
        AssertFile.INSTANCE.assertFileExists(message, path);
    }

    public static void assertFileExists(final ErrorCode errorCode, final File path) {
        AssertFile.INSTANCE.assertFileExists(errorCode, path);
    }

    public static void assertFileReadable(final File path) {
        AssertFile.INSTANCE.assertFileReadable(path);
    }

    public static void assertFileReadable(final String message, final File path) {
        AssertFile.INSTANCE.assertFileReadable(message, path);
    }

    public static void assertFileReadable(final ErrorCode errorCode, final File path) {
        AssertFile.INSTANCE.assertFileReadable(errorCode, path);
    }

    public static void assertFileWrite(final File path) {
        AssertFile.INSTANCE.assertFileWrite(path);
    }

    public static void assertFileWrite(final String message, final File path) {
        AssertFile.INSTANCE.assertFileWrite(message, path);
    }

    public static void assertFileWrite(final ErrorCode errorCode, final File path) {
        AssertFile.INSTANCE.assertFileWrite(errorCode, path);
    }

    public static void assertFileExecutable(final File path) {
        AssertFile.INSTANCE.assertFileExecutable(path);
    }

    public static void assertFileExecutable(final String message, final File path) {
        AssertFile.INSTANCE.assertFileExecutable(message, path);
    }

    public static void assertFileExecutable(final ErrorCode errorCode, final File path) {
        AssertFile.INSTANCE.assertFileExecutable(errorCode, path);
    }

    // -------------------------------------------------------------------------
    // Assert folder
    // -------------------------------------------------------------------------
    public static void assertFolderExists(final File path) {
        AssertFile.INSTANCE.assertFolderExists(path);
    }

    public static void assertFolderExists(final String message, final File path) {
        AssertFile.INSTANCE.assertFolderExists(message, path);
    }

    public static void assertFolderExists(final ErrorCode errorCode, final File path) {
        AssertFile.INSTANCE.assertFolderExists(errorCode, path);
    }


    // -------------------------------------------------------------------------
    // ASSERT LOCALDATE
    // -------------------------------------------------------------------------
    public static void assertBefore(final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertBefore(ref, value);
    }

    public static void assertBefore(final String message, final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertBefore(message, ref, value);
    }

    public static void assertBefore(final Supplier<String> messageProducer,
                                    final ChronoLocalDate ref,
                                    final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertBefore(messageProducer, ref, value);
    }

    public static void assertBefore(final ErrorCode errorCode, final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertBefore(errorCode, ref, value);
    }

    public static void assertAfter(final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertAfter(ref, value);
    }

    public static void assertAfter(final String message, final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertAfter(message, ref, value);
    }

    public static void assertAfter(final Supplier<String> messageProducer,
                                   final ChronoLocalDate ref,
                                   final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertAfter(messageProducer, ref, value);
    }

    public static void assertAfter(final ErrorCode errorCode, final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertAfter(errorCode, ref, value);
    }

    public static void assertEquals(final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertEquals(ref, value);
    }

    public static void assertEquals(final String message, final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertEquals(message, ref, value);
    }

    public static void assertEquals(final Supplier<String> messageProducer,
                                    final ChronoLocalDate ref,
                                    final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertEquals(messageProducer, ref, value);
    }

    public static void assertEquals(final ErrorCode errorCode, final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertLocalDate.INSTANCE.assertEquals(errorCode, ref, value);
    }


    // -------------------------------------------------------------------------
    // ASSERT LOCALDATETIME
    // -------------------------------------------------------------------------
    public static void assertBefore(final ChronoLocalDateTime<LocalDate> ref,
                                    final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertBefore(ref, value);
    }

    public static void assertBefore(final String message,
                                    final ChronoLocalDateTime<LocalDate> ref,
                                    final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertBefore(message, ref, value);
    }


    public static void assertBefore(final Supplier<String> messageProducer,
                                    final ChronoLocalDateTime<LocalDate> ref,
                                    final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertBefore(messageProducer, ref, value);
    }

    public static void assertBefore(final ErrorCode errorCode,
                                    final ChronoLocalDateTime<LocalDate> ref,
                                    final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertBefore(errorCode, ref, value);
    }


    public static void assertAfter(final ChronoLocalDateTime<LocalDate> ref,
                                   final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertAfter(ref, value);
    }

    public static void assertAfter(final String message,
                                   final ChronoLocalDateTime<LocalDate> ref,
                                   final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertAfter(message, ref, value);
    }


    public static void assertAfter(final Supplier<String> messageProducer,
                                   final ChronoLocalDateTime<LocalDate> ref,
                                   final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertAfter(messageProducer, ref, value);
    }


    public static void assertAfter(final ErrorCode errorCode,
                                   final ChronoLocalDateTime<LocalDate> ref,
                                   final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertAfter(errorCode, ref, value);
    }


    public static void assertEquals(final ChronoLocalDateTime<LocalDate> ref,
                                    final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertEquals(ref, value);
    }

    public static void assertEquals(final String message,
                                    final ChronoLocalDateTime<LocalDate> ref,
                                    final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertEquals(message, ref, value);
    }


    public static void assertEquals(final Supplier<String> messageProducer,
                                    final ChronoLocalDateTime<LocalDate> ref,
                                    final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertEquals(messageProducer, ref, value);
    }


    public static void assertEquals(final ErrorCode errorCode,
                                    final ChronoLocalDateTime<LocalDate> ref,
                                    final ChronoLocalDateTime<LocalDate> value) {
        AssertLocalDateTime.INSTANCE.assertEquals(errorCode, ref, value);
    }

    // -------------------------------------------------------------------------
    // WRAP ERROR
    // -------------------------------------------------------------------------
    public static <T> T wrapErrorForSupplierWithException(final ActionWithException action, final ErrorCode errorCode) {
        return wrapErrorForSupplierWithException(action, errorCode, null);
    }

    public static <T> T wrapErrorForSupplierWithException(final ActionWithException action,
                                                          final ErrorCodeResolver resolver) {
        return wrapErrorForSupplierWithException(action, null, resolver);
    }


    public static <T> T wrapErrorForSupplierWithException(final ActionWithException action,
                                                          final ErrorCode errorCode,
                                                          final ErrorCodeResolver resolver) {
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


    public static <T> T wrapErrorForSupplier(final Supplier<T> action,
                                             final ErrorCode errorCode,
                                             final ErrorCodeResolver resolver) {
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


    public static void wrapErrorForVoidFunction(final VoidFunction action,
                                                final ErrorCode errorCode,
                                                final ErrorCodeResolver resolver) {
        assertNotNull(action);
        try {
            action.process();
        } catch (final Throwable e) {
            throw handlerException(errorCode, resolver, e);
        }
    }


    public static void wrapErrorForVoidFunctionWithException(final VoidFunctionWithException action,
                                                             final ErrorCode errorCode) {
        wrapErrorForVoidFunctionWithException(action, errorCode, null);
    }

    public static void wrapErrorForVoidFunctionWithException(final VoidFunctionWithException action,
                                                             final ErrorCodeResolver resolver) {
        wrapErrorForVoidFunctionWithException(action, null, resolver);
    }


    public static void wrapErrorForVoidFunctionWithException(final VoidFunctionWithException action,
                                                             final ErrorCode errorCode,
                                                             final ErrorCodeResolver resolver) {
        assertNotNull(action);
        try {
            action.process();
        } catch (final Throwable e) {
            throw handlerException(errorCode, resolver, e);
        }
    }

    private static ErrorCode resolveErrorCode(final Throwable exception,
                                              final ErrorCode errorCode,
                                              final ErrorCodeResolver resolver) {
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

    private static UncheckedException handlerException(final ErrorCode errorCode,
                                                       final ErrorCodeResolver resolver,
                                                       final Throwable e) {
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
        AssertCommons.INSTANCE.throwException(null, message);
    }

    public static void throwException(final ErrorCode errorCode) {
        AssertCommons.INSTANCE.throwException(errorCode);
    }

    public static void throwException(final ErrorCode errorCode, final String message, final Serializable... args) {
        AssertCommons.INSTANCE.throwException(errorCode, MessagesFormatter.format(message, args));
    }


}
