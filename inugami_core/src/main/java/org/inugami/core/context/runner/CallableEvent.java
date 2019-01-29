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
package org.inugami.core.context.runner;

import java.util.concurrent.Callable;

import org.inugami.api.loggers.Loggers;
import org.inugami.api.metrics.events.MetricsEvents;
import org.inugami.api.models.events.Event;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.models.events.TargetConfig;
import org.inugami.api.providers.concurrent.FutureData;
import org.inugami.api.providers.concurrent.FutureDataBuilder;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.core.context.Context;
import org.inugami.core.services.cache.CacheTypes;
import org.inugami.core.services.sse.SseService;

/**
 * CallableSimpleEvent
 * 
 * @author patrick_guillerm
 * @since 16 janv. 2017
 */
public class CallableEvent implements Callable<FutureData<ProviderFutureResult>> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final GenericEvent                 event;
    
    private final String                       channel;
    
    private final CallableSimpleEventProcessor simpleEventProcessor;
    
    private final CallableEventProcessor       eventProcessor;
    
    private final String                       cronExpression;
    
    private final Plugin                       plugin;
    
    private final boolean                      starting;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CallableEvent(final Plugin plugin, final GenericEvent event, final String channel, final Context context,
                         final String cronExpression, final long timeout, final boolean starting) {
        this.event = event;
        this.channel = channel;
        this.cronExpression = cronExpression;
        this.plugin = plugin;
        this.simpleEventProcessor = new CallableSimpleEventProcessor(plugin, context, timeout);
        this.eventProcessor = new CallableEventProcessor(plugin, context, timeout, channel, cronExpression);
        this.starting = starting;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public FutureData<ProviderFutureResult> call() throws Exception {
        MetricsEvents.onStart(plugin.getGav(), event.getName());
        Loggers.EVENTS.info("call event : {}", event.getName());
        final ProviderFutureResult data = processEvent();
        
        onDone(data, event, cronExpression);
        Loggers.EVENTS.info("call event done : {}", event.getName());
        //@formatter:off
        return new FutureDataBuilder<ProviderFutureResult>()
                            .addData(data)
                            .addChannel(channel)
                            .build();
        //@formatter:on
    }
    
    private ProviderFutureResult processEvent() {
        MetricsEvents.onStart(plugin.getGav(), event.getName());
        GenericEvent localEvent = event;
        if (starting) {
            localEvent = manageBootstrapEvent(event);
        }
        
        ProviderFutureResult result;
        if (localEvent instanceof SimpleEvent) {
            result = simpleEventProcessor.process((SimpleEvent) localEvent);
        }
        else {
            result = eventProcessor.process((Event) localEvent);
        }
        
        return result;
    }
    
    private GenericEvent manageBootstrapEvent(final GenericEvent event) {
        GenericEvent newEvent = event;
        
        final boolean formFirstTimePresent = event.getFromFirstTime().isPresent();
        if (formFirstTimePresent) {
            newEvent = event.cloneObj();
            newEvent.buildFrom(event.getFromFirstTime().get());
            
            if (newEvent instanceof Event) {
                final Event compositEvent = (Event) newEvent;
                
                for (final TargetConfig target : compositEvent.getTargets()) {
                    if (target.getFromFirstTime().isPresent()) {
                        if (target.getFromFirstTime().isPresent()) {
                            target.buildFrom(target.getFromFirstTime().get());
                        }
                        else {
                            target.buildFrom(newEvent.getFromFirstTime().get());
                        }
                    }
                }
            }
        }
        return newEvent;
    }
    
    // =========================================================================
    // ON DONE EVENT
    // =========================================================================
    public ProviderFutureResult onDone(final ProviderFutureResult value, final GenericEvent currentEvent,
                                       final String cronExpression) {
        this.putToCache(value, currentEvent);
        SseService.sendEvent(channel, event.getName(), value, cronExpression);
        MetricsEvents.onStop(plugin.getGav(), currentEvent.getName());
        return value;
    }
    
    private ProviderFutureResult putToCache(final ProviderFutureResult result, final GenericEvent currentEvent) {
        if (allowToCache(result, currentEvent)) {
            Context.putInCache(CacheTypes.EVENTS, currentEvent.getName(), result);
        }
        return result;
    }
    
    private boolean allowToCache(final ProviderFutureResult result, final GenericEvent currentEvent) {
        //@formatter:off
        return (result!=null)
                && !result.getException().isPresent() 
                && result.getData().isPresent();
        //@formatter:on
    }
    
    // =========================================================================
    // GETTERS
    // =========================================================================
    public GenericEvent getEvent() {
        return event;
    }
    
    public String getChannel() {
        return channel;
    }
    
    public CallableSimpleEventProcessor getSimpleEventProcessor() {
        return simpleEventProcessor;
    }
    
    public CallableEventProcessor getEventProcessor() {
        return eventProcessor;
    }
    
    public String getCronExpression() {
        return cronExpression;
    }
    
    public Plugin getPlugin() {
        return plugin;
    }
    
}
