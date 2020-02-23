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
package org.inugami.core.services.sse;

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.models.JsonBuilder;
import org.inugami.api.models.data.basic.JsonObject;

/**
 * MultiSseEvent
 * 
 * @author patrick_guillerm
 * @since 27 sept. 2017
 */
public class MultiSseEvent implements JsonObject {
    
    private static final String FIELD_EVENT_NAME  = "name";
    
    private static final String FIELD_EVENTS      = "events";
    
    private static final String FIELD_CHANNEL     = "channel";
    
    private static final String FIELD_MUTLI_EVENT = "mutliEvent";
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long        serialVersionUID = 6889673531887278115L;
    
    private final List<SendSseEvent> events;
    
    private final String             channel;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MultiSseEvent(final String channel, final List<SendSseEvent> events) {
        super();
        this.events = events;
        this.channel = channel;
    }
    
    @Override
    public JsonObject cloneObj() {
        final List<SendSseEvent> newEvents = new ArrayList<>();
        if (events != null) {
            for (final SendSseEvent item : events) {
                //@formatter:off
                newEvents.add(new SendSseEvent(item.getValue()==null?null:item.getValue().cloneObj(),
                                              item.getEvent(),
                                              item.getCron(),
                                              item.getChannel()));
                //@formatter:on
            }
        }
        return new MultiSseEvent(channel, newEvents);
    }
    // =========================================================================
    // METHODS
    // =========================================================================
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String convertToJson() {
        final long time = System.currentTimeMillis();
        final JsonBuilder json = new JsonBuilder();
        json.openObject();
        json.addField(FIELD_MUTLI_EVENT).write(true).addSeparator();
        json.addField(FIELD_CHANNEL).valueQuot(channel).addSeparator();
        json.addField(FIELD_EVENTS);
        json.openList();
        for (int i = events.size() - 1; i >= 0; i--) {
            convertSendSseEvent(events.get(i), json, time);
            if (i != 0) {
                json.addSeparator();
            }
        }
        
        json.closeList();
        json.closeObject();
        return json.toString();
    }
    
    private void convertSendSseEvent(final SendSseEvent sendSseEvent, final JsonBuilder json, final long time) {
        json.openObject();
        json.addField(FIELD_CHANNEL).valueQuot(sendSseEvent.getChannel()).addSeparator();
        json.addField(FIELD_EVENT_NAME).valueQuot(sendSseEvent.getEvent()).addSeparator();
        json.addField("cron").valueQuot(sendSseEvent.getCron()).addSeparator();
        json.addField("time").write(time).addSeparator();
        json.addField("data");
        if (sendSseEvent.getValue() == null) {
            json.valueNull();
        }
        else {
            json.write(sendSseEvent.getValue().convertToJson());
        }
        json.closeObject();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public List<SendSseEvent> getEvents() {
        return events;
    }
    
    public String getChannel() {
        return channel;
    }
    
}
