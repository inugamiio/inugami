package io.inugami.monitoring.springboot.exception;

import feign.FeignException;
import io.inugami.framework.api.exceptions.ExceptionUtils;
import io.inugami.framework.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCodeResolver;
import io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
import io.inugami.framework.interfaces.spi.SpiLoader;
import io.inugami.monitoring.springboot.api.FeignErrorCodeBuilderSpi;

import java.util.List;


public class FeignErrorCodeResolver implements ErrorCodeResolver, ApplicationLifecycleSPI {
    private List<FeignErrorCodeBuilderSpi> feignErrorCodeBuilderSpi;

    public FeignErrorCodeResolver() {
        initErrorCodeBuilder();
        DefaultApplicationLifecycleSPI.register(this);
    }

    @Override
    public void onContextRefreshed(final Object event) {
        initErrorCodeBuilder();
    }

    private void initErrorCodeBuilder() {
        feignErrorCodeBuilderSpi = SpiLoader.getInstance().loadSpiServicesByPriority(FeignErrorCodeBuilderSpi.class);
    }


    @Override
    public ErrorCode resolve(final Throwable exception) {
        ErrorCode result = null;
        if (exception instanceof FeignException) {
            result = buildError((FeignException) exception);
        }
        return result;
    }

    private ErrorCode buildError(final FeignException exception) {
        final String partner = MdcService.getInstance().partner();
        final int    status  = exception.status();

        final DefaultErrorCode.DefaultErrorCodeBuilder error        = DefaultErrorCode.buildUndefineErrorCode();
        final FeignErrorCodeBuilderSpi                 errorBuilder = resolveErrorBuilder(partner);

        final Throwable cause = ExceptionUtils.searchCause(4, exception);
        error.errorCode(errorBuilder.buildErrorCode(partner, exception))
             .statusCode(status)
             .message(exception.getMessage())
             .messageDetail(cause == null ? null : cause.getMessage());

        return error.build();
    }

    private FeignErrorCodeBuilderSpi resolveErrorBuilder(final String partner) {
        for (final FeignErrorCodeBuilderSpi instance : feignErrorCodeBuilderSpi) {
            if (instance.accept(partner)) {
                return instance;
            }
        }
        // but not possible
        return new DefaultFeignErrorCodeBuilderSpi();
    }


}
