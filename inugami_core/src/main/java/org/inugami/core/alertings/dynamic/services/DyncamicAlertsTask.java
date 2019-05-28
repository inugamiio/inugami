package org.inugami.core.alertings.dynamic.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.inugami.api.alertings.AlertingProvider;
import org.inugami.api.alertings.DynamicAlertingLevel;
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
import org.inugami.core.alertings.dynamic.entities.DynamicLevel;
import org.inugami.core.alertings.dynamic.entities.DynamicLevelValues;
import org.inugami.core.alertings.dynamic.entities.ProviderSource;
import org.inugami.core.alertings.dynamic.entities.Tag;
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
        try {
            process();
        }
        catch (final Exception e) {
            Loggers.ALERTING.error(e.getMessage());
            Loggers.DEBUG.error(e.getMessage(), e);
            throw e;
        }
        
        return null;
    }
    // =========================================================================
    // LOAD DATA
    // =========================================================================
    
    private void process() throws InterruptedException, ExecutionException, ProcessorException {
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
        
        if (data != null) {
            processAlerting(data, event);
        }
    }
    
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
    // APPLY PROCESSOR
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
        final Processor processor = context.getProcessor(entity.getTransformer().getName());
        return processor.process(event, data);
    }
    
    private ProviderFutureResult invokeJavaScriptTransformer(final ProviderFutureResult data, final SimpleEvent event) {
        throw new NotYetImplementedException("javascript transformer isn't currently implemented !");
    }
    
    // =========================================================================
    // PROCESS ALERTING
    // =========================================================================
    private void processAlerting(final ProviderFutureResult data, final SimpleEvent event) {
        final AlertingProvider provider = context.getAlertingProvider();
        if (provider == null) {
            Loggers.ALERTING.error("no default alerting provider found!");
        }
        else {
            final List<DynamicAlertingLevel> levels = buildLevels(entity.getLevels());
            if (!levels.isEmpty()) {
                provider.processDynamicAlert(GAV, event, data, levels, entity.getLabel(), entity.getSubLabel(),
                                             buildTag(entity.getTags()), entity.getProviders());
            }
        }
    }
    
    private List<DynamicAlertingLevel> buildLevels(final List<DynamicLevel> dynamicLevels) {
        final List<DynamicAlertingLevel> result = new ArrayList<>();
        if (dynamicLevels != null) {
            final int currentHour = resolveCurrentHour();
            
            for (final DynamicLevel level : dynamicLevels) {
                if ((level.getData() != null) && !level.getData().isEmpty()) {
                    if (level.getData().size() == 1) {
                        result.add(new DynamicAlertingLevel(level.getName(), level.getData().get(0).getLevel(),
                                                            level.getActivationDelais(), entity.getDuration(),
                                                            entity.getNominal(), entity.getUnit(), entity.getService(),
                                                            entity.getComponent(), entity.isInverse()));
                    }
                    else {
                        for (final DynamicLevelValues hourData : level.getData()) {
                            if (hourData.getHour() == currentHour) {
                                result.add(new DynamicAlertingLevel(level.getName(), hourData.getLevel(),
                                                                    level.getActivationDelais(), entity.getDuration(),
                                                                    entity.getNominal(), entity.getUnit(),
                                                                    entity.getService(), entity.getComponent(),
                                                                    entity.isInverse()));
                                break;
                            }
                        }
                    }
                }
                
            }
        }
        
        return result;
    }
    
    private int resolveCurrentHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private boolean isEmpty(final String eventName) {
        return (eventName == null) || eventName.trim().isEmpty();
    }
    
    private List<String> buildTag(final Set<Tag> tags) {
        //@formatter:off
        return Optional.ofNullable(tags)
                       .orElse(Collections.emptySet())
                       .stream()
                       .map(Tag::getName)
                       .collect(Collectors.toList());
        //@formatter:on
    }
    
}
