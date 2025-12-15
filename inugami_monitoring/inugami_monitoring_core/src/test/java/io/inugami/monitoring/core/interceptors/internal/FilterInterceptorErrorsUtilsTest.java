package io.inugami.monitoring.core.interceptors.internal;

import io.inugami.commons.test.UnitTestHelper;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ExceptionResolver;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.monitoring.ErrorResult;
import io.inugami.monitoring.core.interceptors.ResponseWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.monitoring.core.interceptors.internal.FilterInterceptorErrorsUtils.buildDefaultErrorCode;
import static io.inugami.monitoring.core.interceptors.internal.FilterInterceptorErrorsUtils.defineStatusAndDuration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterInterceptorErrorsUtilsTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @Mock
    private ResponseWrapper   responseWrapper;
    @Mock
    private ExceptionResolver exceptionResolver;

    // =================================================================================================================
    // UTILITY CLASS
    // =================================================================================================================
    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
    }

    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClass(FilterInterceptorErrorsUtils.class);
    }

    // =================================================================================================================
    // resolveError
    // =================================================================================================================
    @Test
    void resolveError_withoutError() {
        assertThat(FilterInterceptorErrorsUtils.resolveError(null,
                                                             responseWrapper,
                                                             List.of(exceptionResolver))).isNull();
    }

    @Test
    void resolveError_nominal() {
        final int status = 404;
        final UncheckedException exception = new UncheckedException(DefaultErrorCode.buildUndefineError().toBuilder()
                                                                                    .statusCode(status)
                                                                                    .build());
        when(responseWrapper.getStatus()).thenReturn(status);
        when(exceptionResolver.resolve(any())).thenReturn(buildErrorResult(status));

        assertText(FilterInterceptorErrorsUtils.resolveError(exception,
                                                             responseWrapper,
                                                             List.of(exceptionResolver)),
                   """
                           {
                              "currentErrorCode" : {
                                "statusCode" : 404,
                                "errorCode" : "ERR-0000",
                                "errorType" : "functional",
                                "exploitationError" : false,
                                "rollbackRequire" : false,
                                "retryable" : false
                              },
                              "errorCode" : "ERR-0000",
                              "errorType" : "functional",
                              "exception" : {
                                "message" : "",
                                "errorCode" : {
                                  "statusCode" : 404,
                                  "errorCode" : "err-undefine",
                                  "errorType" : "technical",
                                  "exploitationError" : false,
                                  "rollbackRequire" : false,
                                  "retryable" : false
                                }
                              },
                              "exploitationError" : false,
                              "httpCode" : 404
                            }
                           """);
    }

    @Test
    void resolveError_withMdcError() {
        final int status = 400;
        MdcService.getInstance().errorCode(DefaultErrorCode.buildUndefineError().toBuilder()
                                                           .statusCode(status)
                                                           .build());
        when(responseWrapper.getStatus()).thenReturn(status);
        when(exceptionResolver.resolve(any())).thenReturn(buildErrorResult(status));

        assertText(FilterInterceptorErrorsUtils.resolveError(null,
                                                             responseWrapper,
                                                             List.of(exceptionResolver)),
                   """
                           {
                              "currentErrorCode" : {
                                "statusCode" : 400,
                                "errorCode" : "err-undefine",
                                "errorType" : "technical",
                                "exploitationError" : false,
                                "rollbackRequire" : false,
                                "retryable" : false
                              },
                              "errorCode" : "err-undefine",
                              "errorType" : "technical",
                              "exception" : {
                                "message" : "",
                                "errorCode" : {
                                  "statusCode" : 400,
                                  "errorCode" : "err-undefine",
                                  "errorType" : "technical",
                                  "exploitationError" : false,
                                  "rollbackRequire" : false,
                                  "retryable" : false
                                }
                              },
                              "exploitationError" : false,
                              "httpCode" : 400
                            }
                           """);
    }


    @Test
    void resolveError_withErrorCodeResolveErrror() {
        final int status = 404;
        final UncheckedException exception = new UncheckedException(DefaultErrorCode.buildUndefineError().toBuilder()
                                                                                    .statusCode(status)
                                                                                    .build());
        when(responseWrapper.getStatus()).thenReturn(status);
        when(exceptionResolver.resolve(any())).thenThrow(new UncheckedException("resolver error"));

        assertText(FilterInterceptorErrorsUtils.resolveError(exception,
                                                             responseWrapper,
                                                             List.of(exceptionResolver)),
                   """
                           {
                             "cause" : "",
                             "currentErrorCode" : {
                               "statusCode" : 404,
                               "errorCode" : "ERR-0000",
                               "errorType" : "functional",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "errorCode" : "ERR-0000",
                             "errorType" : "functional",
                             "exception" : {
                               "message" : "",
                               "errorCode" : {
                                 "statusCode" : 404,
                                 "errorCode" : "err-undefine",
                                 "errorType" : "technical",
                                 "exploitationError" : false,
                                 "rollbackRequire" : false,
                                 "retryable" : false
                               }
                             },
                             "exploitationError" : false,
                             "httpCode" : 404
                           }
                           """);
    }


    @Test
    void buildDefaultErrorCode_withSecurityError401() {
        when(responseWrapper.getStatus()).thenReturn(401);
        assertText(buildDefaultErrorCode(responseWrapper, null),
                   """
                           {
                             "statusCode" : 401,
                             "errorCode" : "ERR-0000",
                             "errorType" : "security",
                             "exploitationError" : false,
                             "rollbackRequire" : false,
                             "retryable" : false
                           }
                           """);
    }

    @Test
    void buildDefaultErrorCode_withSecurityError403() {
        when(responseWrapper.getStatus()).thenReturn(403);
        assertText(buildDefaultErrorCode(responseWrapper, DefaultErrorCode.buildUndefineError()),
                   """
                           {
                              "statusCode" : 403,
                              "errorCode" : "err-undefine",
                              "errorType" : "security",
                              "exploitationError" : false,
                              "rollbackRequire" : false,
                              "retryable" : false
                            }
                           """);
    }

    @Test
    void buildDefaultErrorCode_withSecurityError500() {
        when(responseWrapper.getStatus()).thenReturn(500);
        assertText(buildDefaultErrorCode(responseWrapper, DefaultErrorCode.buildUndefineError()),
                   """
                           {
                             "statusCode" : 500,
                             "errorCode" : "err-undefine",
                             "errorType" : "technical",
                             "exploitationError" : false,
                             "rollbackRequire" : false,
                             "retryable" : false
                           }
                           """);
    }

    @Test
    void defineStatusAndDuration_nominal() {
        when(responseWrapper.getStatus()).thenReturn(200);
        defineStatusAndDuration(responseWrapper, 120L);
        assertText(MdcService.getInstance().getAllMdc(),
                   """
                           {
                             "duration" : "120",
                             "globalStatus" : "success",
                             "status" : "200"
                           }
                           """);
    }

    @Test
    void defineStatusAndDuration_withError400() {
        when(responseWrapper.getStatus()).thenReturn(400);
        defineStatusAndDuration(responseWrapper, 120L);
        assertText(MdcService.getInstance().getAllMdc(),
                   """
                           {
                             "duration" : "120",
                             "globalStatus" : "error",
                             "status" : "400"
                           }
                           """);
    }

    @Test
    void defineStatusAndDuration_withError500() {
        when(responseWrapper.getStatus()).thenReturn(500);
        defineStatusAndDuration(responseWrapper, 120L);
        assertText(MdcService.getInstance().getAllMdc(),
                   """
                           {
                             "duration" : "120",
                             "globalStatus" : "error",
                             "status" : "500"
                           }
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private ErrorResult buildErrorResult(int status) {
        return ErrorResult.builder()
                          .httpCode(status)
                          .errorCode("ERR-0_0")
                          .message("sorry")
                          .build();
    }
}