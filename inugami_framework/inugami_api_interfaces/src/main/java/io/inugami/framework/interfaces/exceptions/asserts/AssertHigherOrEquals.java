package io.inugami.framework.interfaces.exceptions.asserts;

import io.inugami.framework.interfaces.exceptions.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertHigherOrEquals {

    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    private static final String               DEFAULT_MESSAGE = "value must be higher or equals";
    public static final  AssertHigherOrEquals INSTANCE        = new AssertHigherOrEquals();

    // ========================================================================
    // API
    // ========================================================================
    // ------------------------------------------------------------------------
    public void assertHigherOrEquals(final int ref,
                                     final int value) {
        assertHigherOrEquals(DEFAULT_MESSAGE,
                             ref,
                             value);
    }

    public void assertHigherOrEquals(final String message,
                                     final int ref,
                                     final int value) {
        if (ref > value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertHigherOrEquals(final Supplier<String> messageProducer,
                                     final int ref,
                                     final int value) {
        if (ref > value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertHigherOrEquals(final ErrorCode errorCode,
                                     final int ref,
                                     final int value) {
        if (ref > value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertHigherOrEquals(final long ref,
                                     final long value) {
        assertHigherOrEquals(DEFAULT_MESSAGE,
                             ref,
                             value);
    }

    public void assertHigherOrEquals(final String message,
                                     final long ref,
                                     final long value) {
        if (ref > value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertHigherOrEquals(final Supplier<String> messageProducer,
                                     final long ref,
                                     final long value) {
        if (ref > value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertHigherOrEquals(final ErrorCode errorCode,
                                     final long ref,
                                     final long value) {
        if (ref > value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertHigherOrEquals(final float ref,
                                     final float value) {
        assertHigherOrEquals(DEFAULT_MESSAGE,
                             ref,
                             value);
    }

    public void assertHigherOrEquals(final String message,
                                     final float ref,
                                     final float value) {
        if (ref > value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertHigherOrEquals(final Supplier<String> messageProducer,
                                     final float ref,
                                     final float value) {
        if (ref > value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertHigherOrEquals(final ErrorCode errorCode,
                                     final float ref,
                                     final float value) {
        if (ref > value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertHigherOrEquals(final double ref,
                                     final double value) {
        assertHigherOrEquals(DEFAULT_MESSAGE,
                             ref,
                             value);
    }

    public void assertHigherOrEquals(final String message,
                                     final double ref,
                                     final double value) {
        if (ref > value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertHigherOrEquals(final Supplier<String> messageProducer,
                                     final double ref,
                                     final double value) {
        if (ref > value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertHigherOrEquals(final ErrorCode errorCode,
                                     final double ref,
                                     final double value) {
        if (ref > value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertHigherOrEquals(final Integer ref,
                                     final Integer value) {
        assertHigherOrEquals(DEFAULT_MESSAGE,
                             ref,
                             value);
    }

    public void assertHigherOrEquals(final String message,
                                     final Integer ref,
                                     final Integer value) {

        if (checkLower(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertHigherOrEquals(final Supplier<String> messageProducer,
                                     final Integer ref,
                                     final Integer value) {
        if (checkLower(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertHigherOrEquals(final ErrorCode errorCode,
                                     final Integer ref,
                                     final Integer value) {
        if (checkLower(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertHigherOrEquals(final Long ref,
                                     final Long value) {
        assertHigherOrEquals(DEFAULT_MESSAGE,
                             ref,
                             value);
    }

    public void assertHigherOrEquals(final String message,
                                     final Long ref,
                                     final Long value) {

        if (checkLower(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertHigherOrEquals(final Supplier<String> messageProducer,
                                     final Long ref,
                                     final Long value) {
        if (checkLower(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertHigherOrEquals(final ErrorCode errorCode,
                                     final Long ref,
                                     final Long value) {
        if (checkLower(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertHigherOrEquals(final Double ref,
                                     final Double value) {
        assertHigherOrEquals(DEFAULT_MESSAGE,
                             ref,
                             value);
    }

    public void assertHigherOrEquals(final String message,
                                     final Double ref,
                                     final Double value) {

        if (checkLower(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertHigherOrEquals(final Supplier<String> messageProducer,
                                     final Double ref,
                                     final Double value) {
        if (checkLower(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertHigherOrEquals(final ErrorCode errorCode,
                                     final Double ref,
                                     final Double value) {
        if (checkLower(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertHigherOrEquals(final BigDecimal ref,
                                     final BigDecimal value) {
        assertHigherOrEquals(DEFAULT_MESSAGE,
                             ref,
                             value);
    }

    public void assertHigherOrEquals(final String message,
                                     final BigDecimal ref,
                                     final BigDecimal value) {

        if (checkLower(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertHigherOrEquals(final Supplier<String> messageProducer,
                                     final BigDecimal ref,
                                     final BigDecimal value) {
        if (checkLower(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertHigherOrEquals(final ErrorCode errorCode,
                                     final BigDecimal ref,
                                     final BigDecimal value) {
        if (checkLower(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    private static boolean checkLower(final Integer ref, final Integer value) {
        return ref == null ? value != null : ref.compareTo(value) > 0;
    }

    private static boolean checkLower(final Long ref, final Long value) {
        return ref == null ? value != null : ref.compareTo(value) > 0;
    }

    private static boolean checkLower(final Double ref, final Double value) {
        return ref == null ? value != null : ref.compareTo(value) > 0;
    }

    private static boolean checkLower(final BigDecimal ref, final BigDecimal value) {
        return ref == null ? value != null : ref.compareTo(value) > 0;
    }
}
