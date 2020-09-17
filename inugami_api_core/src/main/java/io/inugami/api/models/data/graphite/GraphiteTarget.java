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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.models.data.basic.JsonObject;

import flexjson.JSON;
import flexjson.JSONDeserializer;

/**
 * GraphiteValues
 * 
 * @author patrick_guillerm
 * @since 23 sept. 2016
 */
public class GraphiteTarget implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6818032035661421722L;
    
    private String            target;
    
    @JSON(transformer = DataPointTransformer.class)
    private List<DataPoint>   datapoints;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public GraphiteTarget(final Double value) {
        this(null, value);
    }
    
    public GraphiteTarget(final String target, final Double value) {
        this.target = target == null ? "target" : target;
        datapoints = new ArrayList<>();
        datapoints.add(new DataPoint(value, System.currentTimeMillis()));
    }
    
    public GraphiteTarget(final String target, final List<DataPoint> datapoints) {
        super();
        this.target = target;
        this.datapoints = datapoints;
    }
    
    public GraphiteTarget(final TimeValue timeValue) {
        super();
        Asserts.notNull(timeValue);
        target = timeValue.getPath();
        datapoints = new ArrayList<>();
        datapoints.add(new DataPoint(timeValue.getValue().toDouble(), timeValue.getTime()));
    }
    
    public GraphiteTarget() {
        super();
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
                .append("target=").append(target)
                .append(", datapoints=").append(datapoints)
                .append(']')
                .toString();
        //@formatter:on
    }
    
    @Override
    public int hashCode() {
        return 31 * ((target == null) ? 0 : target.hashCode());
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof GraphiteTarget)) {
            final GraphiteTarget other = (GraphiteTarget) obj;
            result = target == null ? other.getTarget() == null : target.equals(other.getTarget());
        }
        
        return result;
    }
    
    // =========================================================================
    // CONVERT
    // =========================================================================
    @Override
    public <T extends JsonObject> T convertToObject(final byte[] data, final Charset encoding) {
        GraphiteTarget result = null;
        
        if (data != null) {
            final String json = encoding == null ? new String(data) : new String(data, encoding);
            
            result = new JSONDeserializer<GraphiteTarget>().use(".",
                                                                GraphiteTarget.class).use(DataPoint.class,
                                                                                          new DataPointTransformer()).deserialize(json);
        }
        
        return (T) result;
    }
    
    @Override
    public String convertToJson() {
        final JsonBuilder result = new JsonBuilder();
        result.openObject();
        
        result.addField("target");
        result.valueQuot(getTarget());
        result.addSeparator();
        result.addField("datapoints");
        result.write(buildDataPoints());
        
        result.closeObject();
        return result.toString();
    }
    
    private String buildDataPoints() {
        final JsonBuilder result = new JsonBuilder();
        result.openList();
        if ((getDatapoints() != null) && !getDatapoints().isEmpty()) {
            for (int i = 0; i < getDatapoints().size(); i++) {
                if (i != 0) {
                    result.addSeparator();
                }
                result.openObject();
                result.write(buildDataPoint(getDatapoints().get(i)));
                result.closeObject();
            }
        }
        result.closeList();
        return result.toString();
    }
    
    private String buildDataPoint(final DataPoint dataPoint) {
        final JsonBuilder result = new JsonBuilder();
        
        result.addField("value").write(String.valueOf(dataPoint.getValue()));
        result.addSeparator();
        result.addField("timestamp").write(String.valueOf(dataPoint.getTimestamp()));
        
        return result.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getTarget() {
        return target;
    }
    
    public void setTarget(final String target) {
        this.target = target;
    }
    
    public List<DataPoint> getDatapoints() {
        if (datapoints == null) {
            datapoints = new ArrayList<>();
        }
        return datapoints;
    }
    
    public void setDatapoints(final List<DataPoint> datapoints) {
        this.datapoints = datapoints;
    }
    
    public void setDatapoint(final double data) {
        datapoints = new ArrayList<>();
        datapoints.add(new DataPoint(data, System.currentTimeMillis()));
    }
    
    public void addDatapoint(final double data, final long timestamp) {
        if (datapoints == null) {
            datapoints = new ArrayList<>();
        }
        datapoints.add(new DataPoint(data, timestamp));
    }
    
    @Override
    public JsonObject cloneObj() {
        final List<DataPoint> newDataPoints = new ArrayList<>();
        if (datapoints != null) {
            for (final DataPoint item : datapoints) {
                final Double newValue = item.getValue() == null ? null : item.getValue().doubleValue();
                newDataPoints.add(new DataPoint(newValue, item.getTimestamp()));
            }
        }
        return new GraphiteTarget(target, newDataPoints);
    }
    
    public boolean isNotEmpty() {
        return !isEmpty();
    }
    
    public boolean isEmpty() {
        boolean result = datapoints == null;
        if (!result) {
            result = datapoints.isEmpty();
        }
        if (!result) {
            result = hasOnlyNullValue(datapoints);
        }
        
        return result;
    }
    
    private boolean hasOnlyNullValue(final List<DataPoint> data) {
        boolean result = false;
        
        for (final DataPoint point : data) {
            result = point.getValue() != null;
            if (result) {
                break;
            }
        }
        return !result;
    }
}
