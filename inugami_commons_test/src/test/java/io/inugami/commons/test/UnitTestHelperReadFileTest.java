package io.inugami.commons.test;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UnitTestHelperReadFileTest {

    @Test
    public void loadRelativeFile_nominal_shouldPass(){
        final String content = UnitTestHelper.loadRelativeFile("basic_yaml_config.json");
        assertThat(content).isNotBlank();
        UnitTestHelper.assertTextRelative(content, "basic_yaml_config.json");

        UnitTestHelper.assertTextRelative(UnitTestHelper.loadRelativeFile("basic_yaml_config.json", StandardCharsets.UTF_8),
                                          "basic_yaml_config.json");
    }

    @Test
    public void readFile_nominal_shouldPass(){
        final File   file    = UnitTestHelper.buildTestFilePath("basic_yaml_config.json");
        final String content = UnitTestHelper.readFile(file);
        assertThat(content).isNotBlank();
        UnitTestHelper.assertTextRelative(content, "basic_yaml_config.json");

        UnitTestHelper.assertTextRelative(UnitTestHelper.readFile(file,  StandardCharsets.UTF_8),
                                          "basic_yaml_config.json");
    }
}
