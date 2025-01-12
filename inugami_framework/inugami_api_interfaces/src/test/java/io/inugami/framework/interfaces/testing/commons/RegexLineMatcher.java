package io.inugami.framework.interfaces.testing.commons;


import io.inugami.framework.interfaces.exceptions.Asserts;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Builder(toBuilder = true)
@AllArgsConstructor
public class RegexLineMatcher implements LineMatcher {


    private final Pattern       pattern;
    private final Pattern       lineAcceptPattern;
    private final List<Integer> lines;

    public static RegexLineMatcher of(final String pattern, final int... lines) {
        final List<Integer> linesValues = new ArrayList<>();
        for (final int line : lines) {
            linesValues.add(Integer.valueOf(line));
        }
        return RegexLineMatcher.builder()
                               .pattern(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE))
                               .lines(linesValues)
                               .build();
    }

    public static RegexLineMatcher of(final Pattern pattern, final int... lines) {
        final List<Integer> linesValues = new ArrayList<>();
        for (final int line : lines) {
            linesValues.add(Integer.valueOf(line));
        }
        return RegexLineMatcher.builder()
                               .pattern(pattern)
                               .lines(linesValues)
                               .build();
    }

    public static RegexLineMatcher of(final String lineAcceptPattern, final String pattern) {
        final List<Integer> linesValues = new ArrayList<>();

        return RegexLineMatcher.builder()
                               .lineAcceptPattern(Pattern.compile(lineAcceptPattern, Pattern.CASE_INSENSITIVE))
                               .pattern(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE))
                               .lines(linesValues)
                               .build();
    }

    @Override
    public boolean accept(final int index, final String value, final String reference) {
        if (pattern == null) {
            return false;
        }

        if (lines.isEmpty()) {
            Asserts.assertNotNull("line accept pattern is requires", lineAcceptPattern);
            return lineAcceptPattern.matcher(value).matches();
        } else {
            return lines.contains(Integer.valueOf(index));
        }

    }

    @Override
    public boolean skip(final int index, final String value, final String reference) {
        return false;
    }

    @Override
    public boolean match(final int index, final String value, final String reference) {
        return pattern.matcher(value).matches();
    }
}
