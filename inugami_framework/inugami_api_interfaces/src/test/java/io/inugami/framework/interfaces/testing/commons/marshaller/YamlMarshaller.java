package io.inugami.framework.interfaces.testing.commons.marshaller;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.dataformat.yaml.YAMLMapper;

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
        final YAMLFactory yf = YAMLFactory.builder().build();
        objectMapper = YAMLMapper.builder(yf)
                                 .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                                 .disable(MapperFeature.SORT_CREATOR_PROPERTIES_FIRST)
                                 .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                                 .changeDefaultPropertyInclusion(include -> include.withValueInclusion(JsonInclude.Include.NON_NULL)
                                                                                   .withContentInclusion(JsonInclude.Include.NON_NULL)) // <-- Ajout du content inclusion
                                 .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                 .build();
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
        } catch (final JacksonException e) {
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
        } catch (final JacksonException e) {
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
        } catch (final JacksonException e) {
            throw new UncheckedException(YamlMarshallerError.YAML_UNMARSHALLING_ERROR.addDetail(e.getMessage()));
        }
    }

    public <T> String convertToYaml(final T object) {
        Asserts.assertNotNull(YamlMarshallerError.YAML_OBJECT_REQUIRED, object);
        try {
            return objectMapper.writer().writeValueAsString(object);
        } catch (final JacksonException e) {
            throw new UncheckedException(YamlMarshallerError.YAML_UNMARSHALLING_ERROR.addDetail(e.getMessage()));
        }
    }
}