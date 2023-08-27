package io.inugami.commons.test.api;

public interface LineMatcher {
    boolean accept(int index, String value, String reference);

    boolean skip(int index, String value, String reference);

    boolean match(int index, String value, String reference);
}