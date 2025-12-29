package io.inugami.monitoring.springboot.actuator;

import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.StatusAggregator;

import java.util.*;

public class FailSafeStatusAggregator implements StatusAggregator {
    public static final  Status               DEGRADED      = new Status("DEGRADED");
    private static final Map<Status, Integer> STATUS_LEVELS = Map.ofEntries(
            Map.entry(Status.UNKNOWN, 0),
            Map.entry(Status.UP, 1),
            Map.entry(Status.DOWN, 2),
            Map.entry(Status.OUT_OF_SERVICE, 3)
                                                                           );

    @Override
    public Status getAggregateStatus(final Status... statuses) {
        return getAggregateStatus(new LinkedHashSet<>(Arrays.asList(statuses)));
    }

    @Override
    public Status getAggregateStatus(final Set<Status> statuses) {
        int currentLevel = -1;


        for (final Status status : Optional.ofNullable(statuses).orElse(Set.of())) {
            final Integer statusLevel = STATUS_LEVELS.get(status);
            if (statusLevel != null && statusLevel > currentLevel) {
                currentLevel = statusLevel;
            }
        }

        Status result = null;
        if (currentLevel < 1) {
            result = Status.UNKNOWN;
        } else if (currentLevel == 1) {
            result = Status.UP;
        } else {
            result = DEGRADED;
        }
        return result;
    }
}
