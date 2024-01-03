package io.inugami.api.tools.unit.test.marshaller;

import io.inugami.interfaces.exceptions.DefaultErrorCode;
import io.inugami.interfaces.exceptions.ErrorCode;

import static io.inugami.interfaces.exceptions.DefaultErrorCode.newBuilder;

public enum YamlMarshallerError implements ErrorCode {
    YAML_CLASS_REQUIRED(newBuilder()
                                .statusCode(500)
                                .errorCode("YAML-0_0")
                                .message("class is required to unmarshalling yaml to object")),

    YAML_UNMARSHALLING_ERROR(newBuilder()
                                     .statusCode(500)
                                     .errorCode("YAML-0_1")
                                     .message("Yaml can's unmarshall current yaml")),
    YAML_OBJECT_REQUIRED(newBuilder()
                                 .statusCode(500)
                                 .errorCode("YAML-0_2")
                                 .message("object required to serialize as Yaml")),
    YAML_MARSHALLING_ERROR(newBuilder()
                                   .statusCode(500)
                                   .errorCode("YAML-0_3")
                                   .message("can't convert object to Yaml"));
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ErrorCode errorCode;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private YamlMarshallerError(final DefaultErrorCode.DefaultErrorCodeBuilder builder) {
        errorCode = builder.build();
    }

    // =========================================================================
    // API
    // =========================================================================
    @Override
    public ErrorCode getCurrentErrorCode() {
        return errorCode;
    }
}
