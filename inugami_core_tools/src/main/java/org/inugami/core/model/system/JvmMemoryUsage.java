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
package org.inugami.core.model.system;

import java.io.Serializable;
import java.lang.management.MemoryUsage;

import org.inugami.api.models.ClonableObject;

/**
 * MemoryUsage
 * 
 * @author pguillerm
 * @since 28 août 2017
 */
public class JvmMemoryUsage extends MemoryUsage implements Serializable, ClonableObject<JvmMemoryUsage> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 3600290835679912470L;
    
    private static final long MB               = 1_048_576L;
    
    private final double      usedMB;
    
    private final long        timestamp;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public JvmMemoryUsage() {
        super(0l, 0l, 0l, 0l);
        timestamp = System.currentTimeMillis();
        usedMB = 0;
    }
    
    public JvmMemoryUsage(final long init, final long used, final long max) {
        super(init, used, used, max);
        timestamp = System.currentTimeMillis();
        usedMB = ((double) used) / MB;
    }
    
    public JvmMemoryUsage(final long init, final long used, final long max, final long timestamp, final double usedMB) {
        super(init, used, used, max);
        this.timestamp = timestamp;
        this.usedMB = usedMB;
    }
    
    @Override
    public JvmMemoryUsage cloneObj() {
        return new JvmMemoryUsage(getInit(), getUsed(), getMax(), timestamp, usedMB);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof JvmMemoryUsage)) {
            final JvmMemoryUsage other = (JvmMemoryUsage) obj;
            result = (getUsed() == other.getUsed()) && (timestamp == other.getTimestamp());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("JvmMemoryUsage [timestamp=");
        builder.append(timestamp);
        builder.append(",");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public long getTimestamp() {
        return timestamp;
    }
    
    public double getUsedMB() {
        return usedMB;
    }
    
}
