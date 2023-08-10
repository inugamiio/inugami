package io.inugami.core.providers.jenkins.strategies;

import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.core.providers.jenkins.models.JenkinsInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilterJobsStrategyTest implements TestUnitResources {

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
    void testBuildResultFilterExcludeJobs() {
        assertTrue(filterJobsStrategy.accept(QUERY_FILTER_EXCLUDE, strategyData));
        assertTextRelative(filterJobsStrategy.process(strategyData), "core/providers/jenkins/strategies/filterJobsStrategyTest/testBuildResultFilterExcludeJobs.json");
    }

    @Test
    void testBuildResultFilterIncludeJobs() {
        assertTrue(filterJobsStrategy.accept(QUERY_FILTER_INCLUDE, strategyData));
        assertTextRelative(filterJobsStrategy.process(strategyData), "core/providers/jenkins/strategies/filterJobsStrategyTest/testBuildResultFilterIncludeJobs.json");
    }

    @Test
    void testBuildResultFilterIncludeExcludeJobs() {
        assertTrue(filterJobsStrategy.accept(QUERY_FILTER_INCLUDE_EXCLUDE, strategyData));
        assertTextRelative(filterJobsStrategy.process(strategyData), "core/providers/jenkins/strategies/filterJobsStrategyTest/testBuildResultFilterIncludeExcludeJobs.json");
    }

    @Test
    void testBuildResultFilterExcludeIncludeJobs() {
        assertTrue(filterJobsStrategy.accept(QUERY_FILTER_EXCLUDE_INCLUDE, strategyData));
        assertTextRelative(filterJobsStrategy.process(strategyData), "core/providers/jenkins/strategies/filterJobsStrategyTest/testBuildResultFilterExcludeIncludeJobs.json");
    }
}
