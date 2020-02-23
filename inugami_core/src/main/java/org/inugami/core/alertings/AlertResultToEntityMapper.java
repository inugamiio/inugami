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
import org.inugami.api.mapping.Mapper;
import org.inugami.api.models.data.basic.JsonObject;
import org.inugami.api.models.data.basic.StringJson;

import flexjson.JSONSerializer;

/**
 * AlertResultToEntity
 * 
 * @author patrick_guillerm
 * @since 7 mars 2018
 */
public class AlertResultToEntityMapper implements Mapper<AlertEntity, AlertingResult> {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public AlertEntity mapping(final AlertingResult data) {
        final AlertEntity entity = new AlertEntity(data.getAlerteName(), data.getLevel());
        entity.setLevelType(data.getLevelType());
        entity.setLevelNumber(data.getLevelNumber());
        entity.setLabel(data.getMessage());
        entity.setSubLabel(data.getSubLabel());
        entity.setDuration(data.getDuration());
        entity.setData(convertToJson(data.getData()));
        entity.setChannel(data.getChannel());
        entity.setCreated(data.getCreated());
        entity.setUrl(data.getUrl());
        entity.setTtl(data.getCreated() + (data.getDuration() * 1000));
        
        if (data.getProviders() != null) {
            entity.setProviders(new ArrayList<>(data.getProviders()));
        }
        
        return entity;
    }
    
    private String convertToJson(final Object data) {
        String result = null;
        if (data != null) {
            if (data instanceof String) {
                result = new StringJson((String) data).convertToJson();
            }
            else if (data instanceof JsonObject) {
                result = ((JsonObject) data).convertToJson();
            }
            else {
                result = new JSONSerializer().exclude("*.class").deepSerialize(data);
            }
        }
        return result;
    }
    
}
