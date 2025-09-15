package io.inugami.framework.interfaces.testing.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Builder(toBuilder = true)
@AllArgsConstructor
public class LocalDateLineMatcher implements LineMatcher {
    private final static Pattern       REGEX = Pattern.compile(".*[0-9]{4}-((0[1-9])|(1[0-2]))-(([0-2][0-9])|(3[0-1])).*");
    private final        List<Integer> lines;

    public static LocalDateLineMatcher of(final int... lines) {
        final List<Integer> linesValues = new ArrayList<>();
        for (final int line : lines) {
            linesValues.add(Integer.valueOf(line));
        }
        return LocalDateLineMatcher.builder()
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
