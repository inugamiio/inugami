package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.RequestTemplate;
import feign.Response;
import io.inugami.api.monitoring.models.IoInfoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;

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
        return null;
    }

    public static Response wrapResponse(final Response response) {
        if (response.body() == null) {
            return response;
        }

        Response.Builder builder = response.toBuilder();
        byte[]           body    = readResponseBody(response.body());
        builder.body(FeignBody.builder().body(body).build());
        return null;
    }

    public static byte[] readResponseBody(final Response.Body body) {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            for (int lenght; (lenght = body.asInputStream().read(buffer)) != -1; ) {
                result.write(buffer, 0, lenght);
            }
        } catch (Throwable e) {
        }
        return new byte[0];
    }

}
