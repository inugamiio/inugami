package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertNull {

    private static final String     ASSERT_NULL_DEFAULT_MSG     = "objects arguments must be null";
    private static final String     ASSERT_NOT_NULL_DEFAULT_MSG = "this argument is required; it must not be null";
    public static final  AssertNull INSTANCE                    = new AssertNull();
    
    // -------------------------------------------------------------------------
    // IS NULL
    // -------------------------------------------------------------------------
    public void assertNull(final Object... objects) {
        assertNull(ASSERT_NULL_DEFAULT_MSG, objects);
    }

    public void assertNull(final String message,
                           final Object... values) {
        if (values == null) {
            return;
        }
        for (final Object item : values) {
            if (item != null) {
                AssertCommons.INSTANCE.throwException(message == null ? ASSERT_NULL_DEFAULT_MSG : message);
            }
        }
    }

    public void assertNull(final Supplier<String> messageProducer,
                           final Object... values) {
        if (values == null) {
            return;
        }
        for (final Object item : values) {
            if (item != null) {
                AssertCommons.INSTANCE.throwException(
                        messageProducer == null ? ASSERT_NULL_DEFAULT_MSG : messageProducer.get());
            }
        }
    }


    public void assertNull(final ErrorCode errorCode,
                           final Object... values) {
        if (values == null) {
            return;
        }
        for (final Object item : values) {
            if (item != null) {
                AssertCommons.INSTANCE.throwException(errorCode);
            }
        }
    }


    // -------------------------------------------------------------------------
    // NOT NULL
    // -------------------------------------------------------------------------
    public void assertNotNull(final Object... objects) {
        assertNotNull(ASSERT_NOT_NULL_DEFAULT_MSG,
                      objects);
    }


    public void assertNotNull(final String message,
                              final Object... values) {
        if (checkIfHasNull(values)) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_NOT_NULL_DEFAULT_MSG : message);
        }
    }


    public void assertNotNull(final Supplier<String> messageProducer,
                              final Object... values) {
        if (checkIfHasNull(values)) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_NULL_DEFAULT_MSG : messageProducer.get());
        }
    }


    public void assertNotNull(final ErrorCode errorCode,
                              final Object... values) {
        if (checkIfHasNull(values)) {
            AssertCommons.INSTANCE.throwException(errorCode);
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
