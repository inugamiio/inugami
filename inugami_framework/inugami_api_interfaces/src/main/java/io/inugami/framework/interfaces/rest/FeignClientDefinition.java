package io.inugami.framework.interfaces.rest;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeignClientDefinition {
    Class<?> value() default None.class;

    @SuppressWarnings({"java:S2094"})
    public static class None {
    }
}
