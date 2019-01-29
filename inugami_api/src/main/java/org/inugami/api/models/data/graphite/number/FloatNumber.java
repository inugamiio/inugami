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
package org.inugami.api.models.data.graphite.number;

import java.math.BigDecimal;

/**
 * FloatNumber
 * 
 * @author patrick_guillerm
 * @since 17 août 2018
 */
public class FloatNumber implements GraphiteNumber {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -2112757362454710593L;
    
    private final double      value;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public FloatNumber(final double value) {
        super();
        this.value = value;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public GraphiteNumber add(final GraphiteNumber number) {
        final double toAdd = number == null ? 0.0 : number.toDouble();
        return new FloatNumber(value + toAdd);
    }
    
    @Override
    public GraphiteNumber sub(final GraphiteNumber number) {
        final double toSub = number == null ? 0.0 : number.toDouble();
        return new FloatNumber(value - toSub);
    }
    
    @Override
    public String rendering() {
        return String.valueOf(value);
    }
    
    @Override
    public String convertToJson() {
        return rendering();
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && (obj != null) && (obj instanceof GraphiteNumber)) {
            final double other = ((GraphiteNumber) obj).toDouble();
            result = value == other;
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return rendering();
    }
    
    @Override
    public long toLong() {
        return new Double(value).longValue();
    }
    
    @Override
    public double toDouble() {
        return value;
    }
    
    @Override
    public GraphiteNumber cloneNumber() {
        return new FloatNumber(value);
    }
    
    @Override
    public boolean isDecimal() {
        return true;
    }
    
    @Override
    public BigDecimal toBigDecimal() {
        return new BigDecimal(value);
    }
    
}
