package io.inugami.framework.interfaces.rest;

import java.lang.annotation.*;

@Target({
        ElementType.PARAMETER,
        ElementType.METHOD,
        ElementType.TYPE,
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PotentialErrors {
    PotentialError[] value();
}
