package io.inugami.framework.commons.testing.marshaling;


import io.inugami.framework.interfaces.marshalling.jaxb.JaxbClassRegister;

import java.util.List;

public class TestJaxbClassRegister implements JaxbClassRegister {
    @Override
    public List<Class<?>> register() {
        return List.of(
                UserDTOXml.class
        );
    }
}
