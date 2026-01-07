open module ${package}.webapp {
    requires ${package}.api;
    requires ${package}.core;
    requires ${package}.infrastructure;
    requires ${package}.interfaces.api;
    requires ${package}.interfaces.core;
    requires io.inugami.framework.api;
    requires io.inugami.framework.commons.spring.data;
    requires io.inugami.framework.commons.spring;
    requires io.inugami.framework.commons;
    requires io.inugami.framework.interfaces;
    requires io.inugami.monitoring.springboot;
    requires io.inugami.monitoring.sensors.defaults;
    requires io.inugami.monitoring.providers.logs;
    requires lombok;
    requires org.mapstruct;
    requires org.slf4j;
    requires org.springdoc.openapi.ui;
    requires org.springdoc.openapi.webmvc.core;
    requires spring.boot.autoconfigure;
    requires spring.boot.starter.actuator;
    requires spring.boot.starter.tomcat;
    requires spring.boot;
    requires spring.context;
}
