package io.inugami.framework.commons.marshaling;

import io.inugami.commons.test.UnitTestHelper;
import io.inugami.framework.interfaces.marshalling.XmlJaxbMarshallerSpi;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class DefaultXmlJaxbMarshallerSpiTest {
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2023-04-10T11:39:00.000Z"), ZoneId.of("Europe/Paris"));

    @Test
    void convertToXml_nominal() {
        final XmlJaxbMarshallerSpi marshaller = new DefaultXmlJaxbMarshallerSpi();
        final String xml = marshaller.convertToXml(UserDTOXml.builder()
                                                             .id(1L)
                                                             .firstName("John")
                                                             .lastName("Smith")
                                                             .creationDate(LocalDateTime.now(CLOCK))
                                                             .build());
        assertThat(xml).isNotNull();

        UnitTestHelper.assertTextRelative(xml, "commons/marshaling/defaultXmlJaxbMarshallerSpiTest/convertToXml_nominal.xml");
    }


    @Test
    void convertFromXml_nominal() {
        final XmlJaxbMarshallerSpi marshaller = new DefaultXmlJaxbMarshallerSpi();

        final UserDTOXml user = marshaller.convertFromXml(UnitTestHelper.readFileRelative("commons/marshaling/defaultXmlJaxbMarshallerSpiTest/convertToXml_nominal.xml"));
        UnitTestHelper.assertTextRelative(user, "commons/marshaling/defaultXmlJaxbMarshallerSpiTest/convertFromXml_nominal.json");

    }


}