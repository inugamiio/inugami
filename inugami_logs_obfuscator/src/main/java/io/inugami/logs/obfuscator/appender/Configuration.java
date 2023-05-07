package io.inugami.logs.obfuscator.appender;

import lombok.*;

import java.util.Map;

@ToString(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
    @ToString.Include
    private String  mode;
    @ToString.Include
    private boolean encodeAsJson;
    @ToString.Include
    private String  additionalFields;

    @ToString.Include
    private String              file;
    @ToString.Include
    private String              host;
    private Map<String, String> headers;

    private Integer timeout;
    private Integer timeToLive;
    private Integer maxConnections;
    private Integer maxPerRoute;
    private Integer socketTimeout;


    public Configuration(final String encodeAsJson, final String additionalFields) {
        this.encodeAsJson = Boolean.parseBoolean(encodeAsJson);
        this.additionalFields = additionalFields;
    }

    public Configuration(final boolean encodeAsJson, final String additionalFields) {
        this.encodeAsJson = encodeAsJson;
        this.additionalFields = additionalFields;
    }

}
