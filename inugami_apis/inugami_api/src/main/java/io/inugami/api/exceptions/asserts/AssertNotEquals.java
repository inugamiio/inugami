package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertNotEquals {

    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    private static final String          ASSERT_NOT_EQUALS_DEEFAULT_MSG = "values mustn't be equals";
    public static final  AssertNotEquals INSTANCE                       = new AssertNotEquals();

    // ========================================================================
    // API
    // ========================================================================
    public void assertNotEquals(final Object ref,
                                final Object value) {
        assertNotEquals(ASSERT_NOT_EQUALS_DEEFAULT_MSG,
                        ref,
                        value);
    }

    public void assertNotEquals(final String message,
                                final Object ref,
                                final Object value) {
        if (checkObjectEquals(ref, value)) {
            final String msg = message == null ? ASSERT_NOT_EQUALS_DEEFAULT_MSG : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }


    public void assertNotEquals(final Supplier<String> messageProducer,
                                final Object ref,
                                final Object value) {
        if (checkObjectEquals(ref, value)) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_NOT_EQUALS_DEEFAULT_MSG : messageProducer.get());
        }
    }


    public void assertNotEquals(final ErrorCode errorCode,
                                final Object ref,
                                final Object value) {
        if (checkObjectEquals(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    public void assertNotEquals(final int ref,
                                final int value) {
        assertNotEquals(ASSERT_NOT_EQUALS_DEEFAULT_MSG,
                        ref,
                        value);
    }

    public void assertNotEquals(final String message,
                                final int ref,
                                final int value) {
        if (ref == value) {
            final String msg = message == null ? ASSERT_NOT_EQUALS_DEEFAULT_MSG : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }


    public void assertNotEquals(final Supplier<String> messageProducer,
                                final int ref,
                                final int value) {
        if (ref == value) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_NOT_EQUALS_DEEFAULT_MSG : messageProducer.get());
        }
    }


    public void assertNotEquals(final ErrorCode errorCode,
                                final int ref,
                                final int value) {
        if (ref == value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    public void assertNotEquals(final long ref,
                                final long value) {
        assertNotEquals(ASSERT_NOT_EQUALS_DEEFAULT_MSG,
                        ref,
                        value);
    }

    public void assertNotEquals(final String message,
                                final long ref,
                                final long value) {
        if (ref == value) {
            final String msg = message == null ? ASSERT_NOT_EQUALS_DEEFAULT_MSG : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }


    public void assertNotEquals(final Supplier<String> messageProducer,
                                final long ref,
                                final long value) {
        if (ref == value) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_NOT_EQUALS_DEEFAULT_MSG : messageProducer.get());
        }
    }


    public void assertNotEquals(final ErrorCode errorCode,
                                final long ref,
                                final long value) {
        if (ref == value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    public void assertNotEquals(final float ref,
                                final float value) {
        assertNotEquals(ASSERT_NOT_EQUALS_DEEFAULT_MSG,
                        ref,
                        value);
    }

    public void assertNotEquals(final String message,
                                final float ref,
                                final float value) {
        if (ref == value) {
            final String msg = message == null ? ASSERT_NOT_EQUALS_DEEFAULT_MSG : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }


    public void assertNotEquals(final Supplier<String> messageProducer,
                                final float ref,
                                final float value) {
        if (ref == value) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_NOT_EQUALS_DEEFAULT_MSG : messageProducer.get());
        }
    }


    public void assertNotEquals(final ErrorCode errorCode,
                                final float ref,
                                final float value) {
        if (ref == value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    public void assertNotEquals(final double ref,
                                final double value) {
        assertNotEquals(ASSERT_NOT_EQUALS_DEEFAULT_MSG,
                        ref,
                        value);
    }

    public void assertNotEquals(final String message,
                                final double ref,
                                final double value) {
        if (ref == value) {
            final String msg = message == null ? ASSERT_NOT_EQUALS_DEEFAULT_MSG : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }


    public void assertNotEquals(final Supplier<String> messageProducer,
                                final double ref,
                                final double value) {
        if (ref == value) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_NOT_EQUALS_DEEFAULT_MSG : messageProducer.get());
        }
    }


    public void assertNotEquals(final ErrorCode errorCode,
                                final double ref,
                                final double value) {
        if (ref == value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    private static boolean checkObjectEquals(final Object ref, final Object value) {
        return ref == null ? value == null : ref.equals(value);
    }

}
