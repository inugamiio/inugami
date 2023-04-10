package io.inugami.commons.test.api;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder(toBuilder = true)
@AllArgsConstructor
public class SkipLineMatcher implements LineMatcher {
    private final int line;

    public static SkipLineMatcher of(final int line) {
        return SkipLineMatcher.builder().line(line).build();
    }

    @Override
    public boolean accept(final int index, final String value, final String reference) {
        return index == line;
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
