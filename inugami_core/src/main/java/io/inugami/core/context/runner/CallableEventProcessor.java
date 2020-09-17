/* --------------------------------------------------------------------
 *  Inugami  
 * --------------------------------------------------------------------
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.core.context.runner;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.metrics.events.MetricsEvents;
import io.inugami.api.models.data.basic.Json;
import io.inugami.api.models.events.Event;
import io.inugami.api.models.events.TargetConfig;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.task.EventCompositeResult;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderFutureResultBuilder;
import io.inugami.commons.threads.RunAndCloseService;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.core.context.Context;

/**
 * CallableEventProcessor
 * 
 * @author patrick_guillerm
 * @since 9 août 2017
 */
public class CallableEventProcessor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final ProviderFutureResult  EMPTY_PROVIDER_FUTUR = new ProviderFutureResultBuilder().addData(new Json("null")).build();
    
    private final Context                      context;
    
    private final Plugin                       plugin;
    
    private final CallableSimpleEventProcessor simpleEventProcessor;
    
    private final long                         timeout;
    
    private final AlertingsProcessor           alertingProcessor;
    
    private final String                       channel;
    
    private final String                       cronExpression;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CallableEventProcessor(final Plugin plugin, final Context context, final long timeout, final String channel,
                                  final String cronExpression) {
        this.plugin = plugin;
        this.context = context;
        this.timeout = timeout;
        this.channel = channel;
        this.cronExpression = cronExpression;
        simpleEventProcessor = new CallableSimpleEventProcessor(plugin, context, timeout);
        alertingProcessor = new AlertingsProcessor();
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public ProviderFutureResult process(final Event event) {
        final String path = String.join(".", plugin.getGav().getHash(), event.getName());
        
        final Provider providerAggregator = extractProviderAggregator(event);
        
        MetricsEvents.onStart(plugin.getGav(), event.getName());
        final Provider genericProvider = !event.getProvider().isPresent() ? null
                                                                          : context.getProvider(event.getProvider().get());
        
        //@formatter:off
        final List<Callable<ProviderFutureResult>> tasks =  event.getTargets()
                                                                 .stream()
                                                                 .map(target -> buildTargetTask(genericProvider, target))
                                                                 .collect(Collectors.toList());
        
        
        final List<ProviderFutureResult> futureData = new RunAndCloseService<>(path, timeout, tasks.size(), tasks).run();
        //@formatter:on        
        
        ProviderFutureResult resultData;
        try {
            resultData = aggregateResult(futureData, providerAggregator);
        }
        catch (final Exception e) {
            Loggers.PROVIDER.error(e.getMessage(), e);
            resultData = EMPTY_PROVIDER_FUTUR;
        }
        
        if (event.getProcessors().isPresent()) {
            resultData = Context.getInstance().applyProcessors(resultData, event);
        }
        
        final List<AlertingResult> alerts = alertingProcessor.computeAlerts(plugin.getGav(), event, resultData);
        return new EventCompositeResult(null, null, resultData.getData().get(), event, channel, event.getScheduler(),
                                        alerts);
    }
    
    private Callable<ProviderFutureResult> buildTargetTask(final Provider genericProvider, final TargetConfig target) {
        Provider specificProvider = null;
        if (target.getProvider().isPresent()) {
            specificProvider = context.getProvider(target.getProvider().get());
        }
        
        final Provider provider = specificProvider == null ? genericProvider : specificProvider;
        Asserts.notNull(String.format("no provider define for event : %s", target.getName()), provider);
        
        return () -> {
            return simpleEventProcessor.callProvider(target, provider);
        };
    }
    
    private Provider extractProviderAggregator(final Event event) {
        assertSameProviderType(event);
        String providerName = null;
        
        if (event.getProvider().isPresent()) {
            providerName = event.getProvider().get();
        }
        else {
            providerName = event.getTargets().get(0).getProvider().orElse(null);
            Asserts.notNull("Please check provider configuration on event :" + event.getName() + "!", providerName);
        }
        
        return context.getProvider(providerName);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private ProviderFutureResult aggregateResult(final List<ProviderFutureResult> rawData,
                                                 final Provider providerAggregator) throws ProviderException {
        ProviderFutureResult result = null;
        
        if ((rawData == null) || rawData.isEmpty()) {
            result = EMPTY_PROVIDER_FUTUR;
        }
        else if (rawData.size() == 1) {
            result = rawData.get(0);
        }
        else {
            result = providerAggregator.aggregate(rawData);
        }
        
        return result;
    }
    
    private boolean isAllDone(final List<Future<ProviderFutureResult>> futures) {
        boolean allDone = false;
        for (final Future<ProviderFutureResult> future : futures) {
            allDone = future.isDone();
            if (!allDone) {
                break;
            }
        }
        return allDone;
    }
    
    private void sleep() {
        try {
            Thread.currentThread().sleep(250);
        }
        catch (final InterruptedException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }
    
    // =========================================================================
    // EXCEPTIONS
    // =========================================================================
    private void handlerException(final Exception e, final Event event) {
        Loggers.DEBUG.error(e.getMessage(), e);
        Loggers.PLUGINS.error("{} : {}", event.getName(), e.getMessage());
        Loggers.DEBUG.error(e.getMessage(), e);
        
    }
    
    private void assertSameProviderType(final Event event) {
        String providerType = extractProviderType(event.getProvider());
        
        for (final TargetConfig target : event.getTargets()) {
            final String targetProvider = extractProviderType(target.getProvider());
            
            if ((providerType == null) && (targetProvider != null)) {
                providerType = targetProvider;
            }
            
            if ((providerType != null) && (targetProvider != null) && !providerType.equals(targetProvider)) {
                throw new FatalException("Event \"{0}\" have multi providers types!", event.getName());
            }
        }
        
    }
    
    private String extractProviderType(final Optional<String> providerName) {
        String result = null;
        if (providerName.isPresent()) {
            final Provider provider = context.getProvider(providerName.get());
            if (provider != null) {
                result = provider.getType();
            }
        }
        return result;
    }
    
}
