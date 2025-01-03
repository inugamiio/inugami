package io.inugami.commons.test.api;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
public class SkipLineMatcher implements LineMatcher {
    private final List<Integer> lines;

    public static SkipLineMatcher of(final int... lines) {
        List<Integer> values = new ArrayList<>();
        for (int i : lines) {
            values.add(i);
        }
        return SkipLineMatcher.builder().lines(values).build();
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
