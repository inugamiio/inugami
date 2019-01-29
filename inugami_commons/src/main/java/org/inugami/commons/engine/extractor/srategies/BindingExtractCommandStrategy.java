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
package org.inugami.commons.engine.extractor.srategies;

import javax.script.Bindings;

import org.inugami.commons.engine.extractor.ExtractCommand;
import org.inugami.commons.engine.extractor.ExtractCommandStrategy;

/**
 * CollectiontemExtractCommandStrategy
 * @author patrick_guillerm
 * @since 22 mai 2018
 */
public class BindingExtractCommandStrategy implements ExtractCommandStrategy {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public boolean accept(Object value, ExtractCommand cmd) {
        return value instanceof Bindings;
    }
    
    @Override
    public Object process(Object value, ExtractCommand cmd) {
        Object result = null;
        final Bindings binding = (Bindings) value;
        
        final String fieldName = cmd.getFieldName();
        if (fieldName != null) {
            result = binding.get(fieldName);
        }
        
        return result;
    }
}
