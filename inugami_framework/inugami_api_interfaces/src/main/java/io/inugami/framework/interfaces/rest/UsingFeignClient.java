package io.inugami.framework.interfaces.rest;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UsingFeignClient {
    Class<?> feignConfigurationBean() default None.class;

    @SuppressWarnings({"java:S2094"})
    public static class None {
    }
}
