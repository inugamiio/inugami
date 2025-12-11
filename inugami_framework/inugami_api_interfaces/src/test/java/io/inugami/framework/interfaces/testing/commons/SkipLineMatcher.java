package io.inugami.framework.interfaces.testing.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
public class SkipLineMatcher implements LineMatcher {
    private final List<Integer> lines;

    public static SkipLineMatcher of(final int... values) {
        List<Integer> lines = new ArrayList<>();
        for(int value : values){
            lines.add(value);
        }
        return SkipLineMatcher.builder()
                              .lines(lines)
                              .build();
    }

    @Override
    public boolean accept(final int index, final String value, final String reference) {
        return lines.contains(index);
    }

    @Override
    public boolean skip(final int index, final String value, final String reference) {
        return true;
    }

    @Override
    public boolean match(final int index, final String value, final String reference) {
        return true;
    }
}
