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

import io.inugami.api.exceptions.ErrorCode;

import java.time.chrono.ChronoLocalDate;
import java.util.function.Supplier;

public class AssertLocalDate {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String OBJECT_REF_NULL                          = "Reference value is required";
    public static final String OBJECT_NULL                              = "Value is required";
    public static final String CURRENT_DATE_IS_AFTER_THE_REFERENCE      = "Current date is after the reference";
    public static final String CURRENT_DATE_IS_BEFORE_THE_REFERENCE     = "Current date is before the reference";
    public static final String CURRENT_DATE_ISN_T_EQUALS_WITH_REFERENCE = "Current date isn't equals with reference";

    public static final AssertLocalDate INSTANCE = new AssertLocalDate();

    // =================================================================================================================
    // ASSERT BEFORE
    // =================================================================================================================
    public void assertBefore(final ChronoLocalDate ref, final ChronoLocalDate value) {
        assertBefore(CURRENT_DATE_IS_AFTER_THE_REFERENCE, ref, value);
    }

    public void assertBefore(final String message, final ChronoLocalDate ref, final ChronoLocalDate value) {
        if (isAfter(ref, value)) {
            final String msg = message == null ? CURRENT_DATE_IS_AFTER_THE_REFERENCE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }


    public void assertBefore(final Supplier<String> messageProducer,
                             final ChronoLocalDate ref,
                             final ChronoLocalDate value) {
        if (isAfter(ref, value)) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? CURRENT_DATE_IS_AFTER_THE_REFERENCE : messageProducer.get());
        }
    }

    public void assertBefore(final ErrorCode errorCode, final ChronoLocalDate ref, final ChronoLocalDate value) {
        if (isAfter(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    private boolean isBefore(final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertNull.INSTANCE.assertNotNull(OBJECT_REF_NULL, ref);
        AssertNull.INSTANCE.assertNotNull(OBJECT_NULL, value);
        return ref.isAfter(value);
    }

    // =================================================================================================================
    // ASSERT AFTER
    // =================================================================================================================
    public void assertAfter(final ChronoLocalDate ref, final ChronoLocalDate value) {
        assertAfter(CURRENT_DATE_IS_BEFORE_THE_REFERENCE, ref, value);
    }

    public void assertAfter(final String message, final ChronoLocalDate ref, final ChronoLocalDate value) {
        if (isBefore(ref, value)) {
            final String msg = message == null ? CURRENT_DATE_IS_BEFORE_THE_REFERENCE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }


    public void assertAfter(final Supplier<String> messageProducer,
                            final ChronoLocalDate ref,
                            final ChronoLocalDate value) {
        if (isBefore(ref, value)) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? CURRENT_DATE_IS_BEFORE_THE_REFERENCE : messageProducer.get());
        }
    }

    public void assertAfter(final ErrorCode errorCode, final ChronoLocalDate ref, final ChronoLocalDate value) {
        if (isBefore(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    private static boolean isAfter(final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertNull.INSTANCE.assertNotNull(OBJECT_REF_NULL, ref);
        AssertNull.INSTANCE.assertNotNull(OBJECT_NULL, value);
        return ref.isBefore(value);
    }

    // =================================================================================================================
    // ASSERT EQUALS
    // =================================================================================================================
    public void assertEquals(final ChronoLocalDate ref, final ChronoLocalDate value) {
        assertEquals(CURRENT_DATE_ISN_T_EQUALS_WITH_REFERENCE, ref, value);
    }

    public void assertEquals(final String message, final ChronoLocalDate ref, final ChronoLocalDate value) {
        if (!isEquals(ref, value)) {
            final String msg = message == null ? CURRENT_DATE_ISN_T_EQUALS_WITH_REFERENCE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }


    public void assertEquals(final Supplier<String> messageProducer,
                             final ChronoLocalDate ref,
                             final ChronoLocalDate value) {
        if (!isEquals(ref, value)) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? CURRENT_DATE_ISN_T_EQUALS_WITH_REFERENCE : messageProducer.get());
        }
    }

    public void assertEquals(final ErrorCode errorCode, final ChronoLocalDate ref, final ChronoLocalDate value) {
        if (!isEquals(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    private static boolean isEquals(final ChronoLocalDate ref, final ChronoLocalDate value) {
        AssertNull.INSTANCE.assertNotNull(OBJECT_REF_NULL, ref);
        AssertNull.INSTANCE.assertNotNull(OBJECT_NULL, value);
        return ref.isEqual(value);
    }

}
