package io.inugami.api.documentation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Glossary {
    String value() default "";

    String description() default "";

    String language() default "";
}
