package io.inugami.framework.interfaces.documentation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Glossaries {
    Glossary[] value() default {};
}