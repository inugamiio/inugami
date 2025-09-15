package io.inugami.framework.interfaces.exceptions.asserts;

import io.inugami.framework.interfaces.exceptions.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertBoolean {

    private static final String        ASSERT_TRUE_DEFAULT_MSG  = "this expression must be true";
    private static final String        ASSERT_FALSE_DEFAULT_MSG = "this expression must be false";
    public static final  AssertBoolean INSTANCE                 = new AssertBoolean();

    // -------------------------------------------------------------------------
    // IS TRUE
    // -------------------------------------------------------------------------

    public void assertTrue(final boolean expression) {
        assertTrue(ASSERT_TRUE_DEFAULT_MSG, expression);
    }


    public void assertTrue(final String message,
                           final boolean expression) {
        if (!expression) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_TRUE_DEFAULT_MSG : message);
        }
    }


    public void assertTrue(final ErrorCode errorCode,
                           final boolean expression) {
        if (!expression) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    public void assertTrue(final Supplier<String> messageProducer,
                           final boolean expression) {
        if (!expression) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_TRUE_DEFAULT_MSG : messageProducer.get());
        }
    }

    // -------------------------------------------------------------------------
    // IS FALSE
    // -------------------------------------------------------------------------

    public void assertFalse(final boolean expression) {
        assertFalse(ASSERT_FALSE_DEFAULT_MSG, expression);
    }


    public void assertFalse(final String message,
                            final boolean expression) {
        if (expression) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_FALSE_DEFAULT_MSG : message);
        }
    }


    public void assertFalse(final Supplier<String> messageProducer,
                            final boolean expression) {
        if (expression) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_FALSE_DEFAULT_MSG : messageProducer.get());
        }
    }


    public void assertFalse(final ErrorCode errorCode,
                            final boolean expression) {
        if (expression) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }
}
