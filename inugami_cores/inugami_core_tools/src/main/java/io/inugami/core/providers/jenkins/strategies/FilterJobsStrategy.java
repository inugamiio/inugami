package io.inugami.core.providers.jenkins.strategies;

import io.inugami.core.providers.jenkins.models.JenkinsInformation;
import io.inugami.core.providers.jenkins.models.JenkinsJob;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FilterJobsStrategy implements JenkinsJobResolverStrategy {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String  QUERY_JOBS_FILTER = "@filter";
    private              String  query;
    private              String  filterInclude;
    private              String  filterExclude;
    private              Pattern patternInclude;
    private              Pattern patternExclude;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public FilterJobsStrategy() {
        patternExclude = Pattern.compile(".*@filterExclude=(.*?)(@filterInclude.+|$)");
        patternInclude = Pattern.compile(".*@filterInclude=(.*?)(@filterExclude.+|$)");
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public boolean accept(String query, JenkinsInformation eventData) {
        boolean result = false;
        if (query != null && eventData != null && query.contains(QUERY_JOBS_FILTER)) {
            result = query.contains(QUERY_JOBS_FILTER);
            this.query = query;
        }
        return result;
    }

    public JenkinsInformation process(JenkinsInformation eventData) {
        filterInclude = getFilter(patternInclude).orElse("");
        filterExclude = getFilter(patternExclude).orElse("");

        List<JenkinsJob> jobsFiltered = eventData.getJobs().stream()
                                                 .filter(this::toIncludeJob)
                                                 .filter(this::toExcludeJob)
                                                 .collect(Collectors.toList());
        return new JenkinsInformation(jobsFiltered);
    }

    private boolean toIncludeJob(JenkinsJob item) {
        boolean result = true;
        if (!filterInclude.equals("")) {
            result = item.getName().matches(filterInclude);
        }
        return result;
    }

    private boolean toExcludeJob(JenkinsJob item) {
        boolean result = true;
        if (!filterExclude.equals("")) {
            result = !item.getName().matches(filterExclude);
        }
        return result;
    }

    private Optional<String> getFilter(Pattern pattern) {
        Matcher matcher = pattern.matcher(query);

        Optional<String> result = Optional.empty();

        if (matcher.matches()) {
            result = Optional.of(matcher.group(1));
        }
        return result;
    }
}