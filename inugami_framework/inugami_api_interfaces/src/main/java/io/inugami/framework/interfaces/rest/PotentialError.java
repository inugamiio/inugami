package io.inugami.framework.interfaces.rest;

import java.lang.annotation.*;

@Target({
        ElementType.PARAMETER,
        ElementType.METHOD,
        ElementType.TYPE,
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PotentialError {
    String errorCode();

    Class<?> errorCodeClass() default NONE.class;

    Class<? extends Exception> throwsAs() default NONE_EXCEPTION.class;

    int httpStatus() default 500;

    String errorMessage() default "";

    String errorMessageDetail() default "";

    String payload() default "";

    String description() default "";

    String example() default "";
    String domain() default "";
    String subDomain() default "";

    String url() default "";

    String type() default "technical";

    @SuppressWarnings({"java:S2094", "java:S101"})
    public static class NONE {

    }

    @SuppressWarnings({"java:S2094", "java:S101"})
    public static class NONE_EXCEPTION extends Exception {

    }
}
