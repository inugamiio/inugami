package io.inugami.framework.commons.threads;

import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.monitoring.RequestContext;
import io.inugami.framework.interfaces.monitoring.MonitoringInitializer;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.spi.SpiLoader;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings({"java:S3014"})
public class MonitoredThreadFactory implements ThreadFactory {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final List<MonitoringInitializer> monitoringInitializer = initMonitoringInitializers();
    private final        AtomicLong                  threadIndex           = new AtomicLong();
    private final        String                      threadsName;
    private final        boolean                     deamon;
    private final        ThreadGroup                 threadGroup;
    private final        RequestData                 requestContext;

    // =================================================================================================================
    // CONSTRUCTORS
    // =================================================================================================================
    private static List<MonitoringInitializer> initMonitoringInitializers() {
        final List<MonitoringInitializer> spiServices = getSpiServices();
        return spiServices == null ? Collections.emptyList() : spiServices;
    }

    private static List<MonitoringInitializer> getSpiServices() {
        try {
            return SpiLoader.getInstance()
                            .loadSpiService(MonitoringInitializer.class);
        }catch (Throwable e){
            return List.of();
        }
    }

    public MonitoredThreadFactory(final String threadsName, final boolean deamon) {
        super();
        this.threadsName    = threadsName;
        this.deamon         = deamon;
        threadGroup         = Thread.currentThread().getThreadGroup();
        this.requestContext = RequestContext.getInstance();
    }

    // =================================================================================================================
    // OVERRIDES
    // =================================================================================================================
    @Override
    public Thread newThread(final Runnable runnable) {
        final String name = String.join(".", threadsName, String.valueOf(threadIndex.getAndIncrement()));
        return Thread.ofVirtual().name(name).unstarted(() -> {
            RequestContext.setInstance(requestContext);
            MdcService.getInstance().initialize();
            monitoringInitializer.forEach(MonitoringInitializer::initialize);
            runnable.run();
        });
    }

}
