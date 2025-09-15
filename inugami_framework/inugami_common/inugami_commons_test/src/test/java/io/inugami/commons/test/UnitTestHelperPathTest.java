package io.inugami.commons.test;

import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.TestUtils.buildRelativePath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UnitTestHelperPathTest {


    @Test
    void buildTestFilePath_nominal() {
        assertThat(buildRelativePath(UnitTestHelperPath.buildTestFilePath("data", "mock")))
                .isEqualTo("./src/test/resources/data/mock");
    }

    @Test
    void buildIntegrationTestFilePath_nominal() {
        assertThat(buildRelativePath(UnitTestHelperPath.buildIntegrationTestFilePath("data", "mock")))
                .isEqualTo("./src/test_it/resources/data/mock");
    }

    @Test
    void buildPath_nominal() {
        assertThat(buildRelativePath(UnitTestHelperPath.buildPath("data", "mock")))
                .isEqualTo("./data/mock");
    }

    @Test
    void buildPath_withNullValue_shouldReturnNull() {
        assertThat(UnitTestHelperPath.buildPath(null)).isNull();

    }
}