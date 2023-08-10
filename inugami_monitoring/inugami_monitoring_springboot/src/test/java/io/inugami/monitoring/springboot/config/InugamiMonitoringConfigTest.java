package io.inugami.monitoring.springboot.config;

import io.inugami.monitoring.springboot.actuator.FailSafeStatusAggregator;
import io.inugami.monitoring.springboot.exception.SpringDefaultErrorCodeResolver;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.StatusAggregator;

import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class InugamiMonitoringConfigTest {

    @Test
    void springDefaultErrorCodeResolver_nominal() {
        final InugamiMonitoringConfig config = new InugamiMonitoringConfig();
        assertThat(config.springDefaultErrorCodeResolver()).isNotNull();
        assertThat(config.springDefaultErrorCodeResolver()).isInstanceOf(SpringDefaultErrorCodeResolver.class);
    }


    @Test
    void versionHealthIndicator_nominal() {
        final InugamiMonitoringConfig config = new InugamiMonitoringConfig();
        final HealthIndicator         health = config.versionHealthIndicator("io.inugami", "my-application", "1.0.0", "ff79c3f", "2023-04-09T13:15:23");
        assertTextRelative(health.health(), "actuator/versionHealthIndicatorTest/getHealth_nominal.json");
        assertTextRelative(health.getHealth(true), "actuator/versionHealthIndicatorTest/getHealth_nominal.json");
        assertTextRelative(health.getHealth(true), "actuator/versionHealthIndicatorTest/getHealth_nominal.json");
    }


    @Test
    void failSafeStatusAggregator_nominal() {
        final InugamiMonitoringConfig config     = new InugamiMonitoringConfig();
        final StatusAggregator        aggregator = config.failSafeStatusAggregator();
        assertThat(aggregator).isNotNull();
        assertThat(aggregator).isInstanceOf(FailSafeStatusAggregator.class);
    }
}