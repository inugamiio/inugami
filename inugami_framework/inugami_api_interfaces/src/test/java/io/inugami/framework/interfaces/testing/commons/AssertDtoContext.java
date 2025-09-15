package io.inugami.framework.interfaces.testing.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
public class AssertDtoContext<T> {
    private Class<? extends T> objectClass;
    private Function<T, T>     cloneFunction;
    private Supplier<T>        noArgConstructor;
    private Supplier<T>        fullArgConstructor;
    private String             fullArgConstructorRefPath;
    private String             getterRefPath;
    private String             toStringRefPath;
    private Consumer<T>        noEqualsFunction;
    private Consumer<T>        equalsFunction;
    private boolean            checkSetters;
    private boolean            checkSerialization = true;
    private boolean            checkEquals        = true;

    @Override
    public String toString() {
        return objectClass.getName();
    }


}
