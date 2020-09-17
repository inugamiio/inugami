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

import io.inugami.api.models.JsonBuilder;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.graphite.number.GraphiteNumber;
import io.inugami.api.models.data.graphite.number.GraphiteNumberTransformer;

import flexjson.JSON;

/**
 * TimeValue
 * 
 * @author patrick_guillerm
 * @since 14 mai 2018
 */
public class TimeValue implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long    serialVersionUID = -4766022961962855510L;
    
    private final String         path;
    
    @JSON(transformer = GraphiteNumberTransformer.class)
    private final GraphiteNumber value;
    
    private final long           time;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public TimeValue(final String path, final GraphiteNumber value, final long time) {
        super();
        this.path = path;
        this.value = value;
        this.time = time;
    }
    
    @Override
    public JsonObject cloneObj() {
        return new TimeValue(path, value == null ? null : value.cloneNumber(), time);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((path == null) ? 0 : path.hashCode());
        result = (prime * result) + (int) (time ^ (time >>> 32));
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && (obj != null) && (obj instanceof TimeValue)) {
            final TimeValue other = (TimeValue) obj;
            result = (time == other.getTime())
                     && (path == null ? other.getPath() == null : path.equals(other.getPath()));
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TimeValue [path=");
        builder.append(path);
        builder.append(", value=");
        builder.append(value);
        builder.append(", time=");
        builder.append(time);
        builder.append("]");
        return builder.toString();
    }
    
    @Override
    public String convertToJson() {
        final JsonBuilder json = new JsonBuilder();
        json.openObject();
        
        json.addField("path").valueQuot(path).addSeparator();
        json.addField("value").write(value == null ? "null" : value.rendering()).addSeparator();
        json.addField("time").write(time);
        
        json.closeObject();
        return json.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getPath() {
        return path;
    }
    
    public GraphiteNumber getValue() {
        return value;
    }
    
    public long getTime() {
        return time;
    }
    
}
