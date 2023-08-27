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
import java.util.Arrays;
import java.util.List;

import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.basic.JsonObjects;

import flexjson.JSONDeserializer;
import flexjson.JSONException;

/**
 * GraphiteTargets
 * 
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
public class GraphiteTargets implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long    serialVersionUID = -732597415422973512L;
    
    private List<GraphiteTarget> targets;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public GraphiteTargets() {
    }
    
    public GraphiteTargets(final List<GraphiteTarget> tagets) {
        targets = tagets;
    }
    
    public GraphiteTargets(final GraphiteTarget... targets) {
        this.targets = Arrays.asList(targets);
    }
    
    @Override
    public <T extends JsonObject> T convertToObject(final byte[] data, final Charset encoding) {
        GraphiteTargets result = null;
        List<GraphiteTarget> tagets = null;
        
        if (data != null) {
            final String json = encoding == null ? new String(data) : new String(data, encoding).trim();
            
            if (!json.isEmpty()) {
                try {
                    //@formatter:off
                    tagets = new JSONDeserializer<List<GraphiteTarget>>()
                            .use(null, ArrayList.class)
                            .use("values",GraphiteTarget.class)
                            .use(DataPoint.class, new DataPointTransformer())
                            .deserialize(json);
                    //@formatter:on
                    result = new GraphiteTargets(tagets);
                }
                catch (final JSONException error) {
                    if (Loggers.XLLOG.isDebugEnabled()) {
                        Loggers.XLLOG.error("{} : \n playload:\n----------\n{}\n----------\n", error.getMessage(),
                                            json);
                    }
                    else {
                        Loggers.XLLOG.error(error.getMessage());
                    }
                    throw error;
                }
            }
        }
        
        return (T) result;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public List<GraphiteTarget> getTargets() {
        return targets;
    }
    
    public void setTargets(final List<GraphiteTarget> targets) {
        this.targets = targets;
    }
    
    @Override
    public String convertToJson() {
        return new JsonObjects<GraphiteTarget>(targets).convertToJson();
    }
    
    @Override
    public JsonObject cloneObj() {
        final List<GraphiteTarget> newTargets = new ArrayList<>();
        if (targets != null) {
            for (final GraphiteTarget item : targets) {
                newTargets.add((GraphiteTarget) item.cloneObj());
            }
        }
        return new GraphiteTargets(newTargets);
    }
    
    public void addTarget(final GraphiteTarget value) {
        if (targets == null) {
            targets = new ArrayList<>();
        }
        if (value != null) {
            targets.add(value);
        }
        
    }
    
}
