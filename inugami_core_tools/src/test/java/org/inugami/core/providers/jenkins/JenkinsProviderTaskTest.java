package org.inugami.core.providers.jenkins;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.inugami.api.exceptions.services.ProviderException;
import org.inugami.api.models.data.JsonObject;
import org.inugami.api.models.events.SimpleEventBuilder;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.commons.connectors.HttpConnectorResultBuilder;
import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.tools.TestUnitResources;
import org.inugami.configuration.services.ConfigHandlerHashMap;
import org.inugami.core.providers.jenkins.models.JenkinsInformation;
import org.inugami.core.providers.jenkins.models.JenkinsJob;
import org.inugami.core.services.connectors.HttpConnector;
import org.inugami.core.services.connectors.mock.HttpConnectorMock;
import org.junit.Test;

public class JenkinsProviderTaskTest implements TestUnitResources {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String URL               = "test_url";
    
    private final static String QUERY_ALL_JOBS    = "@all";
    
    private final static String QUERY_JOBS_FAILED = "@failed";
    
    // =========================================================================
    // METHODS
    // =========================================================================
    
    @Test
    public void testBuildRequest() throws ProviderException {
        
        final HttpConnector httpConnector = new HttpConnectorMock().build();
        
        final Map<String, String> configMap = new HashMap<>();
        configMap.put("treeKey", "tree");
        configMap.put("treeValue", "jobs[name,url,color,jobs[name,url,color]]");
        
        final ConfigHandler<String, String> config = new ConfigHandlerHashMap(configMap);
        
        final JenkinsProviderTask task = new JenkinsProviderTask(null, URL, httpConnector, config, null,null);
        
        final String query = "test_url?tree=jobs%5Bname%2Curl%2Ccolor%2Cjobs%5Bname%2Curl%2Ccolor%5D%5D";
        
        assertEquals(query, task.buildRequest(new SimpleEventBuilder().addName("event").build()));
        httpConnector.close();
    }
    
    @Test
    public void testBuildResultAllJobs() throws Exception {
        HttpConnector httpConnector = new HttpConnectorMock().build();
        
        final JenkinsProviderTask task = new JenkinsProviderTask(null, URL, httpConnector, null, null,null);
        
        String data = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(),
                                                                  "JenkinsProviderTaskTestBuildResult.json"));
        
        ProviderFutureResult result = task.buildResult(new SimpleEventBuilder().addName("event").addQuery(QUERY_ALL_JOBS).build(),
                                                       new HttpConnectorResultBuilder(data.getBytes()).addStatusCode(200).build());
        
        assertNotNull(result);
        assertFalse(result.getException().isPresent());
        
        JsonObject dataModel = result.getData().get();
        assertNotNull(dataModel);
        assertTrue(dataModel instanceof JenkinsInformation);
        
        final JenkinsInformation jenkinsInformation = (JenkinsInformation) dataModel;
        assertNotNull(jenkinsInformation.getJobs());
        assertEquals(6, jenkinsInformation.getJobs().size());
        
        final List<JenkinsJob> jenkinsJobs = jenkinsInformation.getJobs();
        
        final List<JenkinsJob> jenkinsJobsExpected = new ArrayList<>();
        //@formatter:off
        jenkinsJobsExpected.add(new JenkinsJob("_class_0", "job_name_0", "http://host/jenkins/job/job_name_0", "blue"));
        jenkinsJobsExpected.add(new JenkinsJob("_class_1", "job_name_1", "http://host/jenkins/job/job_name_1", "disabled"));
        jenkinsJobsExpected.add(new JenkinsJob("_class_a", "job_name_a", "http://host/jenkins/job/job_name_a", "yellow_anime"));
        jenkinsJobsExpected.add(new JenkinsJob("_class_b", "job_name_b", "http://host/jenkins/job/job_name_b", "disabled_anime"));
        jenkinsJobsExpected.add(new JenkinsJob("_class_3", "job_name_3", "http://host/jenkins/job/job_name_3", "yellow"));
        jenkinsJobsExpected.add(new JenkinsJob("_class_4", "job_name_4", "http://host/jenkins/job/job_name_4", "red"));
        //@formatter:on
        
        assertEquals(jenkinsJobs.size(), jenkinsJobsExpected.size());
        assertTrue(jenkinsJobs.equals(jenkinsJobsExpected));
        
        httpConnector.close();
    }
    
    @Test
    public void testBuildResultFailedJobs() throws Exception {
        HttpConnector httpConnector = new HttpConnectorMock().build();
        
        final JenkinsProviderTask task = new JenkinsProviderTask(null, URL, httpConnector, null, null,null);
        
        String data = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(),
                                                                  "JenkinsProviderTaskTestBuildResult.json"));
        
        ProviderFutureResult result = task.buildResult(new SimpleEventBuilder().addName("event").addQuery(QUERY_JOBS_FAILED).build(),
                                                       new HttpConnectorResultBuilder(data.getBytes()).addStatusCode(200).build());
        
        assertNotNull(result);
        assertFalse(result.getException().isPresent());
        
        JsonObject dataModel = result.getData().get();
        assertNotNull(dataModel);
        assertTrue(dataModel instanceof JenkinsInformation);
        
        final JenkinsInformation jenkinsInformation = (JenkinsInformation) dataModel;
        assertNotNull(jenkinsInformation.getJobs());
        assertEquals(1, jenkinsInformation.getJobs().size());
        
        final List<JenkinsJob> jenkinsJobs = jenkinsInformation.getJobs();
        
        final List<JenkinsJob> jenkinsJobsExpected = new ArrayList<>();
        jenkinsJobsExpected.add(new JenkinsJob("_class_4", "job_name_4", "http://host/jenkins/job/job_name_4", "red"));
        
        assertEquals(jenkinsJobs.size(), jenkinsJobsExpected.size());
        assertTrue(jenkinsJobs.equals(jenkinsJobsExpected));
        
        httpConnector.close();
    }
    
    @Test
    public void testCall() throws Exception {
        HttpConnector httpConnector = new HttpConnectorMock().build();
        
        final JenkinsProviderTask task = new JenkinsProviderTask(new SimpleEventBuilder().addName("event").build(), URL,
                                                                 httpConnector, null, null,null);
        
        ProviderFutureResult result = task.call();
        assertNotNull(result);
        httpConnector.close();
    }
}
