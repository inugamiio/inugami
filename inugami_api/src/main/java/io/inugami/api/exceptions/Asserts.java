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

/**
 * Asserts
 *
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Asserts {


    // -------------------------------------------------------------------------
    // IS TRUE
    // -------------------------------------------------------------------------
    public static void isTrue(final boolean expression) {
        isTrue("this expression must be true",
               expression);
    }

    public static void isTrue(final String message,
                              final boolean expression) {
        if (!expression) {
            throwException(message);
        }
    }


    public static void isTrue(final ErrorCode errorCode,
                              final boolean expression) {
        if (!expression) {
            throwException(errorCode);
        }
    }


    // -------------------------------------------------------------------------
    // IS FALSE
    // -------------------------------------------------------------------------
    public static void isFalse(final boolean expression) {
        isFalse("this expression must be true",
                expression);
    }

    public static void isFalse(final String message,
                               final boolean expression) {
        if (expression) {
            throwException(message);
        }
    }

    public static void isFalse(final ErrorCode errorCode,
                               final boolean expression) {
        if (expression) {
            throwException(errorCode);
        }
    }

    // -------------------------------------------------------------------------
    // IS NULL
    // -------------------------------------------------------------------------
    public static void isNull(final Object... objects) {
        isNull("objects arguments must be null",
               objects);
    }

    public static void isNull(final String message,
                              final Object... values) {
        for (final Object item : values) {
            if (item != null) {
                throwException(message);
            }
        }
    }

    public static void isNull(final ErrorCode errorCode,
                              final Object... values) {
        for (final Object item : values) {
            if (item != null) {
                throwException(errorCode);
            }
        }
    }

    // -------------------------------------------------------------------------
    // NOT NULL
    // -------------------------------------------------------------------------
    public static void notNull(final Object... objects) {
        notNull("this argument is required; it must not be null",
                objects);
    }

    public static void notNull(final String message,
                               final Object... values) {
        for (final Object item : values) {
            if (item == null) {
                throwException(message);
            }
        }
    }

    public static void notNull(final ErrorCode errorCode,
                               final Object... values) {
        for (final Object item : values) {
            if (item == null) {
                throwException(errorCode);
            }
        }
    }

    // -------------------------------------------------------------------------
    // NOT EMPTY
    // -------------------------------------------------------------------------
    public static void notEmpty(final String message,
                                final String value) {
        if (checkIsBlank(value)) {
            throwException(message);
        }
    }

    public static void notEmpty(final ErrorCode errorCode,
                                final String value) {
        if (checkIsBlank(value)) {
            throwException(errorCode);
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

    public static void notEmpty(final String message,
                                final Collection<?> value) {
        if (value == null || value.isEmpty()) {
            throwException(message);
        }
    }

    public static void notEmpty(final ErrorCode errorCode,
                                final Collection<?> value) {
        if (value == null || value.isEmpty()) {
            throwException(errorCode);
        }
    }

    public static void notEmpty(final String message,
                                final Map<?, ?> value) {
        if (value == null || value.isEmpty()) {
            throwException(message);
        }
    }

    public static void notEmpty(final ErrorCode errorCode,
                                final Map<?, ?> value) {
        if (value == null || value.isEmpty()) {
            throwException(errorCode);
        }
    }

    // -------------------------------------------------------------------------
    // EQUALS
    // -------------------------------------------------------------------------
    public static void equalsObj(final Object ref,
                                 final Object value) {
        equalsObj("object must be equals",
                  ref,
                  value);
    }

    public static void equalsObj(final String message,
                                 final Object ref,
                                 final Object value) {
        boolean result = ref == value;

        if (!result && (ref != null)) {
            result = ref.equals(value);
        }

        if (!result) {
            final String msg = message == null ? "objects must be equals!" : message;
            throwException(message);
        }
    }

    public static void equalsObj(final ErrorCode errorCode,
                                 final Object ref,
                                 final Object value) {
        boolean result = ref == value;

        if (!result && (ref != null)) {
            result = ref.equals(value);
        }

        if (!result) {
            throwException(errorCode);
        }
    }

    // -------------------------------------------------------------------------
    // TOOLS
    // -------------------------------------------------------------------------
    private static void throwException(final String message) {
        throwException(null,
                       message);
    }

    private static void throwException(final ErrorCode errorCode) {
        throw new UncheckedException(errorCode,
                                     errorCode == null ? null : errorCode.getMessage());
    }

    private static void throwException(final ErrorCode errorCode,
                                       final String message) {
        throw new UncheckedException(errorCode,
                                     message);
    }

}
