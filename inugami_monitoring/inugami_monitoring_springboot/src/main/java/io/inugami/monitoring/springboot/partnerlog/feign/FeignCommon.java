package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import io.inugami.framework.api.tools.RunSafeUtils;
import io.inugami.framework.interfaces.monitoring.models.IoInfoDTO;
import io.inugami.framework.interfaces.monitoring.partner.Partner;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static io.inugami.framework.api.tools.ReflectionUtils.getAnnotation;
import static io.inugami.framework.api.tools.RunSafeUtils.runSafeOrElse;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.*;


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
        RunSafeUtils.runSafeVoid(() -> {
            builder.url(request.url());
            builder.headers(request.headers());
            builder.method(request.method());
            applyIfNotNull(request.body(), builder::payload);
            resolvePartnerInformation(request, builder);
        }, log);

        return builder.build();
    }

    protected static void resolvePartnerInformation(final RequestTemplate request,
                                                  final IoInfoDTO.IoInfoDTOBuilder builder) {

        Partner rootPartner = ifNotNull(request.feignTarget(), t -> getAnnotation(t.type(), Partner.class));
        Partner partner     = ifNotNull(request.methodMetadata(), m -> getAnnotation(m.method(), Partner.class));

        String partnerName       = null;
        String partnerService    = null;
        String partnerSubService = null;

        if (rootPartner != null) {
            partnerName    = orNull(rootPartner.name());
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
        final String xDateValue = Optional.ofNullable(response)
                                          .map(Response::request)
                                          .map(Request::headers)
                                          .map(headers -> headers.get(FeignCommon.X_DATE))
                                          .orElse(List.of())
                                          .stream()
                                          .findFirst()
                                          .orElse(null);

        if (xDateValue == null) {
            return 0;
        } else {
            final var currentDate = xDateValue;
            return Optional.ofNullable(RunSafeUtils.runSafe(() -> Long.parseLong(currentDate))).orElse(0L);
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

        applyIfNotNull(wrappedResponse.body(),
                       body -> builder.responsePayload(runSafeOrElse(() -> wrappedResponse.body()
                                                                                          .asInputStream()
                                                                                          .readAllBytes(),
                                                                     new byte[0]))
                      );

        if (wrappedResponse.request() != null) {
            final Request request = wrappedResponse.request();
            builder.url(request.url());
            builder.headers(request.headers());

            applyIfNotNull(request.httpMethod(), httpMethod -> builder.method(httpMethod.name()));
            applyIfNotNull(request.body(), builder::payload);
            applyIfNotNull(Optional.ofNullable(request.requestTemplate())
                                   .map(RequestTemplate::feignTarget)
                                   .orElse(null),
                           feignTarget -> builder.partnerName(feignTarget.name()));
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
        return Optional.ofNullable(RunSafeUtils.runSafe(() -> body.asInputStream().readAllBytes(), log))
                       .orElse(new byte[]{});
    }

}
