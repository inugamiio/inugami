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
package org.inugami.commons.data.model;

import org.inugami.api.models.data.JsonObject;

/**
 * Record
 * 
 * @author patrick_guillerm
 * @since 10 oct. 2017
 */
public class Record implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 789112101362883558L;
    
    private final long        time;
    
    private final double      value;
    
    private final String      unit;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public Record(final long time, final double value, final String unit) {
        super();
        this.time = time;
        this.value = value;
        this.unit = unit;
    }
    
    @Override
    public JsonObject cloneObj() {
        return new Record(time, value, unit);
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Record [time=");
        builder.append(time);
        builder.append(", value=");
        builder.append(value);
        builder.append(", unit=");
        builder.append(unit);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public long getTime() {
        return time;
    }
    
    public double getValue() {
        return value;
    }
    
    public String getUnit() {
        return unit;
    }
    
}
