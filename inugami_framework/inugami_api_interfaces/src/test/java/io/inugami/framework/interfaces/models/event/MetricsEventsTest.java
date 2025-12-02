package io.inugami.framework.interfaces.models.event;

import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.testing.commons.SkipLineMatcher;
import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.models.event.MetricsEvents.isRunning;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class MetricsEventsTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String EVENT = "event";

    @BeforeEach
    public void init() {
        MetricsEvents.clear();
    }

    // =================================================================================================================
    // UTILITY CLASS
    // =================================================================================================================
    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(MetricsEvents.class);
    }


    // =================================================================================================================
    // onStart
    // =================================================================================================================
    @Test
    void onStart_nominal() {
        MetricsEvents.onStart(buildGav(), EVENT);
        assertText(MetricsEvents.getStates(), """
                [ {
                  "delais" : 0,
                  "end" : 0,
                  "eventName" : "io.inugami:inugami_api:3.3.0:jar_event",
                  "running" : true,
                  "start" : 1764644167123
                } ]
                """, SkipLineMatcher.of(5));

        MetricsEvents.onStart(buildGav(), EVENT);
    }

    @Test
    void onStop_nominal() {
        MetricsEvents.onStart(buildGav(), EVENT);
        MetricsEvents.onStop(buildGav(), EVENT);
        //
        MetricsEvents.onStop(buildGav(), "other");
        //
        assertText(MetricsEvents.getStates(), """
                [ {
                     "delais" : 0,
                     "end" : 1764644330312,
                     "eventName" : "io.inugami:inugami_api:3.3.0:jar_event",
                     "running" : false,
                     "start" : 1764644330312
                   }, {
                     "delais" : 0,
                     "end" : 1764644330312,
                     "eventName" : "io.inugami:inugami_api:3.3.0:jar_other",
                     "running" : false,
                     "start" : 1764644330312
                   } ]
                """, SkipLineMatcher.of(2,5,8,11));

        MetricsEvents.onStop(buildGav(), EVENT);
    }

    @Test
    void isRunning_nominal() {
        final var event = SimpleEvent.builder().name(EVENT).build();
        assertThat(isRunning(buildGav(),event)).isFalse();

        MetricsEvents.onStart(buildGav(), EVENT);
        assertThat(isRunning(buildGav(),event)).isTrue();

        MetricsEvents.onStop(buildGav(), EVENT);
        assertThat(isRunning(buildGav(),event)).isFalse();
    }
    // =================================================================================================================
    // DATA
    // =================================================================================================================
    private Gav buildGav() {
        return Gav.builder().groupId("io.inugami").artifactId("inugami_api").version("3.3.0").qualifier("jar").build();
    }
}