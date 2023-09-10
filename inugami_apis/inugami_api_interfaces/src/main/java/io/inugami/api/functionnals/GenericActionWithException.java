package io.inugami.api.functionnals;

@SuppressWarnings({"java:S112"})
@FunctionalInterface
public interface GenericActionWithException<T> {
    T process() throws Throwable;
}
