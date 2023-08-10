package io.inugami.monitoring.springboot.actuator;

import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;

class VersionHealthIndicatorTest {

    @Test
    void getHealth_nominal() {
        final VersionHealthIndicator health = VersionHealthIndicator.builder()
                                                                    .groupId("io.inugami")
                                                                    .artifactId("my-application")
                                                                    .version("1.0.0")
                                                                    .commitId("ff79c3f")
                                                                    .commitDate("2023-04-09T13:15:23")
                                                                    .build();

        assertTextRelative(health.health(), "actuator/versionHealthIndicatorTest/getHealth_nominal.json");
        assertTextRelative(health.getHealth(true), "actuator/versionHealthIndicatorTest/getHealth_nominal.json");
        assertTextRelative(health.getHealth(true), "actuator/versionHealthIndicatorTest/getHealth_nominal.json");
    }
}