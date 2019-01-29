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

import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * HttpConnectorResult
 * 
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
public class HttpConnectorResult implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -576629010706862497L;
    
    private final String      hashHumanReadable;
    
    private final String      jsonInputData;
    
    private final int         statusCode;
    
    private final String      message;
    
    private final int         hashCode;
    
    private final byte[]      data;
    
    private final int         length;
    
    private final String      contentType;
    
    private final long        responseAt;
    
    private final long        delais;
    
    private final String      encoding;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    /* package */ HttpConnectorResult(final URL url, final String verbe, final String jsonInputData, final byte[] data,
                                      final String contentType, final int statusCode, final String message,
                                      final long delais, final long responseAt, final String encoding) {
        super();
        this.data = data;
        this.jsonInputData = jsonInputData;
        this.contentType = contentType;
        this.length = data == null ? 0 : data.length;
        this.responseAt = responseAt;
        this.delais = delais;
        this.statusCode = statusCode;
        this.message = message;
        this.encoding = encoding == null ? "UTF-8" : encoding;
        //@formatter:off
        final StringBuilder hashBuilder = new StringBuilder()
                                              .append('[').append(verbe).append(']')
                                              .append(url);
        //@formatter:on
        
        if (jsonInputData != null) {
            hashBuilder.append("?data=").append(jsonInputData.replaceAll("\\n", ""));
        }
        
        this.hashHumanReadable = hashBuilder.toString();
        hashCode = this.hashHumanReadable.hashCode();
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return hashCode;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && (obj != null) && (obj instanceof HttpConnectorResult)) {
            final HttpConnectorResult other = (HttpConnectorResult) obj;
            result = hashHumanReadable.equals(other.getHashHumanReadable());
        }
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("HttpConnectorResult [hashHumanReadable=");
        builder.append(hashHumanReadable);
        builder.append(", jsonInputData=");
        builder.append(jsonInputData);
        builder.append(", statusCode=");
        builder.append(statusCode);
        builder.append(", message=");
        builder.append(message);
        builder.append(", hashCode=");
        builder.append(hashCode);
        builder.append(", data=");
        builder.append(isEmpty() ? "no data" : "has data");
        builder.append(", length=");
        builder.append(length);
        builder.append(", contenType=");
        builder.append(contentType);
        builder.append(", responseAt=");
        builder.append(responseAt);
        builder.append(", delais=");
        builder.append(delais);
        builder.append(", encoding=");
        builder.append(encoding);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS
    // =========================================================================
    public boolean isEmpty() {
        return (data == null) || (length == 0);
    }
    
    public byte[] getData() {
        return data == null ? new byte[] {} : data;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public String getHashHumanReadable() {
        return hashHumanReadable;
    }
    
    public int getLength() {
        return length;
    }
    
    public long getResponseAt() {
        return responseAt;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getMessage() {
        return message;
    }
    
    public long getDelais() {
        return delais;
    }
    
    public String getEncoding() {
        return encoding;
    }
    
    public Charset getCharset() {
        return Charset.forName(this.encoding);
    }
    
    public String getJsonInputData() {
        return jsonInputData;
    }
    
}
