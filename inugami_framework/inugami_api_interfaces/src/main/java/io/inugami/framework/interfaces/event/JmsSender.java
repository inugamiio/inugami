package io.inugami.framework.interfaces.event;


import io.inugami.framework.interfaces.monitoring.partner.Partner;

import java.lang.annotation.*;
@Partner(type = "JMS")
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JmsSender {
    String id() default "";

    String destination();

}
