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
package io.inugami.commons.spi;

import io.inugami.api.exceptions.FatalException;
import io.inugami.api.loggers.Loggers;

/**
 * ClassBuilder
 * 
 * @author patrick_guillerm
 * @since 3 nov. 2017
 */
public class ClassBuilder {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public <T> Class<T> load(final String classFullName) {
        Class<T> result = null;
        if ((classFullName != null) && !classFullName.trim().isEmpty()) {
            try {
                result = (Class<T>) loadClass(classFullName);
            }
            catch (final ClassCastException error) {
                Loggers.DEBUG.error(error.getMessage(), error);
                Loggers.XLLOG.error(error.getMessage());
                throw new FatalException(error.getMessage(), error);
            }
        }
        
        return result;
    }
    
    public <T> T buildInstance(final Class<?> objectClass) {
        try {
            return objectClass == null ? null : (T) objectClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.XLLOG.error(e.getMessage());
            throw new FatalException(e.getMessage(), e);
        }
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private Class<?> loadClass(final String classFullName) {
        try {
            return Class.forName(classFullName);
        }
        catch (final ClassNotFoundException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.XLLOG.error(e.getMessage());
            throw new FatalException(e.getMessage(), e);
        }
    }
    
}
