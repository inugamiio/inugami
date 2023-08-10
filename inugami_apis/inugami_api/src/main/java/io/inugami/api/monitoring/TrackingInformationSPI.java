package io.inugami.api.monitoring;

import io.inugami.api.monitoring.models.Headers;

import java.util.Map;

@FunctionalInterface
public interface TrackingInformationSPI {
    Map<String, String> getInformation(final Headers headers);
}
