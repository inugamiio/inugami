package io.inugami.commons.marshaling;

import java.util.List;

public interface XmlJaxbMarshallerClassRegisterSpi {
    List<Class<?>> register();
}
