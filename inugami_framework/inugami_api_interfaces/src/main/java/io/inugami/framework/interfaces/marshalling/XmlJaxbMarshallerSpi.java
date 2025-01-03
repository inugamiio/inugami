package io.inugami.framework.interfaces.marshalling;

public interface XmlJaxbMarshallerSpi {

    String convertToXml(final Object value);

    <T> T convertFromXml(final String value);
}
