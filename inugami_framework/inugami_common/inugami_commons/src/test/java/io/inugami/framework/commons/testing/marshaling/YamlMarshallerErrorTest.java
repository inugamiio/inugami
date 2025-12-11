package io.inugami.framework.commons.testing.marshaling;

import io.inugami.commons.test.UnitTestHelper;
import io.inugami.framework.commons.marshaling.YamlMarshallerError;
import org.junit.jupiter.api.Test;

class YamlMarshallerErrorTest {
    @Test
    void assertYamlMarshallerError() {
        UnitTestHelper.assertErrorCodeUnique(YamlMarshallerError.values());
        UnitTestHelper.assertErrorCode("commons/marshaling/assertYamlMarshallerError.json",
                                       YamlMarshallerError.values());
    }
}