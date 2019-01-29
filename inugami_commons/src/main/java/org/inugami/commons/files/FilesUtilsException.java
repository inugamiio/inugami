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
package org.inugami.commons.files;

import org.inugami.api.exceptions.FatalException;

/**
 * FileUtilsException
 * 
 * @author patrick_guillerm
 * @since 22 juin 2017
 */
public class FilesUtilsException extends FatalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 264782626554715606L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public FilesUtilsException() {
        super();
    }
    
    public FilesUtilsException(final String message, final Object... values) {
        super(message, values);
    }
    
    public FilesUtilsException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public FilesUtilsException(final String message) {
        super(message);
    }
    
    public FilesUtilsException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }
    
    public FilesUtilsException(final Throwable cause) {
        super(cause);
    }
}
