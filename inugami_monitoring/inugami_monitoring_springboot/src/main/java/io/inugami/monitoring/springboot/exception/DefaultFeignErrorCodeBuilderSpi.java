package io.inugami.monitoring.springboot.exception;

import feign.FeignException;

import io.inugami.framework.interfaces.spi.SpiPriority;
import io.inugami.monitoring.springboot.api.FeignErrorCodeBuilderSpi;

@SpiPriority(Integer.MIN_VALUE)
public class DefaultFeignErrorCodeBuilderSpi implements FeignErrorCodeBuilderSpi {

    private static final String UNDEFINED = "undefined";
    private static final String SEPARATOR = "-";

    @Override
    public boolean accept(final String partner) {
        return true;
    }

    @Override
    public String buildErrorCode(final String partner, final FeignException exception) {
        return partner == null ? UNDEFINED : partner + SEPARATOR + exception.status();
    }
}
