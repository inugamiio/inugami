package io.inugami.api.exceptions.warnings;

import io.inugami.api.exceptions.Warning;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Warnings {


    // ========================================================================
    // NULL / NOT NULL
    // ========================================================================
    public static void warningNull(final Warning warning, final Object value) {
        warningNull(warning, value, null, null);
    }

    public static void warningNull(final Warning warning, final Object value, final String messageDetail, final Object... values) {
        WarningNull.warningNull(warning, value, messageDetail, values);
    }

    public static void warningNotNull(final Warning warning, final Object value) {
        warningNotNull(warning, value, null, null);
    }

    public static void warningNotNull(final Warning warning, final Object value, final String messageDetail, final Object... values) {
        WarningNull.warningNotNull(warning, value, messageDetail, values);
    }

    // ========================================================================
    // EMPTY
    // ========================================================================
    public static void warningEmpty(final Warning warning, final String value) {
        warningEmpty(warning, value, null, null);
    }

    public static void warningEmpty(final Warning warning, final String value, final String messageDetail, final Object... msgArgs) {
        WarningEmpty.warningEmpty(warning, value, messageDetail, msgArgs);
    }

    public static void warningNotEmpty(final Warning warning, final String value) {
        warningNotEmpty(warning, value, null, null);
    }

    public static void warningNotEmpty(final Warning warning, final String value, final String messageDetail, final Object... msgArgs) {
        WarningEmpty.warningEmpty(warning, value, messageDetail, msgArgs);
    }


    public static void warningEmpty(final Warning warning, final Collection<?> values) {
        warningEmpty(warning, values, null, null);
    }

    public static void warningEmpty(final Warning warning, final Collection<?> values, final String messageDetail, final Object... msgArgs) {
        WarningEmpty.warningEmpty(warning, values, messageDetail, msgArgs);
    }


    public static <T> void warningNotEmpty(final Warning warning, final T[] values) {
        warningNotEmpty(warning, values, null, null);
    }

    public static <T> void warningNotEmpty(final Warning warning, final T[] values, final String messageDetail, final Object... msgArgs) {
        WarningEmpty.warningNotEmpty(warning, values, messageDetail, msgArgs);
    }


    public static <T> void warningEmpty(final Warning warning, final T[] values) {
        warningNotEmpty(warning, values, null, null);
    }

    public static <T> void warningEmpty(final Warning warning, final T[] values, final String messageDetail, final Object... msgArgs) {
        WarningEmpty.warningNotEmpty(warning, values, messageDetail, msgArgs);
    }

    public static void warningNotEmpty(final Warning warning, final Collection<?> values) {
        warningNotEmpty(warning, values, null, null);
    }

    public static void warningNotEmpty(final Warning warning, final Collection<?> values, final String messageDetail, final Object... msgArgs) {
        WarningEmpty.warningNotEmpty(warning, values, messageDetail, msgArgs);
    }


    public static void warningEmpty(final Warning warning, final Map<?, ?> values) {
        WarningEmpty.warningNotEmpty(warning, values, null, null);
    }


    public static void warningEmpty(final Warning warning, final Map<?, ?> values, final String messageDetail, final Object... msgArgs) {
        WarningEmpty.warningNotEmpty(warning, values, messageDetail, msgArgs);
    }

    public static void warningNotEmpty(final Warning warning, final Map<?, ?> values) {
        WarningEmpty.warningNotEmpty(warning, values, null, null);
    }

    public static void warningNotEmpty(final Warning warning, final Map<?, ?> values, final String messageDetail, final Object... msgArgs) {
        WarningEmpty.warningNotEmpty(warning, values, messageDetail, msgArgs);
    }


    // ========================================================================
    // TRUE / FALSE
    // ========================================================================
    public static void warningTrue(final Warning warning, final boolean condition) {
        warningTrue(warning, condition, null, null);
    }

    public static void warningTrue(final Warning warning, final boolean condition, final String messageDetail, final Object... values) {
        WarningTrue.warningTrue(warning, condition, messageDetail, values);
    }

    public static void warningTrue(final Warning warning, final Supplier<Boolean> condition) {
        warningTrue(warning, condition, null, null);
    }

    public static void warningTrue(final Warning warning, final Supplier<Boolean> condition, final String messageDetail, final Object... values) {
        WarningTrue.warningTrue(warning, condition, messageDetail, values);
    }

    public static void warningFalse(final Warning warning, final boolean condition) {
        warningFalse(warning, condition, null, null);
    }

    public static void warningFalse(final Warning warning, final boolean condition, final String messageDetail, final Object... values) {
        WarningTrue.warningFalse(warning, condition, messageDetail, values);
    }

    public static void warningFalse(final Warning warning, final Supplier<Boolean> condition) {
        warningFalse(warning, condition, null, null);
    }

    public static void warningFalse(final Warning warning, final Supplier<Boolean> condition, final String messageDetail, final Object... values) {
        WarningTrue.warningFalse(warning, condition, messageDetail, values);
    }

    // ========================================================================
    // MATCH
    // ========================================================================
    public static void warningRegexMatch(final Warning warning, final String value, final Pattern regex) {
        WarningRegex.warningMatch(warning, value, regex, null, null);
    }

    public static void warningRegexMatch(final Warning warning, final String value, final Pattern regex, final String messageDetail, final Object... values) {
        WarningRegex.warningMatch(warning, value, regex, messageDetail, values);
    }

    public static void warningRegexMatch(final Warning warning, final String value, final String regex) {
        WarningRegex.warningMatch(warning, value, regex, null, null);
    }

    public static void warningRegexMatch(final Warning warning, final String value, final String regex, final String messageDetail, final Object... values) {
        WarningRegex.warningMatch(warning, value, regex, messageDetail, values);
    }

    public static void warningRegexNotMatch(final Warning warning, final String value, final Pattern regex) {
        WarningRegex.warningNotMatch(warning, value, regex, null, null);
    }

    public static void warningRegexNotMatch(final Warning warning, final String value, final Pattern regex, final String messageDetail, final Object... values) {
        WarningRegex.warningNotMatch(warning, value, regex, messageDetail, values);
    }

    public static void warningRegexNotMatch(final Warning warning, final String value, final String regex) {
        WarningRegex.warningNotMatch(warning, value, regex, null, null);
    }

    public static void warningRegexNotMatch(final Warning warning, final String value, final String regex, final String messageDetail, final Object... values) {
        WarningRegex.warningNotMatch(warning, value, regex, messageDetail, values);
    }


    // ========================================================================
    // FIND
    // ========================================================================
    public static void warningRegexFind(final Warning warning, final String value, final Pattern regex) {
        WarningRegex.warningFind(warning, value, regex, null, null);
    }

    public static void warningRegexFind(final Warning warning, final String value, final Pattern regex, final String messageDetail, final Object... values) {
        WarningRegex.warningFind(warning, value, regex, messageDetail, values);
    }

    public static void warningRegexFind(final Warning warning, final String value, final String regex) {
        WarningRegex.warningFind(warning, value, regex, null, null);
    }

    public static void warningRegexFind(final Warning warning, final String value, final String regex, final String messageDetail, final Object... values) {
        WarningRegex.warningFind(warning, value, regex, messageDetail, values);
    }

    public static void warningRegexNotFind(final Warning warning, final String value, final Pattern regex) {
        WarningRegex.warningNotFind(warning, value, regex, null, null);
    }

    public static void warningRegexNotFind(final Warning warning, final String value, final Pattern regex, final String messageDetail, final Object... values) {
        WarningRegex.warningNotFind(warning, value, regex, messageDetail, values);
    }

    public static void warningRegexNotFind(final Warning warning, final String value, final String regex) {
        WarningRegex.warningNotFind(warning, value, regex, null, null);
    }

    public static void warningRegexNotFind(final Warning warning, final String value, final String regex, final String messageDetail, final Object... values) {
        WarningRegex.warningNotFind(warning, value, regex, messageDetail, values);
    }
}

