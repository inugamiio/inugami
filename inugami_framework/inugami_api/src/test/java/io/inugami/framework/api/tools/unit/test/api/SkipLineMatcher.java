package io.inugami.framework.api.tools.unit.test.api;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

import static io.inugami.framework.interfaces.tools.ListUtils.toList;

@Builder(toBuilder = true)
@AllArgsConstructor
public class SkipLineMatcher implements LineMatcher {
    private final List<Integer> line;

    public static SkipLineMatcher of(final Integer... lines) {
        return SkipLineMatcher.builder().line(toList(lines)).build();
    }

    @Override
    public boolean accept(final int index, final String value, final String reference) {
        return line.contains(index);
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
