package io.inugami.framework.commons.marshaling.jaxb;

import io.inugami.framework.interfaces.marshalling.jaxb.JaxbClassRegister;

import java.util.List;

public class DefaultJaxbClassRegister implements JaxbClassRegister {
    @Override
    public List<Class<?>> register() {
        return List.of(
        );
    }
}
