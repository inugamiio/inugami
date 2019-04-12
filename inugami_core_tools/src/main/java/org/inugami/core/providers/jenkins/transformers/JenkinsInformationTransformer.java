package org.inugami.core.providers.jenkins.transformers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.inugami.core.providers.jenkins.models.JenkinsInformation;
import org.inugami.core.providers.jenkins.models.JenkinsJob;

public class JenkinsInformationTransformer {

    public JenkinsInformation process(JenkinsInformation receivedData) {
        
        final List<JenkinsJob> newJenkinsJobList = new ArrayList<>();
        
        receivedData.getJobs().stream()
                              .filter(this::toValidData)
                              .map(this::toNewJenkinsJob)
                              .forEach(newJenkinsJobList::addAll);
        
        return new JenkinsInformation(newJenkinsJobList);
    }
    
    private boolean toValidData(JenkinsJob jenkinsJob) {
        return jenkinsJob.getColor() != null || jenkinsJob.getColor() == null && jenkinsJob.getJobs() != null;
    }
    
    private List<JenkinsJob> toNewJenkinsJob(JenkinsJob jenkinsJob) {
        if (jenkinsJob.getColor() != null) {
            return Collections.singletonList(jenkinsJob);
        } else {
            return process(new JenkinsInformation(jenkinsJob.getJobs())).getJobs();
        }
    }
}
