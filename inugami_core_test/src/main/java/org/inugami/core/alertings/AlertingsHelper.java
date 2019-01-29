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
package org.inugami.core.alertings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.script.ScriptException;

import org.inugami.api.alertings.AlerteLevels;
import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.exceptions.Asserts;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.data.graphite.GraphiteTarget;
import org.inugami.api.models.data.graphite.GraphiteTargets;
import org.inugami.api.models.events.AlertingModel;
import org.inugami.api.models.events.Event;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.commons.engine.JavaScriptEngine;
import org.inugami.configuration.models.EventConfig;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.core.alertings.DefaultAlertingProvider;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.context.Context;

/**
 * AlertingsHelper
 * 
 * @author patrick_guillerm
 * @since 26 déc. 2017
 */
public final class AlertingsHelper {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final ApplicationContext      CONTEXT           = Context.getInstance();
    
    private static final DefaultAlertingProvider ALERTING_PROVIDER = new DefaultAlertingProvider("MOCK", null);
    
    // =========================================================================
    // METHODS EXECUTE FUNCTION
    // =========================================================================
    public static Optional<String> executeFunction(final String functionName, final Object... params) {
        return executeFunction(null, functionName, params);
    }
    
    public static Optional<String> executeFunction(final String preload, final String functionName,
                                                   final Object... params) {
        String result = null;
        try {
            result = JavaScriptEngine.getInstance().execute(functionName, params);
        }
        catch (final ScriptException e) {
            Loggers.DEBUG.error(e.getMessage());
        }
        return Optional.ofNullable(result);
    }
    
    // =========================================================================
    // METHODS GRAPHITE
    // =========================================================================
    public static AlertingResult executeGraphiteData(final String alertName, final String eventName,
                                                     final GraphiteTarget... targets) {
        return executeGraphiteData(null, alertName, eventName, targets);
    }
    
    public static AlertingResult executeGraphiteData(final String preload, final String alertName,
                                                     final String eventName, final GraphiteTarget... targets) {
        
        final GavEvent gavEvent = searchEvent(eventName);
        Asserts.notNull("can't found event :" + eventName, gavEvent);
        final AlertingModel alertModel = searchAlertingModel(alertName, gavEvent);
        if (alertModel == null) {
            return null;
        }
        
        //@formatter:off
        final ProviderFutureResult data = new ProviderFutureResultBuilder()
                                                    .addData(new GraphiteTargets(Arrays.asList(targets)))
                                                    .addEvent(gavEvent.getEvent())
                                                    .addChannel("mock")
                                                    .build();
        //@formatter:on        
        
        return ALERTING_PROVIDER.processAlertingCheck(gavEvent.getGav(), gavEvent.getEvent(), data, alertModel,
                                                      preload);
    }
    
    public static AlertingResult executeGraphiteAssert(final AlerteLevels level, final String preload,
                                                       final String alertName, final String eventName,
                                                       final GraphiteTarget... targets) {
        
        final AlertingResult data = executeGraphiteData(preload, alertName, eventName, targets);
        final String prefix = String.join(" ", "Event:", eventName, "Alert:", alertName, "->");
        
        if (level == null) {
            Asserts.isNull(prefix + "must be null!", data);
        }
        else {
            Asserts.notNull(prefix + "mustn't be null!", data);
            //@formatter:off
            final String msg = new StringBuilder(prefix)
                                        .append("level doesn't matches (ref:")
                                        .append(level)
                                        .append(", actual : ")
                                        .append(data.getLevelType())
                                        .append(")")
                                        .append(" for data :")
                                        .append(new GraphiteTargets(targets).convertToJson())
                                        .toString();
            //@formatter:on
            Asserts.isTrue(msg, level == data.getLevelType());
        }
        return data;
    }
    
    // =========================================================================
    // ASSERTS
    // =========================================================================
    public static void assertLevel(final AlerteLevels level, final AlertingResult value) {
        assertLevel(null, level, value);
    }
    
    public static void assertLevel(final String message, final AlerteLevels level, final AlertingResult value) {
        final String msg = message != null ? message : "assert level(" + level + ") fail";
        Asserts.notNull(msg, value);
        if (level != value.getLevelType()) {
            throw new IllegalArgumentException(msg);
        }
    }
    
    // =========================================================================
    // SEARCH EVENT
    // =========================================================================
    private static GavEvent searchEvent(final String eventName) {
        GavEvent result = null;
        
        if (CONTEXT.getPlugins().isPresent()) {
            for (final Plugin plugin : CONTEXT.getPlugins().get()) {
                if (plugin.getEvents().isPresent()) {
                    result = searchInPlugin(eventName, plugin);
                    if (result != null) {
                        break;
                    }
                }
                
            }
        }
        
        Asserts.notNull("Can't found event:" + eventName, eventName);
        return result;
    }
    
    private static GavEvent searchInPlugin(final String eventName, final Plugin plugin) {
        GavEvent result = null;
        for (final EventConfig eventConfig : plugin.getEvents().orElseGet(Collections::emptyList)) {
            if (eventConfig.getSimpleEvents() != null) {
                result = searchInGenericEvent(eventName, plugin, eventConfig.getSimpleEvents());
                if (result == null) {
                    break;
                }
            }
            if (result == null && eventConfig.getEvents() != null) {
                result = searchInEvent(eventName, plugin, eventConfig.getEvents());
                if (result == null) {
                    break;
                }
            }
        }
        
        return result;
    }
    
    private static GavEvent searchInEvent(final String eventName, final Plugin plugin, final List<Event> events) {
        GavEvent result = null;
        
        for (final Event event : events) {
            if (event.getName().equals(eventName)) {
                result = new GavEvent(event, plugin.getGav());
            }
            else {
                result = searchInGenericEvent(eventName, plugin, event.getTargets());
            }
            
            if (result != null) {
                break;
            }
        }
        
        return result;
    }
    
    private static GavEvent searchInGenericEvent(final String eventName, final Plugin plugin,
                                                 final List<? extends GenericEvent> events) {
        GavEvent result = null;
        //@formatter:off
        final Optional<? extends GenericEvent> foundEvent = events.stream()
                                                                  .filter(event -> event.getName().equals(eventName))
                                                                  .findFirst();
        //@formatter:on
        if (foundEvent.isPresent()) {
            result = new GavEvent(foundEvent.get(), plugin.getGav());
        }
        
        return result;
    }
    
    // =========================================================================
    // SEARCH ALERT
    // =========================================================================
    private static AlertingModel searchAlertingModel(final String alertName, final GavEvent gavEvent) {
        AlertingModel result = null;
        
        final List<AlertingModel> alerts = gavEvent.getEvent().getAlertings().orElse(new ArrayList<>());
        for (final AlertingModel model : alerts) {
            if (model.getName().equals(alertName)) {
                result = model;
                break;
            }
        }
        
        Asserts.notNull("Can't found alert:" + alertName, alertName);
        return result;
    }
    
}
