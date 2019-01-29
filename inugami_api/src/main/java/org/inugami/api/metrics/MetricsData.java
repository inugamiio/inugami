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
package org.inugami.api.metrics;

import org.inugami.api.models.data.JsonObject;

/**
 * MetricsData
 * 
 * @author patrick_guillerm
 * @since 6 juin 2017
 */
public class MetricsData implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long     serialVersionUID = 1097665514789847818L;
    
    private final String          path;
    
    private final Double          value;
    
    private final MetricsDataType type;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MetricsData(final String path, final Double value, final MetricsDataType type) {
        super();
        this.path = path;
        this.value = value;
        this.type = type;
    }
    
    @Override
    public JsonObject cloneObj() {
        return new MetricsData(path, value, type);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return 31 * ((path == null) ? 0 : path.hashCode());
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && (obj != null) && (obj instanceof MetricsData)) {
            final MetricsData other = (MetricsData) obj;
            result = path == null ? other.getPath() == null : path.equals(other.getPath());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        //@formatter:off
        return new StringBuilder(this.getClass().getSimpleName())
                        .append('@')
                        .append(System.identityHashCode(this))
                        .append("[type=").append(type)
                        .append(", value=").append(value)
                        .append(", path=").append(path)
                        .append(']')
                        .toString();
      //@formatter:on
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getPath() {
        return path;
    }
    
    public Double getValue() {
        return value;
    }
    
    public MetricsDataType getType() {
        return type;
    }
    
}
