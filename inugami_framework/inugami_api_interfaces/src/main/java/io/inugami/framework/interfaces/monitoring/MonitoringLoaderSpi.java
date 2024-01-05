package io.inugami.framework.interfaces.monitoring;

import io.inugami.framework.interfaces.monitoring.models.Monitoring;

public interface MonitoringLoaderSpi {
    Monitoring load();
}