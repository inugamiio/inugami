package io.inugami.commons.marshaling;

public interface XmlJaxbMarshallerSpi {

    String convertToXml(final Object value);

    <T> T convertFromXml(final String value);
}
