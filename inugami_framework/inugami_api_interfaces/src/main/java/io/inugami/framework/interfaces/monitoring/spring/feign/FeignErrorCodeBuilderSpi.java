package io.inugami.framework.interfaces.monitoring.spring.feign;



public interface FeignErrorCodeBuilderSpi {

    boolean accept(String partner);

    String buildErrorCode(String partner, final Exception exception);
}
