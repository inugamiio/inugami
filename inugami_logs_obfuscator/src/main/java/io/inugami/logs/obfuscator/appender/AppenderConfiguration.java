package io.inugami.logs.obfuscator.appender;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.api.marshalling.JsonMarshaller;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@ToString(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AppenderConfiguration {
    @ToString.Include
    private String              mode;
    @ToString.Include
    private boolean             encodeAsJson;
    @ToString.Include
    private String              additionalFields;
    @ToString.Include
    private String              file;
    @ToString.Include
    private String              host;
    private String              headers;
    private Map<String, String> headersMap;
    private String              index;
    private String              indexDatePattern;
    private Integer             timeout;
    private Integer             timeToLive;
    private Integer             maxConnections;
    private Integer             maxPerRoute;
    private Integer             socketTimeout;
    private boolean             forceNewLine = true;


    public AppenderConfiguration(final String encodeAsJson, final String additionalFields) {
        this.encodeAsJson = Boolean.parseBoolean(encodeAsJson);
        this.additionalFields = additionalFields;
    }

    public AppenderConfiguration(final boolean encodeAsJson, final String additionalFields) {
        this.encodeAsJson = encodeAsJson;
        this.additionalFields = additionalFields;
    }

    public void init() {
        if (headers != null) {
            try {
                headersMap = JsonMarshaller.getInstance().getDefaultObjectMapper().readValue(headers, Map.class);
            } catch (JsonProcessingException e) {
                headersMap = new HashMap<>();
            }
        }
    }
}
