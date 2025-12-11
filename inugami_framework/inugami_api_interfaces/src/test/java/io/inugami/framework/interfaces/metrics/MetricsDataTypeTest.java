package io.inugami.framework.interfaces.metrics;

import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

class MetricsDataTypeTest {
    @Test
    void assertEnum() {
        UnitTestHelper.assertEnum(MetricsDataType.class,
                                  """
                                          {
                                            "CUMULATIVE" : { },
                                            "AVG" : { },
                                            "FIX" : { }
                                          }
                                          """);
    }
}