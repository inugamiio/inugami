package io.inugami.framework.interfaces.monitoring.logger;


import io.inugami.framework.interfaces.models.JsonBuilder;
import lombok.Builder;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


@Builder(toBuilder = true)
public class LogInfoDTO {
    private Map<String, Serializable> data = new LinkedHashMap<>();

    public static class LogInfoDTOBuilder {
        public LogInfoDTOBuilder with(final String key, final Serializable value) {
            if (this.data == null) {
                data = new LinkedHashMap<>();
            }
            if (key != null) {
                this.data.put(key, value == null ? "null" : value);
            }
            return this;
        }
    }

    @Override
    public String toString() {
        final JsonBuilder result = new JsonBuilder();
        if (data != null) {
            for (Map.Entry<String, Serializable> entry : data.entrySet()) {
                result.write(entry.getKey()).doubleDot();
                result.tab();
                result.write(entry.getValue());
                result.line();
            }
        }
        return result.toString();
    }
}
