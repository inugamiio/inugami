package io.inugami.commons.test;

public interface LineAssertion {
    boolean accept(final int lineNumber,final String value);

    boolean isMatching(final String value, final String ref);

    boolean isSkipped(final String value);

}
