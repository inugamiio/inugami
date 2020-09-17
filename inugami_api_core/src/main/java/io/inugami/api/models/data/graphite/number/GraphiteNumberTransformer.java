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
package io.inugami.api.models.data.graphite.number;

import io.inugami.api.models.JsonBuilder;

import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Transformer;

/**
 * GraphiteNumberTransformer
 * 
 * @author patrick_guillerm
 * @since 30 nov. 2018
 */
public class GraphiteNumberTransformer extends AbstractTransformer implements Transformer {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {
        if (object == null) {
            getContext().write(JsonBuilder.VALUE_NULL);
        }
        else if (object instanceof GraphiteNumber) {
            getContext().write(((GraphiteNumber) object).rendering());
        }
        
    }
    
}
