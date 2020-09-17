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
import io.inugami.api.models.events.Event;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderFutureResultBuilder;
import io.inugami.api.providers.task.SseProviderResult;
import io.inugami.core.services.sse.SseService;

/**
 * OnEventDoneProcessor
 * 
 * @author patrick_guillerm
 * @since 28 sept. 2017
 */
public class OnEventDoneProcessor {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public void process(final ProviderFutureResult value, final GenericEvent currentEvent, final String channel) {
        sendSSE(value, currentEvent, channel);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private ProviderFutureResult sendSSE(final ProviderFutureResult value, final GenericEvent currentEvent,
                                         final String channel) {
        final String currentCronExpr = extractCronExpression(currentEvent);
        
        if (value == null) {
            Loggers.EVENTS.warn("event {} send null data", currentEvent.getName());
        }
        else {
            //@formatter:off
            final ProviderFutureResult data = new ProviderFutureResultBuilder(value)
                                                        .addEvent(currentEvent)
                                                        .addChannel(channel)
                                                        .addCronExpression(currentCronExpr)
                                                        .addAlerts(value.getAlerts())
                                                        .build();
            //@formatter:on
            SseService.sendEvent(channel, currentEvent.getName(), new SseProviderResult(data), currentCronExpr);
        }
        return value;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private String extractCronExpression(final GenericEvent event) {
        String result = null;
        
        if (event instanceof SimpleEvent) {
            result = ((SimpleEvent) event).getScheduler();
        }
        else if (event instanceof Event) {
            result = ((Event) event).getScheduler();
        }
        return result;
    }
    
}
