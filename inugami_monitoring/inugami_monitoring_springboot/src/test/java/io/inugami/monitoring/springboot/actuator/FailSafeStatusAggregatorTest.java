package io.inugami.monitoring.springboot.actuator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Status;

import static io.inugami.monitoring.springboot.actuator.FailSafeStatusAggregator.DEGRADED;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FailSafeStatusAggregatorTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @InjectMocks
    private FailSafeStatusAggregator aggregator;

    // =================================================================================================================
    // TEST
    // =================================================================================================================
    @Test
    void getAggregateStatus_nominal() {
        assertThat(aggregator.getAggregateStatus(Status.UNKNOWN)).isEqualTo(Status.UNKNOWN);
        assertThat(aggregator.getAggregateStatus(Status.UNKNOWN, Status.UP)).isEqualTo(Status.UP);

        assertThat(aggregator.getAggregateStatus(Status.UNKNOWN, Status.DOWN, Status.UP)).isEqualTo(DEGRADED);
        assertThat(aggregator.getAggregateStatus(Status.UNKNOWN, Status.DOWN, Status.OUT_OF_SERVICE, Status.UP)).isEqualTo(DEGRADED);
    }

}