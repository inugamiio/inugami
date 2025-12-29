package io.inugami.monitoring.springboot.partnerlog.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.Request;
import feign.Target;
import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.api.RegexLineMatcher;
import io.inugami.commons.test.api.UuidLineMatcher;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.tools.MapUtils;
import io.inugami.monitoring.core.context.MonitoringContext;
import io.inugami.monitoring.core.context.MonitoringContextUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.inugami.commons.test.UnitTestHelper.assertLogs;
import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeignPartnerRequestInterceptorTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @Mock
    private MonitoringContext              monitoringContext;
    @InjectMocks
    private FeignPartnerRequestInterceptor interceptor;

    @AfterEach
    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
    }

    // =================================================================================================================
    // apply
    // =================================================================================================================
    @Test
    void apply_nominal() throws JsonProcessingException {
        MdcService.getInstance()
                  .deviceIdentifier("47991fbc-529a-42e3-9ac6-9d7992713250")
                  .correlationId("ba26cf88-a6ee-40e2-83cf-89ebf11eeb35")
                  .requestId("f19c4ab9-fa52-4b8e-b34a-3f58127db6a7");

        when(monitoringContext.getTrackingInformation()).thenReturn(MonitoringContextUtils.getTrackingInformation(null));
        final var requestTemplate = RequestTemplateBuilder.builder()
                                                          .target("http://inugami.io/mock/user")
                                                          .method(Request.HttpMethod.POST)
                                                          .feignTarget(new <FeignCommonTest.MyFeignService>Target.HardCodedTarget(FeignCommonTest.MyFeignService.class, "http://inugami.io/mock/user"))
                                                          .body(JsonMarshaller.getInstance()
                                                                              .getIndentedObjectMapper()
                                                                              .writeValueAsString(UnitTestData.USER_1))
                                                          .addHeader("auth", "token")
                                                          .buildFeignRequestTemplate();
        assertLogs(() -> interceptor.apply(requestTemplate),
                   Loggers.PARTNERLOG_NAME,
                   """
                           [
                                 {
                                     "loggerName":"PARTNERLOG",
                                     "level":"INFO",
                                     "mdc":{
                                         "partner":"feign-partner",
                                         "partnerResponseDuration":"0",
                                         "partnerResponseStatus":"0",
                                         "partnerService":"feign_partner",
                                         "partnerUrl":"http://inugami.io/mock/user/",
                                         "partnerVerb":"POST"
                                     },
                                     "message":[
                                         "21/12 23:23:10  INFO [PARTNERLOG:42] - [POST] http://inugami.io/mock/user/",
                                         "request:",
                                         "	headers:",
                                         "		auth : token",
                                         "		Content-Length : 480",
                                         "		x-b3-traceid : 3d623248-5d0e-4be0-830f-d95593e6dcc7",
                                         "		x-correlation-id : ba26cf88-a6ee-40e2-83cf-89ebf11eeb35",
                                         "		x-device-identifier : 47991fbc-529a-42e3-9ac6-9d7992713250",
                                         "	payload:",
                                         "{",
                                         "  \\"birthday\\" : \\"1988-04-12\\",",
                                         "  \\"canton\\" : \\"VD\\",",
                                         "  \\"city\\" : \\"Cheseaux-sur-Lausanne\\",",
                                         "  \\"deviceIdentifier\\" : \\"401f0498-c43f-43ad-a3f4-2888838332ad\\",",
                                         "  \\"email\\" : \\"emilie.lalonde@mock.org\\",",
                                         "  \\"firstName\\" : \\"Émilie\\",",
                                         "  \\"id\\" : 1,",
                                         "  \\"lastName\\" : \\"Lalonde\\",",
                                         "  \\"nationality\\" : \\"CH\\",",
                                         "  \\"old\\" : 35,",
                                         "  \\"phoneNumber\\" : \\"0615031522\\",",
                                         "  \\"sex\\" : \\"FEMALE\\",",
                                         "  \\"socialId\\" : \\"7564971247732\\",",
                                         "  \\"streetName\\" : \\"du Château\\",",
                                         "  \\"streetNumber\\" : \\"10\\",",
                                         "  \\"streetType\\" : \\"Chem.\\",",
                                         "  \\"zipCode\\" : \\"1033\\"",
                                         "}"
                                     ]
                                 }
                             ]
                           """,
                   RegexLineMatcher.of(".*PARTNERLOG.*POST.*http://inugami.io/mock/user.*", 13),
                   UuidLineMatcher.of(18));

        assertText(MapUtils.initMapAndSort(requestTemplate.headers()),
                   """
                           {
                             "Content-Length" : [ "480" ],
                             "auth" : [ "token" ],
                             "x-b3-traceid" : [ "1cecc2bd-fe46-4795-90f9-da95ebecbff6" ],
                             "x-correlation-id" : [ "ba26cf88-a6ee-40e2-83cf-89ebf11eeb35" ],
                             "x-date" : [ "1766355950669" ],
                             "x-device-identifier" : [ "47991fbc-529a-42e3-9ac6-9d7992713250" ]
                           }
                           """,
                   UuidLineMatcher.of(3),
                   RegexLineMatcher.of(".*x-date.*\"[0-9]+.*", 5));
    }
}