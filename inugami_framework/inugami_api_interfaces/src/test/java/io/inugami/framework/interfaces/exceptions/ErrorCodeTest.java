package io.inugami.framework.interfaces.exceptions;

import io.inugami.framework.interfaces.commons.UnitTestData;
import io.inugami.framework.interfaces.commons.UnitTestHelper;
import io.inugami.framework.interfaces.monitoring.logger.LogInfoDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorCodeTest {

    private static final ErrorCode ERROR_CODE = DefaultErrorCode.buildUndefineErrorCode()
                                                                .statusCode(400)
                                                                .errorCode("ERR-1")
                                                                .message("some message")
                                                                .addMessageDetail("detail : {0}", UnitTestData.LOREM)
                                                                .exploitationError()
                                                                .errorTypeFunctional()
                                                                .domain("USER")
                                                                .subDomain("search")
                                                                .payload("{}")
                                                                .url("http://mock.inugami.io")
                                                                .rollback(true)
                                                                .isRetryable()
                                                                .category("GET")
                                                                .field("name")
                                                                .errorHandler(ErrorCodeTest::handlingError)
                                                                .build();


    private static final ErrorCode ERROR_CODE_EMPTY = new ErrorCode() {
        @Override
        public ErrorCode getCurrentErrorCode() {
            return null;
        }
    };


    //==========================================================================
    // TEST
    //==========================================================================
    @Test
    void assertGetter_nominal() {
        UnitTestHelper.assertTextRelative(LogInfoDTO.builder()
                                                    .with(ErrorCode.STATUS_CODE, ERROR_CODE.getStatusCode())
                                                    .with(ErrorCode.ERROR_CODE, ERROR_CODE.getErrorCode())
                                                    .with(ErrorCode.MESSAGE, ERROR_CODE.getMessage())
                                                    .with(ErrorCode.MESSAGE_DETAIL, ERROR_CODE.getMessageDetail())
                                                    .with(ErrorCode.EXPLOITATION_ERROR, ERROR_CODE.isExploitationError())
                                                    .with(ErrorCode.ERROR_TYPE, ERROR_CODE.getErrorType())
                                                    .with(ErrorCode.DOMAIN, ERROR_CODE.getDomain())
                                                    .with(ErrorCode.SUB_DOMAIN, ERROR_CODE.getSubDomain())
                                                    .with(ErrorCode.PAYLOAD, ERROR_CODE.getPayload())
                                                    .with(ErrorCode.URL, ERROR_CODE.getUrl())
                                                    .with(ErrorCode.ROLLBACK, ERROR_CODE.isRollbackRequire())
                                                    .with(ErrorCode.RETRYABLE, ERROR_CODE.isRetryable())
                                                    .with(ErrorCode.ERROR_HANDLER, ERROR_CODE.getErrorHandler() != null)
                                                    .with(ErrorCode.CATEGORY, ERROR_CODE.getCategory())
                                                    .with(ErrorCode.FIELD, ERROR_CODE.getField())
                                                    .build()
                                                    .toString(),
                                          "io/inugami/framework/interfaces/exceptions/errorCodeTest/assertGetter_nominal.txt");
    }

    @Test
    void assertGetter_empty() {
        UnitTestHelper.assertTextRelative(LogInfoDTO.builder()
                                                    .with(ErrorCode.STATUS_CODE, ERROR_CODE_EMPTY.getStatusCode())
                                                    .with(ErrorCode.ERROR_CODE, ERROR_CODE_EMPTY.getErrorCode())
                                                    .with(ErrorCode.MESSAGE, ERROR_CODE_EMPTY.getMessage())
                                                    .with(ErrorCode.MESSAGE_DETAIL, ERROR_CODE_EMPTY.getMessageDetail())
                                                    .with(ErrorCode.EXPLOITATION_ERROR, ERROR_CODE_EMPTY.isExploitationError())
                                                    .with(ErrorCode.ERROR_TYPE, ERROR_CODE_EMPTY.getErrorType())
                                                    .with(ErrorCode.DOMAIN, ERROR_CODE_EMPTY.getDomain())
                                                    .with(ErrorCode.SUB_DOMAIN, ERROR_CODE_EMPTY.getSubDomain())
                                                    .with(ErrorCode.PAYLOAD, ERROR_CODE_EMPTY.getPayload())
                                                    .with(ErrorCode.URL, ERROR_CODE_EMPTY.getUrl())
                                                    .with(ErrorCode.ROLLBACK, ERROR_CODE_EMPTY.isRollbackRequire())
                                                    .with(ErrorCode.RETRYABLE, ERROR_CODE_EMPTY.isRetryable())
                                                    .with(ErrorCode.ERROR_HANDLER,
                                                          ERROR_CODE_EMPTY.getErrorHandler() != null)
                                                    .with(ErrorCode.CATEGORY, ERROR_CODE_EMPTY.getCategory())
                                                    .with(ErrorCode.FIELD, ERROR_CODE_EMPTY.getField())
                                                    .build()
                                                    .toString(),
                                          "io/inugami/framework/interfaces/exceptions/errorCodeTest/assertGetter_empty.txt");
    }

    private static void handlingError(String s, Exception e) {

    }

    @Test
    void keysSet_nominal() {
        assertThat(ERROR_CODE.keysSet()).hasToString("[errorStatus, errorCode, message, messageDetail, errorType, payload, exploitationError, rollback, retryable, field, url, category, errorDomain, errorSubDomain]");
    }

}