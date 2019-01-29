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
package org.inugami.api.mapping.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.script.Bindings;

import org.inugami.api.functionnals.ApplyIfNotNullAndSameType;
import org.inugami.api.mapping.Mapper;
import org.inugami.api.models.events.AlertingModel;

/**
 * ListAlertsMapper
 * 
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
public class ListAlertsMapper implements Mapper<List<AlertingModel>, List<Object>>, ApplyIfNotNullAndSameType {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public List<AlertingModel> mapping(final List<Object> data) {
        final List<AlertingModel> result = new ArrayList<>();
        
        for (final Object obj : data) {
            if ((obj != null) && (obj instanceof Bindings)) {
                result.add(convertToModel((Bindings) obj));
            }
        }
        
        return result;
    }
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    private AlertingModel convertToModel(final Bindings obj) {
        final AlertingModel result = new AlertingModel();
        final Function<Object, String> stringMapper = buildStringMapper();
        //@formatter:off
        ifNotNullAndSameType(obj.get("name")     , stringMapper, result::setName);
        ifNotNullAndSameType(obj.get("provider") , stringMapper, result::setProvider);
        ifNotNullAndSameType(obj.get("message")  , stringMapper, result::setMessage);
        ifNotNullAndSameType(obj.get("level")    , stringMapper, result::setLevel);
        ifNotNullAndSameType(obj.get("condition"), stringMapper, result::setCondition);
        ifNotNullAndSameType(obj.get("function") , stringMapper, result::setFunction);
        //@formatter:on        
        return result;
    }
}
