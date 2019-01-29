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
package org.inugami.core.providers.jira.models;

import java.lang.reflect.Type;
import java.util.Map;

import org.inugami.api.loggers.Loggers;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class CustomFieldsFactory implements ObjectFactory {
    private final Class<?> customClass;
    
    public CustomFieldsFactory(final Class<?> customClass) {
        this.customClass = customClass;
    }
    
    @Override
    public Object instantiate(final ObjectBinder context, final Object value, final Type targetType,
                              final Class targetClass) {
        final IssueFields result = new IssueFields();
        Object instance = null;
        
        try {
            instance = this.customClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {
            Loggers.DEBUG.error("{} : \n customClass:\n----------\n{}\n----------\n", e.getMessage(), this.customClass);
            Loggers.PROVIDER.error(e.getMessage());
        }
        
        if ((value instanceof Map) && (instance != null)) {
            final Object newCustomObject = context.bindIntoObject((Map) value, instance, targetType);
            result.setCustomFields(newCustomObject);
        }
        else {
            result.setCustomFields(value);
        }
        
        return result;
    }
}
