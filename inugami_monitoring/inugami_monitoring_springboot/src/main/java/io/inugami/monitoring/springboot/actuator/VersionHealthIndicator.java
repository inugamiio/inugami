package io.inugami.monitoring.springboot.actuator;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

@Builder
@RequiredArgsConstructor
public class VersionHealthIndicator implements HealthIndicator {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String commitId;
    private final String commitDate;


    @Override
    public Health health() {
        return getHealth(false);
    }

    @Override
    public Health getHealth(final boolean includeDetails) {
        return new Health.Builder()
                .status(Status.UP)
                .withDetail("groupId", groupId)
                .withDetail("artifactId", artifactId)
                .withDetail("version", version)
                .withDetail("commitId", commitId)
                .withDetail("commitDate", commitDate)
                .build();
    }

}
