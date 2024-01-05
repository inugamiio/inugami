package io.inugami.commons.test.api;

public interface LineMatcher {
    boolean accept(int index, String value, String reference);

    boolean skip(int index, String value, String reference);

    boolean match(int index, String value, String reference);

    default boolean acceptReplace(int index, String value) {
        return false;
    }

    default String replace(int index, String value) {
        return value;
    }
}