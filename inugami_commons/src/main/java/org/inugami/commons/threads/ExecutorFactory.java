package org.inugami.commons.threads;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.inugami.api.monitoring.MonitoringInitializer;
import org.inugami.api.monitoring.RequestContext;
import org.inugami.api.monitoring.RequestInformation;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.commons.spi.SpiLoader;

public class ExecutorFactory implements ThreadFactory {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ExecutorService               executor;
    
    private final AtomicLong                    threadIndex = new AtomicLong();
    
    private final String                        threadsName;
    
    private final boolean                       deamon;
    
    private final ThreadGroup                   threadGroup;
    
    private final RequestInformation            requestContext;
    
    private final List<MonitoringInitializer>   monitoringInitializer;
    
    private final ConfigHandler<String, String> configuration;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ExecutorFactory(final int maxThreads, final String threadsName, final boolean deamon,
                           final ConfigHandler<String, String> configuration) {
        super();
        this.executor = Executors.newFixedThreadPool(maxThreads <= 0 ? 1 : maxThreads, this);
        threadGroup = Thread.currentThread().getThreadGroup();
        this.threadsName = threadsName;
        this.deamon = deamon;
        
        this.requestContext = RequestContext.getInstance();
        this.configuration = configuration;
        final List<MonitoringInitializer> spiServices = new SpiLoader().loadSpiService(MonitoringInitializer.class);
        monitoringInitializer = spiServices == null ? Collections.emptyList() : spiServices;
        
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public ExecutorService getExecutorSerice() {
        return executor;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public Thread newThread(final Runnable runnable) {
        final String name = String.join(".", threadsName, String.valueOf(threadIndex.getAndIncrement()));
        final Thread result = new MonitoredThread(threadGroup, runnable, name, 10, requestContext,
                                                  monitoringInitializer, configuration);
        result.setDaemon(deamon);
        return result;
    }
    
    private class MonitoredThread extends Thread {
        
        private final RequestInformation          requestContext;
        
        private final List<MonitoringInitializer> monitoringInitializer;
        
        public MonitoredThread(final ThreadGroup group, final Runnable target, final String name, final long stackSize,
                               final RequestInformation requestContext,
                               final List<MonitoringInitializer> monitoringInitializer,
                               final ConfigHandler<String, String> configuration) {
            super(group, target, name, stackSize);
            this.requestContext = requestContext;
            this.monitoringInitializer = monitoringInitializer;
        }
        
        @Override
        public void run() {
            RequestContext.setInstance(requestContext);
            monitoringInitializer.forEach(MonitoringInitializer::initialize);
            super.run();
        }
        
    }
    
}
