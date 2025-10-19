package io.inugami.framework.interfaces.rest;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebServiceContextInfo {
    String rootContext() default "";

    String authMethod() default "";

    String urlPattern() default "";

    String virtualHost() default "";

    String transportGuarantee() default "";

    boolean secureWSDLAccess() default false;

    String realmName() default "";

    String consume() default "application/xml";

    String produce() default "application/xml";

    String encoding() default "UTF-8";

    String description() default "";
}
