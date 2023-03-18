package io.inugami.commons.threads;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import io.inugami.api.monitoring.MonitoringInitializer;
import io.inugami.api.monitoring.RequestContext;
import io.inugami.api.monitoring.RequestInformation;
import io.inugami.api.spi.SpiLoader;

public class MonitoredThreadFactory implements ThreadFactory {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    private static final List<MonitoringInitializer> monitoringInitializer = initMonitoringInitializers();
    
    private final AtomicLong                         threadIndex           = new AtomicLong();
    
    private final String                             threadsName;
    
    private final boolean                            deamon;
    
    private final ThreadGroup                        threadGroup;
    
    private final RequestInformation                 requestContext;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private static List<MonitoringInitializer> initMonitoringInitializers() {
        final List<MonitoringInitializer> spiServices = SpiLoader.getInstance().loadSpiService(MonitoringInitializer.class);
        return spiServices == null ? Collections.emptyList() : spiServices;
    }
    
    public MonitoredThreadFactory(final String threadsName, final boolean deamon) {
        super();
        this.threadsName = threadsName;
        this.deamon = deamon;
        threadGroup = Thread.currentThread().getThreadGroup();
        this.requestContext = RequestContext.getInstance();
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public Thread newThread(final Runnable runnable) {
        final String name = String.join(".", threadsName, String.valueOf(threadIndex.getAndIncrement()));
        final Thread result = new MonitoredThread(threadGroup, runnable, name, 10, requestContext,
                                                  monitoringInitializer);
        result.setDaemon(deamon);
        return result;
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
}
