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
package io.inugami.core.context;

import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderFutureResultBuilder;
import io.inugami.configuration.models.EventConfig;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.core.services.cache.CacheService;
import io.inugami.core.services.cache.CacheTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final CacheService cache;


    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    ContextProcessEventFromCache(final CacheService cache) {
        this.cache = cache;
    }

    List<ProviderFutureResult> process(final List<Plugin> plugins, final String excludeRegex) {
        final Matcher excludeMatcher =
                excludeRegex == null ? null : Pattern.compile(excludeRegex).matcher("");
        final List<ProviderFutureResult> result = new ArrayList<>();

        plugins.stream()
               .filter(Plugin::isEventConfigPresent)
               .forEach(plugin -> result.addAll(processPlugin(plugin, excludeMatcher)));

        return result;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    private List<ProviderFutureResult> processPlugin(final Plugin plugin, final Matcher excludeMatcher) {
        final List<ProviderFutureResult> result       = new ArrayList<>();
        final List<EventConfig>          eventsConfig = plugin.getEvents().orElseGet(Collections::emptyList);
        final String                     channel      = plugin.buildChannelName();

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
