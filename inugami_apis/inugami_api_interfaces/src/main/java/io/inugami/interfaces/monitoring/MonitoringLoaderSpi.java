package io.inugami.interfaces.monitoring;

import io.inugami.interfaces.monitoring.models.Monitoring;

public interface MonitoringLoaderSpi {
    Monitoring load();
}