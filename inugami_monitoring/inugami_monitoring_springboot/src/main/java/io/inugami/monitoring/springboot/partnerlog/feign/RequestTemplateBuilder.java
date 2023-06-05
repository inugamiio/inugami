package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.*;
import lombok.*;

import java.util.*;


@ToString
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class RequestTemplateBuilder {
    private String                          target;
    private String                          uri;
    private Map<String, Collection<String>> queries;
    private Map<String, Collection<String>> headers;
    private String                          body;
    private Target<?>                       feignTarget;


    private Request.HttpMethod method;


    private boolean          decodeSlash;
    private CollectionFormat collectionFormat;
    private MethodMetadata   methodMetadata;


    public static class RequestTemplateBuilderBuilder {
        public RequestTemplateBuilder.RequestTemplateBuilderBuilder addHeader(final String key, final String value) {
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

        public RequestTemplateBuilder.RequestTemplateBuilderBuilder addQuery(final String key, final String value) {
            if (queries == null) {
                queries = new LinkedHashMap<>();
            }
            if (key != null && value != null) {
                final Collection<String> query = queries.get(key);
                if (query == null) {
                    queries.put(key, new ArrayList<>(List.of(value)));
                } else {
                    query.add(value);
                }
            }
            return this;
        }

        public RequestTemplate buildFeignRequestTemplate() {
            final RequestTemplate result = new RequestTemplate();
            result.headers(headers);
            result.target(target);
            result.uri(uri);
            result.method(method == null ? Request.HttpMethod.GET : method);

            if (queries != null) {
                result.queries(queries);
            }

            if (body != null) {
                result.body(body);
            }
            if (target != null) {
                result.target(target);
            }

            if (feignTarget != null) {
                result.feignTarget(feignTarget);
            }
            return result;
        }
    }
}
