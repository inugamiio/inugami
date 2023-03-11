package io.inugami.commons.test;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class UnitTestHelperLoadYamlTest {


    @Test
    public void loadYaml_withPath_shouldPass() {
        Map<String, Object> result = UnitTestHelper.loadYaml(UnitTestHelper.buildTestFilePath("basic_yaml_config.yaml"));
        UnitTestHelper.assertTextRelative(result, "basic_yaml_config.json");

        Config config = UnitTestHelper.loadYaml(UnitTestHelper.buildTestFilePath("basic_yaml_config.yaml"), Config.class);
        UnitTestHelper.assertTextRelative(result, "basic_yaml_config.json");
    }

    //  =======================================================================
    // TOOLS
    //  =======================================================================
    @Setter
    @Getter
    public static class Config {
        private String       name;
        private List<String> tags;
        private Facetes      facetes;
    }

    @Setter
    @Getter
    public static class Facetes {
        private String name;
        private String value;
    }
}