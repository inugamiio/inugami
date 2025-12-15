package io.inugami.monitoring.core.interceptors;

import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static io.inugami.commons.test.UnitTestHelper.assertText;

class FilterInterceptorErrorResolverTest {

    private static final FilterInterceptorErrorResolver ERROR_RESOLVER = new FilterInterceptorErrorResolver();

    @Test
    void resolve_nominal() {
        assertText(ERROR_RESOLVER.resolve(new UncheckedException(DefaultErrorCode.buildUndefineError())),
                   """
                           {
                             "currentErrorCode" : {
                               "statusCode" : 500,
                               "errorCode" : "ERR-0-000",
                               "message" : "unknow error",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "errorCode" : "ERR-0-000",
                             "exploitationError" : false,
                             "httpCode" : 500
                           }
                           """);

        assertText(ERROR_RESOLVER.resolve(new IOException("sorry")),
                   """
                           {
                             "cause" : "sorry",
                             "currentErrorCode" : {
                               "statusCode" : 500,
                               "errorCode" : "ERR-0-000",
                               "message" : "unknow error",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "errorCode" : "ERR-0-000",
                             "exploitationError" : false,
                             "httpCode" : 500
                           }
                           """);

        assertText(ERROR_RESOLVER.resolve(new AuthenticationException()),
                   """
                           {
                             "currentErrorCode" : {
                               "statusCode" : 401,
                               "errorCode" : "ERR-1-0",
                               "errorType" : "security",
                               "message" : "Authentication failed",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "errorCode" : "ERR-1-0",
                             "errorType" : "security",
                             "exploitationError" : false,
                             "httpCode" : 401
                           }
                           """);

        assertText(ERROR_RESOLVER.resolve(new UnauthorizedException()),
                   """
                           {
                             "currentErrorCode" : {
                               "statusCode" : 403,
                               "errorCode" : "ERR-1-2",
                               "errorType" : "security",
                               "message" : "Authentication failed",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "errorCode" : "ERR-1-2",
                             "errorType" : "security",
                             "exploitationError" : false,
                             "httpCode" : 403
                           }
                           """);

        assertText(ERROR_RESOLVER.resolve(new SocketTimeoutException()),
                   """
                           {
                             "currentErrorCode" : {
                               "statusCode" : 504,
                               "errorCode" : "ERR-2-0",
                               "errorType" : "technical",
                               "message" : "timeout",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "errorCode" : "ERR-2-0",
                             "errorType" : "technical",
                             "exploitationError" : false,
                             "httpCode" : 504
                           }
                           """);


        assertText(ERROR_RESOLVER.resolve(new DaoValidatorException()),
                   """
                           {
                             "currentErrorCode" : {
                               "statusCode" : 422,
                               "errorCode" : "ERR-4-3",
                               "errorType" : "technical",
                               "message" : "Entity contraint exception",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "errorCode" : "ERR-4-3",
                             "errorType" : "technical",
                             "exploitationError" : false,
                             "httpCode" : 422
                           }
                           """);

        assertText(ERROR_RESOLVER.resolve(new DaoEntityNotFoundException()),
                   """
                           {
                             "currentErrorCode" : {
                               "statusCode" : 404,
                               "errorCode" : "ERR-4-2",
                               "errorType" : "functional",
                               "message" : "Entity not found",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "errorCode" : "ERR-4-2",
                             "errorType" : "functional",
                             "exploitationError" : false,
                             "httpCode" : 404
                           }
                           """);

        assertText(ERROR_RESOLVER.resolve(new IllegalStateException()),
                   """
                           {
                             "currentErrorCode" : {
                               "statusCode" : 501,
                               "errorCode" : "ERR-3-6",
                               "errorType" : "technical",
                               "message" : "Technical error",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "errorCode" : "ERR-3-6",
                             "errorType" : "technical",
                             "exploitationError" : false,
                             "httpCode" : 501
                           }
                           """);
    }

    private static class AuthenticationException extends Exception{

    }

    private static class UnauthorizedException extends Exception{

    }
    private static class DaoValidatorException extends Exception{

    }
    private static class DaoEntityNotFoundException extends Exception{

    }
}