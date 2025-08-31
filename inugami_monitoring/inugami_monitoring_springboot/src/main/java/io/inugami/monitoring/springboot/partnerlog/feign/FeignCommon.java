package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import io.inugami.framework.interfaces.monitoring.models.IoInfoDTO;
import io.inugami.framework.interfaces.monitoring.partner.Partner;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;

import static io.inugami.framework.api.tools.ReflectionUtils.getAnnotation;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.orNull;


@SuppressWarnings({"java:S1181", "java:S108"})
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeignCommon {

    public static final String X_DATE = "x-date";

    public static IoInfoDTO buildInfo(final RequestTemplate request) {
        final IoInfoDTO.IoInfoDTOBuilder builder = IoInfoDTO.builder();
        if (request == null) {
            return builder.build();
        }
        try {
            builder.url(request.url());
            builder.headers(request.headers());
            builder.method(request.method());

            if (request.body() != null) {
                builder.payload(request.body());
            }

            resolvePartnerInformation(request, builder);

        } catch (final Throwable e) {
        }

        return builder.build();
    }

    private static void resolvePartnerInformation(final RequestTemplate request,
                                                  final IoInfoDTO.IoInfoDTOBuilder builder) {
        Partner rootPartner = null;
        Partner partner     = null;

        String partnerName       = null;
        String partnerService    = null;
        String partnerSubService = null;

        if (request.feignTarget() != null) {
            rootPartner = getAnnotation(request.feignTarget().type(), Partner.class);
        }

        if (request.methodMetadata() != null) {
            partner = getAnnotation(request.methodMetadata().method(), Partner.class);
        }

        if (rootPartner != null) {
            partnerName = orNull(rootPartner.name());
            partnerService = orNull(rootPartner.service());

        }

        if (partner != null) {
            final String currentName = orNull(partner.name());
            if (currentName != null) {
                partnerName = currentName;
            }

            final String currentPartnerService = orNull(partner.service());
            if (currentPartnerService != null) {
                partnerService = currentPartnerService;
            }

            partnerSubService = orNull(partner.subService());
        }

        if (partnerName == null && request.feignTarget() != null) {
            partnerName = request.feignTarget().name();
        }

        builder.partnerName(partnerName);
        builder.partnerService(partnerService);
        builder.partnerSubService(partnerSubService);
    }

    public static long resolveCallDate(final Response response) {
        final Collection<String> xDate =
                response.request() == null || response.request().headers() == null ? null : response.request()
                                                                                                    .headers()
                                                                                                    .get(FeignCommon.X_DATE);
        String xDateValue = null;
        if (xDate != null && !xDate.isEmpty()) {
            xDateValue = xDate.toArray(new String[]{})[0];
        }
        if (xDateValue == null) {
            return 0;
        } else {
            try {
                return Long.parseLong(xDateValue);
            } catch (final Exception e) {
                return 0;
            }
        }

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
