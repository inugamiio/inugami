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
package io.inugami.core.context.scripts;

import javax.script.Bindings;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.events.Event;
import io.inugami.api.models.events.EventBuilder;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.models.events.SimpleEventBuilder;
import io.inugami.api.models.events.TargetConfig;

/**
 * JavaScriptDashboardTvApi
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2017
 */
public final class JavaScriptApi {
    
    // =========================================================================
    // BUILDERS
    // =========================================================================
    public static AlertingResult buildAlert(final String level, final String message, final Object data) {
        return JsBuildersHelper.buildAlert(level, message, data);
    }
    
    public static SimpleEvent buildSimpleEvent(final Object data) {
        SimpleEvent result = null;
        if ((data != null) && (data instanceof Bindings)) {
            result = new SimpleEventBuilder((Bindings) data).build();
        }
        
        return result;
    }
    
    public static Event buildEvent(final Object data) {
        Event result = null;
        if ((data != null) && (data instanceof Bindings)) {
            result = new EventBuilder((Bindings) data).build();
        }
        return result;
    }
    
    // =========================================================================
    // CONFIG
    // =========================================================================
    public static String global(final String configName) {
        return JsConfigHelper.global(configName);
    }
    
    public static String providerConfig(final String providerName, final String configName) {
        return JsConfigHelper.providerConfig(providerName, configName);
    }
    
    // =========================================================================
    // PROVIDERS
    // =========================================================================
    public static JsonObject callGraphiteProvider(final String providerName, final String query, final String from,
                                                  final String until) {
        return JsProviderHelper.callGraphiteProvider(providerName, query, from, until);
    }
    
    public static String extractGraphiteQuery(final GenericEvent event) {
        String result = null;
        
        if (event != null) {
            if (event instanceof SimpleEvent) {
                result = cleanTarget(((SimpleEvent) event).getQuery());
            }
            else if (event instanceof Event) {
                final StringBuilder buffer = new StringBuilder();
                boolean first = true;
                for (final TargetConfig target : ((Event) event).getTargets()) {
                    if (!first) {
                        buffer.append("&");
                    }
                    buffer.append(cleanTarget(target.getQuery()));
                    first = false;
                }
            }
        }
        return result;
    }
    
    static String cleanTarget(final String target) {
        final String[] parts = target.split("@target");
        StringBuilder buffer = new StringBuilder();
        
        boolean first = true;
        for (final String item : parts) {
            if (first) {
                buffer.append("target=");
            }
            else {
                buffer.append("&target");
            }
            buffer.append(item.trim());
            
            first = false;
        }
        
        final String secondPass = buffer.toString().replaceAll("\n", "");
        final String[] secondPassItems = secondPass.split(",");
        buffer = new StringBuilder();
        first = true;
        for (final String item : secondPassItems) {
            if (!first) {
                buffer.append(",");
            }
            buffer.append(item.trim());
            first = false;
        }
        
        return buffer.toString();
    }
}
