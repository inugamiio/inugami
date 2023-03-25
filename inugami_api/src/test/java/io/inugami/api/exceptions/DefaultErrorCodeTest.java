package io.inugami.api.exceptions;

import org.junit.jupiter.api.Test;

import static io.inugami.api.exceptions.DefaultErrorCode.newBuilder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultErrorCodeTest {

    @Test
    public void addDetail_nominal_shouldAddDetail(){
        assertThat(TestErrorCode.REQUEST_REQUIRE.addDetail("simple message {0}" , "hello").toString())
                .isEqualTo("DefaultErrorCode(statusCode=500, errorCode=HTTP-1, message=request is require, messageDetail=simple message hello, errorType=null, retryable=false, rollback=false, payload=null, category=null, url=null, exploitationError=false, field=null, errorHandler=null)");
    }


    private static enum TestErrorCode implements  ErrorCode{
        REQUEST_REQUIRE(newBuilder()
                                .statusCode(500)
                                .errorCode("HTTP-1")
                                .message("request is require")
        );

        private final ErrorCode errorCode;

        private TestErrorCode(DefaultErrorCode.DefaultErrorCodeBuilder builder) {
            errorCode = builder.build();
        }

        @Override
        public ErrorCode getCurrentErrorCode() {
            return errorCode;
        }
    }
}