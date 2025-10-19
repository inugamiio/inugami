package io.inugami.framework.interfaces.event;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JmsEvent {
    Class<?> value() default None.class;

    @SuppressWarnings({"java:S2094"})
    public static class None {

    }
}
