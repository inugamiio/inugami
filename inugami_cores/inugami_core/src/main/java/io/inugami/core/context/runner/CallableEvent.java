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

import io.inugami.api.loggers.Loggers;
import io.inugami.api.metrics.events.MetricsEvents;
import io.inugami.api.models.events.Event;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.models.events.TargetConfig;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.concurrent.FutureDataBuilder;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.core.context.Context;
import io.inugami.core.services.cache.CacheTypes;
import io.inugami.core.services.sse.SseService;
import lombok.Getter;

import java.util.concurrent.Callable;

/**
 * CallableSimpleEvent
 *
 * @author patrick_guillerm
 * @since 16 janv. 2017
 */
@SuppressWarnings({"java:S3655"})
@Getter
public class CallableEvent implements Callable<FutureData<ProviderFutureResult>> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final GenericEvent event;

    private final String channel;

    private final CallableSimpleEventProcessor simpleEventProcessor;

    private final CallableEventProcessor eventProcessor;

    private final String cronExpression;

    private final Plugin plugin;

    private final boolean starting;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CallableEvent(final Plugin plugin,
                         final GenericEvent event,
                         final String channel,
                         final Context context,
                         final String cronExpression,
                         final long timeout,
                         final boolean starting) {
        this.event = event;
        this.channel = channel;
        this.cronExpression = cronExpression;
        this.plugin = plugin;
        this.simpleEventProcessor = CallableSimpleEventProcessor.builder()
                                                                .plugin(plugin)
                                                                .context(context)
                                                                .timeout(timeout)
                                                                .build();
        this.eventProcessor = new CallableEventProcessor(plugin, context, timeout, channel);
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

        return new FutureDataBuilder<ProviderFutureResult>()
                .addData(data)
                .addChannel(channel)
                .build();
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
        } else {
            result = eventProcessor.process((Event) localEvent);
        }

        return result;
    }

    private GenericEvent manageBootstrapEvent(final GenericEvent event) {
        GenericEvent newEvent = event;

        final boolean formFirstTimePresent = event.getFromFirstTime().isPresent();
        if (!formFirstTimePresent) {
            return newEvent;
        }
        newEvent = event.cloneObj();
        newEvent.buildFrom(event.getFromFirstTime().get());

        if (newEvent instanceof Event) {
            final Event compositEvent = (Event) newEvent;

            for (final TargetConfig target : compositEvent.getTargets()) {
                if (target.getFromFirstTime().isEmpty()) {
                    continue;
                }

                if (target.getFromFirstTime().isPresent()) {
                    target.buildFrom(target.getFromFirstTime().get());
                } else {
                    target.buildFrom(newEvent.getFromFirstTime().get());
                }

            }
        }

        return newEvent;
    }

    // =========================================================================
    // ON DONE EVENT
    // =========================================================================
    public ProviderFutureResult onDone(final ProviderFutureResult value,
                                       final GenericEvent currentEvent,
                                       final String cronExpression) {
        this.putToCache(value, currentEvent);
        SseService.sendEvent(channel, event.getName(), value, cronExpression);
        MetricsEvents.onStop(plugin.getGav(), currentEvent.getName());
        return value;
    }

    private ProviderFutureResult putToCache(final ProviderFutureResult result, final GenericEvent currentEvent) {
        if (allowToCache(result)) {
            Context.putInCache(CacheTypes.EVENTS, currentEvent.getName(), result);
        }
        return result;
    }

    private boolean allowToCache(final ProviderFutureResult result) {
        //@formatter:off
        return (result!=null)
                && !result.getException().isPresent() 
                && result.getData().isPresent();
        //@formatter:on
    }


}
