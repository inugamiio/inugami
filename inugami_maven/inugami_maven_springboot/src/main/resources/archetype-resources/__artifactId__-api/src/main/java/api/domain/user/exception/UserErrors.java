package ${package}.api.domain.user.exception;

import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;

import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.newBuilder;

public enum UserErrors implements ErrorCode {
    //==================================================================================================================
    // 0 - GENERIC
    //==================================================================================================================
    UNDEFINED(newBuilder().errorCode("USER-0_0")
                          .message("Undefined error occurs")
                          .domain(Constants.USER)
                          .statusCode(500)
                          .errorTypeTechnical()),

    //==================================================================================================================
    // 1 - CREATE
    //==================================================================================================================
    CREATE_INVALID_DATA(newBuilder().errorCode("USER-1_0")
                                    .message("invalid data for user creation")
                                    .domain(Constants.USER)
                                    .statusCode(400)
                                    .errorTypeFunctional()),

    //==================================================================================================================
    // 2 - READ
    //==================================================================================================================
    READ_USER_UID_REQUIRED(newBuilder().errorCode("USER-2_0")
                                       .message("uid required to find user")
                                       .domain(Constants.USER)
                                       .statusCode(400)
                                       .errorTypeFunctional()),
    READ_USER_NOT_FOUND(newBuilder().errorCode("USER-2_1")
                                    .message("user not found")
                                    .domain(Constants.USER)
                                    .statusCode(404)
                                    .errorTypeFunctional()),
    //==================================================================================================================
    // 3 - UPDATE
    //==================================================================================================================
    UPDATE_INVALID_DATA(newBuilder().errorCode("USER-3_0")
                                    .message("invalid data for user modification")
                                    .domain(Constants.USER)
                                    .statusCode(400)
                                    .errorTypeFunctional()),
    //==================================================================================================================
    // 4 - DELETE
    //==================================================================================================================
    DELETE_USER_UID_REQUIRED(newBuilder().errorCode("USER-4_0")
                                         .message("uid required to delete users")
                                         .domain(Constants.USER)
                                         .statusCode(400)
                                         .errorTypeFunctional())
    ;


    private final ErrorCode errorCode;

    private UserErrors(final DefaultErrorCode.DefaultErrorCodeBuilder errorBuilder) {
        errorCode = errorBuilder.build();
    }

    @Override
    public ErrorCode getCurrentErrorCode() {
        return errorCode;
    }

    private static class Constants {
        public static final String USER = "USER";
    }
}
