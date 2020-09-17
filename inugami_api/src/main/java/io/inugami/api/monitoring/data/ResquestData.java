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
package io.inugami.api.monitoring.data;

import java.util.Map;

/**
 * ResquestData
 * 
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
public class ResquestData {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String              method;
    
    private final String              contentType;
    
    private final String              uri;
    
    private final String              contextPath;
    
    private final String              content;
    
    private final Map<String, String> hearder;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ResquestData(String method, String contentType, String uri, String contextPath, String content,
                        Map<String, String> hearder) {
        super();
        this.method = method;
        this.contentType = contentType;
        this.uri = uri;
        this.contextPath = contextPath;
        this.content = content;
        this.hearder = hearder;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ResquestData [method=");
        builder.append(method);
        builder.append(", contentType=").append(contentType);
        builder.append(", uri=").append(uri);
        builder.append(", contextPath=").append(contextPath);
        builder.append(", hasContent=").append(content != null && !content.isEmpty());
        
        builder.append(", hearder=");
        if (hearder == null) {
            builder.append("null");
        }
        else {
            builder.append('[');
            for (Map.Entry<String, String> entry : hearder.entrySet()) {
                builder.append('{');
                builder.append(entry.getKey());
                builder.append('=');
                builder.append(entry.getValue());
                builder.append("},");
            }
            builder.append(']');
        }
        
        builder.append("]");
        return builder.toString();
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getMethod() {
        return method;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public String getUri() {
        return uri;
    }
    
    public String getContextPath() {
        return contextPath;
    }
    
    public String getContent() {
        return content;
    }
    
    public Map<String, String> getHearder() {
        return hearder;
    }
    
}
