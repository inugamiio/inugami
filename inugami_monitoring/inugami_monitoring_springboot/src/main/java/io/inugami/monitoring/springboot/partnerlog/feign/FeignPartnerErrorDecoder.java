package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.*;
import feign.codec.ErrorDecoder;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.MessagesFormatter;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.models.IoInfoDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@SuppressWarnings({"java:S2629"})
@Getter
@Builder
@RequiredArgsConstructor
public class FeignPartnerErrorDecoder implements ErrorDecoder {
    private final List<FeignPartnerErrorResolver> errorResolvers;
    private final FeignPartnerErrorResolver       defaultResolver = new DefaultFeignPartnerErrorResolver();


    @Override
    public Exception decode(final String methodKey, final Response response) {
        final long now      = System.currentTimeMillis();
        final long callDate = FeignCommon.resolveCallDate(response);
        long       duration = 0;
        if (callDate != 0) {
            duration = now - callDate;
        }

        final Response  wrappedResponse = FeignCommon.wrapResponse(response);
        final IoInfoDTO ioInfo          = FeignCommon.buildInfo(wrappedResponse, duration);

        final var requestTemplate = Optional.ofNullable(response)
                                            .map(Response::request)
                                            .map(Request::requestTemplate)
                                            .orElse(null);
        final String feignClient = Optional.ofNullable(requestTemplate)
                                           .map(RequestTemplate::feignTarget)
                                           .map(Target::name)
                                           .orElse(null);
        final String urlTemplate = Optional.ofNullable(requestTemplate)
                                           .map(RequestTemplate::methodMetadata)
                                           .map(MethodMetadata::configKey)
                                           .orElse(null);
        final var resolver  = resolveFeignPartnerErrorResolver(wrappedResponse, feignClient, urlTemplate);
        final var errorCode = resolveErrorCode(resolver, wrappedResponse, feignClient, urlTemplate);

        MdcService.getInstance()
                  .ioinfoPartner(ioInfo)
                  .errorCode(errorCode);

        Loggers.PARTNERLOG.error(ioInfo.toString());

        MdcService.getInstance().partnerRemove();

        return buildException(errorCode, resolver);
    }

    protected Exception buildException(final ErrorCode errorCode, final FeignPartnerErrorResolver resolver) {
        final Exception result = resolver.buildException(errorCode);
        return Optional.ofNullable(result).orElse(defaultResolver.buildException(errorCode));
    }


    protected FeignPartnerErrorResolver resolveFeignPartnerErrorResolver(final Response wrappedResponse,
                                                                         final String feignClient,
                                                                         final String urlTemplate) {
        for (final FeignPartnerErrorResolver resolver : Optional.ofNullable(errorResolvers).orElse(List.of())) {
            if (resolver.accept(wrappedResponse, feignClient, urlTemplate)) {
                return resolver;
            }
        }
        return new DefaultFeignPartnerErrorResolver();
    }

    protected ErrorCode resolveErrorCode(final FeignPartnerErrorResolver resolver,
                                         final Response wrappedResponse,
                                         final String feignClient,
                                         final String urlTemplate) {
        final ErrorCode result = resolver.resolve(wrappedResponse, feignClient, urlTemplate);
        return result != null ? result : defaultResolver.resolve(wrappedResponse, feignClient, urlTemplate);
    }

    protected static class DefaultFeignPartnerErrorResolver implements FeignPartnerErrorResolver {

        @Override
        public boolean accept(final Response wrappedResponse, final String feignClient, final String urlTemplate) {
            return true;
        }

        @Override
        public ErrorCode resolve(final Response wrappedResponse, final String feignClient, final String urlTemplate) {
            return DefaultErrorCode
                    .buildUndefineErrorCode()
                    .statusCode(wrappedResponse.status())
                    .errorCode(MessagesFormatter.format("{0}-{1}", feignClient, wrappedResponse.status()))
                    .message(wrappedResponse.reason())
                    .exploitationError(isXlError(wrappedResponse))
                    .build();
        }
    }
}
