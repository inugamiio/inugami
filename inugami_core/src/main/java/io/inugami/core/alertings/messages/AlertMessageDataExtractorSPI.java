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
package io.inugami.core.alertings.messages;

import java.util.List;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.models.data.basic.Json;

import flexjson.JSONDeserializer;

/**
 * AlertMessageDataExtractorSPI
 * 
 * @author patrick_guillerm
 * @since 22 mars 2018
 */
public interface AlertMessageDataExtractorSPI {
    String UNDEFINE = "---";
    
    boolean accept(String key, AlertingResult alert);
    
    List<String> extract(String key, AlertingResult alert);
    
    default Object getAlertData(final AlertingResult alert) {
        Object result = null;
        
        if ((alert != null) && (alert.getData() != null)) {
            if (alert.getData() instanceof Json) {
                final String json = ((Json) alert.getData()).convertToJson();
                final JSONDeserializer<Object> deserializer = new JSONDeserializer<Object>();
                result = deserializer.deserialize(json);
            }
            else {
                result = alert.getData();
            }
        }
        
        return result;
    }
    
}
