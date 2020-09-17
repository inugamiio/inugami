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
package io.inugami.commons.engine.js.objects;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import flexjson.ObjectBinder;
import flexjson.factories.ListObjectFactory;

/**
 * JsListFactory
 * 
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
public class JsListFactory extends ListObjectFactory {
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public Object instantiate(final ObjectBinder context, final Object value, final Type targetType,
                              final Class targetClass) {
        if (value instanceof Collection) {
            return context.bindIntoCollection((Collection) value, new JsList(), targetType);
        }
        else {
            final ArrayList<Object> set = new ArrayList<Object>();
            set.add(context.bind(value));
            return set;
        }
    }
}
