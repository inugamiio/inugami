package io.inugami.monitoring.springboot.exception;

import feign.FeignException;

import io.inugami.framework.interfaces.monitoring.spring.feign.FeignErrorCodeBuilderSpi;
import io.inugami.framework.interfaces.spi.SpiPriority;

@SpiPriority(Integer.MIN_VALUE)
public class DefaultFeignErrorCodeBuilderSpi implements FeignErrorCodeBuilderSpi {

    private static final String UNDEFINED = "undefined";
    private static final String SEPARATOR = "-";

    @Override
    public boolean accept(final String partner) {
        return true;
    }


    @Override
    public String buildErrorCode(final String partner, final Exception exception) {
        return partner == null ? UNDEFINED : partner + SEPARATOR + getStatus((FeignException) exception);
    }

    private static int getStatus(final Exception exception) {
        if (exception instanceof FeignException feignError) {
            return feignError.status();
        }
        return 500;
    }
}
