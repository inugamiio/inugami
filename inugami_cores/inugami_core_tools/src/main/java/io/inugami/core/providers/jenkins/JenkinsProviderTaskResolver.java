package io.inugami.core.providers.jenkins;

import io.inugami.core.providers.jenkins.models.JenkinsInformation;
import io.inugami.core.providers.jenkins.strategies.FailedJobsStrategy;
import io.inugami.core.providers.jenkins.strategies.FailedRunningJobsStrategy;
import io.inugami.core.providers.jenkins.strategies.FilterJobsStrategy;
import io.inugami.core.providers.jenkins.strategies.JenkinsJobResolverStrategy;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JenkinsProviderTaskResolver {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final JenkinsJobResolverStrategy[] resolverStrategies = {
            new FilterJobsStrategy(),
            new FailedJobsStrategy(),
            new FailedRunningJobsStrategy()
    };

    // =========================================================================
    // METHODS
    // =========================================================================
    public JenkinsInformation build(String query, JenkinsInformation eventData) {
        JenkinsInformation result = eventData;
        for (int i = 0; i < resolverStrategies.length; i++) {
            if (resolverStrategies[i].accept(query, eventData)) {
                result = resolverStrategies[i].process(eventData);
            }
        }
        return result;
    }
}
