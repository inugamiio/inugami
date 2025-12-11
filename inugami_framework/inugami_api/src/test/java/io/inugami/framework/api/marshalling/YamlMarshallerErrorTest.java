package io.inugami.framework.api.marshalling;

import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

class YamlMarshallerErrorTest {
    @Test
    void assertYamlMarshallerError() {
        UnitTestHelper.assertErrorCodeUnique(YamlMarshallerError.values());
        UnitTestHelper.assertErrorCode("io/inugami/framework/api/marshalling/assertYamlMarshallerError.json",
                                       YamlMarshallerError.values());
    }
}