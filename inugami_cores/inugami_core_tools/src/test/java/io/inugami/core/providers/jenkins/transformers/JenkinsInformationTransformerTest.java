package io.inugami.core.providers.jenkins.transformers;

import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.core.providers.jenkins.models.JenkinsInformation;
import io.inugami.core.providers.jenkins.models.JenkinsJob;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JenkinsInformationTransformerTest implements TestUnitResources {
    @Test
    void testProcessDepthThree() throws Exception {
        final String data = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(),
                                                                        "JenkinsInformationTransformerTest.json"));
        final JenkinsInformation dataInput = new JenkinsInformation()
                .convertToObject(data.getBytes(), Charset.forName("UTF-8"));

        final JenkinsInformation dataOutput = new JenkinsInformationTransformer().process(dataInput);

        assertNotNull(dataOutput);
        assertNotNull(dataOutput.getJobs());

        assertEquals(4, dataOutput.getJobs().size());

        final List<JenkinsJob> jenkinsJobsExpected = new ArrayList<>();
        jenkinsJobsExpected.add(JenkinsJob.builder()
                                          .classAtt("_class_a")
                                          .name("job_name_a")
                                          .url("http://host/jenkins/job/job_name_a")
                                          .color("yellow_anime")
                                          .build());

        jenkinsJobsExpected.add(JenkinsJob.builder()
                                          .classAtt("_class_b")
                                          .name("job_name_b")
                                          .url("http://host/jenkins/job/job_name_b")
                                          .color("disabled_anime")
                                          .build());

        jenkinsJobsExpected.add(JenkinsJob.builder()
                                          .classAtt("_class_3")
                                          .name("job_name_3")
                                          .url("http://host/jenkins/job/job_name_3")
                                          .color("yellow")
                                          .build());


        jenkinsJobsExpected.add(JenkinsJob.builder()
                                          .classAtt("_class_4")
                                          .name("job_name_4")
                                          .url("http://host/jenkins/job/job_name_4")
                                          .color("red")
                                          .build());


        assertEquals(dataOutput.getJobs(), jenkinsJobsExpected);
    }
}
