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
package io.inugami.core.alertings.senders.slack.sender;

import java.io.Serializable;

import io.inugami.api.models.data.basic.JsonObject;

/**
 * SlackModel
 * 
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
public abstract class AbstractSlackModel implements Serializable, JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -2957530797423599471L;
    
    private String            channel;
    
    private String            username;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public AbstractSlackModel() {
    }
    
    public AbstractSlackModel(final String username, final String channel) {
        super();
        this.username = username;
        this.channel = channel;
    }
    
    // =========================================================================
    // ABSTRACT
    // =========================================================================
    public abstract AbstractSlackModel clone(String channel);
    
    public abstract void childrenToString(StringBuilder builder);
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("[channel=");
        builder.append(channel);
        builder.append(", username=");
        builder.append(username);
        builder.append(",");
        childrenToString(builder);
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getChannel() {
        return channel;
    }
    
    public void setChannel(final String channel) {
        this.channel = channel;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
}
