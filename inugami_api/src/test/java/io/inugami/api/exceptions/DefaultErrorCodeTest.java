package io.inugami.api.exceptions;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Map;

import static io.inugami.api.exceptions.DefaultErrorCode.newBuilder;
import static io.inugami.api.exceptions.ErrorCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DefaultErrorCodeTest {

    @Test
    public void addDetail_nominal_shouldAddDetail() {
        assertThat(TestErrorCode.REQUEST_REQUIRE.addDetail("simple message {0}", "hello").toString())
                .isEqualTo("DefaultErrorCode(statusCode=500, errorCode=HTTP-1, message=request is require, messageDetail=simple message hello, errorType=null, retryable=false, rollback=false, payload=null, category=null, domain=null, subDomain=null, url=null, exploitationError=false, field=null, errorHandler=null)");
    }

    @Test
    public void toMap() {
        final Map<String, Serializable> map = TestErrorCode.PARTNER_DOWN.addDetail("hello {0}", "joe").toMap();
        assertThat(map.get(STATUS_CODE)).isEqualTo(500);
        assertThat(map.get(ERROR_CODE)).isEqualTo("HTTP-2");
        assertThat(map.get(MESSAGE)).isEqualTo("request is require");
        assertThat(map.get(MESSAGE_DETAIL)).isEqualTo("hello joe");
        assertThat(map.get(ERROR_TYPE)).isEqualTo("functional");
        assertThat(map.get(PAYLOAD)).isEqualTo("{}");
        assertThat((Boolean) map.get(EXPLOITATION_ERROR)).isTrue();
        assertThat((Boolean) map.get(ROLLBACK)).isTrue();
        assertThat((Boolean) map.get(RETRYABLE)).isTrue();
        assertThat(map.get(FIELD)).isEqualTo("user.id");

        assertThat(map.get(DOMAIN)).isEqualTo("creation");
        assertThat(map.get(SUB_DOMAIN)).isEqualTo("address");
        assertThat(map.get(URL)).isEqualTo("http://localhost");
    }

    private static enum TestErrorCode implements ErrorCode {
        REQUEST_REQUIRE(newBuilder()
                                .statusCode(500)
                                .errorCode("HTTP-1")
                                .message("request is require")),

        PARTNER_DOWN(newBuilder()
                             .statusCode(500)
                             .errorCode("HTTP-2")
                             .message("request is require")
                             .errorTypeFunctional()
                             .payload("{}")
                             .exploitationError(true)
                             .rollbackRequire()
                             .retryable(true)
                             .field("user.id")
                             .url("http://localhost")
                             .category("user")
                             .domain("creation")
                             .subDomain("address")

        );

        private final ErrorCode errorCode;

        private TestErrorCode(final DefaultErrorCode.DefaultErrorCodeBuilder builder) {
            errorCode = builder.build();
        }

        @Override
        public ErrorCode getCurrentErrorCode() {
            return errorCode;
        }
    }
}