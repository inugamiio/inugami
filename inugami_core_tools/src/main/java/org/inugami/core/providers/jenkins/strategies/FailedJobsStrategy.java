package org.inugami.core.providers.jenkins.strategies;

import java.util.List;
import java.util.stream.Collectors;

import org.inugami.core.providers.jenkins.models.JenkinsInformation;
import org.inugami.core.providers.jenkins.models.JenkinsJob;

public class FailedJobsStrategy implements JenkinsJobResolverStrategy{
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String QUERY_JOBS_FAILED = "@failed";

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public FailedJobsStrategy() {}

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
