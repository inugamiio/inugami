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
 * LongValue
 * 
 * @author patrick_guillerm
 * @since 17 août 2018
 */
public class LongNumber implements GraphiteNumber {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 5221104927236998757L;
    
    private final long        value;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public LongNumber(final long value) {
        this.value = value;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public GraphiteNumber add(final GraphiteNumber number) {
        final long toAdd = number == null ? 0L : number.toLong();
        return new LongNumber(value + toAdd);
    }
    
    @Override
    public GraphiteNumber sub(final GraphiteNumber number) {
        final long toSub = number == null ? 0L : number.toLong();
        return new LongNumber(value - toSub);
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
            final long other = ((GraphiteNumber) obj).toLong();
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
        return value;
    }
    
    @Override
    public double toDouble() {
        return new Double(value).doubleValue();
    }
    
    @Override
    public GraphiteNumber cloneNumber() {
        return new LongNumber(value);
    }
    
    @Override
    public boolean isDecimal() {
        return false;
    }
    
    @Override
    public BigDecimal toBigDecimal() {
        return new BigDecimal(value);
    }
}
