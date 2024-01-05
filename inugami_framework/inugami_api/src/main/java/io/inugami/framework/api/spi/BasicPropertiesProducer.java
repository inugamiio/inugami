package io.inugami.framework.api.spi;

import io.inugami.framework.interfaces.spi.PropertiesProducerSpi;

import java.util.HashMap;
import java.util.Map;

public class BasicPropertiesProducer implements PropertiesProducerSpi {
    
    @Override
    public Map<String, String> produce() {
        final Map<String, String> result = new HashMap<>();
        
        result.put("EVERY_10_SECONDS", "0/10 * * * * ?");
        result.put("EVERY_MIN", "0 * * * * ?");
        result.put("EVERY_MINUTE", "0 * * * * ?");
        result.put("EVERY_TWO_MIN", "0 0/2 * * * ?");
        result.put("EVERY_FIVE_MIN", "0 0/5 * * * ?");
        result.put("EVERY_TEN_MIN", "0 0/10 * * * ?");
        result.put("EVERY_HOUR", "0 0 * * * ?");
        result.put("EVERY_DAY", "0 0 0 * * ?");
        
        return result;
    }
}
