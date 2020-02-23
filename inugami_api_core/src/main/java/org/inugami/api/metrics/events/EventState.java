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
package org.inugami.api.metrics.events;

import org.inugami.api.models.data.basic.JsonObject;

/**
 * EventState
 * 
 * @author patrick_guillerm
 * @since 8 août 2017
 */
public class EventState implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5004046074596299588L;
    
    private String            eventName;
    
    private long              start;
    
    private long              end;
    
    private boolean           running;
    
    private long              delais;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    public EventState() {
        super();
    }
    
    public EventState(final String eventName, final long start, final long end, final boolean running,
                      final long delais) {
        super();
        this.eventName = eventName;
        this.start = start;
        this.end = end;
        this.running = running;
        this.delais = delais;
    }
    
    @Override
    public JsonObject cloneObj() {
        return new EventState(eventName, start, end, running, delais);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return 31 * ((eventName == null) ? 0 : eventName.hashCode());
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof EventState)) {
            final EventState other = (EventState) obj;
            result = eventName == null ? other.getEventName() == null : eventName.equals(other.getEventName());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("EventState [eventName=");
        builder.append(eventName);
        builder.append(", start=");
        builder.append(start);
        builder.append(", end=");
        builder.append(end);
        builder.append(", running=");
        builder.append(running);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getEventName() {
        return eventName;
    }
    
    public void setEventName(final String eventName) {
        this.eventName = eventName;
    }
    
    public long getStart() {
        return start;
    }
    
    public void setStart(final long start) {
        this.start = start;
    }
    
    public long getEnd() {
        return end;
    }
    
    public void setEnd(final long end) {
        this.end = end;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public void setRunning(final boolean running) {
        this.running = running;
    }
    
    public long getDelais() {
        return delais;
    }
    
}
