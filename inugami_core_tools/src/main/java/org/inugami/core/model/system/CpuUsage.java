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

import org.inugami.api.models.ClonableObject;

/**
 * CpuUsage
 * 
 * @author pguillerm
 * @since 28 août 2017
 */
public class CpuUsage implements Serializable, ClonableObject<CpuUsage> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -4200727492762474506L;
    
    private final long        timestamp;
    
    private final double      use;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CpuUsage(final double use) {
        this(System.currentTimeMillis(), use);
    }
    
    public CpuUsage(final long timestamp, final double use) {
        super();
        this.timestamp = timestamp;
        this.use = use;
    }
    
    @Override
    public CpuUsage cloneObj() {
        return new CpuUsage(timestamp, use);
    }
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (int) (timestamp ^ (timestamp >>> 32));
        long temp;
        temp = Double.doubleToLongBits(use);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && (obj != null) && (obj instanceof CpuUsage)) {
            final CpuUsage other = (CpuUsage) obj;
            result = (timestamp == other.getTimestamp()) && Double.valueOf(use).equals(other.getUse());
        }
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("CpuUsage [timestamp=");
        builder.append(timestamp);
        builder.append(", use=");
        builder.append(use);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public long getTimestamp() {
        return timestamp;
    }
    
    public double getUse() {
        return use;
    }
    
}
