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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.listeners.TaskFinishListener;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.metrics.events.MetricsEvents;
import org.inugami.api.models.events.Event;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.providers.NoForcingEventProvider;
import org.inugami.api.providers.Provider;
import org.inugami.api.providers.concurrent.FutureData;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.commons.threads.ThreadsExecutorService;
import org.inugami.configuration.models.EventConfig;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.core.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EventRunner
 * 
 * @author patrick_guillerm
 * @since 13 janv. 2017
 */
public class PluginEventsRunner implements Callable<EventRunnerFuture>, TaskFinishListener {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger LOGGER = LoggerFactory.getLogger(PluginEventsRunner.class);
    
    private final Plugin        plugin;
    
    private final String        cronExpression;
    
    private final Context       context;
    
    private final boolean       processAllEvents;
    
    private final long          timeout;
    
    private final int           maxParallelEventsByPlugins;
    
    private final boolean       starting;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public PluginEventsRunner(final Plugin plugin, final int maxParallelEventsByPlugins, final String cronExpression,
                              final Context context, final long timeout, final boolean starting) {
        
        Loggers.PLUGINS.info("process events on plugin : {}", plugin.getGav());
        Asserts.notNull("plugin mustn't be null!", plugin);
        this.starting = starting;
        this.context = context;
        this.plugin = plugin;
        
        this.cronExpression = cronExpression;
        processAllEvents = cronExpression == null;
        
        this.maxParallelEventsByPlugins = maxParallelEventsByPlugins;
        this.timeout = plugin.getTimeout().orElse(timeout);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public EventRunnerFuture call() {
        final ThreadsExecutorService executor = new ThreadsExecutorService(plugin.getGav().getHash(),
                                                                           maxParallelEventsByPlugins, false, timeout);
        
        final List<Callable<FutureData<ProviderFutureResult>>> tasks = new ArrayList<>();
        
        if (plugin.getEvents().isPresent()) {
            final String channel = plugin.buildChannelName();
            
            for (final EventConfig eventConfig : plugin.getEvents().get()) {
                if (eventConfig.getEnable()) {
                    
                    //@formatter:off
                     if (eventConfig.getSimpleEvents() != null) {
                         eventConfig.getSimpleEvents()
                                    .stream()
                                    .filter(event->manageNoForcingProvider(event,cronExpression))
                                    .filter(event->isNotRunning(plugin,event))
                                    .filter(this::isMatchingCron)
                                    .map(event->buildTask(event, channel))
                                    .forEach(tasks::add);
                     }
                     if (eventConfig.getEvents() != null) {
                         eventConfig.getEvents()
                                    .stream()
                                    .filter(event->manageNoForcingProvider(event,cronExpression))
                                    .filter(event->isNotRunning(plugin,event))
                                    .filter(this::isMatchingCron)
                                    .map(event->buildTask(event, channel))
                                    .forEach(tasks::add);
                     }
                     //@formatter:on
                }
                
            }
        }
        
        try {
            executor.runAndGrab(tasks, null, this::onError, timeout);
        }
        catch (final TechnicalException e) {
            Loggers.PLUGINS.error(e.getMessage(), e);
        }
        executor.shutdown();
        return null;
    }
    
    private boolean manageNoForcingProvider(final GenericEvent event, final String cron) {
        boolean result = cron != null;
        if (!result) {
            final List<String> providersName = new ArrayList<>();
            if (event instanceof SimpleEvent) {
                final String provider = event.getProvider().orElse(null);
                Asserts.notNull("simple event must have provider", provider);
                providersName.add(provider);
            }
            else {
                final String provider = event.getProvider().orElse(null);
                if (provider != null) {
                    providersName.add(provider);
                }
                //@formatter:off
                ((Event)event).getTargets()
                              .stream()
                              .map(GenericEvent::getProvider)
                              .filter(itemProvider->itemProvider.isPresent())
                              .forEach(itemProvider-> providersName.add(itemProvider.get()));
                //@formatter:on
            }
            
            result = !containsNoForcingEventProvider(providersName);
        }
        return result;
    }
    
    private boolean containsNoForcingEventProvider(final List<String> providersName) {
        boolean result = false;
        for (final String providerName : providersName) {
            final Provider provider = Context.getInstance().getProvider(providerName);
            if (provider instanceof NoForcingEventProvider) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    private CallableEvent buildTask(final GenericEvent event, final String channel) {
        return new CallableEvent(plugin, event, channel, context, cronExpression, timeout, starting);
    }
    
    //@formatter:off
    private boolean isMatchingCron(final SimpleEvent event) {
        return isMatchingCron(event.getScheduler());
    }
    private boolean isMatchingCron(final Event event) {
        return isMatchingCron(event.getScheduler());
    }
    
    private boolean isMatchingCron(final String cron) {
        return processAllEvents || sameCron(cronExpression,cron);
    }
    //@formatter:on
    
    private boolean sameCron(final String ref, final String value) {
        return ref == null ? false : ref.equals(value);
    }
    
    private void onError(final Exception error, final Callable<FutureData<ProviderFutureResult>> task) {
        final CallableEvent realTask = (CallableEvent) task;
        Loggers.PLUGINS.error("{} : {}", realTask.getEvent().getName(), error.getMessage());
        Loggers.DEBUG.error(error.getMessage(), error);
    }
    
    // =========================================================================
    // LISTENERS
    // =========================================================================
    
    @Override
    public void onFinish(final long time, final long delais, final String name, final Object result,
                         final Exception error) {
        if (error == null) {
            LOGGER.debug("plugin event finish :{} @ {}", name, time);
        }
        else {
            LOGGER.debug("plugin event finish : {} @ {} - error : {}", name, time, error.getMessage());
        }
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private boolean isNotRunning(final Plugin plugin, final GenericEvent event) {
        return !MetricsEvents.isRunning(plugin.getGav(), event);
    }
    
    // =========================================================================
    // BUILDER
    // =========================================================================
    private String buildEventName(final GenericEvent event) {
        //@formatter:off
        return new StringBuilder(plugin.getGav().getHash())
                .append('-')
                .append(event.getName())
                .toString();
        //@formatter:on
    }
    
}
