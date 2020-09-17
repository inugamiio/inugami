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
package io.inugami.core.model.system;

import java.io.Serializable;

/**
 * ThreadInfo
 * 
 * @author pguillerm
 * @since 5 sept. 2017
 */
public class SimpleThreadInfo implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long  serialVersionUID = -6168266861849202380L;
    
    private final String       name;
    
    private final Thread.State state;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SimpleThreadInfo(final String name, final Thread.State state) {
        super();
        this.name = name;
        this.state = state;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof SimpleThreadInfo)) {
            final SimpleThreadInfo other = (SimpleThreadInfo) obj;
            result = name == null ? other.getName() == null : name.equals(other.getName());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ThreadInfo [name=");
        builder.append(name);
        builder.append(", state=");
        builder.append(state);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getName() {
        return name;
    }
    
    public Thread.State getState() {
        return state;
    }
    
}
