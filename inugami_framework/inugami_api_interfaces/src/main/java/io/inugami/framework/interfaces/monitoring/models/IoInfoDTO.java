package io.inugami.framework.interfaces.monitoring.models;

import io.inugami.framework.interfaces.models.JsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
@Builder(toBuilder = true)
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

        public IoInfoDTOBuilder addHeader(final String key, final String value) {
            if (headers == null) {
                headers = new LinkedHashMap<>();
            }

            if (key != null && value != null) {
                if (headers.containsKey(key)) {
                    final Collection<String> values    = headers.get(key);
                    List<String>             newValues = new ArrayList<>(values);
                    newValues.add(value);
                    headers.put(key, newValues);
                } else {
                    headers.put(key, List.of(value));
                }

            }
            return this;
        }

        public IoInfoDTOBuilder addHeaders(final Iterator<Map.Entry<String, String>> values) {
            if (headers == null) {
                headers = new LinkedHashMap<>();
            }

            if (values != null) {
                while (values.hasNext()) {
                    final Map.Entry<String, String> value = values.next();
                    addHeader(value.getKey(), value.getValue());
                }
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
            result.tab().write("duration: ").write(duration).write("ms").line();
            result.tab().write("message: ").write(message).line();
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

        final String currentValue = new String(payload,
                                               requestCharset == null ? StandardCharsets.UTF_8 : requestCharset);
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
            result.tab()
                  .tab()
                  .write(entry.getKey())
                  .write(HEADER_SEP)
                  .write(String.join(HEADER_VALUES_SEP, entry.getValue()))
                  .line();
        }
        return result.toString();
    }
}
