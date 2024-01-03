package io.inugami.api.exceptions.asserts;

import io.inugami.interfaces.exceptions.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertLower {

    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    private static final String      DEFAULT_MESSAGE = "value must be lower";
    public static final  AssertLower INSTANCE        = new AssertLower();

    // ========================================================================
    // API
    // ========================================================================
    // ------------------------------------------------------------------------
    public void assertLower(final int ref,
                            final int value) {
        assertLower(DEFAULT_MESSAGE,
                    ref,
                    value);
    }

    public void assertLower(final String message,
                            final int ref,
                            final int value) {
        if (ref <= value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLower(final Supplier<String> messageProducer,
                            final int ref,
                            final int value) {
        if (ref <= value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLower(final ErrorCode errorCode,
                            final int ref,
                            final int value) {
        if (ref <= value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLower(final long ref,
                            final long value) {
        assertLower(DEFAULT_MESSAGE,
                    ref,
                    value);
    }

    public void assertLower(final String message,
                            final long ref,
                            final long value) {
        if (ref <= value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLower(final Supplier<String> messageProducer,
                            final long ref,
                            final long value) {
        if (ref <= value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLower(final ErrorCode errorCode,
                            final long ref,
                            final long value) {
        if (ref <= value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLower(final float ref,
                            final float value) {
        assertLower(DEFAULT_MESSAGE,
                    ref,
                    value);
    }

    public void assertLower(final String message,
                            final float ref,
                            final float value) {
        if (ref <= value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLower(final Supplier<String> messageProducer,
                            final float ref,
                            final float value) {
        if (ref <= value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLower(final ErrorCode errorCode,
                            final float ref,
                            final float value) {
        if (ref <= value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLower(final double ref,
                            final double value) {
        assertLower(DEFAULT_MESSAGE,
                    ref,
                    value);
    }

    public void assertLower(final String message,
                            final double ref,
                            final double value) {
        if (ref <= value) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLower(final Supplier<String> messageProducer,
                            final double ref,
                            final double value) {
        if (ref <= value) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLower(final ErrorCode errorCode,
                            final double ref,
                            final double value) {
        if (ref <= value) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLower(final Integer ref,
                            final Integer value) {
        assertLower(DEFAULT_MESSAGE,
                    ref,
                    value);
    }

    public void assertLower(final String message,
                            final Integer ref,
                            final Integer value) {

        if (checkHigher(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLower(final Supplier<String> messageProducer,
                            final Integer ref,
                            final Integer value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLower(final ErrorCode errorCode,
                            final Integer ref,
                            final Integer value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLower(final Long ref,
                            final Long value) {
        assertLower(DEFAULT_MESSAGE,
                    ref,
                    value);
    }

    public void assertLower(final String message,
                            final Long ref,
                            final Long value) {

        if (checkHigher(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLower(final Supplier<String> messageProducer,
                            final Long ref,
                            final Long value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLower(final ErrorCode errorCode,
                            final Long ref,
                            final Long value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLower(final Double ref,
                            final Double value) {
        assertLower(DEFAULT_MESSAGE,
                    ref,
                    value);
    }

    public void assertLower(final String message,
                            final Double ref,
                            final Double value) {

        if (checkHigher(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLower(final Supplier<String> messageProducer,
                            final Double ref,
                            final Double value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLower(final ErrorCode errorCode,
                            final Double ref,
                            final Double value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    public void assertLower(final BigDecimal ref,
                            final BigDecimal value) {
        assertLower(DEFAULT_MESSAGE,
                    ref,
                    value);
    }

    public void assertLower(final String message,
                            final BigDecimal ref,
                            final BigDecimal value) {

        if (checkHigher(ref, value)) {
            final String msg = message == null ? DEFAULT_MESSAGE : message;
            AssertCommons.INSTANCE.throwException(msg);
        }
    }

    public void assertLower(final Supplier<String> messageProducer,
                            final BigDecimal ref,
                            final BigDecimal value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer == null ? DEFAULT_MESSAGE : messageProducer.get());
        }
    }

    public void assertLower(final ErrorCode errorCode,
                            final BigDecimal ref,
                            final BigDecimal value) {
        if (checkHigher(ref, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // ------------------------------------------------------------------------
    private static boolean checkHigher(final Integer ref, final Integer value) {
        return ref == null ? value != null : ref.compareTo(value) <= 0;
    }

    private static boolean checkHigher(final Long ref, final Long value) {
        return ref == null ? value != null : ref.compareTo(value) <= 0;
    }

    private static boolean checkHigher(final Double ref, final Double value) {
        return ref == null ? value != null : ref.compareTo(value) <= 0;
    }

    private static boolean checkHigher(final BigDecimal ref, final BigDecimal value) {
        return ref == null ? value != null : ref.compareTo(value) <= 0;
    }
}
