package io.inugami.core.providers.jenkins;

import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.events.SimpleEventBuilder;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.commons.connectors.HttpConnectorResultBuilder;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import io.inugami.core.providers.jenkins.models.JenkinsInformation;
import io.inugami.core.providers.jenkins.models.JenkinsJob;
import io.inugami.core.services.connectors.HttpConnector;
import io.inugami.core.services.connectors.mock.HttpConnectorMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JenkinsProviderTaskTest implements TestUnitResources {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String URL = "test_url";

    private final static String QUERY_ALL_JOBS = "@all";

    private final static String QUERY_JOBS_FAILED = "@failed";

    // =========================================================================
    // METHODS
    // =========================================================================

    @Test
    void testBuildRequest() throws ProviderException {

        final HttpConnector httpConnector = new HttpConnectorMock().build();

        final Map<String, String> configMap = new HashMap<>();
        configMap.put("treeKey", "tree");
        configMap.put("treeValue", "jobs[name,url,color,jobs[name,url,color]]");

        final ConfigHandler<String, String> config = new ConfigHandlerHashMap(configMap);

        final JenkinsProviderTask task = new JenkinsProviderTask(null, URL, httpConnector, config, null, null);

        final String query = "test_url?tree=jobs%5Bname%2Curl%2Ccolor%2Cjobs%5Bname%2Curl%2Ccolor%5D%5D";

        assertEquals(query, task.buildRequest(new SimpleEventBuilder().addName("event").build()));
        httpConnector.close();
    }

    @Test
    void testBuildResultAllJobs() throws Exception {
        final HttpConnector httpConnector = new HttpConnectorMock().build();

        final JenkinsProviderTask task = new JenkinsProviderTask(null, URL, httpConnector, null, null, null);

        final String data = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(), "JenkinsProviderTaskTestBuildResult.json"));

        final ProviderFutureResult result = task.buildResult(new SimpleEventBuilder().addName("event")
                                                                                     .addQuery(QUERY_ALL_JOBS)
                                                                                     .build(), new HttpConnectorResultBuilder(data.getBytes()).addStatusCode(200)
                                                                                                                                              .build());

        assertNotNull(result);
        assertFalse(result.getException().isPresent());

        final JsonObject dataModel = result.getData().get();
        assertNotNull(dataModel);
        assertTrue(dataModel instanceof JenkinsInformation);

        final JenkinsInformation jenkinsInformation = (JenkinsInformation) dataModel;
        assertNotNull(jenkinsInformation.getJobs());
        assertEquals(6, jenkinsInformation.getJobs().size());

        final List<JenkinsJob> jenkinsJobs = jenkinsInformation.getJobs();

        final List<JenkinsJob> jenkinsJobsExpected = new ArrayList<>();

        jenkinsJobsExpected.add(JenkinsJob.builder()
                                          .classAtt("_class_0")
                                          .name("job_name_0")
                                          .url("http://host/jenkins/job/job_name_0")
                                          .color("blue")
                                          .build());

        jenkinsJobsExpected.add(JenkinsJob.builder()
                                          .classAtt("_class_1")
                                          .name("job_name_1")
                                          .url("http://host/jenkins/job/job_name_1")
                                          .color("disabled")
                                          .build());

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


        assertEquals(jenkinsJobs.size(), jenkinsJobsExpected.size());
        assertEquals(jenkinsJobs, jenkinsJobsExpected);

        httpConnector.close();
    }

    @Test
    void testBuildResultFailedJobs() throws Exception {
        final HttpConnector httpConnector = new HttpConnectorMock().build();

        final JenkinsProviderTask task = new JenkinsProviderTask(null, URL, httpConnector, null, null, null);

        final String data = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(), "JenkinsProviderTaskTestBuildResult.json"));

        final ProviderFutureResult result = task.buildResult(new SimpleEventBuilder().addName("event")
                                                                                     .addQuery(QUERY_JOBS_FAILED)
                                                                                     .build(), new HttpConnectorResultBuilder(data.getBytes()).addStatusCode(200)
                                                                                                                                              .build());

        assertNotNull(result);
        assertFalse(result.getException().isPresent());

        final JsonObject dataModel = result.getData().get();
        assertNotNull(dataModel);
        assertTrue(dataModel instanceof JenkinsInformation);

        final JenkinsInformation jenkinsInformation = (JenkinsInformation) dataModel;
        assertNotNull(jenkinsInformation.getJobs());
        assertEquals(1, jenkinsInformation.getJobs().size());

        final List<JenkinsJob> jenkinsJobs = jenkinsInformation.getJobs();

        final List<JenkinsJob> jenkinsJobsExpected = new ArrayList<>();
        jenkinsJobsExpected.add(JenkinsJob.builder()
                                          .classAtt("_class_4")
                                          .name("job_name_4")
                                          .url("http://host/jenkins/job/job_name_4")
                                          .color("red")
                                          .build());

        assertEquals(jenkinsJobs.size(), jenkinsJobsExpected.size());
        assertEquals(jenkinsJobs, jenkinsJobsExpected);

        httpConnector.close();
    }

    @Test
    void testCall() throws Exception {
        final HttpConnector httpConnector = new HttpConnectorMock().build();

        final JenkinsProviderTask task = new JenkinsProviderTask(new SimpleEventBuilder().addName("event")
                                                                                         .build(), URL, httpConnector, null, null, null);

        final ProviderFutureResult result = task.call();
        assertNotNull(result);
        httpConnector.close();
    }
}
