package io.inugami.core.alertings.dynamic.services;

import io.inugami.api.alertings.AlertingProvider;
import io.inugami.api.alertings.DynamicAlertingLevel;
import io.inugami.api.exceptions.NotYetImplementedException;
import io.inugami.api.exceptions.services.ProcessorException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.Processor;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.core.alertings.dynamic.entities.*;
import io.inugami.core.context.ApplicationContext;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S1172"})
@Builder
@AllArgsConstructor
public class DynamicAlertsTask implements Callable<Void> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Gav GAV = Gav.builder().groupId("io.inugami").artifactId("inugami_core").build();

    private final DynamicAlertEntity entity;

    private final ApplicationContext context;


    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @SuppressWarnings({"java:S2139"})
    @Override
    public Void call() throws Exception {
        try {
            process();
        } catch (final Exception e) {
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
        ProviderFutureResult data  = null;
        SimpleEvent          event = null;
        if (entity.getSource() != null) {
            try {
                event = buildEvent();
                data = loadDataFromProvider(event);
            } catch (final TimeoutException timeoutErr) {
                Loggers.ALERTING.error("timeout on load data for dynamic alert {} (provider {})", entity.getAlerteName(), entity.getSource()
                                                                                                                                .getProvider());
            }

        }

        if (data != null) {
            data = applyProcessor(data, event);
        }

        if (data != null) {
            processAlerting(data, event);
        }
    }

    private ProviderFutureResult loadDataFromProvider(final SimpleEvent event) throws InterruptedException, ExecutionException, TimeoutException {
        final Provider       provider = context.getProvider(entity.getSource().getProvider());
        ProviderFutureResult result   = null;
        if (provider == null) {
            Loggers.ALERTING.error("provider {} define in dynamic alert {}  dosn't exists", entity.getSource()
                                                                                                  .getProvider(), entity.getAlerteName());
        } else {
            final FutureData<ProviderFutureResult> futur = provider.callEvent(event, GAV);
            result = futur.getFuture().get(context.getApplicationConfiguration().getTimeout(), TimeUnit.MILLISECONDS);
        }

        return result;
    }

    private SimpleEvent buildEvent() {
        final ProviderSource source = entity.getSource();
        return SimpleEvent.simpleEventBuilder()
                          .name(isEmpty(source.getEventName()) ? entity.getAlerteName() : source.getEventName())
                          .until(applyProperties(source.getTo()))
                          .from(applyProperties(source.getFrom()))
                          .provider(applyProperties(source.getProvider()))
                          .scheduler(applyProperties(source.getCronExpression()))
                          .query(source.getQuery())
                          .build();
    }

    private String applyProperties(final String value) {
        return context.getGlobalConfiguration().applyProperties(value);
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
            } else {
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
        } else {
            final List<DynamicAlertingLevel> levels = buildLevels(entity.getLevels());
            if (!levels.isEmpty()) {
                provider.processDynamicAlert(GAV, event, data, levels, entity.getLabel(), entity.getSubLabel(), buildTag(entity.getTags()), entity.getProviders());
            }
        }
    }

    private List<DynamicAlertingLevel> buildLevels(final List<DynamicLevel> dynamicLevels) {
        final List<DynamicAlertingLevel> result = new ArrayList<>();
        if (dynamicLevels != null) {
            final int currentHour = resolveCurrentHour();
            for (final DynamicLevel level : dynamicLevels) {
                result.addAll(processBuildLevels(level, currentHour));
            }
        }

        return result;
    }

    private List<DynamicAlertingLevel> processBuildLevels(final DynamicLevel level, final int currentHour) {
        final List<DynamicAlertingLevel> result = new ArrayList<>();
        if (level.getData() == null || level.getData().isEmpty()) {
            return result;
        }


        if (level.getData().size() == 1) {
            result.add(DynamicAlertingLevel.builder()
                                           .name(level.getName())
                                           .addLevel(level.getData().get(0).getLevel())
                                           .activationDelay(level.getActivationDelais())
                                           .duration(entity.getDuration())
                                           .nominal(entity.getNominal())
                                           .unit(entity.getUnit())
                                           .service(entity.getService())
                                           .component(entity.getComponent())
                                           .inverse(entity.isInverse())
                                           .build());

        } else {
            for (final DynamicLevelValues hourData : level.getData()) {
                if (hourData.getHour() == currentHour) {
                    result.add(DynamicAlertingLevel.builder()
                                                   .name(level.getName())
                                                   .addLevel(hourData.getLevel())
                                                   .activationDelay(level.getActivationDelais())
                                                   .duration(entity.getDuration())
                                                   .nominal(entity.getNominal())
                                                   .unit(entity.getUnit())
                                                   .service(entity.getService())
                                                   .component(entity.getComponent())
                                                   .inverse(entity.isInverse())
                                                   .build());
                    break;
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