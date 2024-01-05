package io.inugami.framework.interfaces.exceptions.asserts;

import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.MessagesFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertRegex {

    private static final String      NOT_MATCH         = "current value {0} doesn't match with {1}";
    private static final String      MATCH             = "current value {0} should not match with {1}";
    private static final String      FIND              = "current value {0} doesn't find any result with {1}";
    private static final String      NOT_FIND          = "current value {0} should have any result with {1}";
    public static final  String      REGEX_IS_REQUIRED = "regex is required";
    public static final  AssertRegex INSTANCE          = new AssertRegex();

    // ========================================================================
    // assertRegexMatch
    // ========================================================================
    public void assertRegexMatch(final Pattern regex,
                                 final String value) {
        assertRegexMatch(() -> MessagesFormatter.format(NOT_MATCH, value, regex), regex, value);
    }

    public void assertRegexMatch(final String message,
                                 final Pattern regex,
                                 final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !regex.matcher(value).matches()) {
            AssertCommons.INSTANCE.throwException(
                    message == null ? MessagesFormatter.format(NOT_MATCH, value, regex) : message);
        }
    }

    public void assertRegexMatch(final Supplier<String> messageProducer,
                                 final Pattern regex,
                                 final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !regex.matcher(value).matches()) {
            AssertCommons.INSTANCE.throwException(messageProducer.get());
        }
    }

    public void assertRegexMatch(final ErrorCode errorCode,
                                 final Pattern regex,
                                 final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !regex.matcher(value).matches()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    public void assertRegexMatch(final String regex,
                                 final String value) {
        assertRegexMatch(() -> MessagesFormatter.format(NOT_MATCH, value, regex), regex, value);
    }

    public void assertRegexMatch(final String message,
                                 final String regex,
                                 final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !match(regex, value)) {
            AssertCommons.INSTANCE.throwException(
                    message == null ? MessagesFormatter.format(NOT_MATCH, value, regex) : message);
        }
    }


    public void assertRegexMatch(final Supplier<String> messageProducer,
                                 final String regex,
                                 final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !match(regex, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer.get());
        }
    }

    public void assertRegexMatch(final ErrorCode errorCode,
                                 final String regex,
                                 final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !match(regex, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    // ========================================================================
    // assertRegexNotMatch
    // ========================================================================
    public void assertRegexNotMatch(final Pattern regex,
                                    final String value) {
        assertRegexNotMatch(() -> MessagesFormatter.format(MATCH, value, regex), regex, value);
    }

    public void assertRegexNotMatch(final String message,
                                    final Pattern regex,
                                    final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || regex.matcher(value).matches()) {
            AssertCommons.INSTANCE.throwException(
                    message == null ? MessagesFormatter.format(MATCH, value, regex) : message);
        }
    }

    public void assertRegexNotMatch(final Supplier<String> messageProducer,
                                    final Pattern regex,
                                    final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || regex.matcher(value).matches()) {
            AssertCommons.INSTANCE.throwException(messageProducer.get());
        }
    }

    public void assertRegexNotMatch(final ErrorCode errorCode,
                                    final Pattern regex,
                                    final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || regex.matcher(value).matches()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    public void assertRegexNotMatch(final String regex,
                                    final String value) {
        assertRegexNotMatch(() -> MessagesFormatter.format(MATCH, value, regex), regex, value);
    }

    public void assertRegexNotMatch(final String message,
                                    final String regex,
                                    final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || match(regex, value)) {
            AssertCommons.INSTANCE.throwException(
                    message == null ? MessagesFormatter.format(MATCH, value, regex) : message);
        }
    }


    public void assertRegexNotMatch(final Supplier<String> messageProducer,
                                    final String regex,
                                    final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || match(regex, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer.get());
        }
    }

    public void assertRegexNotMatch(final ErrorCode errorCode,
                                    final String regex,
                                    final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || match(regex, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    // ========================================================================
    // assertRegexFind
    // ========================================================================
    public void assertRegexFind(final Pattern regex,
                                final String value) {
        assertRegexFind(() -> MessagesFormatter.format(FIND, value, regex), regex, value);
    }

    public void assertRegexFind(final String message,
                                final Pattern regex,
                                final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !regex.matcher(value).find()) {
            AssertCommons.INSTANCE.throwException(
                    message == null ? MessagesFormatter.format(FIND, value, regex) : message);
        }
    }

    public void assertRegexFind(final Supplier<String> messageProducer,
                                final Pattern regex,
                                final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !regex.matcher(value).find()) {
            AssertCommons.INSTANCE.throwException(messageProducer.get());
        }
    }

    public void assertRegexFind(final ErrorCode errorCode,
                                final Pattern regex,
                                final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !regex.matcher(value).find()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    public void assertRegexFind(final String regex,
                                final String value) {
        assertRegexFind(() -> MessagesFormatter.format(FIND, value, regex), regex, value);
    }

    public void assertRegexFind(final String message,
                                final String regex,
                                final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !find(regex, value)) {
            AssertCommons.INSTANCE.throwException(
                    message == null ? MessagesFormatter.format(FIND, value, regex) : message);
        }
    }


    public void assertRegexFind(final Supplier<String> messageProducer,
                                final String regex,
                                final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !find(regex, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer.get());
        }
    }

    public void assertRegexFind(final ErrorCode errorCode,
                                final String regex,
                                final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || !find(regex, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    // ========================================================================
    // assertRegexNotFind
    // ========================================================================
    public void assertRegexNotFind(final Pattern regex,
                                   final String value) {
        assertRegexNotFind(() -> MessagesFormatter.format(NOT_FIND, value, regex), regex, value);
    }

    public void assertRegexNotFind(final String message,
                                   final Pattern regex,
                                   final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || regex.matcher(value).find()) {
            AssertCommons.INSTANCE.throwException(
                    message == null ? MessagesFormatter.format(NOT_FIND, value, regex) : message);
        }
    }

    public void assertRegexNotFind(final Supplier<String> messageProducer,
                                   final Pattern regex,
                                   final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || regex.matcher(value).find()) {
            AssertCommons.INSTANCE.throwException(messageProducer.get());
        }
    }

    public void assertRegexNotFind(final ErrorCode errorCode,
                                   final Pattern regex,
                                   final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || regex.matcher(value).find()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    public void assertRegexNotFind(final String regex,
                                   final String value) {
        assertRegexNotFind(() -> MessagesFormatter.format(NOT_FIND, value, regex), regex, value);
    }

    public void assertRegexNotFind(final String message,
                                   final String regex,
                                   final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || find(regex, value)) {
            AssertCommons.INSTANCE.throwException(
                    message == null ? MessagesFormatter.format(NOT_FIND, value, regex) : message);
        }
    }


    public void assertRegexNotFind(final Supplier<String> messageProducer,
                                   final String regex,
                                   final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || find(regex, value)) {
            AssertCommons.INSTANCE.throwException(messageProducer.get());
        }
    }

    public void assertRegexNotFind(final ErrorCode errorCode,
                                   final String regex,
                                   final String value) {
        Objects.requireNonNull(regex, REGEX_IS_REQUIRED);
        if (value == null || find(regex, value)) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }


    // ========================================================================
    // PRIVATE
    // ========================================================================
    private static boolean match(final String regex, final String value) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(value).matches();
    }

    private static boolean find(final String regex, final String value) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(value).find();
    }
}
