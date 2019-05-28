package org.inugami.api.monitoring;

import org.inugami.api.monitoring.models.Monitoring;

public interface MonitoringLoaderSpi {
    Monitoring load();
}
