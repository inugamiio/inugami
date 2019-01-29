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
package org.inugami.commons.transformer;

import java.util.Optional;

import org.inugami.api.models.JsonBuilder;

import flexjson.transformer.AbstractTransformer;

/**
 * OptionalTransformer
 * 
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
public class OptionalTransformer extends AbstractTransformer {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {
        Object realObject = null;
        
        if ((object != null) && (object instanceof Optional<?>)) {
            realObject = ((Optional<?>) object).orElse(null);
        }
        
        if (realObject == null) {
            getContext().write(JsonBuilder.VALUE_NULL);
        }
        else {
            getContext().transform(realObject);
        }
    }
    
}
