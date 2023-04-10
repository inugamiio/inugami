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
package io.inugami.api.metrics.events;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.tools.Chrono;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MetricsEvents
 *
 * @author patrick_guillerm
 * @since 27 sept. 2017
 */
public final class MetricsEvents {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String SEPARATOR = "_";

    private final static Map<String, Chrono> EVENTS_STATES = new ConcurrentHashMap<>();

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    private MetricsEvents() {
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public static void onStart(final Gav gav, final String eventName) {
        final String key    = validatePluginGavEvent(gav, eventName);
        Chrono       chrono = EVENTS_STATES.get(key);
        if (chrono == null) {
            chrono = Chrono.startChrono();
            EVENTS_STATES.put(key, chrono);
        } else {
            chrono.start();
        }
    }

    public static void onStop(final Gav gav, final String eventName) {
        final String key    = validatePluginGavEvent(gav, eventName);
        Chrono       chrono = EVENTS_STATES.get(key);
        if (chrono == null) {
            chrono = Chrono.startChrono();
            EVENTS_STATES.put(key, chrono);
        }
        chrono.stop();
    }

    public static boolean isRunning(final Gav plugin, final GenericEvent event) {
        final String key    = validatePluginGavEvent(plugin, event == null ? null : event.getName());
        final Chrono chrono = EVENTS_STATES.get(key);
        return (chrono != null) && chrono.isRunning();
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private static String validatePluginGavEvent(final Gav gav, final String event) {
        Asserts.assertNotNull("Plugin gav is mandatory", gav);
        Asserts.assertNotNull("Event is mandatory", event);
        return buildKey(gav.getHash(), event);
    }

    private static String buildKey(final String gav, final String eventName) {
        return String.join(SEPARATOR, gav, eventName);
    }

    private static EventState mapEventState(final Map.Entry<String, Chrono> item) {
        final Chrono chrono = item.getValue();
        return new EventState(item.getKey(), chrono.getStartTimeMs(), chrono.getStopTimeMs(), chrono.isRunning(),
                              chrono.getDelaisInMillis());
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public static Set<EventState> getStates() {
        final Set<EventState> result = new HashSet<>();
        for (final Map.Entry<String, Chrono> entry : EVENTS_STATES.entrySet()) {
            result.add(mapEventState(entry));
        }
        return result;
    }

}
