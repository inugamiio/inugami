package io.inugami.monitoring.springboot.api;

import feign.FeignException;

public interface FeignErrorCodeBuilderSpi {

    boolean accept(String partner);

    String buildErrorCode(String partner, final FeignException exception);
}
