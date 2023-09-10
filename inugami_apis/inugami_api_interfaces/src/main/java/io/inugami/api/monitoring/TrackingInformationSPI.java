package io.inugami.api.monitoring;

import io.inugami.api.monitoring.models.IHeaders;

import java.util.Map;

@FunctionalInterface
public interface TrackingInformationSPI {
    Map<String, String> getInformation(final IHeaders headers);
}
