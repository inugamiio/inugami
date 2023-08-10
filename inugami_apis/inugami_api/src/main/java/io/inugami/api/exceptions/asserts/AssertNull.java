package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertNull {
    private static final String ASSERT_NULL_DEFAULT_MSG     = "objects arguments must be null";
    private static final String ASSERT_NOT_NULL_DEFAULT_MSG = "this argument is required; it must not be null";

    // -------------------------------------------------------------------------
    // IS NULL
    // -------------------------------------------------------------------------
    public static void assertNull(final Object... objects) {
        assertNull(ASSERT_NULL_DEFAULT_MSG, objects);
    }

    public static void assertNull(final String message,
                                  final Object... values) {
        if (values == null) {
            return;
        }
        for (final Object item : values) {
            if (item != null) {
                AssertCommons.throwException(message == null ? ASSERT_NULL_DEFAULT_MSG : message);
            }
        }
    }

    public static void assertNull(final Supplier<String> messageProducer,
                                  final Object... values) {
        if (values == null) {
            return;
        }
        for (final Object item : values) {
            if (item != null) {
                AssertCommons.throwException(messageProducer == null ? ASSERT_NULL_DEFAULT_MSG : messageProducer.get());
            }
        }
    }


    public static void assertNull(final ErrorCode errorCode,
                                  final Object... values) {
        if (values == null) {
            return;
        }
        for (final Object item : values) {
            if (item != null) {
                AssertCommons.throwException(errorCode);
            }
        }
    }


    // -------------------------------------------------------------------------
    // NOT NULL
    // -------------------------------------------------------------------------
    public static void assertNotNull(final Object... objects) {
        assertNotNull(ASSERT_NOT_NULL_DEFAULT_MSG,
                      objects);
    }


    public static void assertNotNull(final String message,
                                     final Object... values) {
        if (checkIfHasNull(values)) {
            AssertCommons.throwException(message == null ? ASSERT_NOT_NULL_DEFAULT_MSG : message);
        }
    }

    
    public static void assertNotNull(final Supplier<String> messageProducer,
                                     final Object... values) {
        if (checkIfHasNull(values)) {
            AssertCommons.throwException(messageProducer == null ? ASSERT_NULL_DEFAULT_MSG : messageProducer.get());
        }
    }


    public static void assertNotNull(final ErrorCode errorCode,
                                     final Object... values) {
        if (checkIfHasNull(values)) {
            AssertCommons.throwException(errorCode);
        }
    }

    private static boolean checkIfHasNull(final Object... values) {
        if (values == null) {
            return true;
        }
        for (final Object value : values) {
            if (value == null) {
                return true;
            }
        }
        return false;
    }

}
