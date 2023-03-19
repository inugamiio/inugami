package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.MessagesFormatter;
import io.inugami.api.exceptions.UncheckedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertRegex {

    private static final String NOT_MATCH = "current value {0} doesn't match with {1}";
    private static final String MATCH = "current value {0} should not match with {1}";

    private static final String FIND = "current value {0} doesn't find any result with {1}";
    private static final String NOT_FIND = "current value {0} should have any result with {1}";


    // ========================================================================
    // assertRegexMatch
    // ========================================================================
    public static void assertRegexMatch(final Pattern regex,
                                        final String value) {
        assertRegexMatch(() -> MessagesFormatter.format(NOT_MATCH, value, regex), regex, value);
    }

    public static void assertRegexMatch(final String message,
                                        final Pattern regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !regex.matcher(value).matches()) {
            AssertCommons.throwException(message == null ? MessagesFormatter.format(NOT_MATCH, value, regex) : message);
        }
    }

    public static void assertRegexMatch(final Supplier<String> messageProducer,
                                        final Pattern regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !regex.matcher(value).matches()) {
            AssertCommons.throwException(messageProducer.get());
        }
    }

    public static void assertRegexMatch(final ErrorCode errorCode,
                                        final Pattern regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !regex.matcher(value).matches()) {
            AssertCommons.throwException(errorCode);
        }
    }


    public static void assertRegexMatch(final String regex,
                                        final String value) {
        assertRegexMatch(() -> MessagesFormatter.format(NOT_MATCH, value, regex), regex, value);
    }

    public static void assertRegexMatch(final String message,
                                        final String regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !match(regex, value)) {
            AssertCommons.throwException(message == null ? MessagesFormatter.format(NOT_MATCH, value, regex) : message);
        }
    }


    public static void assertRegexMatch(final Supplier<String> messageProducer,
                                        final String regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !match(regex, value)) {
            AssertCommons.throwException(messageProducer.get());
        }
    }

    public static void assertRegexMatch(final ErrorCode errorCode,
                                        final String regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !match(regex, value)) {
            AssertCommons.throwException(errorCode);
        }
    }


    // ========================================================================
    // assertRegexNotMatch
    // ========================================================================
    public static void assertRegexNotMatch(final Pattern regex,
                                           final String value) {
        assertRegexNotMatch(() -> MessagesFormatter.format(MATCH, value, regex), regex, value);
    }

    public static void assertRegexNotMatch(final String message,
                                           final Pattern regex,
                                           final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || regex.matcher(value).matches()) {
            AssertCommons.throwException(message == null ? MessagesFormatter.format(MATCH, value, regex) : message);
        }
    }

    public static void assertRegexNotMatch(final Supplier<String> messageProducer,
                                           final Pattern regex,
                                           final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || regex.matcher(value).matches()) {
            AssertCommons.throwException(messageProducer.get());
        }
    }

    public static void assertRegexNotMatch(final ErrorCode errorCode,
                                           final Pattern regex,
                                           final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || regex.matcher(value).matches()) {
            AssertCommons.throwException(errorCode);
        }
    }


    public static void assertRegexNotMatch(final String regex,
                                           final String value) {
        assertRegexNotMatch(() -> MessagesFormatter.format(MATCH, value, regex), regex, value);
    }

    public static void assertRegexNotMatch(final String message,
                                           final String regex,
                                           final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || match(regex, value)) {
            AssertCommons.throwException(message == null ? MessagesFormatter.format(MATCH, value, regex) : message);
        }
    }


    public static void assertRegexNotMatch(final Supplier<String> messageProducer,
                                           final String regex,
                                           final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || match(regex, value)) {
            AssertCommons.throwException(messageProducer.get());
        }
    }

    public static void assertRegexNotMatch(final ErrorCode errorCode,
                                           final String regex,
                                           final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || match(regex, value)) {
            AssertCommons.throwException(errorCode);
        }
    }


    // ========================================================================
    // assertRegexFind
    // ========================================================================
    public static void assertRegexFind(final Pattern regex,
                                        final String value) {
        assertRegexFind(() -> MessagesFormatter.format(FIND, value, regex), regex, value);
    }

    public static void assertRegexFind(final String message,
                                        final Pattern regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !regex.matcher(value).find()) {
            AssertCommons.throwException(message == null ? MessagesFormatter.format(FIND, value, regex) : message);
        }
    }

    public static void assertRegexFind(final Supplier<String> messageProducer,
                                        final Pattern regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !regex.matcher(value).find()) {
            AssertCommons.throwException(messageProducer.get());
        }
    }

    public static void assertRegexFind(final ErrorCode errorCode,
                                        final Pattern regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !regex.matcher(value).find()) {
            AssertCommons.throwException(errorCode);
        }
    }


    public static void assertRegexFind(final String regex,
                                        final String value) {
        assertRegexFind(() -> MessagesFormatter.format(FIND, value, regex), regex, value);
    }

    public static void assertRegexFind(final String message,
                                        final String regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !find(regex, value)) {
            AssertCommons.throwException(message == null ? MessagesFormatter.format(FIND, value, regex) : message);
        }
    }


    public static void assertRegexFind(final Supplier<String> messageProducer,
                                        final String regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !find(regex, value)) {
            AssertCommons.throwException(messageProducer.get());
        }
    }

    public static void assertRegexFind(final ErrorCode errorCode,
                                        final String regex,
                                        final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || !find(regex, value)) {
            AssertCommons.throwException(errorCode);
        }
    }


    // ========================================================================
    // assertRegexNotFind
    // ========================================================================
    public static void assertRegexNotFind(final Pattern regex,
                                       final String value) {
        assertRegexNotFind(() -> MessagesFormatter.format(NOT_FIND, value, regex), regex, value);
    }

    public static void assertRegexNotFind(final String message,
                                       final Pattern regex,
                                       final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || regex.matcher(value).find()) {
            AssertCommons.throwException(message == null ? MessagesFormatter.format(NOT_FIND, value, regex) : message);
        }
    }

    public static void assertRegexNotFind(final Supplier<String> messageProducer,
                                       final Pattern regex,
                                       final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || regex.matcher(value).find()) {
            AssertCommons.throwException(messageProducer.get());
        }
    }

    public static void assertRegexNotFind(final ErrorCode errorCode,
                                       final Pattern regex,
                                       final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || regex.matcher(value).find()) {
            AssertCommons.throwException(errorCode);
        }
    }


    public static void assertRegexNotFind(final String regex,
                                       final String value) {
        assertRegexNotFind(() -> MessagesFormatter.format(NOT_FIND, value, regex), regex, value);
    }

    public static void assertRegexNotFind(final String message,
                                       final String regex,
                                       final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || find(regex, value)) {
            AssertCommons.throwException(message == null ? MessagesFormatter.format(NOT_FIND, value, regex) : message);
        }
    }


    public static void assertRegexNotFind(final Supplier<String> messageProducer,
                                       final String regex,
                                       final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || find(regex, value)) {
            AssertCommons.throwException(messageProducer.get());
        }
    }

    public static void assertRegexNotFind(final ErrorCode errorCode,
                                       final String regex,
                                       final String value) {
        Objects.requireNonNull(regex, "regex is required");
        if (value == null || find(regex, value)) {
            AssertCommons.throwException(errorCode);
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
