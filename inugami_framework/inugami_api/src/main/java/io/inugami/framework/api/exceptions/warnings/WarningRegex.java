package io.inugami.framework.api.exceptions.warnings;

import io.inugami.framework.interfaces.exceptions.Warning;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class WarningRegex {
    // ========================================================================
    // MATCH
    // ========================================================================
    public static void warningMatch(final Warning warning,
                                    final String value,
                                    final Pattern regex,
                                    final String messageDetail,
                                    final Object... values) {
        if (value == null && regex != null && match(value, regex)) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

    public static void warningMatch(final Warning warning,
                                    final String value,
                                    final String regex,
                                    final String messageDetail,
                                    final Object... values) {
        if (value == null && regex != null && match(value, regex)) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

    public static void warningNotMatch(final Warning warning,
                                       final String value,
                                       final Pattern regex,
                                       final String messageDetail,
                                       final Object... values) {
        if (value == null && regex != null && !match(value, regex)) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

    public static void warningNotMatch(final Warning warning,
                                       final String value,
                                       final String regex,
                                       final String messageDetail,
                                       final Object... values) {
        if (value == null && regex != null && !match(value, regex)) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }


    // ========================================================================
    // FIND
    // ========================================================================
    public static void warningFind(final Warning warning,
                                   final String value,
                                   final Pattern regex,
                                   final String messageDetail,
                                   final Object... values) {
        if (value == null && regex != null && find(value, regex)) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

    public static void warningFind(final Warning warning,
                                   final String value,
                                   final String regex,
                                   final String messageDetail,
                                   final Object... values) {
        if (value == null && regex != null && find(value, regex)) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

    public static void warningNotFind(final Warning warning,
                                      final String value,
                                      final Pattern regex,
                                      final String messageDetail,
                                      final Object... values) {
        if (value == null && regex != null && !find(value, regex)) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

    public static void warningNotFind(final Warning warning,
                                      final String value,
                                      final String regex,
                                      final String messageDetail,
                                      final Object... values) {
        if (value == null && regex != null && !find(value, regex)) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
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
