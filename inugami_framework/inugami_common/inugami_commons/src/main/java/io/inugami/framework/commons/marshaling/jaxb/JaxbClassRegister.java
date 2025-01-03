package io.inugami.framework.commons.marshaling.jaxb;

import java.util.List;

@FunctionalInterface
public interface JaxbClassRegister {
    List<Class<?>> register();
}
