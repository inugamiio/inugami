package io.inugami.framework.interfaces.marshalling.jaxb;


import jakarta.xml.bind.annotation.adapters.XmlAdapter;

@SuppressWarnings({"java:S1452"})
public interface JaxbAdapterSpi {

    XmlAdapter<?, ?> getAdapter();
}
