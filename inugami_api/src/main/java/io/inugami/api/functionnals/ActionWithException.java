package io.inugami.api.functionnals;

@FunctionalInterface
public interface ActionWithException {
    <T> T process() throws Throwable;
}
