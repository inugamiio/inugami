package org.inugami.core.providers.jenkins;

import org.inugami.core.providers.jenkins.models.JenkinsInformation;
import org.inugami.core.providers.jenkins.strategies.FailedJobsStrategy;
import org.inugami.core.providers.jenkins.strategies.FailedRunningJobsStrategy;
import org.inugami.core.providers.jenkins.strategies.FilterJobsStrategy;
import org.inugami.core.providers.jenkins.strategies.JenkinsJobResolverStrategy;

public class JenkinsProviderTaskResolver {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static JenkinsJobResolverStrategy[] resolverStrategies = {
        new FilterJobsStrategy(),
    	new FailedJobsStrategy(),
        new FailedRunningJobsStrategy()
    };

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public JenkinsProviderTaskResolver() {}

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
