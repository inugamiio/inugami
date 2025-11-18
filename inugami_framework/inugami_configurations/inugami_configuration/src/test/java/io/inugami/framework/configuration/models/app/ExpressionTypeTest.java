package io.inugami.framework.configuration.models.app;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

class ExpressionTypeTest {
    @Test
    void assertEnum() {
        UnitTestHelper.assertEnum(ExpressionType.class,
                                  """
                                          {
                                            "EXACT" : { },
                                            "REGEX" : { }
                                          }
                                          """);
    }
}