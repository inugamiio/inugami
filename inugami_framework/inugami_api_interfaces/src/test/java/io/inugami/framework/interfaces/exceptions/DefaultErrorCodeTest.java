package io.inugami.framework.interfaces.exceptions;

import io.inugami.framework.interfaces.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.newBuilder;

class DefaultErrorCodeTest {

    @Test
    void addDetail_nominal_shouldAddDetail() {
        UnitTestHelper.assertErrorCode("io/inugami/framework/interfaces/exceptions/defaultErrorCodeTest/addDetail_nominal_shouldAddDetail.json",
                                       TestErrorCode.REQUEST_REQUIRE.addDetail("simple message {0}", "hello"));
    }

    @Test
    void toMap() {
        UnitTestHelper.assertTextRelative(TestErrorCode.PARTNER_DOWN.addDetail("hello {0}", "joe").toMap(),
                                          "io/inugami/framework/interfaces/exceptions/defaultErrorCodeTest/toMap.json");
    }

    private enum TestErrorCode implements ErrorCode {
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