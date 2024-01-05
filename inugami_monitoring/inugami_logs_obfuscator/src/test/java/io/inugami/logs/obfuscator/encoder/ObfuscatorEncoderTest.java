package io.inugami.logs.obfuscator.encoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.marshalling.JsonMarshaller;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


class ObfuscatorEncoderTest {


    @Test
    void initMessageEncoder_nominal() {
        Loggers.APPLICATION.info("password = qwertz123456");
    }


    @Test
    void messageAsJson() throws JsonProcessingException {
        Map<String, Serializable> data = new LinkedHashMap<>();
        data.put("hits", 15);
        data.put("duration", 250L);
        data.put("prices", 125.99);
        data.put("tags", new ArrayList<>(List.of("computer")));
        data.put("ref", "aafd16f5-98b6-4eeb-93fa-80e0c0abbb81");

        Loggers.METRICS.info(JsonMarshaller.getInstance().getIndentedObjectMapper().writeValueAsString(data));
    }
}