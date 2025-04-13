package io.inugami.monitoring.springboot.actuator;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

@Builder
@RequiredArgsConstructor
public class VersionHealthIndicator implements HealthIndicator {
    private static final String UNDEFINED = "undefined";
    private final        String groupId;
    private final        String artifactId;
    private final        String version;
    private final        String commitId;
    private final        String commitDate;


    @Override
    public Health health() {
        return getHealth(false);
    }

    @Override
    public Health getHealth(final boolean includeDetails) {
        return new Health.Builder()
                .status(Status.UP)
                .withDetail("groupId", groupId == null ? UNDEFINED : groupId)
                .withDetail("artifactId", artifactId == null ? UNDEFINED : artifactId)
                .withDetail("version", version == null ? UNDEFINED : version)
                .withDetail("commitId", commitId == null ? UNDEFINED : commitId)
                .withDetail("commitDate", commitDate == null ? UNDEFINED : commitDate)
                .build();
    }

}
