package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertHigher {

    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    private static final String DEFAULT_MESSAGE = "value must be higher";

    // ========================================================================
    // API
    // ========================================================================
    // ------------------------------------------------------------------------
    public static void assertHigher(final int ref,
                                   final int value) {
        assertHigher(DEFAULT_MESSAGE,
                    ref,
                    value);
    }
    public static void assertHigher(final String message,
                                   final int ref,
                                   final int value) {
        if (ref >= value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }
    public static void assertHigher(final Supplier<String> messageProducer,
                                   final int ref,
                                   final int value) {
        if (ref >= value) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }
    public static void assertHigher(final ErrorCode errorCode,
                                   final int ref,
                                   final int value) {
        if (ref >= value) {
            AssertCommons.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final long ref,
                                   final long value) {
        assertHigher(DEFAULT_MESSAGE,
                    ref,
                    value);
    }
    public static void assertHigher(final String message,
                                   final long ref,
                                   final long value) {
        if (ref >= value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }
    public static void assertHigher(final Supplier<String> messageProducer,
                                   final long ref,
                                   final long value) {
        if (ref >= value) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }
    public static void assertHigher(final ErrorCode errorCode,
                                   final long ref,
                                   final long value) {
        if (ref >= value) {
            AssertCommons.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final float ref,
                                   final float value) {
        assertHigher(DEFAULT_MESSAGE,
                    ref,
                    value);
    }
    public static void assertHigher(final String message,
                                   final float ref,
                                   final float value) {
        if (ref >= value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }
    public static void assertHigher(final Supplier<String> messageProducer,
                                   final float ref,
                                   final float value) {
        if (ref >= value) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }
    public static void assertHigher(final ErrorCode errorCode,
                                   final float ref,
                                   final float value) {
        if (ref >= value) {
            AssertCommons.throwException(errorCode);
        }
    }
    // ------------------------------------------------------------------------
    public static void assertHigher(final double ref,
                                   final double value) {
        assertHigher(DEFAULT_MESSAGE,
                    ref,
                    value);
    }
    public static void assertHigher(final String message,
                                   final double ref,
                                   final double value) {
        if (ref >= value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }
    public static void assertHigher(final Supplier<String> messageProducer,
                                   final double ref,
                                   final double value) {
        if (ref >= value) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }
    public static void assertHigher(final ErrorCode errorCode,
                                   final double ref,
                                   final double value) {
        if (ref >= value) {
            AssertCommons.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final Integer ref,
                                   final Integer value) {
        assertHigher(DEFAULT_MESSAGE,
                    ref,
                    value);
    }
    public static void assertHigher(final String message,
                                   final Integer ref,
                                   final Integer value) {

        if (checkLower(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }
    public static void assertHigher(final Supplier<String> messageProducer,
                                   final Integer ref,
                                   final Integer value) {
        if (checkLower(ref, value)) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }
    public static void assertHigher(final ErrorCode errorCode,
                                   final Integer ref,
                                   final Integer value) {
        if (checkLower(ref, value)) {
            AssertCommons.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final Long ref,
                                   final Long value) {
        assertHigher(DEFAULT_MESSAGE,
                    ref,
                    value);
    }
    public static void assertHigher(final String message,
                                   final Long ref,
                                   final Long value) {

        if (checkLower(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }
    public static void assertHigher(final Supplier<String> messageProducer,
                                   final Long ref,
                                   final Long value) {
        if (checkLower(ref, value)) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }
    public static void assertHigher(final ErrorCode errorCode,
                                   final Long ref,
                                   final Long value) {
        if (checkLower(ref, value)) {
            AssertCommons.throwException(errorCode);
        }
    }
    // ------------------------------------------------------------------------
    public static void assertHigher(final Double ref,
                                   final Double value) {
        assertHigher(DEFAULT_MESSAGE,
                    ref,
                    value);
    }
    public static void assertHigher(final String message,
                                   final Double ref,
                                   final Double value) {

        if (checkLower(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }
    public static void assertHigher(final Supplier<String> messageProducer,
                                   final Double ref,
                                   final Double value) {
        if (checkLower(ref, value)) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }
    public static void assertHigher(final ErrorCode errorCode,
                                   final Double ref,
                                   final Double value) {
        if (checkLower(ref, value)) {
            AssertCommons.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public static void assertHigher(final BigDecimal ref,
                                   final BigDecimal value) {
        assertHigher(DEFAULT_MESSAGE,
                    ref,
                    value);
    }
    public static void assertHigher(final String message,
                                   final BigDecimal ref,
                                   final BigDecimal value) {

        if (checkLower(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.throwException(message);
        }
    }
    public static void assertHigher(final Supplier<String> messageProducer,
                                   final BigDecimal ref,
                                   final BigDecimal value) {
        if (checkLower(ref, value)) {
            AssertCommons.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }
    public static void assertHigher(final ErrorCode errorCode,
                                   final BigDecimal ref,
                                   final BigDecimal value) {
        if (checkLower(ref, value)) {
            AssertCommons.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    private static boolean checkLower(final Integer ref, final Integer value) {
        return ref==null?value!=null: ref.compareTo(value)>0;
    }

    private static boolean checkLower(final Long ref, final Long value) {
        return ref==null?value!=null:ref.compareTo(value)>0;
    }
    private static boolean checkLower(final Double ref, final Double value) {
        return ref==null?value!=null: ref.compareTo(value)>0;
    }
    private static boolean checkLower(final BigDecimal ref, final BigDecimal value) {
        return ref==null?value!=null: ref.compareTo(value)>0;
    }
}
