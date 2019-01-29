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
package org.inugami.monitoring.api.data;

import java.io.Serializable;

import org.inugami.api.models.ClonableObject;

/**
 * ResponseData
 * 
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
public class ResponseData implements Serializable, ClonableObject<ResponseData> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 2949186095649126700L;
    
    private final long        duration;
    
    private final long        datetime;
    
    private final int         code;
    
    private final String      content;
    
    private final String      contentType;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ResponseData(int code, String content, String contentType, long duration, long datetime) {
        super();
        this.duration = duration;
        this.datetime = datetime;
        this.code = code;
        this.content = content;
        this.contentType = contentType;
    }
    
    @Override
    public ResponseData cloneObj() {
        return new ResponseData(code, content, contentType, duration, datetime);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        //@formatter:off
        StringBuilder builder = new StringBuilder();
        builder.append("ResponseData [code=").append(code)
               .append(", contentType=").append(contentType)
               .append(", hasContent=").append(content!=null && !content.isEmpty())
               .append("]");
        //@formatter:on
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public int getCode() {
        return code;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public long getDatetime() {
        return datetime;
    }
    
}
