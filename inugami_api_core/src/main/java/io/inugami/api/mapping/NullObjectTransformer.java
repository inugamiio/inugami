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
package io.inugami.api.mapping;

import java.lang.reflect.Type;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

/**
 * OnboardServiceTransformer
 * 
 * @author patrick_guillerm
 * @since 6 déc. 2017
 */
public class NullObjectTransformer extends AbstractTransformer implements ObjectFactory {
    
    @Override
    public void transform(final Object object) {
    }
    
    @Override
    public Object instantiate(final ObjectBinder context, final Object value, final Type targetType,
                              final Class targetClass) {
        return null;
    }
    
}
