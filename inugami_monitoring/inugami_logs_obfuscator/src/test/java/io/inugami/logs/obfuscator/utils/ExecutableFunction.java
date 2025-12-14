package io.inugami.logs.obfuscator.utils;

@SuppressWarnings({"java:S112"})
@FunctionalInterface
public interface ExecutableFunction {
    void execute() throws Throwable;
}
