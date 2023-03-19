package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertEquals {

    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    private static final String DEFAULT_MESSAGE = "object must be equals";

    // ========================================================================
    // API
    // ========================================================================
    public static void assertEquals(final Object ref,
                                    final Object value) {
        assertEquals(DEFAULT_MESSAGE,
                     ref,
                     value);
    }

    public static void assertEquals(final String message,
                                    final Object ref,
                                    final Object value) {
        boolean result = ref == value;

        if (!result && (ref != null)) {
            result = ref.equals(value);
        }

        if (!result) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }

    public static void assertEquals(final Supplier<String> messageProducer,
                                    final Object ref,
                                    final Object value) {
        boolean result = ref == value;

        if (!result && (ref != null)) {
            result = ref.equals(value);
        }

        if (checkNotEquals(ref,value)) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public static void assertEquals(final ErrorCode errorCode,
                                    final Object ref,
                                    final Object value) {
        boolean result = ref == value;

        if (!result && (ref != null)) {
            result = ref.equals(value);
        }

        if (!result) {
            AssertCommons.throwException(errorCode);
        }
    }


    public static void assertEquals(final int ref,
                                    final int value) {
        assertEquals(DEFAULT_MESSAGE,
                     ref,
                     value);
    }

    public static void assertEquals(final String message,
                                    final int ref,
                                    final int value) {
        if (ref!=value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }

    public static void assertEquals(final Supplier<String> messageProducer,
                                    final int ref,
                                    final int value) {
        if (ref!=value) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public static void assertEquals(final ErrorCode errorCode,
                                    final int ref,
                                    final int value) {
        if (ref!=value) {
            AssertCommons.throwException(errorCode);
        }
    }

    public static void assertEquals(final long ref,
                                    final long value) {
        assertEquals(DEFAULT_MESSAGE,
                     ref,
                     value);
    }

    public static void assertEquals(final String message,
                                    final long ref,
                                    final long value) {
        if (ref!=value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }

    public static void assertEquals(final Supplier<String> messageProducer,
                                    final long ref,
                                    final long value) {
        if (ref!=value) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public static void assertEquals(final ErrorCode errorCode,
                                    final long ref,
                                    final long value) {
        if (ref!=value) {
            AssertCommons.throwException(errorCode);
        }
    }
    public static void assertEquals(final float ref,
                                    final float value) {
        assertEquals(DEFAULT_MESSAGE,
                     ref,
                     value);
    }

    public static void assertEquals(final String message,
                                    final float ref,
                                    final float value) {
        if (ref!=value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }

    public static void assertEquals(final Supplier<String> messageProducer,
                                    final float ref,
                                    final float value) {
        if (ref!=value) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public static void assertEquals(final ErrorCode errorCode,
                                    final float ref,
                                    final float value) {
        if (ref!=value) {
            AssertCommons.throwException(errorCode);
        }
    }

    public static void assertEquals(final double ref,
                                    final double value) {
        assertEquals(DEFAULT_MESSAGE,
                     ref,
                     value);
    }

    public static void assertEquals(final String message,
                                    final double ref,
                                    final double value) {
        if (ref!=value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }

    public static void assertEquals(final Supplier<String> messageProducer,
                                    final double ref,
                                    final double value) {
        if (ref!=value) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public static void assertEquals(final ErrorCode errorCode,
                                    final double ref,
                                    final double value) {
        if (ref!=value) {
            AssertCommons.throwException(errorCode);
        }
    }

    private static boolean checkNotEquals(final Object ref, final Object value) {
        return (ref==null && value!=null) | (ref!=null && value==null) | !ref.equals(value);
    }
}