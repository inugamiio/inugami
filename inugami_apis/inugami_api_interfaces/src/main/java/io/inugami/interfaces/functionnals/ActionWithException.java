package io.inugami.interfaces.functionnals;

@SuppressWarnings({"java:S112"})
@FunctionalInterface
public interface ActionWithException {
    <T> T process() throws Throwable;
}
