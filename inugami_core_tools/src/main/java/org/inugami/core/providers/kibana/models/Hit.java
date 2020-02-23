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
package org.inugami.core.providers.kibana.models;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.JsonBuilder;
import org.inugami.api.models.data.basic.JsonObject;

import flexjson.JSON;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 * Hit
 * 
 * @author patrick_guillerm
 * @since 24 oct. 2016
 */
public class Hit implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long   serialVersionUID = 7170918784407627145L;
    
    private static final String BODY_FIELD       = "body";
    
    @JSON(name = "_index")
    private String              index;
    
    @JSON(name = "_type")
    private String              type;
    
    @JSON(name = "_id")
    private String              id;
    
    @JSON(name = "_score")
    private Double              score;
    
    @JSON(name = "_source")
    private Map<String, String> source;
    
    private List<String>        sort;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public Hit() {
        super();
    }
    
    public Hit(final String index, final String type, final String id, final Double score, final List<String> sort) {
        super();
        this.index = index;
        this.type = type;
        this.id = id;
        this.score = score;
        this.sort = sort;
    }
    
    public Hit(final String index, final String type, final String id, final Double score, final List<String> sort,
               final Map<String, String> source) {
        super();
        this.index = index;
        this.type = type;
        this.id = id;
        this.score = score;
        this.sort = sort;
        this.source = source;
    }
    
    public Hit(final Map<String, String> source) {
        this.source = source;
    }
    
    @Override
    public JsonObject cloneObj() {
        final List<String> newSort = new ArrayList<>();
        if (sort != null) {
            newSort.addAll(sort);
        }
        return new Hit(getIndex(), getType(), getId(), getScore(), newSort);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String convertToJson() {
        final JsonBuilder json = new JsonBuilder();
        json.openObject();
        json.addField("index").valueQuot(index).addSeparator();
        json.addField("type").valueQuot(type).addSeparator();
        json.addField("id").valueQuot(id).addSeparator();
        json.addField("score").write(score);
        if (sort != null) {
            json.addSeparator();
            json.addField("sort");
            json.writeListString(sort);
        }
        if (source != null) {
            final String[] keys = source.keySet().toArray(new String[] {});
            json.addSeparator();
            json.addField("source");
            json.openObject();
            for (int i = 0; i < keys.length; i++) {
                final String key = keys[i];
                if (i != 0) {
                    json.addSeparator();
                }
                json.addField(key);
                json.valueQuot(cleanContent(key));
            }
            
            json.closeObject();
        }
        json.closeObject();
        return json.toString();
    }
    
    @Override
    public <T extends JsonObject> T convertToObject(final byte[] data, final Charset encoding) {
        final String json = new String(data, encoding);
        T result = null;
        Map<String, Object> rawObject = null;
        try {
            rawObject = new JSONDeserializer<Map<String, Object>>().deserialize(json);
        }
        catch (final Exception e) {
            Loggers.DEBUG.debug(e.getMessage(), e);
        }
        
        if (rawObject != null) {
            final String index = str("index", rawObject);
            final String type = str("type", rawObject);
            final String id = str("id", rawObject);
            final Double score = convertDouble("score", rawObject);
            final List<String> sort = loadSort(rawObject);
            final Map<String, String> source = loadSource(rawObject);
            result = (T) new Hit(index, type, id, score, sort, source);
        }
        
        return result;
    }
    
    private List<String> loadSort(final Map<String, Object> rawObject) {
        final Object data = rawObject.get("sort");
        return data == null ? null : (List<String>) data;
    }
    
    private String str(final String key, final Map<String, Object> rawObject) {
        return String.valueOf(rawObject.get(key));
    }
    
    private Double convertDouble(final String key, final Map<String, Object> rawObject) {
        final String value = String.valueOf(rawObject.get(key));
        return (value == null) || "null".equals(value) ? null : Double.parseDouble(value);
    }
    
    private Map<String, String> loadSource(final Map<String, Object> rawObject) {
        final Map<String, String> data = new HashMap<>();
        final Object raw = rawObject.get("source");
        
        if (raw instanceof Map) {
            final Map<String, Object> map = ((Map<String, Object>) raw);
            for (final Map.Entry<String, Object> entry : map.entrySet()) {
                data.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        
        return data;
    }
    
    private String cleanContent(final String key) {
        String result = null;
        final Object data = source.get(key);
        
        if (data instanceof String) {
            result = (String) data;
        }
        else {
            result = new JSONSerializer().exclude("*.class").deepSerialize(data);
        }
        
        return result.replaceAll("\"", "\\\\\"").replaceAll("\n", "\\\\n");
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Hit [index=");
        builder.append(index);
        builder.append(", type=");
        builder.append(type);
        builder.append(", id=");
        builder.append(id);
        builder.append(", score=");
        builder.append(score);
        builder.append(", source=");
        builder.append(source);
        builder.append(", sort=");
        builder.append(sort);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getIndex() {
        return index;
    }
    
    public void setIndex(final String index) {
        this.index = index;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public Double getScore() {
        return score;
    }
    
    public void setScore(final Double score) {
        this.score = score;
    }
    
    public List<String> getSort() {
        return sort;
    }
    
    public void setSort(final List<String> sort) {
        this.sort = sort;
    }
    
    public Map<String, String> getSource() {
        return source;
    }
    
    public void setSource(final Map<String, String> source) {
        this.source = source;
    }
    
    public String getContent() {
        String result = null;
        if (source != null) {
            result = source.get(BODY_FIELD);
        }
        return result;
    }
    
}
