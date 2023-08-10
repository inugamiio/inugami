package io.inugami.monitoring.springboot.actuator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.StatusAggregator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FailSafeStatusAggregatorTest {

    @Test
    void getAggregateStatus_nominal() {
        final StatusAggregator aggregator = new FailSafeStatusAggregator();

        assertThat(aggregator.getAggregateStatus(Status.UP, Status.UP, Status.UP)).isEqualTo(Status.UP);
        assertThat(aggregator.getAggregateStatus(Status.UNKNOWN, Status.UP, Status.UNKNOWN)).isEqualTo(Status.UP);
        assertThat(aggregator.getAggregateStatus(Status.UNKNOWN, Status.UNKNOWN)).isEqualTo(Status.UNKNOWN);
        assertThat(aggregator.getAggregateStatus(Status.UP, Status.DOWN, Status.UP)).isEqualTo(FailSafeStatusAggregator.DEGRADED);
        assertThat(aggregator.getAggregateStatus(Status.DOWN, Status.DOWN)).isEqualTo(FailSafeStatusAggregator.DEGRADED);
    }

}