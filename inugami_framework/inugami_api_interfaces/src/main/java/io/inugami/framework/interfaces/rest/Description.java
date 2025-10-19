package io.inugami.framework.interfaces.rest;

import java.lang.annotation.*;

@Target({
        ElementType.PARAMETER,
        ElementType.METHOD,
        ElementType.TYPE,
        ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Description {
    String value() default "";

    String example() default "";

    String url() default "";

    PotentialError[] potentialErrors() default {};

}
