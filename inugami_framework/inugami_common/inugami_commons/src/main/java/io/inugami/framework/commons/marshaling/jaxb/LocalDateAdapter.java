package io.inugami.framework.commons.marshaling.jaxb;


import io.inugami.framework.interfaces.marshalling.jaxb.JaxbAdapterSpi;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> implements JaxbAdapterSpi {
    @Override
    public LocalDate unmarshal(final String value) throws Exception {
        return LocalDate.parse(value);
    }

    @Override
    public String marshal(final LocalDate value) throws Exception {
        return value == null ? null : value.format(DateTimeFormatter.ISO_DATE);
    }


    @Override
    public XmlAdapter<?, ?> getAdapter() {
        return this;
    }
}