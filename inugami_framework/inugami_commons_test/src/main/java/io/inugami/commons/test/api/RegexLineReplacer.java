package io.inugami.commons.test.api;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Builder(toBuilder = true)
@AllArgsConstructor
public class RegexLineReplacer implements LineMatcher {


    private final Pattern       pattern;
    private final String        replacePattern;
    private final List<Integer> lines;

    public static RegexLineReplacer of(final String pattern, final String replacePattern, final int... lines) {
        final List<Integer> linesValues = new ArrayList<>();
        for (final int line : lines) {
            linesValues.add(Integer.valueOf(line));
        }
        return RegexLineReplacer.builder()
                                .pattern(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE))
                                .replacePattern(replacePattern)
                                .lines(linesValues)
                                .build();
    }


    @Override
    public boolean accept(final int index, final String value, final String reference) {
        return false;
    }

    @Override
    public boolean skip(final int index, final String value, final String reference) {
        return false;
    }

    @Override
    public boolean match(final int index, final String value, final String reference) {
        return false;
    }

    @Override
    public boolean acceptReplace(int index, String value) {
        return pattern != null
               && (lines == null || lines.isEmpty() || lines.contains(index))
               && pattern.matcher(value).matches();
    }

    @Override
    public String replace(int index, String value) {
        return pattern.matcher(value).replaceAll(replacePattern);
    }
}
