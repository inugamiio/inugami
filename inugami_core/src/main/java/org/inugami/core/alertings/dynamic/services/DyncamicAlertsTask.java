package org.inugami.core.alertings.dynamic.services;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.inugami.api.exceptions.NotYetImplementedException;
import org.inugami.api.exceptions.services.ProcessorException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.Gav;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.models.events.SimpleEventBuilder;
import org.inugami.api.processors.Processor;
import org.inugami.api.providers.Provider;
import org.inugami.api.providers.concurrent.FutureData;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.core.alertings.dynamic.entities.DynamicAlertEntity;
import org.inugami.core.alertings.dynamic.entities.ProviderSource;
import org.inugami.core.context.ApplicationContext;

public class DyncamicAlertsTask implements Callable<Void> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Gav         GAV = new Gav("org.inugami", "inugami_core");
    
    private final DynamicAlertEntity entity;
    
    private final ApplicationContext context;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DyncamicAlertsTask(final DynamicAlertEntity entity, final ApplicationContext context) {
        super();
        this.entity = entity;
        this.context = context;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    @Override
    public Void call() throws Exception {
        ProviderFutureResult data = null;
        SimpleEvent event = null;
        if (entity.getSource() != null) {
            try {
                event = buildEvent();
                data = loadDataFromProvider(event);
            }
            catch (final TimeoutException timeoutErr) {
                Loggers.ALERTING.error("timeout on load data for dynamic alert {} (provider {})",
                                       entity.getAlerteName(), entity.getSource().getProvider());
            }
            
        }
        
        if (data != null) {
            data = applyProcessor(data, event);
        }
        
        return null;
    }
    // =========================================================================
    // LOAD DATA
    // =========================================================================
    
    private ProviderFutureResult loadDataFromProvider(final SimpleEvent event) throws InterruptedException,
                                                                               ExecutionException, TimeoutException {
        final Provider provider = context.getProvider(entity.getSource().getProvider());
        ProviderFutureResult result = null;
        if (provider == null) {
            Loggers.ALERTING.error("provider {} define in dynamic alert {}  dosn't exists",
                                   entity.getSource().getProvider(), entity.getAlerteName());
        }
        else {
            final FutureData<ProviderFutureResult> futur = provider.callEvent(event, GAV);
            
            result = futur.getFuture().get(context.getApplicationConfiguration().getTimeout(), TimeUnit.MILLISECONDS);
        }
        
        return result;
    }
    
    private SimpleEvent buildEvent() {
        final SimpleEventBuilder builder = new SimpleEventBuilder();
        final ProviderSource source = entity.getSource();
        builder.addName(isEmpty(source.getEventName()) ? entity.getAlerteName() : source.getEventName());
        builder.addFrom(source.getFrom());
        builder.addUntil(source.getTo());
        builder.addProvider(source.getProvider());
        builder.addScheduler(source.getCronExpression());
        builder.addQuery(source.getQuery());
        return builder.build();
    }
    
    // =========================================================================
    // POST
    // =========================================================================
    private ProviderFutureResult applyProcessor(final ProviderFutureResult data,
                                                final SimpleEvent event) throws ProcessorException {
        ProviderFutureResult result = data;
        if (entity.getTransformer() != null) {
            if (entity.getTransformer().getName() != null) {
                result = invokeProcessor(data, event);
            }
            else {
                result = invokeJavaScriptTransformer(data, event);
            }
        }
        return result;
    }
    
    private ProviderFutureResult invokeProcessor(final ProviderFutureResult data,
                                                 final SimpleEvent event) throws ProcessorException {
        ProviderFutureResult result = data;
        final Processor processor = context.getProcessor(entity.getTransformer().getName());
        if (processor == null) {
            Loggers.ALERTING.error("can't transform data from dynamic alert {} with unkown processor : {}",
                                   entity.getAlerteName(), entity.getTransformer().getName());
        }
        else {
            result = processor.process(event, data);
        }
        return result;
    }
    
    private ProviderFutureResult invokeJavaScriptTransformer(final ProviderFutureResult data, final SimpleEvent event) {
        throw new NotYetImplementedException("javascript transformer isn't currently implemented !");
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private boolean isEmpty(final String eventName) {
        return (eventName == null) || eventName.trim().isEmpty();
    }
    
}
