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
package io.inugami.api.models.data;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import io.inugami.api.models.data.basic.JsonObject;

import flexjson.JSONSerializer;
import flexjson.JsonNumber;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

/**
 * JsonObjectFactory
 * 
 * @author patrick_guillerm
 * @since 9 mars 2018
 */
public class JsonObjectStrFactory extends AbstractTransformer implements ObjectFactory {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {
        if (object instanceof JsonObject) {
            getContext().write(((JsonObject) object).convertToJson());
        }
    }
    
    @Override
    public Object instantiate(final ObjectBinder context, final Object value, final Type targetType,
                              final Class targetClass) {
        //@formatter:off
        return new JSONSerializer()
                    .exclude("*.class")
                    .transform(new JsonNumberTransformer(), JsonNumber.class)
                    .deepSerialize(value);
        //@formatter:on
    }
    
    private class JsonNumberTransformer extends AbstractTransformer implements ObjectFactory {
        
        @Override
        public void transform(final Object object) {
            if (object instanceof JsonNumber) {
                final BigDecimal number = ((JsonNumber) object).toBigDecimal();
                getContext().write(String.valueOf(number));
            }
            else {
                getContext().write("0");
            }
            
        }
        
        @Override
        public Object instantiate(final ObjectBinder context, final Object value, final Type targetType,
                                  final Class targetClass) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
}
