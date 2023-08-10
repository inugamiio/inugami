package io.inugami.api.exceptions;

import org.junit.jupiter.api.Test;

import static io.inugami.api.exceptions.DefaultErrorCode.newBuilder;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertErrorCode;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertTextRelative;

class DefaultErrorCodeTest {

    @Test
    void addDetail_nominal_shouldAddDetail() {
        assertErrorCode("api/exceptions/defaultErrorCodeTest/addDetail_nominal_shouldAddDetail.json",
                        TestErrorCode.REQUEST_REQUIRE.addDetail("simple message {0}", "hello"));
    }

    @Test
    void toMap() {
        assertTextRelative(TestErrorCode.PARTNER_DOWN.addDetail("hello {0}", "joe").toMap(),
                           "api/exceptions/defaultErrorCodeTest/toMap.json");
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