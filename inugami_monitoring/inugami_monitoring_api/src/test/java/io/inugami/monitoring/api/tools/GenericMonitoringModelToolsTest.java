package io.inugami.monitoring.api.tools;

import io.inugami.commons.test.UnitTestHelper;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.framework.api.monitoring.RequestContext;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.monitoring.api.tools.GenericMonitoringModelTools.*;
import static org.assertj.core.api.Assertions.assertThat;

class GenericMonitoringModelToolsTest {

    // =================================================================================================================
    // INIT
    // =================================================================================================================
    @BeforeEach
    public void init() {
        RequestContext.setInstance(null);
    }

    // =================================================================================================================
    // UTILITY CLASS
    // =================================================================================================================
    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClass(GenericMonitoringModelTools.class);
    }


    // =================================================================================================================
    // TEST
    // =================================================================================================================
    @Test
    void initResultBuilder_nominal() {
        assertText(initResultBuilder(),
                   """
                           {
                             "nonTemporalHash" : ":::::::technical_main:::",
                             "service" : "technical_main",
                             "time" : 0,
                             "timestamp" : 1765741891000
                           }
                           """,
                   SkipLineMatcher.of(4));

        RequestContext.setInstance(RequestData.builder()
                                              .env("test")
                                              .asset("inugami")
                                              .instanceName("inu")
                                              .instanceNumber("001")
                                              .service("user")
                                              .deviceType("desktop")
                                              .build());

        assertText(initResultBuilder(),
                   """
                           {
                             "asset" : "inugami",
                             "device" : "desktop",
                             "environment" : "test",
                             "instanceName" : "inu",
                             "instanceNumber" : "001",
                             "nonTemporalHash" : "inugami:test:inu:001::desktop::user:::",
                             "service" : "user",
                             "time" : 0,
                             "timestamp" : 1765741916000
                           }
                           """,
                   SkipLineMatcher.of(9));
    }

    @Test
    void buildSingleResult_nominal() {
        assertText(buildSingleResult(initResultBuilder().toBuilder()
                                                        .value(15)
                                                        .build()),
                   """
                           [ {
                             "nonTemporalHash" : ":::::::technical_main:::",
                             "service" : "technical_main",
                             "time" : 0,
                             "timestamp" : 1765742055000,
                             "value" : 15
                           } ]
                           """,
                   SkipLineMatcher.of(4));

        assertText(buildSingleResult(null),
                   """
                           [ ]
                           """);
    }

    @Test
    void getPercentilValues_nominal() {
        final List<Long> values = List.of(1L, 3L, 5L, 6L, 10L, 8L, 9L, 4L);
        assertThat(getPercentilValues(values, 0)).isEqualTo(1L);
        assertThat(getPercentilValues(values, 1)).isEqualTo(10L);
        assertThat(getPercentilValues(values, 0.5)).isEqualTo(6L);
        assertThat(getPercentilValues(values, 0.9)).isEqualTo(10L);

        final List<Integer> nullValues = null;
        assertThat(getPercentilValues(nullValues, 0.9, null)).isNull();
        assertThat(getPercentilValues(new ArrayList<Integer>(), 0.9, null)).isNull();
        assertThat(getPercentilValues(values, 2, null)).isNull();
        assertThat(getPercentilValues(values, -1, null)).isNull();
    }

    @Test
    void buildTimeUnit_nominal() {
        assertThat(buildTimeUnit("ms", 0)).isEqualTo("ms");
        assertThat(buildTimeUnit(null, 1000)).isEqualTo("1000ms");
    }
}