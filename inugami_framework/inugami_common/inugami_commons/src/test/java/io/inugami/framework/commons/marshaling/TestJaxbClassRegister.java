package io.inugami.framework.commons.marshaling;


import io.inugami.framework.commons.marshaling.jaxb.JaxbClassRegister;

import java.util.List;

public class TestJaxbClassRegister implements JaxbClassRegister {
    @Override
    public List<Class<?>> register() {
        return List.of(
                UserDTOXml.class
        );
    }
}
