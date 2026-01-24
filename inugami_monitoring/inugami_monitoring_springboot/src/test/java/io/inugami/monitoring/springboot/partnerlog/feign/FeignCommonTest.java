package io.inugami.monitoring.springboot.partnerlog.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.Request;
import feign.Response;
import feign.Target;
import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.dto.UserDataDTO;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.inugami.framework.interfaces.monitoring.partner.Partner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FeignCommonTest {


    // =================================================================================================================
    // buildInfo request
    // =================================================================================================================
    @Test
    void buildInfo_withNullValue() {
        assertText(FeignCommon.buildInfo(null),
                   """
                           {
                             "duration" : 0,
                             "status" : 0
                           }
                           """);
    }

    @Test
    void buildInfo_nominal() throws JsonProcessingException {

        final var requestTemplate = RequestTemplateBuilder.builder()
                                                          .target("http://inugami.io/mock/user")
                                                          .method(Request.HttpMethod.GET)
                                                          .feignTarget(new <MyFeignService>Target.HardCodedTarget(MyFeignService.class, "http://inugami.io/mock"))
                                                          .body(JsonMarshaller.getInstance()
                                                                              .getIndentedObjectMapper()
                                                                              .writeValueAsString(UnitTestData.USER_1))
                                                          .addHeader("auth", "token")
                                                          .buildFeignRequestTemplate();

        assertText(FeignCommon.buildInfo(requestTemplate),
                   """
                           {
                             "duration" : 0,
                             "headers" : {
                               "auth" : [ "token" ],
                               "Content-Length" : [ "480" ]
                             },
                             "method" : "GET",
                             "partnerName" : "feign-partner",
                             "partnerService" : "feign_partner",
                             "payload" : "ewogICJiaXJ0aGRheSIgOiAiMTk4OC0wNC0xMiIsCiAgImNhbnRvbiIgOiAiVkQiLAogICJjaXR5IiA6ICJDaGVzZWF1eC1zdXItTGF1c2FubmUiLAogICJkZXZpY2VJZGVudGlmaWVyIiA6ICI0MDFmMDQ5OC1jNDNmLTQzYWQtYTNmNC0yODg4ODM4MzMyYWQiLAogICJlbWFpbCIgOiAiZW1pbGllLmxhbG9uZGVAbW9jay5vcmciLAogICJmaXJzdE5hbWUiIDogIsOJbWlsaWUiLAogICJpZCIgOiAxLAogICJsYXN0TmFtZSIgOiAiTGFsb25kZSIsCiAgIm5hdGlvbmFsaXR5IiA6ICJDSCIsCiAgIm9sZCIgOiAzNSwKICAicGhvbmVOdW1iZXIiIDogIjA2MTUwMzE1MjIiLAogICJzZXgiIDogIkZFTUFMRSIsCiAgInNvY2lhbElkIiA6ICI3NTY0OTcxMjQ3NzMyIiwKICAic3RyZWV0TmFtZSIgOiAiZHUgQ2jDonRlYXUiLAogICJzdHJlZXROdW1iZXIiIDogIjEwIiwKICAic3RyZWV0VHlwZSIgOiAiQ2hlbS4iLAogICJ6aXBDb2RlIiA6ICIxMDMzIgp9",
                             "status" : 0,
                             "url" : "http://inugami.io/mock/user/"
                           }
                           """);
    }

    // =================================================================================================================
    // buildInfo response
    // =================================================================================================================
    @Test
    void buildInfo_response_nominal() throws JsonProcessingException {
        Response nullResponse = null;
        assertText(FeignCommon.buildInfo(nullResponse, 54L),
                   """
                           {
                             "duration" : 54,
                             "status" : 0
                           }
                           """);

        final Map<String, Collection<String>> headers = new LinkedHashMap<>();
        final var request = FeignRequestBuilder.builder()
                                               .httpMethod(Request.HttpMethod.GET)
                                               .url("http://inugami.io/mock/user")
                                               .addHeader(FeignCommon.X_DATE, "1766349269775")
                                               .body(new byte[]{})
                                               .charset(StandardCharsets.UTF_8)
                                               .buildFeignRequest();

        assertText(FeignCommon.buildInfo(
                           FeignCommon.wrapResponse(Response.builder()
                                                            .body(JsonMarshaller.getInstance()
                                                                                .getIndentedObjectMapper()
                                                                                .writeValueAsBytes(UnitTestData.USER_1))
                                                            .status(200)
                                                            .headers(Map.of(Headers.X_CORRELATION_ID, List.of(UnitTestData.UID),
                                                                            FeignCommon.X_DATE, List.of("1766349269775")))
                                                            .request(request)
                                                            .requestTemplate(RequestTemplateBuilder.builder()
                                                                                                   .target("http://inugami.io/mock/user")
                                                                                                   .method(Request.HttpMethod.GET)
                                                                                                   .feignTarget(new <MyFeignService>Target.HardCodedTarget(MyFeignService.class, "http://inugami.io/mock/user"))
                                                                                                   .body(JsonMarshaller.getInstance()
                                                                                                                       .getIndentedObjectMapper()
                                                                                                                       .writeValueAsString(UnitTestData.USER_1))
                                                                                                   .addHeader("auth", "token")
                                                                                                   .buildFeignRequestTemplate())
                                                            .build()), 54L),
                   """
                           {
                             "duration" : 54,
                             "headers" : {
                               "x-date" : [ "1766349269775" ]
                             },
                             "method" : "GET",
                             "payload" : "",
                             "responseHeaders" : {
                               "x-correlation-id" : [ "bb895294-efe7-484b-b670-14d004eaf461" ],
                               "x-date" : [ "1766349269775" ]
                             },
                             "responsePayload" : "ewogICJiaXJ0aGRheSIgOiAiMTk4OC0wNC0xMiIsCiAgImNhbnRvbiIgOiAiVkQiLAogICJjaXR5IiA6ICJDaGVzZWF1eC1zdXItTGF1c2FubmUiLAogICJkZXZpY2VJZGVudGlmaWVyIiA6ICI0MDFmMDQ5OC1jNDNmLTQzYWQtYTNmNC0yODg4ODM4MzMyYWQiLAogICJlbWFpbCIgOiAiZW1pbGllLmxhbG9uZGVAbW9jay5vcmciLAogICJmaXJzdE5hbWUiIDogIsOJbWlsaWUiLAogICJpZCIgOiAxLAogICJsYXN0TmFtZSIgOiAiTGFsb25kZSIsCiAgIm5hdGlvbmFsaXR5IiA6ICJDSCIsCiAgIm9sZCIgOiAzNSwKICAicGhvbmVOdW1iZXIiIDogIjA2MTUwMzE1MjIiLAogICJzZXgiIDogIkZFTUFMRSIsCiAgInNvY2lhbElkIiA6ICI3NTY0OTcxMjQ3NzMyIiwKICAic3RyZWV0TmFtZSIgOiAiZHUgQ2jDonRlYXUiLAogICJzdHJlZXROdW1iZXIiIDogIjEwIiwKICAic3RyZWV0VHlwZSIgOiAiQ2hlbS4iLAogICJ6aXBDb2RlIiA6ICIxMDMzIgp9",
                             "status" : 200,
                             "url" : "http://inugami.io/mock/user"
                           }
                           """);
    }

    @Test
    void resolveCallDate_nominal() throws JsonProcessingException {


        final Map<String, Collection<String>> headers = new LinkedHashMap<>();
        headers.put(FeignCommon.X_DATE, List.of("1766349269775"));
        assertThat(FeignCommon.resolveCallDate(
                           Response.builder()
                                   .body(JsonMarshaller.getInstance()
                                                       .getIndentedObjectMapper()
                                                       .writeValueAsBytes(UnitTestData.USER_1))
                                   .status(200)
                                   .headers(Map.of(Headers.X_CORRELATION_ID, List.of(UnitTestData.UID)))
                                   .request(Request.create(Request.HttpMethod.GET,
                                                           "http://inugami.io/mock/user",
                                                           headers,
                                                           new byte[]{},
                                                           StandardCharsets.UTF_8))
                                   .requestTemplate(RequestTemplateBuilder.builder()
                                                                          .target("http://inugami.io/mock/user")
                                                                          .method(Request.HttpMethod.GET)
                                                                          .feignTarget(new <MyFeignService>Target.HardCodedTarget(MyFeignService.class, "http://inugami.io/mock/user"))
                                                                          .body(JsonMarshaller.getInstance()
                                                                                              .getIndentedObjectMapper()
                                                                                              .writeValueAsString(UnitTestData.USER_1))
                                                                          .addHeader("auth", "token")
                                                                          .buildFeignRequestTemplate())
                                   .build())
                  ).isEqualTo(1766349269775L);

        assertThat(FeignCommon.resolveCallDate(null)).isZero();
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    @Partner(name = "feign-partner", service = "feign_partner")
    interface MyFeignService {

        @Partner(name = "get_user", service = "feign_partner_user")
        UserDataDTO getUser();
    }
}