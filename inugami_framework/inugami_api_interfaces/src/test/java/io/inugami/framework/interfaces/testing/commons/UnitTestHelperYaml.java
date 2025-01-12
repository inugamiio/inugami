package io.inugami.framework.interfaces.testing.commons;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.testing.commons.marshaller.YamlMarshaller;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperYaml {

    public static final String CAN_T_LOAD_YAML_OBJECT_FROM_NULL_PATH = "can't load Yaml Object from null path";

    static <T> T loadRelativeYaml(final String path, final Class<? extends T> objectType) {
        Asserts.assertNotNull(path, CAN_T_LOAD_YAML_OBJECT_FROM_NULL_PATH);
        final String yaml = UnitTestHelperFile.readFileRelative(path);
        return convertFromYaml(yaml, objectType);
    }

    static JsonNode loadRelativeYaml(final String path) {
        Asserts.assertNotNull(path, CAN_T_LOAD_YAML_OBJECT_FROM_NULL_PATH);
        final String yaml = UnitTestHelperFile.readFileRelative(path);
        return convertFromYaml(yaml);
    }


    static <T> T loadIntegrationYaml(final String path, final Class<? extends T> objectType) {
        Asserts.assertNotNull(path, CAN_T_LOAD_YAML_OBJECT_FROM_NULL_PATH);
        final String yaml = UnitTestHelperFile.readFileIntegration(path);
        return convertFromYaml(yaml, objectType);
    }

    static JsonNode loadIntegrationYaml(final String path) {
        Asserts.assertNotNull(path, CAN_T_LOAD_YAML_OBJECT_FROM_NULL_PATH);
        final String yaml = UnitTestHelperFile.readFileIntegration(path);
        return convertFromYaml(yaml);
    }

    static <T> T loadYaml(final String path, final Class<? extends T> objectType) {
        Asserts.assertNotNull(path, CAN_T_LOAD_YAML_OBJECT_FROM_NULL_PATH);
        final String yaml = UnitTestHelperFile.readFile(path);
        return convertFromYaml(yaml, objectType);
    }

    static JsonNode loadYaml(final String path) {
        Asserts.assertNotNull(path, CAN_T_LOAD_YAML_OBJECT_FROM_NULL_PATH);
        final String yaml = UnitTestHelperFile.readFile(path);
        return convertFromYaml(yaml);
    }

    static <T> T convertFromYaml(final String content, final Class<? extends T> userDtoClass) {
        return YamlMarshaller.getInstance().convertFromYaml(content, userDtoClass);
    }

    static <T> T convertFromYaml(final String content, final TypeReference<T> userDtoClass) {
        return YamlMarshaller.getInstance().convertFromYaml(content, userDtoClass);
    }


    static JsonNode convertFromYaml(final String content) {
        return YamlMarshaller.getInstance().convertFromYaml(content);
    }
}
