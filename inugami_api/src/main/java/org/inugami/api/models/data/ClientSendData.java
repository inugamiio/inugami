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
package org.inugami.api.models.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Pojo
 * 
 * @author patrick_guillerm
 * @since 22 sept. 2016
 */
public class ClientSendData {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String           name;
    
    private Long             time;
    
    private List<JsonObject> targets = new ArrayList<>();
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ClientSendData() {
        super();
    }
    
    public ClientSendData(final Long time) {
        super();
        this.time = time;
    }
    
    public ClientSendData(final Long time, final String name) {
        this(time);
        this.name = name;
    }
    
    public List<JsonObject> getTargets() {
        return targets;
    }
    
    public void setTargets(final List<JsonObject> targets) {
        this.targets = targets;
    }
    
    public void addTarget(final JsonObject target) {
        targets.add(target);
    }
    
    // =========================================================================
    // OVERRIDE
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((name == null) ? 0 : name.hashCode());
        result = (prime * result) + ((time == null) ? 0 : time.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && (obj != null) && (obj instanceof ClientSendData)) {
            final ClientSendData other = (ClientSendData) obj;
            //@formatter:off
            result = name == null ? other.getName() == null : name.equals(other.getName())
                  && (time == null) ? other.getTime() == null : time.equals(other.getTime());
            //@formatter:on
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        //@formatter:off
        return new StringBuilder(this.getClass().getSimpleName())
                        .append('@')
                        .append(System.identityHashCode(this))
                        .append("[name=").append(name)
                        .append(", time=").append(time)
                        .append(']')
                        .toString();
      //@formatter:on
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Long getTime() {
        return time;
    }
    
    public void setTime(final Long time) {
        this.time = time;
    }
    
}
