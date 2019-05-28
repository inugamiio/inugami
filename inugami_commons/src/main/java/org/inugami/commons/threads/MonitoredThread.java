package org.inugami.commons.threads;

import java.util.List;

import org.inugami.api.monitoring.MdcService;
import org.inugami.api.monitoring.MonitoringInitializer;
import org.inugami.api.monitoring.RequestContext;
import org.inugami.api.monitoring.RequestInformation;

public class MonitoredThread extends Thread {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final RequestInformation          requestContext;
    
    private final List<MonitoringInitializer> monitoringInitializer;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MonitoredThread(final ThreadGroup group, final Runnable target, final String name, final long stackSize,
                           final RequestInformation requestContext,
                           final List<MonitoringInitializer> monitoringInitializer) {
        super(group, target, name, stackSize);
        this.requestContext = requestContext;
        this.monitoringInitializer = monitoringInitializer;
    }
    
    // =========================================================================
    // RUN
    // =========================================================================
    @Override
    public void run() {
        RequestContext.setInstance(requestContext);
        MdcService.initialize();
        monitoringInitializer.forEach(MonitoringInitializer::initialize);
        super.run();
    }
    
}
