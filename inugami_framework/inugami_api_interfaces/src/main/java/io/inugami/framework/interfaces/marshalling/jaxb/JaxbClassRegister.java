package io.inugami.framework.interfaces.marshalling.jaxb;

import java.util.List;

@FunctionalInterface
public interface JaxbClassRegister {
    List<Class<?>> register();
}
