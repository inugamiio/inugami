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
import java.util.function.Function;

import javax.script.Bindings;

import io.inugami.api.functionnals.ApplyIfNotNullAndSameType;
import io.inugami.api.mapping.Mapper;
import io.inugami.api.processors.ProcessorModel;

/**
 * ListProcessorMapper
 * 
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
public class ListProcessorMapper implements Mapper<List<ProcessorModel>, List<Object>>, ApplyIfNotNullAndSameType {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public List<ProcessorModel> mapping(final List<Object> data) {
        final List<ProcessorModel> result = new ArrayList<>();
        
        for (final Object obj : data) {
            if ((obj != null) && (obj instanceof Bindings)) {
                result.add(convertToProcessorModel((Bindings) obj));
            }
        }
        
        return result;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    private ProcessorModel convertToProcessorModel(final Bindings obj) {
        final ProcessorModel result = new ProcessorModel();
        final Function<Object, String> stringMapper = buildStringMapper();
        ifNotNullAndSameType(obj.get("name"), stringMapper, result::setName);
        ifNotNullAndSameType(obj.get("className"), stringMapper, result::setName);
        return result;
    }
    
}
