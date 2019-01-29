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
package org.inugami.commons.engine;

import java.util.List;

import org.inugami.api.models.data.JsonObject;
import org.inugami.commons.connectors.HttpConnectorResult;
import org.inugami.commons.engine.js.objects.JsListFactory;

import flexjson.JSONDeserializer;

/**
 * HttpConnectorResultJs
 * 
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
public class HttpConnectorResultJs implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -1095296950030192324L;
    
    private int               status           = 500;
    
    private String            message;
    
    private Object            data;
    
    private long              responseAt;
    
    private long              delais;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public HttpConnectorResultJs(final HttpConnectorResult httpResult) {
        if (httpResult != null) {
            status = httpResult.getStatusCode();
            message = httpResult.getMessage();
            responseAt = httpResult.getResponseAt();
            delais = httpResult.getDelais();
            if (httpResult.getData() != null) {
                //@formatter:off
                data = new JSONDeserializer<Object>()
                            .use(List.class, new JsListFactory())
                            .deserialize(new String(httpResult.getData()));
                //@formatter:on
            }
        }
    }
    
    @Override
    public String toString() {
        return convertToJson();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public int getStatus() {
        return status;
    }
    
    public void setStatus(final int status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(final Object data) {
        this.data = data;
    }
    
    public long getResponseAt() {
        return responseAt;
    }
    
    public void setResponseAt(final long responseAt) {
        this.responseAt = responseAt;
    }
    
    public long getDelais() {
        return delais;
    }
    
    public void setDelais(final long delais) {
        this.delais = delais;
    }
}
