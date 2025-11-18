package io.inugami.framework.configuration.models.plugins;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

class RessourceTypeTest {
    @Test
    void assertEnum() {
        UnitTestHelper.assertEnum(RessourceType.class,
                                  """
                                          {
                                            "CSS" : { },
                                            "JAVASCRIPT" : { },
                                            "PAGE" : { }
                                          }
                                          """);
    }
}