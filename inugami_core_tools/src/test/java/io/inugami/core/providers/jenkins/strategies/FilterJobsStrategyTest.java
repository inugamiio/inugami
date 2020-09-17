package io.inugami.core.providers.jenkins.strategies;

import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.core.providers.jenkins.models.JenkinsInformation;
import io.inugami.core.providers.jenkins.models.JenkinsJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilterJobsStrategyTest implements TestUnitResources {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    private final static String QUERY_FILTER_EXCLUDE = "@filterExclude=^C.*";

    private final static String QUERY_FILTER_INCLUDE = "@filterInclude=^C.*";

    private final static String QUERY_FILTER_INCLUDE_EXCLUDE = "@filterInclude=^C.*@filterExclude=.*4$";

    private final static String QUERY_FILTER_EXCLUDE_INCLUDE = "@filterExclude=.*4$@filterInclude=^C.*";

    private static FilterJobsStrategy filterJobsStrategy;

    private static JenkinsInformation strategyData;

    // =========================================================================
    // METHODS
    // =========================================================================

    @BeforeEach
    public void init() throws Exception {
        filterJobsStrategy = new FilterJobsStrategy();

        final String data = FilesUtils
                .readContent(FilesUtils.buildFile(initResourcesPath(), "JenkinsProviderTaskFilterStrategyInput.json"));
        strategyData = new JenkinsInformation().convertToObject(data.getBytes(), Charset.forName("UTF-8"));
    }

    @Test
    public void testBuildResultFilterExcludeJobs() {

        assertTrue(filterJobsStrategy.accept(QUERY_FILTER_EXCLUDE, strategyData));

        final JenkinsInformation filteredData = filterJobsStrategy.process(strategyData);

        assertNotNull(filteredData);
        assertNotNull(filteredData.getJobs());
        assertEquals(3, filteredData.getJobs().size());

        final List<JenkinsJob> jenkinsJobs = filteredData.getJobs();

        final List<JenkinsJob> jenkinsJobsExpected = new ArrayList<>();
        jenkinsJobsExpected.add(new JenkinsJob("org.jenkinsci.plugins.workflow.job.WorkflowJob", "_TESLA_installNewUSN",
                                               "http://host.jenkins/jenkins/job/_TESLA_installNewUSN/", "blue"));
        jenkinsJobsExpected.add(new JenkinsJob("org.jenkinsci.plugins.workflow.job.WorkflowJob", "_TESLA_installNewUSN",
                                               "http://host.jenkins/jenkins/job/_TESLA_installService/", "blue"));
        jenkinsJobsExpected.add(new JenkinsJob("hudson.model.FreeStyleProject", "AutomatorUpdate_release",
                                               "http://host.jenkins/jenkins/job/AutomatorUpdate_release/", "disabled"));

        assertTrue(jenkinsJobs.equals(jenkinsJobsExpected));
    }

    @Test
    public void testBuildResultFilterIncludeJobs() {
        assertTrue(filterJobsStrategy.accept(QUERY_FILTER_INCLUDE, strategyData));

        final JenkinsInformation filteredData = filterJobsStrategy.process(strategyData);

        assertNotNull(filteredData);
        assertNotNull(filteredData.getJobs());
        assertEquals(2, filteredData.getJobs().size());

        final List<JenkinsJob> jenkinsJobs = filteredData.getJobs();

        final List<JenkinsJob> jenkinsJobsExpected = new ArrayList<>();
        jenkinsJobsExpected.add(new JenkinsJob("com.cloudbees.hudson.plugins.folder.Folder", "USN4",
                                               "http://host.jenkins/jenkins/job/USN4/", "blue"));
        jenkinsJobsExpected.add(new JenkinsJob("com.cloudbees.hudson.plugins.folder.Folder", "CI-VMA",
                                               "http://host.jenkins/jenkins/job/CI-VMA/", "blue"));

        assertTrue(jenkinsJobs.equals(jenkinsJobsExpected));
    }

    @Test
    public void testBuildResultFilterIncludeExcludeJobs() {
        assertTrue(filterJobsStrategy.accept(QUERY_FILTER_INCLUDE_EXCLUDE, strategyData));

        final JenkinsInformation filteredData = filterJobsStrategy.process(strategyData);

        assertNotNull(filteredData);
        assertNotNull(filteredData.getJobs());
        assertEquals(1, filteredData.getJobs().size());

        final List<JenkinsJob> jenkinsJobs = filteredData.getJobs();

        final List<JenkinsJob> jenkinsJobsExpected = new ArrayList<>();
        jenkinsJobsExpected.add(new JenkinsJob("com.cloudbees.hudson.plugins.folder.Folder", "CI-VMA",
                                               "http://host.jenkins/jenkins/job/CI-VMA/", "blue"));

        assertTrue(jenkinsJobs.equals(jenkinsJobsExpected));
    }

    @Test
    public void testBuildResultFilterExcludeIncludeJobs() {
        assertTrue(filterJobsStrategy.accept(QUERY_FILTER_EXCLUDE_INCLUDE, strategyData));

        final JenkinsInformation filteredData = filterJobsStrategy.process(strategyData);

        assertNotNull(filteredData);
        assertNotNull(filteredData.getJobs());
        assertEquals(1, filteredData.getJobs().size());

        final List<JenkinsJob> jenkinsJobs = filteredData.getJobs();

        final List<JenkinsJob> jenkinsJobsExpected = new ArrayList<>();
        jenkinsJobsExpected.add(new JenkinsJob("com.cloudbees.hudson.plugins.folder.Folder", "CI-VMA",
                                               "http://host.jenkins/jenkins/job/CI-VMA/", "blue"));

        assertTrue(jenkinsJobs.equals(jenkinsJobsExpected));
    }
}
