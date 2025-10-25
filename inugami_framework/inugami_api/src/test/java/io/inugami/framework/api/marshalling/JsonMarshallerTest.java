package io.inugami.framework.api.marshalling;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.DefaultWarning;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.Warning;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.newBuilder;
import static org.assertj.core.api.Assertions.assertThat;

class JsonMarshallerTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String NOMINAL = """
            {
              "statusCode" : 500,
              "category" : "configuration",
              "domain" : "CONFIG",
              "errorCode" : "WORKSPACE_UNDEFINED",
              "errorType" : "technical",
              "field" : "config.file",
              "message" : "workspace not defined",
              "messageDetail" : "please check your configuration",
              "payload" : "{\\"name\\":\\"some value\\"}\\n",
              "subDomain" : "APP",
              "url" : "http://inugami.io/mock/documentation",
              "exploitationError" : true,
              "rollbackRequire" : true,
              "retryable" : true
            }""";
    public static final String NOMINAL_WARNING = """
            {
              "warningCode" : "WARN-ENGINE-0_0",
              "warningMessage" : "workspace not defined",
              "warningMessageDetail" : "please check your configuration",
              "warningType" : "functional",
              "warningCategory" : "configuration",
              "warningDomain" : "CONFIG",
              "warningSubDomain" : "APP"
            }""";

    @Test
    void errorCode_serialize() throws JsonProcessingException {
        final ErrorCode errorCode = EngineErrors.WORKSPACE_UNDEFINED.getCurrentErrorCode();
        assertThat(JsonMarshaller.getInstance()
                                 .getIndentedObjectMapper()
                                 .writeValueAsString(errorCode))
                .isEqualTo(NOMINAL);
    }

    @Test
    void errorCode_deserialize() throws JsonProcessingException {
        final ErrorCode errorCode = JsonMarshaller.getInstance()
                                                  .getIndentedObjectMapper()
                                                  .readValue(NOMINAL, ErrorCode.class);
        assertThat(JsonMarshaller.getInstance()
                                 .getIndentedObjectMapper()
                                 .writeValueAsString(errorCode))
                .isEqualTo(NOMINAL);

    }


    @Test
    void warning_serialize() throws JsonProcessingException {
        final Warning value = EngineWarning.WORKSPACE_UNDEFINED.getCurrentWaring();
        assertThat(JsonMarshaller.getInstance()
                                 .getIndentedObjectMapper()
                                 .writeValueAsString(value))
                .isEqualTo(NOMINAL_WARNING);
    }

    @Test
    void warning_deserialize() throws JsonProcessingException {
        final Warning value = JsonMarshaller.getInstance()
                                                  .getIndentedObjectMapper()
                                                  .readValue(NOMINAL_WARNING, Warning.class);
        assertThat(JsonMarshaller.getInstance()
                                 .getIndentedObjectMapper()
                                 .writeValueAsString(value))
                .isEqualTo(NOMINAL_WARNING);

    }
    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private enum EngineErrors implements ErrorCode {
        WORKSPACE_UNDEFINED(newBuilder().errorCode("ENGINE-0_0")
                                        .message("workspace not defined")
                                        .messageDetail("please check your configuration")
                                        .retryable(true)
                                        .rollback(true)
                                        .payload("""
                                                         {"name":"some value"}
                                                         """)
                                        .category("configuration")
                                        .domain("CONFIG")
                                        .subDomain("APP")
                                        .url("http://inugami.io/mock/documentation")
                                        .exploitationError(true)
                                        .field("config.file")
                                        .statusCode(500)
                                        .errorTypeTechnical());

        private final ErrorCode errorCode;

        private EngineErrors(final DefaultErrorCode.DefaultErrorCodeBuilder errorBuilder) {
            errorCode = errorBuilder.errorCode(this.name()).build();
        }

        @Override
        public ErrorCode getCurrentErrorCode() {
            return errorCode;
        }
    }

    private enum EngineWarning implements Warning {
        WORKSPACE_UNDEFINED(DefaultWarning.builder()
                                          .warningCode("WARN-ENGINE-0_0")
                                          .message("workspace not defined")
                                          .messageDetail("please check your configuration")
                                          .category("configuration")
                                          .domain("CONFIG")
                                          .subDomain("APP")
                                          .typeFunctional());

        private final Warning warning;

        private EngineWarning(final DefaultWarning.DefaultWarningBuilder builder) {
            warning = builder.build();
        }

        @Override
        public Warning getCurrentWaring() {
            return warning;
        }
    }
}