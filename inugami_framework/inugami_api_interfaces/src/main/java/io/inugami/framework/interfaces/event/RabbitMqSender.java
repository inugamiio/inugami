package io.inugami.framework.interfaces.event;


import io.inugami.framework.interfaces.monitoring.partner.Partner;

import java.lang.annotation.*;

@Partner(type = "RABBIT_MQ")
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RabbitMqSender {
    String id() default "";

    String echangeName() default "";

    String queue() default "";

    String routingKey() default "";
}
