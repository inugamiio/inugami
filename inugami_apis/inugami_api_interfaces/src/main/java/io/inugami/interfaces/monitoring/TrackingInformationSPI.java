package io.inugami.interfaces.monitoring;

import io.inugami.interfaces.monitoring.models.IHeaders;

import java.util.Map;

@FunctionalInterface
public interface TrackingInformationSPI {
    Map<String, String> getInformation(final IHeaders headers);
}
