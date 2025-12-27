open module ${package}.api {
    requires lombok;
    requires jdk.compiler;
    requires org.jspecify;
    requires io.inugami.framework.interfaces;

    exports ${package}.api.domain.user;
    exports ${package}.api.domain.user.dto;
    exports ${package}.api.domain.user.exception;
}
