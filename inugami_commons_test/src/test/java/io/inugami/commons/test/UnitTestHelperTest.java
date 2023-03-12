package io.inugami.commons.test;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.inugami.api.exceptions.Asserts.assertThrow;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class UnitTestHelperTest {

    private final static TypeReference<Config> CONFIG_TYPE = new TypeReference<>(){};
    // =========================================================================
    // LOAD / READ FILE
    // =========================================================================
    @Test
    public void loadRelativeFile_nominal_shouldPass() {
        final String content = UnitTestHelper.loadRelativeFile("basic_yaml_config.json");
        assertThat(content).isNotBlank();
        UnitTestHelper.assertTextRelative(content, "basic_yaml_config.json");

        UnitTestHelper.assertTextRelative(UnitTestHelper.loadRelativeFile("basic_yaml_config.json", StandardCharsets.UTF_8),
                                          "basic_yaml_config.json");
    }

    @Test
    public void loadRelativeFile_withoutFile_shouldThrow() {
        assertThrow("can't read file from  \"   \" relative path!", () -> UnitTestHelper.loadRelativeFile("   "));
        assertThrow("can't read file from  \"null\" relative path!", () -> UnitTestHelper.loadRelativeFile(null));
        assertThrow("the file .*foobar.json doesn't exists", () -> UnitTestHelper.loadRelativeFile("foobar.json"));
    }

    @Test
    public void readFile_nominal_shouldPass() {
        final File   file    = UnitTestHelper.buildTestFilePath("basic_yaml_config.json");
        final String content = UnitTestHelper.readFile(file);
        assertThat(content).isNotBlank();
        UnitTestHelper.assertTextRelative(content, "basic_yaml_config.json");

        UnitTestHelper.assertTextRelative(UnitTestHelper.readFile(file, StandardCharsets.UTF_8),
                                          "basic_yaml_config.json");
    }

    @Test
    public void readFile_withoutFile_shouldThrow() {
        assertThrow("the file is required", () -> UnitTestHelper.readFile(null));
        assertThrow("the file joe doesn't exists", () -> UnitTestHelper.readFile(new File("joe")));
    }

    // =========================================================================
    // YAML
    // =========================================================================
    @Test
    public void loadYaml_nominal_shouldPass() {
        Map<String, Object> result = UnitTestHelper.loadYaml(UnitTestHelper.buildTestFilePath("basic_yaml_config.yaml"));
        UnitTestHelper.assertTextRelative(result, "basic_yaml_config.json");

        UnitTestHelperTest.Config config = UnitTestHelper.loadYaml(UnitTestHelper.buildTestFilePath("basic_yaml_config.yaml"), UnitTestHelperTest.Config.class);
        UnitTestHelper.assertTextRelative(result, "basic_yaml_config.json");
    }


    @Test
    public void loadRelativeYaml_nominal_shouldPass() {
        Map<String, Object> result = UnitTestHelper.loadRelativeYaml("basic_yaml_config.yaml");
        UnitTestHelper.assertTextRelative(result, "basic_yaml_config.json");

        UnitTestHelperTest.Config config = UnitTestHelper.loadRelativeYaml("basic_yaml_config.yaml", UnitTestHelperTest.Config.class);
        UnitTestHelper.assertTextRelative(result, "basic_yaml_config.json");
    }


    // =========================================================================
    // CONVERT TO JSON
    // =========================================================================
    @Test
    public void convertToJson_withObject_shouldConvert() {
        Config config = buildConfig();
        String value = UnitTestHelper.convertToJson(config);
        UnitTestHelper.assertTextRelative(value, "convertToJson_withObject_shouldConvert.json");
    }
    @Test
    public void forceConvertToJson_withObject_shouldConvert() {
        Config config = buildConfig();
        String value = UnitTestHelper.forceConvertToJson(config);
        UnitTestHelper.assertTextRelative(value, "forceConvertToJson_withObject_shouldConvert.json");
    }
    @Test
    public void convertToJsonWithoutIndent_withObject_shouldConvert() {
        Config config = buildConfig();
        String value = UnitTestHelper.convertToJsonWithoutIndent(config);
        UnitTestHelper.assertTextRelative(value, "convertToJsonWithoutIndent_withObject_shouldConvert.json");
    }

    @Test
    public void loadJson_withPath_shouldConvert(){
        Config value = UnitTestHelper.loadJson("convertToJson_withObject_shouldConvert.json",CONFIG_TYPE);
        UnitTestHelper.assertTextRelative(value, "loadJson_withPath_shouldConvert.json");
    }


    @Test
    public void convertFromJson_withJson_shouldConvert(){
        Config config = buildConfig();
        String json = UnitTestHelper.convertToJson(config);
        Config newConfig = UnitTestHelper.convertFromJson(json, CONFIG_TYPE);
        UnitTestHelper.assertTextRelative(newConfig, "convertFromJson_withJson_shouldConvert.json");
    }

    @Test
    public void convertFromJson_withByte_shouldConvert(){
        Config config = buildConfig();
        String json = UnitTestHelper.convertToJson(config);
        Config newConfig = UnitTestHelper.convertFromJson(json.getBytes(StandardCharsets.UTF_8), CONFIG_TYPE);
        UnitTestHelper.assertTextRelative(newConfig, "convertFromJson_withJson_shouldConvert.json");
    }
    @Test
    public void loadJsonFromResponse_withByte_shouldConvert(){
        Config config = buildConfig();
        String json = UnitTestHelper.convertToJson(config);
        Config newConfig = UnitTestHelper.loadJsonFromResponse(json.getBytes(StandardCharsets.UTF_8), CONFIG_TYPE);
        UnitTestHelper.assertTextRelative(newConfig, "convertFromJson_withJson_shouldConvert.json");
    }
    // =========================================================================
    // assertTextRelative
    // =========================================================================
    @Test
    public void assertTextRelative_withSkipLine_shouldPass() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("title", "hello");
        values.put("uid", UUID.randomUUID().toString());
        values.put("name", "joe");

        UnitTestHelper.assertTextRelative(values, "assertTextRelative_withSkipLine_shouldPass.json", DefaultLineAssertion.skipLine(2));
        UnitTestHelper.assertTextRelativeSkipLine(values, "assertTextRelative_withSkipLine_shouldPass.json", 2);

    }


    //  =======================================================================
    // TOOLS
    //  =======================================================================
    private static Config buildConfig() {
        return Config.builder()
                     .name("Simple test")
                     .facetes(Facetes.builder()
                                     .name("YamlTest")
                                     .value("some value")
                                     .build())
                     .tags(List.of("test", "unit"))
                     .build();
    }
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Config {
        private String       name;
        private List<String> tags;
        private Facetes      facetes;
    }

    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Facetes {
        private String name;
        private String value;
    }
}