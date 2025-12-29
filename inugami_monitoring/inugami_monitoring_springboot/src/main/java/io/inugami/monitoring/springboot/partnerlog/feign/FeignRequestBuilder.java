package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.Request;
import feign.RequestTemplate;
import lombok.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@ToString
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class FeignRequestBuilder {
    private Request.HttpMethod              httpMethod;
    private String                          url;
    private Map<String, Collection<String>> headers;
    private byte[]                          body;
    private Charset                         charset;
    private RequestTemplate                 requestTemplate;


    public static class FeignRequestBuilderBuilder {

        public FeignRequestBuilderBuilder addHeader(final String key, final String value) {
            if (headers == null) {
                headers = new LinkedHashMap<>();
            }
            if (key != null && value != null) {
                final Collection<String> header = headers.get(key);
                if (header == null) {
                    headers.put(key, new ArrayList<>(List.of(value)));
                } else {
                    header.add(value);
                }
            }
            return this;
        }

        public Request buildFeignRequest() {
            return Request.create(Optional.ofNullable(httpMethod).orElse(Request.HttpMethod.GET),
                                  Optional.ofNullable(url).orElse("http://mock"),
                                  new LinkedHashMap<>(Optional.ofNullable(headers).orElse(Map.of())),
                                  body,
                                  Optional.ofNullable(charset).orElse(StandardCharsets.UTF_8),
                                  Optional.ofNullable(requestTemplate).orElse(RequestTemplateBuilder.builder()
                                                                                                    .buildFeignRequestTemplate())
                                 );
        }
    }
}
