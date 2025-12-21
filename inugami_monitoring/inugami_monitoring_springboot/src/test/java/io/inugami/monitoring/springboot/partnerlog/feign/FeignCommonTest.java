package io.inugami.monitoring.springboot.partnerlog.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.Request;
import feign.Target;
import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.dto.UserDataDTO;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.monitoring.partner.Partner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.inugami.commons.test.UnitTestHelper.assertText;

@ExtendWith(MockitoExtension.class)
class FeignCommonTest {


    // =================================================================================================================
    // TEST
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
                                                          .feignTarget(new <MyFeignService>Target.HardCodedTarget(MyFeignService.class, "http://inugami.io/mock/user"))
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


    @Partner(name = "feign-partner", service = "feign_partner")
    interface MyFeignService {

        @Partner(name = "get_user", service = "feign_partner_user")
        UserDataDTO getUser();
    }
}