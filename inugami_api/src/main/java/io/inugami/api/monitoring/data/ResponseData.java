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

import java.io.Serializable;
import java.util.Map;

import io.inugami.api.models.ClonableObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * ResponseData
 * 
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@Getter
public class ResponseData implements Serializable, ClonableObject<ResponseData> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 2949186095649126700L;
    
    private final long        duration;
    
    private final long        datetime;

    @ToString.Include
    private final int         code;
    
    private final String      content;
    @ToString.Include
    private final String      contentType;

    private Map<String, String> hearder;

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
        return toBuilder().build();
    }
    

    
}
