package io.inugami.commons.marshaling.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> implements JaxbAdapterSpi {
    @Override
    public LocalDateTime unmarshal(final String value) throws Exception {
        return LocalDateTime.parse(value);
    }

    @Override
    public String marshal(final LocalDateTime value) throws Exception {
        return value == null ? null : value.format(DateTimeFormatter.ISO_DATE_TIME);
    }


    @Override
    public XmlAdapter<?, ?> getAdapter() {
        return this;
    }
}