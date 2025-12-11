package io.inugami.framework.commons.testing.marshaling;

import io.inugami.commons.test.UnitTestHelper;
import io.inugami.framework.commons.marshaling.DefaultXmlJaxbMarshallerSpiError;
import org.junit.jupiter.api.Test;

class DefaultXmlJaxbMarshallerSpiErrorTest {

    @Test
    void assertDefaultXmlJaxbMarshallerSpiError() {
        UnitTestHelper.assertErrorCodeUnique(DefaultXmlJaxbMarshallerSpiError.values());
        UnitTestHelper.assertErrorCode("commons/marshaling/assertDefaultXmlJaxbMarshallerSpiError.json",
                                       DefaultXmlJaxbMarshallerSpiError.values());
    }
}