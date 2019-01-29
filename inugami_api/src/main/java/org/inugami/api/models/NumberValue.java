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
package org.inugami.api.models;

/**
 * LoveScoreData
 * 
 * @author patrick_guillerm
 * @since 22 sept. 2016
 */
public class NumberValue {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final double last;
    
    private final double current;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public NumberValue(final double last, final double current) {
        super();
        this.last = last;
        this.current = current;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(current);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(last);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof NumberValue)) {
            final NumberValue other = (NumberValue) obj;
            result = (Double.doubleToLongBits(current) == Double.doubleToLongBits(other.current))
                     && (Double.doubleToLongBits(last) != Double.doubleToLongBits(other.last));
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        //@formatter:off
        return new StringBuilder(this.getClass().getSimpleName())
                        .append('@')
                        .append(System.identityHashCode(this))
                        .append("[current=").append(current)
                        .append(", last=").append(last)
                        .append(']')
                        .toString();
      //@formatter:on
    }
    
    public double getLast() {
        return last;
    }
    
    public double getCurrent() {
        return current;
    }
    
}
