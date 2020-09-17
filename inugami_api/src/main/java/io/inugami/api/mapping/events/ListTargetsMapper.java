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
package io.inugami.api.mapping.events;

import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;

import io.inugami.api.functionnals.ApplyIfNotNullAndSameType;
import io.inugami.api.mapping.Mapper;
import io.inugami.api.models.events.TargetConfig;

/**
 * ListTargetsMapper
 * 
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
public class ListTargetsMapper implements Mapper<List<TargetConfig>, List<Object>>, ApplyIfNotNullAndSameType {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final TargetMapper targetMapper = new TargetMapper();
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public List<TargetConfig> mapping(final List<Object> data) {
        final List<TargetConfig> result = new ArrayList<>();
        
        for (final Object obj : data) {
            if ((obj != null) && (obj instanceof Bindings)) {
                final TargetConfig value = convertToModel(obj);
                if (value != null) {
                    result.add(value);
                }
            }
        }
        
        return result;
    }
    
    private TargetConfig convertToModel(final Object obj) {
        return targetMapper.mapping(obj);
    }
    
}
