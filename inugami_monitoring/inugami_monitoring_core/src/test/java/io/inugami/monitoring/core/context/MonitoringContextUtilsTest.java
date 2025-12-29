package io.inugami.monitoring.core.context;

import io.inugami.commons.test.UnitTestHelper;
import io.inugami.commons.test.api.UuidLineMatcher;
import io.inugami.framework.api.monitoring.MdcService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertText;

class MonitoringContextUtilsTest {
    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
    }

    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(MonitoringContextUtils.class);
    }

    @Test
    void getTrackingInformation_nominal() {
        assertText(MonitoringContextUtils.getTrackingInformation(null),
                   """
                           {
                             "x-correlation-id" : "8014a01a-03e4-4fa9-b377-c6d8143dce64",
                             "x-b3-traceid" : "f78a1997-96b7-49dc-a978-4c632b60835e"
                           }
                           """,
                   UuidLineMatcher.of(1, 2));
    }
}