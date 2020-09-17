package io.inugami.core.providers.jenkins.transformers;

import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.core.providers.jenkins.models.JenkinsInformation;
import io.inugami.core.providers.jenkins.models.JenkinsJob;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JenkinsInformationTransformerTest implements TestUnitResources {
    @Test
    public void testProcessDepthThree() throws Exception {
        final String data = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(),
                                                                        "JenkinsInformationTransformerTest.json"));
        final JenkinsInformation dataInput = new JenkinsInformation()
                .convertToObject(data.getBytes(), Charset.forName("UTF-8"));

        final JenkinsInformation dataOutput = new JenkinsInformationTransformer().process(dataInput);

        assertNotNull(dataOutput);
        assertNotNull(dataOutput.getJobs());

        assertEquals(4, dataOutput.getJobs().size());

        final List<JenkinsJob> jenkinsJobsExpected = new ArrayList<>();
        jenkinsJobsExpected.add(new JenkinsJob("_class_a", "job_name_a",
                                               "http://host/jenkins/job/job_name_a", "yellow_anime"));
        jenkinsJobsExpected.add(new JenkinsJob("_class_b", "job_name_b",
                                               "http://host/jenkins/job/job_name_b", "disabled_anime"));
        jenkinsJobsExpected.add(new JenkinsJob("_class_3", "job_name_3",
                                               "http://host/jenkins/job/job_name_3", "yellow"));
        jenkinsJobsExpected.add(new JenkinsJob("_class_4", "job_name_4",
                                               "http://host/jenkins/job/job_name_4", "red"));

        assertTrue(dataOutput.getJobs().equals(jenkinsJobsExpected));
    }
}
