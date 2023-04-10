package io.inugami.commons.marshaling;

import io.inugami.api.listeners.ApplicationLifecycleSPI;
import io.inugami.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.api.spi.SpiLoader;

public class XmlJaxbMarshallerSpiFactory implements XmlJaxbMarshallerSpi, ApplicationLifecycleSPI {

    private XmlJaxbMarshallerSpi marshaller;

    private static final XmlJaxbMarshallerSpiFactory INSTANCE = new XmlJaxbMarshallerSpiFactory();

    public static XmlJaxbMarshallerSpiFactory getInstance() {
        return INSTANCE;
    }

    private XmlJaxbMarshallerSpiFactory() {
        DefaultApplicationLifecycleSPI.register(this);
        onContextRefreshed(null);
    }

    @Override
    public void onContextRefreshed(final Object event) {
        marshaller = SpiLoader.getInstance().loadSpiSingleServicesByPriority(XmlJaxbMarshallerSpi.class);
    }

    @Override
    public String convertToXml(final Object value) {
        return marshaller.convertToXml(value);
    }

    @Override
    public <T> T convertFromXml(final String value) {
        return marshaller.convertFromXml(value);
    }
}
