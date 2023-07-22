package io.inugami.commons.marshaling.jaxb;


import javax.xml.bind.annotation.adapters.XmlAdapter;

@SuppressWarnings({"java:S1452"})
public interface JaxbAdapterSpi {

    XmlAdapter<?, ?> getAdapter();
}
