package io.inugami.commons.test;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;

import static io.inugami.api.exceptions.DefaultErrorCode.newBuilder;


public enum TestHelpersErrorCodes implements ErrorCode {
    MUST_BE_NULL(newBuilder().message("value must be null")),


    MUST_BE_NOT_NULL(newBuilder().message("value mustn't be null")),


    MUST_BE_EQUALS(newBuilder().message("values must be equals"));

    private final ErrorCode errorCode;

    private TestHelpersErrorCodes(final DefaultErrorCode.DefaultErrorCodeBuilder errorBuilder) {
        errorCode = errorBuilder.errorCode(this.name()).build();
    }

    @Override
    public ErrorCode getCurrentErrorCode() {
        return errorCode;
    }


}
