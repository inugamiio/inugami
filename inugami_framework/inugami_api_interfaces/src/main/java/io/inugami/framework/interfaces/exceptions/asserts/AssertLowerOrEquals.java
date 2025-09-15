package io.inugami.framework.interfaces.exceptions.asserts;

import io.inugami.framework.interfaces.exceptions.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertLowerOrEquals {

    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    private static final String              DEFAULT_MESSAGE = "value must be lower or equals";
    public static final  AssertLowerOrEquals INSTANCE        = new AssertLowerOrEquals();

    // ========================================================================
    // API
    // ========================================================================
    // ------------------------------------------------------------------------
    public void assertLowerOrEquals(final int ref,
                                    final int value) {
        assertLowerOrEquals(DEFAULT_MESSAGE,
                            ref,
                            value);
    }

    public void assertLowerOrEquals(final String message,
                                    final int ref,
                                    final int value) {
        if (ref < value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLowerOrEquals(final Supplier<String> messageProducer,
                                    final int ref,
                                    final int value) {
        if (ref < value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLowerOrEquals(final ErrorCode errorCode,
                                    final int ref,
                                    final int value) {
        if (ref < value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLowerOrEquals(final long ref,
                                    final long value) {
        assertLowerOrEquals(DEFAULT_MESSAGE,
                            ref,
                            value);
    }

    public void assertLowerOrEquals(final String message,
                                    final long ref,
                                    final long value) {
        if (ref < value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLowerOrEquals(final Supplier<String> messageProducer,
                                    final long ref,
                                    final long value) {
        if (ref < value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLowerOrEquals(final ErrorCode errorCode,
                                    final long ref,
                                    final long value) {
        if (ref < value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLowerOrEquals(final float ref,
                                    final float value) {
        assertLowerOrEquals(DEFAULT_MESSAGE,
                            ref,
                            value);
    }

    public void assertLowerOrEquals(final String message,
                                    final float ref,
                                    final float value) {
        if (ref < value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLowerOrEquals(final Supplier<String> messageProducer,
                                    final float ref,
                                    final float value) {
        if (ref < value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLowerOrEquals(final ErrorCode errorCode,
                                    final float ref,
                                    final float value) {
        if (ref < value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLowerOrEquals(final double ref,
                                    final double value) {
        assertLowerOrEquals(DEFAULT_MESSAGE,
                            ref,
                            value);
    }

    public void assertLowerOrEquals(final String message,
                                    final double ref,
                                    final double value) {
        if (ref < value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLowerOrEquals(final Supplier<String> messageProducer,
                                    final double ref,
                                    final double value) {
        if (ref < value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLowerOrEquals(final ErrorCode errorCode,
                                    final double ref,
                                    final double value) {
        if (ref < value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLowerOrEquals(final Integer ref,
                                    final Integer value) {
        assertLowerOrEquals(DEFAULT_MESSAGE,
                            ref,
                            value);
    }

    public void assertLowerOrEquals(final String message,
                                    final Integer ref,
                                    final Integer value) {

        if (checkHigher(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLowerOrEquals(final Supplier<String> messageProducer,
                                    final Integer ref,
                                    final Integer value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLowerOrEquals(final ErrorCode errorCode,
                                    final Integer ref,
                                    final Integer value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLowerOrEquals(final Long ref,
                                    final Long value) {
        assertLowerOrEquals(DEFAULT_MESSAGE,
                            ref,
                            value);
    }

    public void assertLowerOrEquals(final String message,
                                    final Long ref,
                                    final Long value) {

        if (checkHigher(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLowerOrEquals(final Supplier<String> messageProducer,
                                    final Long ref,
                                    final Long value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLowerOrEquals(final ErrorCode errorCode,
                                    final Long ref,
                                    final Long value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLowerOrEquals(final Double ref,
                                    final Double value) {
        assertLowerOrEquals(DEFAULT_MESSAGE,
                            ref,
                            value);
    }

    public void assertLowerOrEquals(final String message,
                                    final Double ref,
                                    final Double value) {

        if (checkHigher(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLowerOrEquals(final Supplier<String> messageProducer,
                                    final Double ref,
                                    final Double value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLowerOrEquals(final ErrorCode errorCode,
                                    final Double ref,
                                    final Double value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLowerOrEquals(final BigDecimal ref,
                                    final BigDecimal value) {
        assertLowerOrEquals(DEFAULT_MESSAGE,
                            ref,
                            value);
    }

    public void assertLowerOrEquals(final String message,
                                    final BigDecimal ref,
                                    final BigDecimal value) {

        if (checkHigher(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLowerOrEquals(final Supplier<String> messageProducer,
                                    final BigDecimal ref,
                                    final BigDecimal value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLowerOrEquals(final ErrorCode errorCode,
                                    final BigDecimal ref,
                                    final BigDecimal value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    private static boolean checkHigher(final Integer ref, final Integer value) {
        return ref == null ? value != null : ref.compareTo(value) < 0;
    }

    private static boolean checkHigher(final Long ref, final Long value) {
        return ref == null ? value != null : ref.compareTo(value) < 0;
    }

    private static boolean checkHigher(final Double ref, final Double value) {
        return ref == null ? value != null : ref.compareTo(value) < 0;
    }

    private static boolean checkHigher(final BigDecimal ref, final BigDecimal value) {
        return ref == null ? value != null : ref.compareTo(value) < 0;
    }
}
