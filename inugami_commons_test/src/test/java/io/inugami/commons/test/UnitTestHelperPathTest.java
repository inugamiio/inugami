package io.inugami.commons.test;

import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.TestUtils.buildRelativePath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UnitTestHelperPathTest {


    @Test
    public void buildTestFilePath_nominal() {
        assertThat(buildRelativePath(UnitTestHelperPath.buildTestFilePath("data", "mock")))
                .isEqualTo("./src/test/resources/data/mock");
    }

    @Test
    public void buildIntegrationTestFilePath_nominal() {
        assertThat(buildRelativePath(UnitTestHelperPath.buildIntegrationTestFilePath("data", "mock")))
                .isEqualTo("./src/test_it/resources/data/mock");
    }

    @Test
    public void buildPath_nominal() {
        assertThat(buildRelativePath(UnitTestHelperPath.buildPath("data", "mock")))
                .isEqualTo("./data/mock");
    }

    @Test
    public void buildPath_withNullValue_shouldReturnNull() {
        assertThat(UnitTestHelperPath.buildPath(null)).isNull();

    }
}