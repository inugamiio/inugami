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
package org.inugami.core.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.configuration.models.EventConfig;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.core.context.runner.OnEventDoneProcessor;
import org.inugami.core.services.cache.CacheService;
import org.inugami.core.services.cache.CacheTypes;

/**
 * ContextProcessEventFromCache
 * 
 * @author patrick_guillerm
 * @since 28 sept. 2017
 */
class ContextProcessEventFromCache {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final CacheService         cache;
    
    private final OnEventDoneProcessor onEventDoneProcessor;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    ContextProcessEventFromCache(final CacheService cache) {
        this.cache = cache;
        onEventDoneProcessor = new OnEventDoneProcessor();
    }
    
    List<ProviderFutureResult> process(final List<Plugin> plugins, final String excludeRegex) {
        final Matcher excludeMatcher = excludeRegex == null ? null : Pattern.compile(excludeRegex).matcher("");
        final List<ProviderFutureResult> result = new ArrayList<>();
        
        plugins.stream().filter(Plugin::isEventConfigPresent).forEach(plugin -> {
            result.addAll(processPlugin(plugin, excludeMatcher, false));
        });
        
        return result;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    private List<ProviderFutureResult> processPlugin(final Plugin plugin, final Matcher excludeMatcher,
                                                     final boolean sendSSE) {
        final List<ProviderFutureResult> result = new ArrayList<>();
        final List<EventConfig> eventsConfig = plugin.getEvents().orElseGet(Collections::emptyList);
        final String channel = plugin.buildChannelName();
        
        //@formatter:off
        
        eventsConfig.stream().map(EventConfig::getSimpleEvents)
                             .filter(Objects::nonNull)
                             .flatMap(List::stream)
                             .filter(event ->isNotExclude(event, excludeMatcher))
                             .map(event->processEvent(event,channel))
                             .filter(Objects::nonNull)
                             .forEach(result::add);
        
        eventsConfig.stream().map(EventConfig::getEvents)
                             .filter(Objects::nonNull)
                             .flatMap(List::stream)
                             .filter(event ->isNotExclude(event, excludeMatcher))
                             .map(event->processEvent(event,channel))
                             .filter(Objects::nonNull)
                             .forEach(result::add);
        //@formatter:on
        return result;
    }
    
    private boolean isNotExclude(final GenericEvent event, final Matcher excludeMatcher) {
        boolean result = true;
        if (excludeMatcher != null) {
            result = !excludeMatcher.reset(event.getName()).matches();
        }
        return result;
    }
    
    private ProviderFutureResult processEvent(final GenericEvent event, final String channel) {
        return new ProviderFutureResultBuilder(loadData(event)).addChannel(channel).build();
    }
    
    // =========================================================================
    // LOAD DATA
    // =========================================================================
    private ProviderFutureResult loadData(final GenericEvent event) {
        return cache.get(CacheTypes.EVENTS, event.getName());
    }
    
}
