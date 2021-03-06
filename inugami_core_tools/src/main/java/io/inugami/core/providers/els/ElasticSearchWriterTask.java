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
package io.inugami.core.providers.els;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;

import io.inugami.api.dao.Identifiable;
import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.core.services.connectors.HttpConnector;

/**
 * ElasticSearchWriterTask
 * 
 * @author patrick_guillerm
 * @since 3 oct. 2018
 */
public class ElasticSearchWriterTask implements Callable<Void> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final HttpConnector    httpConnector;
    
    private final String           url;
    
    private final List<JsonObject> values;
    
    private final ElsData          data;
    
    private final String           bulkCommand;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ElasticSearchWriterTask(final HttpConnector httpConnector, final String url, final ElsData data,
                                   final List<JsonObject> values) {
        this(httpConnector, url, data, values, "index");
    }
    
    public ElasticSearchWriterTask(final HttpConnector httpConnector, final String url, final ElsData data,
                                   final List<JsonObject> values, final String bulkCommand) {
        this.httpConnector = httpConnector;
        this.url = url;
        this.values = values;
        this.data = data;
        this.bulkCommand = bulkCommand;
    }
    // =========================================================================
    // METHODS
    // =========================================================================
    
    @Override
    public Void call() throws Exception {
        
        final String jsonBulk = renderBulk(data, values);
        
        try {
            if (jsonBulk != null) {
                httpConnector.post(url, jsonBulk);
                Loggers.PROVIDER.info("send {} documents to ELS {} : {}", values.size(), data.getIndex(),
                                      data.getType());
            }
        }
        catch (final ConnectorException e) {
            Loggers.PROVIDER.error(e.getMessage());
            Loggers.DEBUG.error(e.getMessage(), e);
        }
        
        return null;
    }
    
    // =========================================================================
    // RENDER BULK
    // =========================================================================
    private String renderBulk(final ElsData data, final List<JsonObject> values) {
        String result = null;
        
        if (data.getValues() != null) {
            final JsonBuilder json = new JsonBuilder();
            
            for (final JsonObject value : values) {
                json.write(encodeBulkAction(data, value)).addLine();
                
                switch (bulkCommand) {
                    case "index":
                        json.write(value.convertToJson()).addLine();
                        break;
                    case "update":
                        json.openObject().addField("doc").write(value.convertToJson()).closeObject().addLine();
                        break;
                    default:
                        break;
                }
                
            }
            
            result = json.toString();
        }
        return result;
    }
    
    private String encodeBulkAction(final ElsData data, final JsonObject value) {
        final JsonBuilder json = new JsonBuilder();
        json.openObject();
        json.addField(bulkCommand);
        
        json.openObject();
        // data.getIndex().toLowerCase()
        json.addField("_index").valueQuot(data.getIndex()).addSeparator();
        json.addField("_type").valueQuot(data.getType());
        
        Serializable uid = null;
        if (value instanceof Identifiable) {
            uid = ((Identifiable<Serializable>) value).getUid();
        }
        if (uid != null) {
            json.addSeparator().addField("_id").valueQuot(uid);
        }
        json.closeObject();
        
        json.closeObject();
        return json.toString();
    }
    
    // =========================================================================
    // UPDATE DATE
    // =========================================================================
    public void updateData(final List<? extends JsonObject> data) {
        this.values.clear();
        if (data != null) {
            this.values.addAll(data);
        }
    }
}
