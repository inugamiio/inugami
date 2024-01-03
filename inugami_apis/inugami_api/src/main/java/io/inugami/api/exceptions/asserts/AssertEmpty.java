package io.inugami.api.exceptions.asserts;

import io.inugami.interfaces.exceptions.ErrorCode;
import io.inugami.interfaces.functionnals.IsEmptyFacet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertEmpty {
    private static final String      ASSERT_NOT_EMPTY_DEFAULT_MSG = "the value mustn't be empty";
    private static final String      ASSERT_EMPTY_DEFAULT_MSG     = "the value should be empty";
    public static final  AssertEmpty INSTANCE                     = new AssertEmpty();

    // -------------------------------------------------------------------------
    // NOT EMPTY
    // -------------------------------------------------------------------------
    public void assertNotEmpty(final String value) {
        if (checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(ASSERT_NOT_EMPTY_DEFAULT_MSG);
        }
    }


    public void assertNotEmpty(final String message,
                               final String value) {
        if (checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : message);
        }
    }


    public void assertNotEmpty(final Supplier<String> messageProducer,
                               final String value) {
        if (checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }


    public void assertNotEmpty(final ErrorCode errorCode,
                               final String value) {
        if (checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    public boolean checkIsBlank(final String value) {

        boolean result = (value == null) || (value.length() == 0) || value.isEmpty();

        if (!result) {
            result = value.isBlank();
        }

        return result;
    }

    public void assertNotEmpty(final Collection<?> value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(ASSERT_NOT_EMPTY_DEFAULT_MSG);
        }
    }

    public void assertNotEmpty(final Supplier<String> messageProducer,
                               final Collection<?> value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }


    public void assertNotEmpty(final String message,
                               final Collection<?> value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : message);
        }
    }


    public void assertNotEmpty(final ErrorCode errorCode,
                               final Collection<?> value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    public void assertNotEmpty(final IsEmptyFacet value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(ASSERT_NOT_EMPTY_DEFAULT_MSG);
        }
    }

    public void assertNotEmpty(final String message,
                               final IsEmptyFacet value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public void assertNotEmpty(final Supplier<String> messageProducer,
                               final IsEmptyFacet value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public void assertNotEmpty(final ErrorCode errorCode,
                               final IsEmptyFacet value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    public void assertNotEmpty(final Map<?, ?> value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(ASSERT_NOT_EMPTY_DEFAULT_MSG);
        }
    }


    public void assertNotEmpty(final String message,
                               final Map<?, ?> value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public void assertNotEmpty(final Supplier<String> messageProducer,
                               final Map<?, ?> value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_NOT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }


    public void assertNotEmpty(final ErrorCode errorCode,
                               final Map<?, ?> value) {
        if (value == null || value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    // -------------------------------------------------------------------------
    // EMPTY
    // -------------------------------------------------------------------------
    public void assertEmpty(final String value) {
        if (!checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public void assertEmpty(final String message,
                            final String value) {
        if (!checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public void assertEmpty(final Supplier<String> messageProducer,
                            final String value) {
        if (!checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public void assertEmpty(final ErrorCode errorCode,
                            final String value) {
        if (!checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    public void assertEmpty(final Collection<?> value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public void assertEmpty(final Supplier<String> messageProducer,
                            final Collection<?> value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public void assertEmpty(final String message,
                            final Collection<?> value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public void assertEmpty(final ErrorCode errorCode,
                            final Collection<?> value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    public void assertEmpty(final Map<?, ?> value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public void assertEmpty(final String message,
                            final Map<?, ?> value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public void assertEmpty(final Supplier<String> messageProducer,
                            final Map<?, ?> value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public void assertEmpty(final ErrorCode errorCode,
                            final Map<?, ?> value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    public void assertEmpty(final IsEmptyFacet value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public void assertEmpty(final Supplier<String> messageProducer,
                            final IsEmptyFacet value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public void assertEmpty(final String message,
                            final IsEmptyFacet value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public void assertEmpty(final ErrorCode errorCode,
                            final IsEmptyFacet value) {
        if (value == null || !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    // -------------------------------------------------------------------------
    // NULL OR EMPTY
    // -------------------------------------------------------------------------
    public void assertNullOrEmpty(final String value) {
        if (value != null && !checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public void assertNullOrEmpty(final String message,
                                  final String value) {
        if (value != null && !checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public void assertNullOrEmpty(final Supplier<String> messageProducer,
                                  final String value) {
        if (value != null && !checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public void assertNullOrEmpty(final ErrorCode errorCode,
                                  final String value) {
        if (value != null && !checkIsBlank(value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    public void assertNullOrEmpty(final Collection<?> value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public void assertNullOrEmpty(final Supplier<String> messageProducer,
                                  final Collection<?> value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public void assertNullOrEmpty(final String message,
                                  final Collection<?> value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public void assertNullOrEmpty(final ErrorCode errorCode,
                                  final Collection<?> value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    public void assertNullOrEmpty(final Map<?, ?> value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public void assertNullOrEmpty(final String message,
                                  final Map<?, ?> value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public void assertNullOrEmpty(final Supplier<String> messageProducer,
                                  final Map<?, ?> value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public void assertNullOrEmpty(final ErrorCode errorCode,
                                  final Map<?, ?> value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    public void assertNullOrEmpty(final IsEmptyFacet value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(ASSERT_EMPTY_DEFAULT_MSG);
        }
    }

    public void assertNullOrEmpty(final Supplier<String> messageProducer,
                                  final IsEmptyFacet value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(
                    messageProducer == null ? ASSERT_EMPTY_DEFAULT_MSG : messageProducer.get());
        }
    }

    public void assertNullOrEmpty(final String message,
                                  final IsEmptyFacet value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(message == null ? ASSERT_EMPTY_DEFAULT_MSG : message);
        }
    }

    public void assertNullOrEmpty(final ErrorCode errorCode,
                                  final IsEmptyFacet value) {
        if (value != null && !value.isEmpty()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }
}
