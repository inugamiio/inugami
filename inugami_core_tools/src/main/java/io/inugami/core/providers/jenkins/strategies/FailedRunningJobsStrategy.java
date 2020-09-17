package io.inugami.core.providers.jenkins.strategies;

import java.util.List;
import java.util.stream.Collectors;

import io.inugami.core.providers.jenkins.models.JenkinsInformation;
import io.inugami.core.providers.jenkins.models.JenkinsJob;

public class FailedRunningJobsStrategy implements JenkinsJobResolverStrategy {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String QUERY_JOBS_FAILED_AND_RUNNING = "@failedAndRunning";

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public FailedRunningJobsStrategy() {}

    // =========================================================================
    // METHODS
    // =========================================================================
    public boolean accept(String query, JenkinsInformation eventData) {
        return query != null && eventData != null && query.equals(QUERY_JOBS_FAILED_AND_RUNNING);
    }

    public JenkinsInformation process(JenkinsInformation eventData) {
        List<JenkinsJob> jobsFiltered = eventData.getJobs().stream()
                .filter(this::toFailedRunningJob)
                .collect(Collectors.toList());
        return new JenkinsInformation(jobsFiltered);
    }

    private boolean toFailedRunningJob(JenkinsJob item) {
        return item.getColor() != null && (item.getColor().equals("red") 
        		|| item.getColor().matches("(.*)_anime"));
    }

}
