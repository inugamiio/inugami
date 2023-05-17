package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import io.inugami.api.monitoring.models.IoInfoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeignCommon {

    public static final String X_DATE = "x-date";

    public static IoInfoDTO buildInfo(final RequestTemplate requestTemplate) {
        return null;
    }

    public static long resolveCallDate(final Response response) {
        return 0;
    }

    public static IoInfoDTO buildInfo(final Response wrappedResponse, final long duration) {
        final IoInfoDTO.IoInfoDTOBuilder builder = IoInfoDTO.builder()
                                                            .duration(duration);

        if (wrappedResponse == null) {
            return builder.build();
        }

        builder.status(wrappedResponse.status())
               .message(wrappedResponse.reason())
               .headers(wrappedResponse.request().headers())
               .responseHeaders(wrappedResponse.headers());
        if (wrappedResponse.body() != null) {
            byte[] body = new byte[0];
            try {
                body = wrappedResponse.body().asInputStream().readAllBytes();
            } catch (final IOException e) {
            }
            builder.responsePayload(body);
        }

        if (wrappedResponse.request() != null) {
            final Request request = wrappedResponse.request();
            builder.url(request.url());
            builder.headers(request.headers());

            if (request.httpMethod() != null) {
                builder.method(request.httpMethod().name());
            }
            if (request.body() != null) {
                builder.payload(request.body());
            }
            if (request.requestTemplate() != null && request.requestTemplate().feignTarget() != null) {
                builder.partnerName(request.requestTemplate().feignTarget().name());
            }

        }
        return builder.build();
    }

    private static Charset resolveEncoding(final Response wrappedResponse) {
        return wrappedResponse.charset() == null ? StandardCharsets.UTF_8 : wrappedResponse.charset();
    }

    public static Response wrapResponse(final Response response) {
        if (response.body() == null) {
            return response;
        }

        final Response.Builder builder = response.toBuilder();
        final byte[]           body    = readResponseBody(response.body());
        builder.body(FeignBody.builder().body(body).build());
        return builder.build();
    }

    public static byte[] readResponseBody(final Response.Body body) {
        try {
            return body.asInputStream().readAllBytes();
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
            return new byte[]{};
        }
    }

}
