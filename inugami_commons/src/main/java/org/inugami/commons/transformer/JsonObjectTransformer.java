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

import org.inugami.api.models.data.basic.JsonObject;

import flexjson.JSONContext;
import flexjson.SerializationType;
import flexjson.transformer.AbstractTransformer;

/**
 * JsonObjectTransformer
 * 
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
public class JsonObjectTransformer extends AbstractTransformer {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {
        final JSONContext ctx = getContext();
        if (object == null) {
            getContext().write("null");
        }
        else if (object instanceof JsonObject) {
            final String json = ((JsonObject) object).convertToJson();
            ctx.serializationType(SerializationType.SHALLOW);
            ctx.write(json);
        }
    }
}
