package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.Request;
import feign.Response;
import feign.Target;
import io.inugami.api.monitoring.models.IoInfoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;
import static org.assertj.core.api.Assertions.assertThat;

class FeignCommonTest {
    @Test
    void buildInfo_withRequest_nominal() {
        final IoInfoDTO iologInfo = FeignCommon.buildInfo(RequestTemplateBuilder.builder()
                                                                                .method(Request.HttpMethod.POST)
                                                                                .addHeader("someKey", "value")
                                                                                .body("lorem ipsum")
                                                                                .uri("/my/service")
                                                                                .addQuery("page", "12")
                                                                                .addQuery("pageSize", "20")
                                                                                .feignTarget(new Target.HardCodedTarget(SimpleFeignClient.class, "http://mock"))
                                                                                .buildFeignRequestTemplate()
        );
        assertTextRelative(iologInfo.toString(), "partnerlog/feign/feignCommonTest/buildInfo_withRequest_nominal.txt");
    }

    @Test
    void buildInfo_withRequest_withoutData() {
        assertThat(FeignCommon.buildInfo(null)).isNotNull();
    }

    @Test
    void buildInfo_nominal() {
        final Response response = Response.builder()
                                          .body("some value", StandardCharsets.UTF_8)
                                          .status(200)
                                          .reason("created")
                                          .headers(Map.ofEntries(Map.entry("x-traceId", List.of("6e3eb0fa-9710-4b3a-8037-4cb380698d03"))))
                                          .request(Request.create(Request.HttpMethod.POST,
                                                                  "http://mock",
                                                                  Map.ofEntries(Map.entry("x-traceId", List.of("000eb0fa-9710-4b3a-8037-4cb380698d03"))),
                                                                  "requestPayload".getBytes(StandardCharsets.UTF_8),
                                                                  StandardCharsets.UTF_8,
                                                                  RequestTemplateBuilder.builder()
                                                                                        .feignTarget(new Target.HardCodedTarget(SimpleFeignClient.class, "http://mock"))
                                                                                        .buildFeignRequestTemplate()
                                          ))
                                          .build();
        final IoInfoDTO iologInfo = FeignCommon.buildInfo(response, 200L);
        assertTextRelative(iologInfo.toString(), "partnerlog/feign/feignCommonTest/buildInfo_nominal.txt");
    }

    @Test
    void buildInfo_withoutValue() {
        final IoInfoDTO iologInfo = FeignCommon.buildInfo(null, 200L);
        assertTextRelative(iologInfo.toString(), "partnerlog/feign/feignCommonTest/buildInfo_withoutValue.txt");
    }


    @FeignClient(value = "partner", url = "http://mock")
    private static interface SimpleFeignClient {
        @PostMapping(value = "/say-hello")
        String sayHello(@RequestBody String value);
    }
}