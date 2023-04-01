package io.inugami.api.monitoring.models;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class IoInfoDTOTest {


    private static final String KEY = "tracking";

    @Test
    public void toString_withResponse_shouldRender() {
        Map<String, Collection<String>> header = new LinkedHashMap<>();
        header.put("x-device-identifier", List.of("6c90fb97-f88e-45bc-b3fe-e9609e10a92d"));

        Map<String, Collection<String>> responseHeader = new LinkedHashMap<>();
        responseHeader.put("x-device-identifier", List.of("6c90fb97-f88e-45bc-b3fe-e9609e10a92d"));
        responseHeader.put("x-correlation-id", List.of("3e8f8272-25c8-4aab-beea-0406f31fdd93"));

        Map<String, String> response = new LinkedHashMap<>();
        final IoInfoDTO info = IoInfoDTO.builder()
                                        .url("http://inugami.io/mock")
                                        .method("POST")
                                        .partnerName("inugami")
                                        .partnerService("createMock")
                                        .partnerSubService("testMock")
                                        .addPayload("{\"name\":\"John Foobar\"}")
                                        .requestCharset(StandardCharsets.UTF_8)
                                        .headers(header)
                                        .status(202)
                                        .duration(15)
                                        .message("created")
                                        .responseHeaders(responseHeader)
                                        .addResponsePayload("{\"id\":1,\"name\":\"John Foobar\"}")
                                        .build();

        assertThat(info.toString()).isEqualTo("[POST] http://inugami.io/mock\n" +
                                                      "request:\n" +
                                                      "\theaders:\n" +
                                                      "\t\tx-device-identifier : 6c90fb97-f88e-45bc-b3fe-e9609e10a92d\n" +
                                                      "\tpayload:\n" +
                                                      "{\"name\":\"John Foobar\"}\n" +
                                                      "response:\n" +
                                                      "\tstatus: 202\n" +
                                                      "\tduration: 15ms\n" +
                                                      "\tmessage: created\n" +
                                                      "\theaders:\n" +
                                                      "\t\tx-device-identifier : 6c90fb97-f88e-45bc-b3fe-e9609e10a92d\n" +
                                                      "\t\tx-correlation-id : 3e8f8272-25c8-4aab-beea-0406f31fdd93\n" +
                                                      "\tpayload:\n" +
                                                      "{\"id\":1,\"name\":\"John Foobar\"}" +
                                                      "\n");
    }


    @Test
    public void addHeader_withKeyValue_shouldAdd(){
        final IoInfoDTO.IoInfoDTOBuilder builder = IoInfoDTO.builder();
        builder.addHeader(KEY, "AAA");
        assertThat(builder.build().getHeaders()).isNotNull();
        assertThat(builder.build().getHeaders().get(KEY).size()).isEqualTo(1);
        assertThat(builder.build().getHeaders().get(KEY)).isEqualTo(List.of("AAA"));

        builder.addHeader(KEY, "BBB");
        assertThat(builder.build().getHeaders().get(KEY).size()).isEqualTo(2);
        assertThat(builder.build().getHeaders().get(KEY)).isEqualTo(List.of("AAA","BBB"));
    }
}
