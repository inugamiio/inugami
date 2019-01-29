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
package org.inugami.commons.connectors;

import java.net.MalformedURLException;
import java.net.URL;

import org.inugami.api.exceptions.FatalException;
import org.inugami.api.models.Builder;

/**
 * HttpConnectorResultBuilder
 * 
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
public class HttpConnectorResultBuilder implements Builder<HttpConnectorResult> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private URL    url;
    
    private String verbe;
    
    private byte[] data;
    
    private String contenType;
    
    private String jsonInputData;
    
    private long   begin = 0L;
    
    private int    statusCode;
    
    private String encoding;
    
    private String message;
    
    private long   responseAt;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public HttpConnectorResultBuilder() {
        super();
    }
    
    public HttpConnectorResultBuilder(final byte[] data) {
        this.data = data;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public HttpConnectorResultBuilder addUrl(final URL url) {
        this.url = url;
        return this;
    }
    
    public HttpConnectorResultBuilder addVerbe(final String verbe) {
        this.verbe = verbe;
        return this;
    }
    
    public HttpConnectorResultBuilder addData(final byte[] data) {
        this.data = data;
        return this;
    }
    
    public HttpConnectorResultBuilder addContenType(final String contenType) {
        this.contenType = contenType;
        return this;
    }
    
    public HttpConnectorResultBuilder addBegin() {
        begin = System.currentTimeMillis();
        return this;
    }
    
    public HttpConnectorResultBuilder addResponseAt() {
        responseAt = System.currentTimeMillis();
        return this;
    }
    
    public HttpConnectorResultBuilder addMessage(final String message) {
        this.message = message;
        return this;
    }
    
    public HttpConnectorResultBuilder addStatusCode(final int statusCode) {
        this.statusCode = statusCode;
        return this;
    }
    
    public HttpConnectorResultBuilder addJsonInputData(final String jsonInputData) {
        this.jsonInputData = jsonInputData;
        return this;
    }
    
    public HttpConnectorResultBuilder addEncoding(final String encoding) {
        this.encoding = encoding;
        return this;
    }
    
    @Override
    public HttpConnectorResult build() {
        return new HttpConnectorResult(url, verbe, jsonInputData, data, contenType, statusCode, message,
                                       responseAt - begin, responseAt, encoding);
    }
    
    public void addUrl(final String url) {
        try {
            this.url = new URL(url);
        }
        catch (final MalformedURLException e) {
            throw new FatalException(e.getMessage(), e);
        }
        
    }
    
}
