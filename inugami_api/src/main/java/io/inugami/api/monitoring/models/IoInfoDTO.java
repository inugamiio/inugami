package io.inugami.api.monitoring.models;

import io.inugami.api.models.JsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class IoInfoDTO {
    private static final String                          HEADER_SEP        = " : ";
    private static final String                          HEADER_VALUES_SEP = ";";
    private final        String                          url;
    private final        String                          method;
    private final        String                          partnerName;
    private final        String                          partnerService;
    private final        String                          partnerSubService;
    private final        Map<String, Collection<String>> headers;
    private final        Charset                         requestCharset;
    private final        byte[]                          payload;

    private final int                             status;
    private final long                            duration;
    private final String                          message;
    private final Map<String, Collection<String>> responseHeaders;
    private final Charset                         responseCharset;
    private final byte[]                          responsePayload;


    public static class IoInfoDTOBuilder {
        public IoInfoDTOBuilder addPayload(final String payload) {
            if (payload != null) {
                this.payload = payload.getBytes(StandardCharsets.UTF_8);
                this.requestCharset = StandardCharsets.UTF_8;
            }
            return this;
        }

        public IoInfoDTOBuilder addResponsePayload(final String payload) {
            if (payload != null) {
                this.responsePayload = payload.getBytes(StandardCharsets.UTF_8);
                this.requestCharset = StandardCharsets.UTF_8;
            }
            return this;
        }
    }

    @Override
    public String toString() {
        final JsonBuilder result = new JsonBuilder();
        result.write("[").write(method).write("] ").write(url).line();
        result.write("request:").line();
        result.write(writeHeaders(headers));
        result.write(writePayload(payload, requestCharset));

        if (status > 0) {
            result.write("response:").line();
            result.tab().write("status: ").write(status).line();
            result.tab().write("duration: ").write(status).write("ms").line();
            result.tab().write("message: ").write(status).line();
            result.write(writeHeaders(responseHeaders));
            result.write(writePayload(responsePayload, responseCharset));
        }

        return result.toString();
    }

    private String writePayload(final byte[] payload, final Charset requestCharset) {
        final JsonBuilder result = new JsonBuilder();
        if (payload == null) {
            return result.toString();
        }

        final String currentValue = new String(payload, requestCharset == null ? StandardCharsets.UTF_8 : requestCharset);
        result.tab().write("payload:").line();
        result.write(currentValue).line();
        return result.toString();
    }

    private String writeHeaders(final Map<String, Collection<String>> headers) {
        final JsonBuilder result = new JsonBuilder();
        if (headers == null || headers.isEmpty()) {
            return result.toString();
        }

        result.tab().write("headers:").line();
        for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
            result.tab().tab().write(entry.getKey()).write(HEADER_SEP).write(String.join(HEADER_VALUES_SEP, entry.getValue())).line();
        }
        return result.toString();
    }
}
