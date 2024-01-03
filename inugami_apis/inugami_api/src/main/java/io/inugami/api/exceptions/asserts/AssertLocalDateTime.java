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
package io.inugami.api.exceptions.asserts;

import io.inugami.interfaces.exceptions.ErrorCode;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.function.Supplier;

public class AssertLocalDateTime {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String              OBJECT_REF_NULL                          = "Reference value is required";
    public static final String              OBJECT_NULL                              = "Value is required";
    public static final String              CURRENT_DATE_IS_AFTER_THE_REFERENCE      = "Current date is after the reference";
    public static final String              CURRENT_DATE_IS_BEFORE_THE_REFERENCE     = "Current date is before the reference";
    public static final String              CURRENT_DATE_ISN_T_EQUALS_WITH_REFERENCE = "Current date isn't equals with reference";
    public static final AssertLocalDateTime INSTANCE                                 = new AssertLocalDateTime();

    // =================================================================================================================
    // ASSERT BEFORE
    // =================================================================================================================
    public void assertBefore(final ChronoLocalDateTime<LocalDate> ref, final ChronoLocalDateTime<LocalDate> value) {
        assertBefore(CURRENT_DATE_IS_AFTER_THE_REFERENCE, ref, value);
    }

    public void assertBefore(final String message,
                             final ChronoLocalDateTime<LocalDate> ref,
                             final ChronoLocalDateTime<LocalDate> value) {
        if (isAfter(ref, value)) {
            final String msg = message == null ? CURRENT_DATE_IS_AFTER_THE_REFERENCE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }


    public void assertBefore(final Supplier<String> messageProducer,
                             final ChronoLocalDateTime<LocalDate> ref,
                             final ChronoLocalDateTime<LocalDate> value) {
        if (isAfter(ref, value)) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? CURRENT_DATE_IS_AFTER_THE_REFERENCE : messageProducer.get());
        }
    }

    public void assertBefore(final ErrorCode errorCode,
                             final ChronoLocalDateTime<LocalDate> ref,
                             final ChronoLocalDateTime<LocalDate> value) {
        if (isAfter(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    private boolean isBefore(final ChronoLocalDateTime<LocalDate> ref, final ChronoLocalDateTime<LocalDate> value) {
        AssertNull.INSTANCE.assertNotNull(OBJECT_REF_NULL, ref);
        AssertNull.INSTANCE.assertNotNull(OBJECT_NULL, value);
        return ref.isAfter(value);
    }

    // =================================================================================================================
    // ASSERT AFTER
    // =================================================================================================================
    public void assertAfter(final ChronoLocalDateTime<LocalDate> ref, final ChronoLocalDateTime<LocalDate> value) {
        assertAfter(CURRENT_DATE_IS_BEFORE_THE_REFERENCE, ref, value);
    }

    public void assertAfter(final String message,
                            final ChronoLocalDateTime<LocalDate> ref,
                            final ChronoLocalDateTime<LocalDate> value) {
        if (isBefore(ref, value)) {
            final String msg = message == null ? CURRENT_DATE_IS_BEFORE_THE_REFERENCE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }


    public void assertAfter(final Supplier<String> messageProducer,
                            final ChronoLocalDateTime<LocalDate> ref,
                            final ChronoLocalDateTime<LocalDate> value) {
        if (isBefore(ref, value)) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? CURRENT_DATE_IS_BEFORE_THE_REFERENCE : messageProducer.get());
        }
    }

    public void assertAfter(final ErrorCode errorCode,
                            final ChronoLocalDateTime<LocalDate> ref,
                            final ChronoLocalDateTime<LocalDate> value) {
        if (isBefore(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    private static boolean isAfter(final ChronoLocalDateTime<LocalDate> ref,
                                   final ChronoLocalDateTime<LocalDate> value) {
        AssertNull.INSTANCE.assertNotNull(OBJECT_REF_NULL, ref);
        AssertNull.INSTANCE.assertNotNull(OBJECT_NULL, value);
        return ref.isBefore(value);
    }

    // =================================================================================================================
    // ASSERT EQUALS
    // =================================================================================================================
    public void assertEquals(final ChronoLocalDateTime<LocalDate> ref, final ChronoLocalDateTime<LocalDate> value) {
        assertEquals(CURRENT_DATE_ISN_T_EQUALS_WITH_REFERENCE, ref, value);
    }

    public void assertEquals(final String message,
                             final ChronoLocalDateTime<LocalDate> ref,
                             final ChronoLocalDateTime<LocalDate> value) {
        if (!isEquals(ref, value)) {
            final String msg = message == null ? CURRENT_DATE_ISN_T_EQUALS_WITH_REFERENCE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }


    public void assertEquals(final Supplier<String> messageProducer,
                             final ChronoLocalDateTime<LocalDate> ref,
                             final ChronoLocalDateTime<LocalDate> value) {
        if (!isEquals(ref, value)) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? CURRENT_DATE_ISN_T_EQUALS_WITH_REFERENCE : messageProducer.get());
        }
    }

    public void assertEquals(final ErrorCode errorCode,
                             final ChronoLocalDateTime<LocalDate> ref,
                             final ChronoLocalDateTime<LocalDate> value) {
        if (!isEquals(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    private static boolean isEquals(final ChronoLocalDateTime<LocalDate> ref,
                                    final ChronoLocalDateTime<LocalDate> value) {
        AssertNull.INSTANCE.assertNotNull(OBJECT_REF_NULL, ref);
        AssertNull.INSTANCE.assertNotNull(OBJECT_NULL, value);
        return ref.isEqual(value);
    }

}
