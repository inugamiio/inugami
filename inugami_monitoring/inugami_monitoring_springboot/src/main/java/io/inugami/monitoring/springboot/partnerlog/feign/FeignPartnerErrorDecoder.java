package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.MessagesFormatter;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.models.IoInfoDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@SuppressWarnings({"java:S2629"})
@Getter
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

        final String feignClient = response.request().requestTemplate().feignTarget().name();
        final String urlTemplate = response.request().requestTemplate().methodMetadata().configKey();

        final FeignPartnerErrorResolver resolver  = resolveFeignPartnerErrorResolver(wrappedResponse, feignClient, urlTemplate);
        final ErrorCode                 errorCode = resolveErrorCode(resolver, wrappedResponse, feignClient, urlTemplate);

        MdcService.getInstance()
                  .ioinfoPartner(ioInfo)
                  .errorCode(errorCode);

        Loggers.PARTNERLOG.error(ioInfo.toString());

        MdcService.getInstance().partnerRemove();

        return buildException(errorCode, resolver);
    }

    private Exception buildException(final ErrorCode errorCode, final FeignPartnerErrorResolver resolver) {
        final Exception result = resolver.buildException(errorCode);
        return result == null ? defaultResolver.buildException(errorCode) : result;
    }


    private FeignPartnerErrorResolver resolveFeignPartnerErrorResolver(final Response wrappedResponse,
                                                                       final String feignClient,
                                                                       final String urlTemplate) {
        FeignPartnerErrorResolver result = null;
        if (errorResolvers != null) {
            for (final FeignPartnerErrorResolver resolver : errorResolvers) {
                if (resolver.accept(wrappedResponse, feignClient, urlTemplate)) {
                    result = resolver;
                    break;
                }
            }
        }

        if (result == null) {
            result = defaultResolver;
        }
        return result;
    }

    private ErrorCode resolveErrorCode(final FeignPartnerErrorResolver resolver,
                                       final Response wrappedResponse,
                                       final String feignClient,
                                       final String urlTemplate) {
        final ErrorCode result = resolver.resolve(wrappedResponse, feignClient, urlTemplate);
        return result != null ? result : defaultResolver.resolve(wrappedResponse, feignClient, urlTemplate);
    }

    private static class DefaultFeignPartnerErrorResolver implements FeignPartnerErrorResolver {

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
