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
package io.inugami.core.services.sse;

import io.inugami.api.models.data.basic.JsonObject;

/**
 * SendSseEventTask
 * 
 * @author patrick_guillerm
 * @since 12 sept. 2017
 */
public class SendSseEvent {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String     cron;
    
    private final String     channel;
    
    private final String     event;
    
    private final JsonObject value;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SendSseEvent(final JsonObject value, final String event, final String cron, final String channel) {
        super();
        this.channel = channel;
        this.event = event;
        this.cron = cron;
        this.value = value;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SendSseEvent [cron=");
        builder.append(cron);
        builder.append(", channel=");
        builder.append(channel);
        builder.append(", event=");
        builder.append(event);
        builder.append(", value=");
        builder.append(value);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS
    // =========================================================================
    public String getCron() {
        return cron;
    }
    
    public String getChannel() {
        return channel;
    }
    
    public String getEvent() {
        return event;
    }
    
    public JsonObject getValue() {
        return value;
    }
    
}
