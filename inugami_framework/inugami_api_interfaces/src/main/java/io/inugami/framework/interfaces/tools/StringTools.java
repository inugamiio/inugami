package io.inugami.framework.interfaces.tools;

import io.inugami.framework.interfaces.exceptions.MessagesFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"java:S5361", "java:S3776", "java:S1125"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringTools {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Pattern IN_COMBINING_DIACRITICAL_MARKS = Pattern.compile("[\\p{InCombiningDiacriticalMarks}]");

    private static final List<UnaryOperator<String>> STARTEGIES = initializeStrategies();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private static List<UnaryOperator<String>> initializeStrategies() {
        final List<UnaryOperator<String>> result = new ArrayList<>();

        result.add(value -> StringTools.containsChars(value, "Ł") ? value.replaceAll("Ł", "L") : value);
        result.add(value -> StringTools.containsChars(value, "ł") ? value.replaceAll("ł", "l") : value);
        result.add(value -> StringTools.containsChars(value, "Ø") ? value.replaceAll("Ø", "O") : value);
        result.add(value -> StringTools.containsChars(value, "ø") ? value.replaceAll("ø", "o") : value);
        result.add(value -> StringTools.containsChars(value, "Œ") ? value.replaceAll("Œ", "OE") : value);
        result.add(value -> StringTools.containsChars(value, "œ") ? value.replaceAll("œ", "oe") : value);
        result.add(value -> StringTools.containsChars(value, "Æ") ? value.replaceAll("Æ", "AE") : value);
        result.add(value -> StringTools.containsChars(value, "æ") ? value.replaceAll("æ", "ae") : value);
        result.add(value -> StringTools.containsChars(value, "ß") ? value.replaceAll("ß", "ss") : value);
        result.add(value -> StringTools.containsChars(value, "ẞ") ? value.replaceAll("ẞ", "SS") : value);

        return result;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public static String replaceAll(final Pattern regex, final String data, final String replacement) {
        String        result  = data;
        final Matcher matcher = regex.matcher(data);

        if (matcher.find()) {
            result = matcher.replaceAll(replacement);
        }

        return result;
    }

    public static String convertToAscii(final String value) {
        if (value == null) {
            return null;
        }
        final String firstPass = replaceAll(IN_COMBINING_DIACRITICAL_MARKS, Normalizer.normalize(value, Form.NFD), "");

        return cleanSpecialChars(firstPass);
    }

    private static String cleanSpecialChars(final String value) {
        String result = value;

        for (final UnaryOperator<String> cleaner : STARTEGIES) {
            result = cleaner.apply(result);
        }
        return result;
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    public static boolean containsChars(final String value, final String specialChar) {
        return value == null ? false : value.contains(specialChar);
    }

    public static String format(final String message, final Object... values) {
        return MessagesFormatter.format(message, values);
    }
}
