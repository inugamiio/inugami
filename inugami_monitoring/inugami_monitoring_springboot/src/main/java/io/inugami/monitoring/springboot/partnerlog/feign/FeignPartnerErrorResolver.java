package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.Response;
import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.UncheckedException;
import io.inugami.api.monitoring.AuthenticationErrorNotXLError;

import java.util.List;

public interface FeignPartnerErrorResolver {
    List<Integer> XL_STATUS      = List.of(502, 503, 504, 505, 507);
    List<Integer> XL_AUTH_STATUS = List.of(401, 403);

    boolean accept(final Response wrappedResponse, final String feignClient, final String urlTemplate);

    ErrorCode resolve(final Response wrappedResponse, final String feignClient, final String urlTemplate);

    default Exception buildException(ErrorCode errorCode) {
        return new UncheckedException(errorCode);
    }

    @SuppressWarnings({"java:S1066"})
    default boolean isXlError(final Response wrappedResponse) {
        boolean result = false;

        if (wrappedResponse == null) {
            return result;
        }
        result = XL_STATUS.contains(wrappedResponse.status());

        if (!result && XL_AUTH_STATUS.contains(wrappedResponse.status())) {
            if (!(wrappedResponse.request().requestTemplate().feignTarget() instanceof AuthenticationErrorNotXLError)) {
                result = true;
            }
        }

        return result;
    }
}
