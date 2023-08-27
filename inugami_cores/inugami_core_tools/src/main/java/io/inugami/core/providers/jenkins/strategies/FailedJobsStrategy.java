package io.inugami.core.providers.jenkins.strategies;

import io.inugami.core.providers.jenkins.models.JenkinsInformation;
import io.inugami.core.providers.jenkins.models.JenkinsJob;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class FailedJobsStrategy implements JenkinsJobResolverStrategy {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String QUERY_JOBS_FAILED = "@failed";


    // =========================================================================
    // METHODS
    // =========================================================================
    public boolean accept(String query, JenkinsInformation eventData) {
        return query != null && eventData != null && query.equals(QUERY_JOBS_FAILED);
    }

    public JenkinsInformation process(JenkinsInformation eventData) {
        List<JenkinsJob> jobsFiltered = eventData.getJobs().stream()
                                                 .filter(this::toFailedJob)
                                                 .collect(Collectors.toList());
        return new JenkinsInformation(jobsFiltered);
    }

    private boolean toFailedJob(JenkinsJob item) {
        return item.getColor() != null && item.getColor().equals("red");
    }
}
