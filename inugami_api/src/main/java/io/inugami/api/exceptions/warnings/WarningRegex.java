package io.inugami.api.exceptions.warnings;

import io.inugami.api.exceptions.Warning;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class WarningRegex {
    // ========================================================================
    // MATCH
    // ========================================================================
    static void warningMatch(final Warning warning, final String value, final Pattern regex, final String messageDetail, final Object... values) {
        if (value == null && regex != null) {
            if (match(value, regex)) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }

    static void warningMatch(final Warning warning, final String value, final String regex, final String messageDetail, final Object... values) {
        if (value == null && regex != null) {
            if (match(value, regex)) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }
    static void warningNotMatch(final Warning warning, final String value, final Pattern regex, final String messageDetail, final Object... values) {
        if (value == null && regex != null) {
            if (!match(value, regex)) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }

    static void warningNotMatch(final Warning warning, final String value, final String regex, final String messageDetail, final Object... values) {
        if (value == null && regex != null) {
            if (!match(value, regex)) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }


    // ========================================================================
    // FIND
    // ========================================================================
    static void warningFind(final Warning warning, final String value, final Pattern regex, final String messageDetail, final Object... values) {
        if (value == null && regex != null) {
            if (find(value, regex)) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }

    static void warningFind(final Warning warning, final String value, final String regex, final String messageDetail, final Object... values) {
        if (value == null && regex != null) {
            if (find(value, regex)) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }
    static void warningNotFind(final Warning warning, final String value, final Pattern regex, final String messageDetail, final Object... values) {
        if (value == null && regex != null) {
            if (!find(value, regex)) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }

    static void warningNotFind(final Warning warning, final String value, final String regex, final String messageDetail, final Object... values) {
        if (value == null && regex != null) {
            if (!find(value, regex)) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }


    // ========================================================================
    // TOOLS
    // ========================================================================
    private static boolean match(final String value, final Pattern regex) {
        return regex.matcher(value).matches();
    }

    private static boolean match(final String value, final String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(value).matches();
    }
    private static boolean find(final String value, final Pattern regex) {
        return regex.matcher(value).find();
    }

    private static boolean find(final String value, final String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(value).find();
    }

}
