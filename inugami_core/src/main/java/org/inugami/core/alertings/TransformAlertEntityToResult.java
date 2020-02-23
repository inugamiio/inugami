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
package org.inugami.core.alertings;

import java.util.ArrayList;

import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.functionnals.ApplyIfNotNull;
import org.inugami.api.mapping.Mapper;
import org.inugami.api.models.JsonBuilder;
import org.inugami.api.models.data.basic.Json;
import org.inugami.api.models.data.basic.JsonObject;

/**
 * TransformAlertEntityToResult
 * 
 * @author patrickguillerm
 * @since 20 janv. 2018
 */
public class TransformAlertEntityToResult implements Mapper<AlertingResult, AlertEntity>, ApplyIfNotNull {
    
    // =========================================================================
    // MAPPING AlertEntity -> AlertingResult
    // =========================================================================
    @Override
    public AlertingResult mapping(final AlertEntity data) {
        return data == null ? null : processMapping(data);
    }
    
    private AlertingResult processMapping(final AlertEntity data) {
        final AlertingResult result = new AlertingResult();
        //@formatter:off
        applyIfNotNull(data.getAlerteName(), result::setAlerteName);
        applyIfNotNull(data.getChannel(),    result::setChannel);
        applyIfNotNull(data.getLevel(),      result::setLevel);
        applyIfNotNull(data.getLabel(),      result::setMessage);
        applyIfNotNull(data.getSubLabel(),   result::setSubLabel);
        applyIfNotNull(data.getUrl(),        result::setUrl);
        applyIfNotNull(data.getData(),       (value)-> result.setData(buildData(value)));
        applyIfNotNull(data.getProviders(),  (value)-> result.setProviders(new ArrayList<>(value)));
        //@formatter:on
        
        result.setCreated(data.getCreated());
        result.setDuration(data.getDuration());
        
        return result;
    }
    
    private JsonObject buildData(final String value) {
        String json = value == null ? JsonBuilder.VALUE_NULL : value.trim();
        if (json.startsWith("\"")) {
            json = json.substring(1);
        }
        if (json.endsWith("\"")) {
            json = json.substring(0, json.length() - 1);
        }
        
        return new Json(json);
    }
    
}
