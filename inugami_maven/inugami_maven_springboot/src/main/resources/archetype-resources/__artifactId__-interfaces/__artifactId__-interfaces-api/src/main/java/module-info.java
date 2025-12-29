open module ${package}.interfaces.api {
    requires com.fasterxml.jackson.annotation;
    requires io.inugami.framework.interfaces;
    requires io.swagger.v3.oas.annotations;
    requires lombok;
    requires org.slf4j;
    requires spring.web;

    exports ${package}.interfaces.api.domain.user;
    exports ${package}.interfaces.api.domain.user.dto;
}
