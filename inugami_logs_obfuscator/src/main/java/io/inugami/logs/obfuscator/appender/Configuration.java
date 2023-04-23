package io.inugami.logs.obfuscator.appender;

import java.util.Map;

public class Configuration {
    private String              mode;
    private boolean             encodeAsJson;
    private String              additionalFields;
    private String              file;
    private String              host;
    private Map<String, String> headers;

    private Integer timeout;
    private Integer timeToLive;
    private Integer maxConnections;
    private Integer maxPerRoute;
    private Integer socketTimeout;


    public Configuration() {
    }

    public Configuration(final String encodeAsJson, final String additionalFields) {
        this.encodeAsJson = Boolean.parseBoolean(encodeAsJson);
        this.additionalFields = additionalFields;
    }

    public Configuration(final boolean encodeAsJson, final String additionalFields) {
        this.encodeAsJson = encodeAsJson;
        this.additionalFields = additionalFields;
    }

    @Override
    public String toString() {
        return "ObfuscatorEncoderConfiguration{" +
                "encodeAsJson=" + encodeAsJson +
                ", additionalFields='" + additionalFields + '\'' +
                '}';
    }

    public boolean isEncodeAsJson() {
        return encodeAsJson;
    }

    public void setEncodeAsJson(final boolean encodeAsJson) {
        this.encodeAsJson = encodeAsJson;
    }

    public String getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(final String additionalFields) {
        this.additionalFields = additionalFields;
    }

    public String getFile() {
        return file;
    }

    public void setFile(final String file) {
        this.file = file;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(final Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(final Integer timeToLive) {
        this.timeToLive = timeToLive;
    }

    public Integer getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(final Integer maxConnections) {
        this.maxConnections = maxConnections;
    }

    public Integer getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(final Integer maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(final Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}
