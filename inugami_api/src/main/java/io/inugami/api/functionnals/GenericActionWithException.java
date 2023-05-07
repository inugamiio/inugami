package io.inugami.api.functionnals;

@FunctionalInterface
public interface GenericActionWithException<T> {
    T process() throws Throwable;
}
