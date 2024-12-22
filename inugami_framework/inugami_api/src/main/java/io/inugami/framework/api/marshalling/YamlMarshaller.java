package io.inugami.framework.api.marshalling;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;

public class YamlMarshaller {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final        ObjectMapper   objectMapper;
    private static final YamlMarshaller INSTANCE = new YamlMarshaller();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private YamlMarshaller() {
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        objectMapper.registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public static final YamlMarshaller getInstance() {
        return INSTANCE;
    }

    // =========================================================================
    // API
    // =========================================================================

    public <T> T convertFromYaml(final String content, final Class<? extends T> objectClass) {
        if (content == null) {
            return null;
        }
        Asserts.assertNotNull(YamlMarshallerError.YAML_CLASS_REQUIRED, objectClass);


        try {
            return objectMapper.readValue(content, objectClass);
        } catch (final JsonProcessingException e) {
            throw new UncheckedException(DefaultErrorCode.fromErrorCode(YamlMarshallerError.YAML_UNMARSHALLING_ERROR)
                                                         .message(
                                                                 YamlMarshallerError.YAML_UNMARSHALLING_ERROR.getMessage() +
                                                                 " " + e.getMessage())
                                                         .build(),
                                         e);
        }
    }

    public <T> T convertFromYaml(final String content, final TypeReference<T> objectClass) {
        if (content == null) {
            return null;
        }
        Asserts.assertNotNull(YamlMarshallerError.YAML_CLASS_REQUIRED, objectClass);


        try {
            return objectMapper.readValue(content, objectClass);
        } catch (final JsonProcessingException e) {
            throw new UncheckedException(DefaultErrorCode.fromErrorCode(YamlMarshallerError.YAML_UNMARSHALLING_ERROR)
                                                         .message(
                                                                 YamlMarshallerError.YAML_UNMARSHALLING_ERROR.getMessage() +
                                                                 " " + e.getMessage())
                                                         .build(),
                                         e);
        }
    }

    public JsonNode convertFromYaml(final String content) {
        if (content == null) {
            return null;
        }

        try {
            return objectMapper.readTree(content);
        } catch (final JsonProcessingException e) {
            throw new UncheckedException(YamlMarshallerError.YAML_UNMARSHALLING_ERROR.addDetail(e.getMessage()));
        }
    }

    public <T> String convertToYaml(final T object) {
        Asserts.assertNotNull(YamlMarshallerError.YAML_OBJECT_REQUIRED, object);


        try {
            return objectMapper.writer().writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new UncheckedException(YamlMarshallerError.YAML_UNMARSHALLING_ERROR.addDetail(e.getMessage()));
        }
    }
}
