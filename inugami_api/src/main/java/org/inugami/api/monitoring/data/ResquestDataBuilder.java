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
package org.inugami.api.monitoring.data;

import java.util.Map;

/**
 * ResquestDataBuilder
 * 
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
public class ResquestDataBuilder {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String              method;
    
    private String              contentType;
    
    private String              uri;
    
    private String              contextPath;
    
    private String              content;
    
    private Map<String, String> hearder;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ResquestData build() {
        return new ResquestData(method, contentType, uri, contextPath, content, hearder);
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public ResquestDataBuilder setMethod(String method) {
        this.method = method;
        return this;
    }
    
    public ResquestDataBuilder setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
    
    public ResquestDataBuilder setUri(String uri) {
        this.uri = uri;
        return this;
    }
    
    public ResquestDataBuilder setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }
    
    public ResquestDataBuilder setContent(String content) {
        this.content = content;
        return this;
    }
    
    public ResquestDataBuilder setHearder(Map<String, String> hearder) {
        this.hearder = hearder;
        return this;
    }
    
}
