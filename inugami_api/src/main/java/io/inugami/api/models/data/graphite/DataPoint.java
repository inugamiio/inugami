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
package io.inugami.api.models.data.graphite;
import io.inugami.api.models.data.basic.JsonObject;

/**
 * DataPoint
 * 
 * @author patrick_guillerm
 * @since 23 sept. 2016
 */
public class DataPoint implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -6203322773055819499L;
    
    private Double            value;
    
    private long              timestamp;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DataPoint() {
        super();
    }
    
    public DataPoint(final Double value, final long timestamp) {
        super();
        this.value = value;
        this.timestamp = timestamp;
    }
    
    // =========================================================================
    // OVERRIDE
    // =========================================================================
    @Override
    public String toString() {
        //@formatter:off
        return new StringBuilder(this.getClass().getSimpleName())
                .append('@')
                .append(System.identityHashCode(this))
                .append('[')
                .append("value=").append(value)
                .append(", timestamp=").append(timestamp)
                .append(']')
                .toString();
        //@formatter:on
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public Double getValue() {
        return value;
    }
    
    public void setValue(final Double value) {
        this.value = value;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }
}
