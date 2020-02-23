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

import java.util.Set;

import org.inugami.api.metrics.events.EventState;
import org.inugami.api.metrics.events.MetricsEvents;
import org.inugami.api.models.JsonBuilder;
import org.inugami.api.models.data.basic.Json;
import org.inugami.api.models.data.basic.JsonObject;
import org.inugami.api.monitoring.RequestContext;
import org.inugami.api.providers.concurrent.ThreadSleep;
import org.inugami.core.services.sse.SseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MetricsEventsSenderSse
 * 
 * @author patrick_guillerm
 * @since 27 sept. 2017
 */
public class MetricsEventsSenderSse extends Thread {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger        LOGGER            = LoggerFactory.getLogger("MetricsEventsSenderSse");
    
    private final static EventState[]  TYPE              = {};
    
    private static final char          SPACE             = ' ';
    
    private static final char          CSV_LINE          = '\n';
    
    private static final String        CSV_WAIT          = "wait   ";
    
    private static final String        CSV_RUNNING       = "running";
    
    private static final String        CSV_SEPARATOR     = ";";
    
    private static final String        CSV_HEADER        = "state  ;       delais;        start;         stop;event\n";
    
    private static final String        START_STOP_EVENTS = "start_stop_events";
    
    private static final StringBuilder CSV               = new StringBuilder();
    
    private static final JsonBuilder   JSON              = new JsonBuilder();
    
    private EventState[]               previous          = null;
    
    private boolean                    stop              = false;
    
    private boolean                    running           = false;
    
    private final int                  delaisSize        = 13;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MetricsEventsSenderSse() {
        super(MetricsEventsSenderSse.class.getSimpleName());
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void run() {
        RequestContext.getInstance();
        final ThreadSleep threadSleep = new ThreadSleep(5000);
        while (!stop) {
            if (!running) {
                process();
            }
            threadSleep.sleep();
        }
    }
    
    private void process() {
        running = true;
        final Set<EventState> states = MetricsEvents.getStates();
        final EventState[] localState = states == null ? TYPE : states.toArray(TYPE);
        if (hasChange(localState, previous)) {
            previous = localState;
            final JsonObject json = convertToJson(localState);
            SseService.sendAdminEvent(START_STOP_EVENTS, json);
            traceCsv(states);
        }
        running = false;
    }
    
    private boolean hasChange(final EventState[] states, final EventState[] previousStates) {
        boolean result = (previousStates == null) && (states != null);
        if (!result && (states != null)) {
            result = previousStates.length != states.length;
            
            if (!result) {
                for (int i = previousStates.length - 1; i >= 0; i--) {
                    final EventState current = states[i];
                    final EventState previous = previousStates[i];
                    
                    result = (current.isRunning() != previous.isRunning()) || (current.getEnd() != previous.getEnd());
                    if (result) {
                        break;
                    }
                }
            }
        }
        return result;
    }
    
    private JsonObject convertToJson(final EventState[] states) {
        JSON.clear();
        JSON.openList();
        
        if (states != null) {
            for (int i = states.length - 1; i >= 0; i--) {
                JSON.write(states[i].convertToJson());
                if (i != 0) {
                    JSON.addSeparator();
                }
            }
        }
        
        JSON.closeList();
        return new Json(JSON.toString());
    }
    
    private void traceCsv(final Set<EventState> states) {
        clear(CSV);
        CSV.append(CSV_HEADER);
        for (final EventState event : states) {
            CSV.append(event.isRunning() ? CSV_RUNNING : CSV_WAIT).append(CSV_SEPARATOR);
            
            final long delaisValue = event.isRunning() ? 0 : event.getDelais();
            final String delais = String.valueOf(delaisValue);
            format(CSV, delais, delaisSize);
            CSV.append(CSV_SEPARATOR);
            CSV.append(event.getStart()).append(CSV_SEPARATOR);
            CSV.append(event.getEnd()).append(CSV_SEPARATOR);
            CSV.append(event.getEventName());
            CSV.append(CSV_LINE);
        }
        LOGGER.info("#============ Running events ===============\n{}", CSV.toString());
    }
    
    private void clear(final StringBuilder builder) {
        builder.setLength(0);
        builder.trimToSize();
    }
    
    protected void format(final StringBuilder csv, final String value, final int size) {
        final int diff = size - value.length();
        if (diff > 0) {
            for (int i = diff - 1; i >= 0; i--) {
                csv.append(SPACE);
            }
        }
        csv.append(value);
    }
    
    @Override
    public void interrupt() {
        stop = true;
    }
    
}
