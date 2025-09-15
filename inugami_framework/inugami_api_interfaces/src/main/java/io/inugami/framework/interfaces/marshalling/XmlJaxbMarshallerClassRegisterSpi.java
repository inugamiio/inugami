package io.inugami.framework.interfaces.marshalling;

import java.util.List;

public interface XmlJaxbMarshallerClassRegisterSpi {
    List<Class<?>> register();
}
