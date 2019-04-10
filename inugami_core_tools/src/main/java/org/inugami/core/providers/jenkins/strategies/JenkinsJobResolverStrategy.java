package org.inugami.core.providers.jenkins.strategies;

import org.inugami.core.providers.jenkins.models.JenkinsInformation;

public interface JenkinsJobResolverStrategy {

    boolean accept(String query, JenkinsInformation eventData);

    JenkinsInformation process(JenkinsInformation eventData);
}
