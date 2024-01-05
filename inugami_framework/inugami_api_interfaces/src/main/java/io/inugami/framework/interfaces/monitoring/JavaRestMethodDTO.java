package io.inugami.framework.interfaces.monitoring;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Method;

@Getter
@Builder(toBuilder = true)
@ToString
@RequiredArgsConstructor
public class JavaRestMethodDTO {
    private final Class<?> restClass;
    private final Method   restMethod;
}
