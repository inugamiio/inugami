package io.inugami.monitoring.springboot.partnerlog.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.Request;
import feign.Response;
import feign.Target;
import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.api.RegexLineMatcher;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.dto.ProblemDTO;
import io.inugami.framework.interfaces.exceptions.dto.ProblemErrorDTO;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertLogs;
import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeignPartnerErrorDecoderTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @Mock
    private FeignPartnerErrorResolver feignPartnerErrorResolver;


    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
        lenient().when(feignPartnerErrorResolver.accept(any(), any(), any()))
                 .thenReturn(true);
        lenient().when(feignPartnerErrorResolver.resolve(any(), any(), any()))
                 .thenReturn(DefaultErrorCode.buildUndefineErrorCode()
                                             .statusCode(404)
                                             .errorCode("USER-2-0")
                                             .build());
    }

    @AfterEach
    public void clean() {
        MdcService.getInstance().clear();
    }

    // =================================================================================================================
    // decode
    // =================================================================================================================
    @Test
    void decode_nominal() throws JsonProcessingException {

        final var response = buildResponse();

        assertLogs(() -> assertText(errorDecoder().decode("getUsers", response),
                                    """
                                            {
                                              "message" : "",
                                              "errorCode" : {
                                                "statusCode" : 404,
                                                "errorCode" : "USER-2-0",
                                                "errorType" : "technical",
                                                "exploitationError" : false,
                                                "rollbackRequire" : false,
                                                "retryable" : false
                                              }
                                            }
                                            """),
                   Loggers.PARTNERLOG_NAME,
                   """
                           [
                                {
                                    "loggerName":"PARTNERLOG",
                                    "level":"ERROR",
                                    "mdc":{
                                        "errorCode":"USER-2-0",
                                        "errorStatus":"404",
                                        "errorType":"technical",
                                        "exploitationError":"false",
                                        "partner":"http://inugami.io/mock/user",
                                        "partnerResponseDuration":"3414322",
                                        "partnerResponseStatus":"404",
                                        "partnerUrl":"http://inugami.io/mock/user",
                                        "partnerVerb":"GET",
                                        "retryable":"false",
                                        "rollback":"false"
                                    },
                                    "message":[
                                        "21/12 22:31:24 ERROR [PARTNERLOG:58] - [GET] http://inugami.io/mock/user",
                                        "request:",
                                        "	headers:",
                                        "		x-date : 1766349269775",
                                        "	payload:",
                                        "",
                                        "response:",
                                        "	status: 404",
                                        "	duration: 3414322ms",
                                        "	message: null",
                                        "	headers:",
                                        "		x-correlation-id : bb895294-efe7-484b-b670-14d004eaf461",
                                        "		x-date : 1766349269775",
                                        "	payload:",
                                        "{",
                                        "  \\"errors\\" : [ {",
                                        "    \\"name\\" : \\"USER-2-0\\",",
                                        "    \\"reason\\" : \\"user not found\\"",
                                        "  } ],",
                                        "  \\"parameters\\" : [ ],",
                                        "  \\"reasonPhrase\\" : \\"user not found\\",",
                                        "  \\"status\\" : 404",
                                        "}"
                                    ]
                                }
                            ]
                           """,
                   RegexLineMatcher.of(".*http://inugami.io/mock/user.*", 18),
                   SkipLineMatcher.of(10, 26));
    }


    @Test
    void decode_withDefaultResolver() throws JsonProcessingException {
        when(feignPartnerErrorResolver.accept(any(), any(), any())).thenReturn(false);
        final var response = buildResponse();

        assertLogs(() -> assertText(errorDecoder().decode("getUsers", response),
                                    """
                                            {
                                               "message" : "",
                                               "errorCode" : {
                                                 "statusCode" : 404,
                                                 "errorCode" : "http://inugami.io/mock/user-404",
                                                 "errorType" : "technical",
                                                 "exploitationError" : false,
                                                 "rollbackRequire" : false,
                                                 "retryable" : false
                                               }
                                             }
                                            """),
                   Loggers.PARTNERLOG_NAME,
                   """
                           [
                               {
                                   "loggerName":"PARTNERLOG",
                                   "level":"ERROR",
                                   "mdc":{
                                       "errorCode":"http://inugami.io/mock/user-404",
                                       "errorStatus":"404",
                                       "errorType":"technical",
                                       "exploitationError":"false",
                                       "partner":"http://inugami.io/mock/user",
                                       "partnerResponseDuration":"3414263",
                                       "partnerResponseStatus":"404",
                                       "partnerUrl":"http://inugami.io/mock/user",
                                       "partnerVerb":"GET",
                                       "retryable":"false",
                                       "rollback":"false"
                                   },
                                   "message":[
                                       "21/12 22:31:24 ERROR [PARTNERLOG:58] - [GET] http://inugami.io/mock/user",
                                       "request:",
                                       "	headers:",
                                       "		x-date : 1766349269775",
                                       "	payload:",
                                       "",
                                       "response:",
                                       "	status: 404",
                                       "	duration: 3414263ms",
                                       "	message: null",
                                       "	headers:",
                                       "		x-correlation-id : bb895294-efe7-484b-b670-14d004eaf461",
                                       "		x-date : 1766349269775",
                                       "	payload:",
                                       "{",
                                       "  \\"errors\\" : [ {",
                                       "    \\"name\\" : \\"USER-2-0\\",",
                                       "    \\"reason\\" : \\"user not found\\"",
                                       "  } ],",
                                       "  \\"parameters\\" : [ ],",
                                       "  \\"reasonPhrase\\" : \\"user not found\\",",
                                       "  \\"status\\" : 404",
                                       "}"
                                   ]
                               }
                           ]
                           """,
                   SkipLineMatcher.of(10, 26),
                   RegexLineMatcher.of(".*http://inugami.io/mock/user.*", 18)
                  );
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    FeignPartnerErrorDecoder errorDecoder() {
        return FeignPartnerErrorDecoder.builder()
                                       .errorResolvers(List.of(feignPartnerErrorResolver))
                                       .build();
    }

    private static Response buildResponse() throws JsonProcessingException {
        final var requestTemplate = RequestTemplateBuilder.builder()
                                                          .target("http://inugami.io/mock/user")
                                                          .method(Request.HttpMethod.GET)
                                                          .feignTarget(new <FeignCommonTest.MyFeignService>Target.HardCodedTarget(FeignCommonTest.MyFeignService.class, "http://inugami.io/mock/user"))
                                                          .addHeader("auth", "token")
                                                          .buildFeignRequestTemplate();
        return Response.builder()
                       .body(JsonMarshaller.getInstance()
                                           .getIndentedObjectMapper()
                                           .writeValueAsBytes(ProblemDTO.builder()
                                                                        .status(404)
                                                                        .reasonPhrase("user not found")
                                                                        .errors(List.of(ProblemErrorDTO.builder()
                                                                                                       .name("USER-2-0")
                                                                                                       .reason("user not found")
                                                                                                       .build()))
                                                                        .build()))
                       .status(404)
                       .headers(Map.of(Headers.X_CORRELATION_ID, List.of(UnitTestData.UID),
                                       FeignCommon.X_DATE, List.of("1766349269775")))
                       .request(FeignRequestBuilder.builder()
                                                   .httpMethod(Request.HttpMethod.GET)
                                                   .url("http://inugami.io/mock/user")
                                                   .addHeader(FeignCommon.X_DATE, "1766349269775")
                                                   .body(new byte[]{})
                                                   .charset(StandardCharsets.UTF_8)
                                                   .requestTemplate(requestTemplate)
                                                   .buildFeignRequest())
                       .requestTemplate(requestTemplate)
                       .build();
    }
}