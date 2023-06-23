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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Asserts
 *
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Checks {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final BiFunction<ErrorCode, String, CheckedException> EXCEPTION_BUILDER = CheckedException::new;


    // -------------------------------------------------------------------------
    // IS TRUE
    // -------------------------------------------------------------------------
    public static void isTrue(final boolean expression) throws CheckedException {
        isTrue("this expression must be true",
               expression);
    }

    public static void isTrue(final String message,
                              final boolean expression) throws CheckedException {
        if (!expression) {
            throwException(message, EXCEPTION_BUILDER);
        }
    }


    public static void isTrue(final ErrorCode errorCode,
                              final boolean expression) throws CheckedException {
        if (!expression) {
            throwException(errorCode, EXCEPTION_BUILDER);
        }
    }


    public static <E extends CheckedException> void isTrue(final boolean expression,
                                                           final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        isTrue("this expression must be true",
               expression,
               errorBuilder);
    }

    public static <E extends CheckedException> void isTrue(final String message,
                                                           final boolean expression,
                                                           final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        if (!expression) {
            throwException(message, errorBuilder);
        }
    }


    public static <E extends CheckedException> void isTrue(final ErrorCode errorCode,
                                                           final boolean expression,
                                                           final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        if (!expression) {
            throwException(errorCode, errorBuilder);
        }
    }

    // -------------------------------------------------------------------------
    // IS FALSE
    // -------------------------------------------------------------------------
    public static void isFalse(final boolean expression) throws CheckedException {
        isFalse("this expression must be true",
                expression);
    }

    public static void isFalse(final String message,
                               final boolean expression) throws CheckedException {
        if (expression) {
            throwException(message, EXCEPTION_BUILDER);
        }
    }

    public static void isFalse(final ErrorCode errorCode,
                               final boolean expression) throws CheckedException {
        if (expression) {
            throwException(errorCode, EXCEPTION_BUILDER);
        }
    }


    public static <E extends CheckedException> void isFalse(final boolean expression,
                                                            final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        isFalse("this expression must be true",
                expression,
                errorBuilder);
    }

    public static <E extends CheckedException> void isFalse(final String message,
                                                            final boolean expression,
                                                            final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        if (expression) {
            throwException(message, errorBuilder);
        }
    }

    public static <E extends CheckedException> void isFalse(final ErrorCode errorCode,
                                                            final boolean expression,
                                                            final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        if (expression) {
            throwException(errorCode, errorBuilder);
        }
    }

    // -------------------------------------------------------------------------
    // IS NULL
    // -------------------------------------------------------------------------
    public static void isNull(final Object... objects) throws CheckedException {
        isNull("objects arguments must be null",
               objects);
    }

    public static void isNull(final String message,
                              final Object... values) throws CheckedException {
        for (final Object item : values) {
            if (item != null) {
                throwException(message, EXCEPTION_BUILDER);
            }
        }
    }

    public static void isNull(final ErrorCode errorCode,
                              final Object... values) throws CheckedException {
        for (final Object item : values) {
            if (item != null) {
                throwException(errorCode, EXCEPTION_BUILDER);
            }
        }
    }


    public static <E extends CheckedException> void isNull(final BiFunction<ErrorCode, String, E> errorBuilder,
                                                           final Object... objects) throws E {
        isNull("objects arguments must be null",
               errorBuilder,
               objects);
    }

    public static <E extends CheckedException> void isNull(final String message,
                                                           final BiFunction<ErrorCode, String, E> errorBuilder,
                                                           final Object... values) throws E {
        for (final Object item : values) {
            if (item != null) {
                throwException(message, errorBuilder);
            }
        }
    }

    public static <E extends CheckedException> void isNull(final ErrorCode errorCode,
                                                           final BiFunction<ErrorCode, String, E> errorBuilder,
                                                           final Object... values) throws E {
        for (final Object item : values) {
            if (item != null) {
                throwException(errorCode, errorBuilder);
            }
        }
    }

    // -------------------------------------------------------------------------
    // NOT NULL
    // -------------------------------------------------------------------------
    public static void notNull(final Object... objects) throws CheckedException {
        notNull("this argument is required; it must not be null",
                objects);
    }

    public static void notNull(final String message,
                               final Object... values) throws CheckedException {
        for (final Object item : values) {
            if (item == null) {
                throwException(message, EXCEPTION_BUILDER);
            }
        }
    }

    public static void notNull(final ErrorCode errorCode,
                               final Object... values) throws CheckedException {
        for (final Object item : values) {
            if (item == null) {
                throwException(errorCode, EXCEPTION_BUILDER);
            }
        }
    }


    public static <E extends CheckedException> void notNull(final BiFunction<ErrorCode, String, E> errorBuilder,
                                                            final Object... objects) throws E {
        notNull("this argument is required; it must not be null",
                errorBuilder,
                objects);
    }

    public static <E extends CheckedException> void notNull(final String message,
                                                            final BiFunction<ErrorCode, String, E> errorBuilder,
                                                            final Object... values) throws E {
        for (final Object item : values) {
            if (item == null) {
                throwException(message, errorBuilder);
            }
        }
    }

    public static <E extends CheckedException> void notNull(final ErrorCode errorCode,
                                                            final BiFunction<ErrorCode, String, E> errorBuilder,
                                                            final Object... values) throws E {
        for (final Object item : values) {
            if (item == null) {
                throwException(errorCode, errorBuilder);
            }
        }
    }

    // -------------------------------------------------------------------------
    // NOT EMPTY
    // -------------------------------------------------------------------------
    public static void notEmpty(final String message,
                                final String value) throws CheckedException {
        if (checkIsBlank(value)) {
            throwException(message, EXCEPTION_BUILDER);
        }
    }

    public static void notEmpty(final ErrorCode errorCode,
                                final String value) throws CheckedException {
        if (checkIsBlank(value)) {
            throwException(errorCode, EXCEPTION_BUILDER);
        }
    }

    public static <E extends CheckedException> void notEmpty(final String message,
                                                             final String value,
                                                             final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        if (checkIsBlank(value)) {
            throwException(message, errorBuilder);
        }
    }

    public static <E extends CheckedException> void notEmpty(final ErrorCode errorCode,
                                                             final String value,
                                                             final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        if (checkIsBlank(value)) {
            throwException(errorCode, errorBuilder);
        }
    }


    public static boolean checkIsBlank(final String value) {

        boolean result = (value == null) || (value.length() == 0);

        if (!result) {
            final int length = value.length();
            for (int i = 0; i < length; i++) {
                if (Character.isWhitespace(value.charAt(i)) == false) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    public static <E extends CheckedException> void notEmpty(final String message,
                                                             final Collection<?> value,
                                                             final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        if (value == null || value.isEmpty()) {
            throwException(message, errorBuilder);
        }
    }

    public static <E extends CheckedException> void notEmpty(final ErrorCode errorCode,
                                                             final Collection<?> value,
                                                             final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        if (value == null || value.isEmpty()) {
            throwException(errorCode, errorBuilder);
        }
    }

    public static <E extends CheckedException> void notEmpty(final String message,
                                                             final Map<?, ?> value,
                                                             final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        if (value == null || value.isEmpty()) {
            throwException(message, errorBuilder);
        }
    }

    public static <E extends CheckedException> void notEmpty(final ErrorCode errorCode,
                                                             final Map<?, ?> value,
                                                             final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        if (value == null || value.isEmpty()) {
            throwException(errorCode, errorBuilder);
        }
    }


    public static void notEmpty(final String message,
                                final Collection<?> value) throws CheckedException {
        if (value == null || value.isEmpty()) {
            throwException(message, EXCEPTION_BUILDER);
        }
    }

    public static void notEmpty(final ErrorCode errorCode,
                                final Collection<?> value) throws CheckedException {
        if (value == null || value.isEmpty()) {
            throwException(errorCode, EXCEPTION_BUILDER);
        }
    }

    public static void notEmpty(final String message,
                                final Map<?, ?> value) throws CheckedException {
        if (value == null || value.isEmpty()) {
            throwException(message, EXCEPTION_BUILDER);
        }
    }

    public static void notEmpty(final ErrorCode errorCode,
                                final Map<?, ?> value) throws CheckedException {
        if (value == null || value.isEmpty()) {
            throwException(errorCode, EXCEPTION_BUILDER);
        }
    }

    // -------------------------------------------------------------------------
    // EQUALS
    // -------------------------------------------------------------------------
    public static void equalsObj(final Object ref,
                                 final Object value) throws CheckedException {
        equalsObj(null,
                  ref,
                  value);
    }

    public static void equalsObj(final String message,
                                 final Object ref,
                                 final Object value) throws CheckedException {
        boolean result = ref == value;

        if (!result && (ref != null)) {
            result = ref.equals(value);
        }

        if (!result) {
            final String msg = message == null ? "objects must be equals!" : message;
            throwException(message, EXCEPTION_BUILDER);
        }
    }


    public static <E extends CheckedException> void equalsObj(final Object ref,
                                                              final Object value,
                                                              final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        equalsObj(null,
                  ref,
                  value,
                  errorBuilder);
    }

    public static <E extends CheckedException> void equalsObj(final String message,
                                                              final Object ref,
                                                              final Object value,
                                                              final BiFunction<ErrorCode, String, E> errorBuilder) throws E {
        boolean result = ref == value;

        if (!result && (ref != null)) {
            result = ref.equals(value);
        }

        if (!result) {
            final String msg = message == null ? "objects must be equals!" : message;
            throwException(message, errorBuilder);
        }
    }

    // -------------------------------------------------------------------------
    // TOOLS
    // -------------------------------------------------------------------------
    private static void throwException(final String message) throws CheckedException {
        throwException(null, message, null);
    }


    private static <E extends CheckedException> void throwException(final String message,
                                                                    final BiFunction<ErrorCode, String, E> exceptionBuilder) throws E {
        throwException(null, message, exceptionBuilder);
    }

    private static void throwException(final ErrorCode errorCode) throws CheckedException {
        throwException(errorCode, null, null);
    }

    private static <E extends CheckedException> void throwException(final ErrorCode errorCode,
                                                                    final BiFunction<ErrorCode, String, E> exceptionBuilder) throws E {
        throwException(errorCode, null, exceptionBuilder);
    }

    private static <E extends CheckedException> void throwException(final ErrorCode errorCode,
                                                                    final String message,
                                                                    final BiFunction<ErrorCode, String, E> exceptionBuilder) throws E {

        throw exceptionBuilder.apply(errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode, message);
    }

}
