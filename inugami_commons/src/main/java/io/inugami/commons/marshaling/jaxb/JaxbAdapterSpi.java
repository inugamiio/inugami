package io.inugami.commons.marshaling.jaxb;


import javax.xml.bind.annotation.adapters.XmlAdapter;

public interface JaxbAdapterSpi {

    XmlAdapter<?, ?> getAdapter();
}
