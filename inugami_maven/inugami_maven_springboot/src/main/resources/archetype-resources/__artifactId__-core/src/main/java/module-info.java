open module ${package}.core {
    requires com.fasterxml.jackson.annotation;
    requires io.inugami.framework.commons.spring;
    requires io.inugami.framework.commons;
    requires io.inugami.framework.configurations.configuration;
    requires io.inugami.framework.api;
    requires io.inugami.framework.interfaces;
    requires jakarta.annotation;
    requires jdk.compiler;
    requires lombok;
    requires org.jspecify;
    requires org.slf4j;
    requires spring.beans;
    requires spring.boot;
    requires spring.context;
    requires spring.tx;
    //
    requires ${package}.api;
    //
    exports ${package}.core.domain.user;
}
