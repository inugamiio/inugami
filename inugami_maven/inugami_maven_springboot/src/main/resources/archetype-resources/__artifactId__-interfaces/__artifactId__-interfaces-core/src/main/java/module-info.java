open module ${package}.interfaces.core {
    requires io.inugami.framework.interfaces;
    requires lombok;
    requires org.mapstruct;
    requires org.slf4j;
    requires spring.context;
    requires spring.web;
    //
    requires ${package}.api;
    requires ${package}.interfaces.api;

    exports ${package}.interfaces.core.domain.user;
    exports ${package}.interfaces.core.domain.user.mapper;
}
