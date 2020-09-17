package io.inugami.api.monitoring;

import io.inugami.api.monitoring.models.Monitoring;

public interface MonitoringLoaderSpi {
    Monitoring load();
}
