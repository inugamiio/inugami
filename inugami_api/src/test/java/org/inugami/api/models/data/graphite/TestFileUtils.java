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
package org.inugami.api.models.data.graphite;

import java.io.IOException;
import java.io.InputStream;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.TechnicalException;

import sun.misc.IOUtils;

/**
 * FileUtils
 * 
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
public class TestFileUtils {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public byte[] readFromClassLoader(final String resourceName) throws TechnicalException {
        Asserts.notNull(resourceName);
        byte[] result = null;
        final InputStream resource = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        if (resource == null) {
            throw new FileUtilsException("can't found file {0} in classPath", resourceName);
        }
        
        try {
            result = IOUtils.readFully(resource, -1, true);
        }
        catch (final IOException e) {
            throw new FileUtilsException(e.getMessage(), e);
        }
        
        return result;
    }
    
    // =========================================================================
    // EXCEPTION
    // =========================================================================
    public class FileUtilsException extends TechnicalException {
        
        private static final long serialVersionUID = 2994314231451872696L;
        
        public FileUtilsException(final String message, final Object... values) {
            super(message, values);
        }
        
        public FileUtilsException(final String message, final Throwable cause) {
            super(message, cause);
        }
        
    }
    
}
