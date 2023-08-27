package io.inugami.commons.marshaling;

import io.inugami.api.exceptions.UncheckedException;
import io.inugami.api.listeners.ApplicationLifecycleSPI;
import io.inugami.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.api.spi.SpiLoader;
import io.inugami.commons.marshaling.jaxb.JaxbClassRegister;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"java:S1181", "java:S1141", "java:S1181"})
@Slf4j
public class DefaultXmlJaxbMarshallerSpi implements XmlJaxbMarshallerSpi, ApplicationLifecycleSPI {

    private JAXBContext context;

    public DefaultXmlJaxbMarshallerSpi() {
        try {
            DefaultApplicationLifecycleSPI.register(this);
        } catch (final Throwable e) {
            log.error(e.getMessage(), e);
        }

        onContextRefreshed(null);
    }

    @Override
    public void onContextRefreshed(final Object event) {
        try {
            List<JaxbClassRegister> jaxbRegister = SpiLoader.getInstance()
                                                            .loadSpiServicesByPriority(JaxbClassRegister.class);

            final Set<Class<?>> classes = new LinkedHashSet<>();
            for (final JaxbClassRegister currentJaxbRegister : jaxbRegister) {
                final List<Class<?>> currentClasses = currentJaxbRegister.register();
                if (currentClasses != null) {
                    classes.addAll(currentClasses);
                }
            }
            try {
                context = JAXBContext.newInstance(classes.toArray(new Class<?>[]{}));
            } catch (final JAXBException e) {
                throw new UncheckedException(e.getMessage(), e);
            }

        } catch (final Throwable e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public String convertToXml(final Object value) {
        if (value == null) {
            return null;
        }
        final StringWriter writer = new StringWriter();

        try {
            final Marshaller marshaller = buildMarshaller();
            marshaller.marshal(value, writer);
        } catch (final JAXBException e) {
            throw new UncheckedException(DefaultXmlJaxbMarshallerSpiError.MARSHALLING_ERROR.addDetail(e.getMessage()), e);
        }
        return writer.toString();
    }


    @Override
    public <T> T convertFromXml(final String value) {
        if (value == null) {
            return null;
        }

        T                  result = null;
        final StringReader reader = new StringReader(value);

        try {
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            result = (T) unmarshaller.unmarshal(reader);
        } catch (final JAXBException e) {
            throw new UncheckedException(DefaultXmlJaxbMarshallerSpiError.UNMARSHALLING_ERROR.addDetail(e.getMessage()), e);
        }
        return result;
    }


    // ========================================================================
    // TOOLS
    // ========================================================================
    private Marshaller buildMarshaller() throws JAXBException {
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return marshaller;
    }
}
