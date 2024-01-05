package io.inugami.commons.test.api;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings({"java:S1124", "java:S6353"})
@Builder(toBuilder = true)
@AllArgsConstructor
public class NumberLineMatcher implements LineMatcher {
    private final static Pattern       REGEX = Pattern.compile(".*[0-9]+.*");
    private final        List<Integer> lines;

    public static NumberLineMatcher of(final int... lines) {
        final List<Integer> linesValues = new ArrayList<>();
        for (final int line : lines) {
            linesValues.add(Integer.valueOf(line));
        }
        return NumberLineMatcher.builder()
                                .lines(linesValues)
                                .build();
    }


    @Override
    public boolean accept(final int index, final String value, final String reference) {
        return lines.contains(Integer.valueOf(index));
    }

    @Override
    public boolean skip(final int index, final String value, final String reference) {
        return false;
    }

    @Override
    public boolean match(final int index, final String value, final String reference) {
        return REGEX.matcher(value).matches();
    }
}
