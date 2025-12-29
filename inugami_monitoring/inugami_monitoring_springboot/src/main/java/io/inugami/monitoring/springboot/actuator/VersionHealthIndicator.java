package io.inugami.monitoring.springboot.actuator;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

import java.util.Optional;

@Builder
@RequiredArgsConstructor
public class VersionHealthIndicator implements HealthIndicator {
    private static final String UNDEFINED   = "undefined";
    public static final  String GROUP_ID    = "groupId";
    public static final  String ARTIFACT_ID = "artifactId";
    public static final  String VERSION     = "version";
    public static final  String COMMIT_ID   = "commitId";
    public static final  String COMMIT_DATE = "commitDate";

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
                .withDetail(GROUP_ID, Optional.ofNullable(groupId).orElse(UNDEFINED))
                .withDetail(ARTIFACT_ID, Optional.ofNullable(artifactId).orElse(UNDEFINED))
                .withDetail(VERSION, Optional.ofNullable(version).orElse(UNDEFINED))
                .withDetail(COMMIT_ID, Optional.ofNullable(commitId).orElse(UNDEFINED))
                .withDetail(COMMIT_DATE, Optional.ofNullable(commitDate).orElse(UNDEFINED))
                .build();
    }

}
