package io.inugami.api.tools.unit.test.api;

public class DefaultLineMatcher implements LineMatcher {
    @Override
    public boolean accept(final int index, final String value, final String reference) {
        return true;
    }

    @Override
    public boolean skip(final int index, final String value, final String reference) {
        return false;
    }

    @Override
    public boolean match(final int index, final String value, final String reference) {
        return value.equals(reference);
    }
}
