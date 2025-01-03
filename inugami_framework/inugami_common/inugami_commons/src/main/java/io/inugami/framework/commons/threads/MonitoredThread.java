package io.inugami.framework.commons.threads;


import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.monitoring.RequestContext;
import io.inugami.framework.interfaces.monitoring.MonitoringInitializer;
import io.inugami.framework.interfaces.monitoring.RequestInformation;

import java.util.List;

@SuppressWarnings({"java:S3014"})
public class MonitoredThread extends Thread {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private final RequestInformation          requestContext;
    private final List<MonitoringInitializer> monitoringInitializer;

    // =================================================================================================================
    // CONSTRUCTORS
    // =================================================================================================================
    public MonitoredThread(final ThreadGroup group, final Runnable target, final String name, final long stackSize,
                           final RequestInformation requestContext,
                           final List<MonitoringInitializer> monitoringInitializer) {
        super(group, target, name, stackSize);
        this.requestContext = requestContext;
        this.monitoringInitializer = monitoringInitializer;
    }

    // =================================================================================================================
    // RUN
    // =================================================================================================================
    @Override
    public void run() {
        RequestContext.setInstance(requestContext);
        MdcService.getInstance().initialize();
        monitoringInitializer.forEach(MonitoringInitializer::initialize);
        super.run();
    }

}
