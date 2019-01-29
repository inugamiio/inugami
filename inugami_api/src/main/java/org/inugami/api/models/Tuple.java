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
 * Tuple
 * 
 * @author patrick_guillerm
 * @since 10 janv. 2017
 */
public class Tuple<K, V> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private transient final K key;
    
    private transient final V value;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Tuple(final K key, final V value) {
        super();
        this.key = key;
        this.value = value;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return (key == null) ? 0 : key.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        Tuple<?, ?> other = null;
        
        if (!result && (obj != null) && (obj instanceof Tuple<?, ?>)) {
            other = (Tuple<?, ?>) obj;
        }
        
        if (other != null) {
            result = key == null ? other.getKey() == null : key.equals(other.getKey());
        }
        return result;
    }
    
    @Override
    public String toString() {
        //@formatter:off
        return new StringBuilder(this.getClass().getSimpleName())
                        .append('@')
                        .append(System.identityHashCode(this))
                        .append("[key=").append(key)
                        .append(", value=").append(value)
                        .append(']')
                        .toString();
      //@formatter:on
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }
    
}
