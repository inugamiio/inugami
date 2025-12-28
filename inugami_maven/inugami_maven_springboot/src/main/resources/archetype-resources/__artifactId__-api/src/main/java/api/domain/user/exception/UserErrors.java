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

    CREATE_UID_FORBIDDEN(newBuilder().errorCode("USER-1_1")
                                     .message("uid is forbidden for user creation")
                                     .domain(Constants.USER)
                                     .statusCode(400)
                                     .errorTypeFunctional()),

    CREATE_FIRSTNAME_INVALID(newBuilder().errorCode("USER-1_2")
                                         .message("invalid firstname")
                                         .domain(Constants.USER)
                                         .statusCode(400)
                                         .errorTypeFunctional()),

    CREATE_LASTNAME_INVALID(newBuilder().errorCode("USER-1_3")
                                        .message("invalid lastname")
                                        .domain(Constants.USER)
                                        .statusCode(400)
                                        .errorTypeFunctional()),

    CREATE_EMAIL_INVALID(newBuilder().errorCode("USER-1_4")
                                     .message("invalid email")
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
    UPDATE_USERS_NOT_FOUND(newBuilder().errorCode("USER-3_1")
                                       .message("users not found")
                                       .domain(Constants.USER)
                                       .statusCode(404)
                                       .errorTypeFunctional()),

    UPDATE_UID_REQUIRED(newBuilder().errorCode("USER-3_2")
                                    .message("uid is required for user modification")
                                    .domain(Constants.USER)
                                    .statusCode(400)
                                    .errorTypeFunctional()),

    UPDATE_FIRSTNAME_INVALID(newBuilder().errorCode("USER-3_3")
                                         .message("invalid firstname")
                                         .domain(Constants.USER)
                                         .statusCode(400)
                                         .errorTypeFunctional()),

    UPDATE_LASTNAME_INVALID(newBuilder().errorCode("USER-3_4")
                                        .message("invalid lastname")
                                        .domain(Constants.USER)
                                        .statusCode(400)
                                        .errorTypeFunctional()),

    UPDATE_EMAIL_INVALID(newBuilder().errorCode("USER-3_5")
                                     .message("invalid email")
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
                                         .errorTypeFunctional()),
    DELETE_USERS_NOT_FOUND(newBuilder().errorCode("USER-4_1")
                                       .message("users not found")
                                       .domain(Constants.USER)
                                       .statusCode(404)
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
