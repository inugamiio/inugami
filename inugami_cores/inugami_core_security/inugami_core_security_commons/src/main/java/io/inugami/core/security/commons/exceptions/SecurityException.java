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
package io.inugami.core.security.commons.exceptions;

import io.inugami.api.exceptions.TechnicalException;

/**
 * SecurityException
 * 
 * @author patrick_guillerm
 * @since 11 déc. 2017
 */
public class SecurityException extends TechnicalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -2744833901509919836L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SecurityException() {
        super();
    }
    
    public SecurityException(String message, Object... values) {
        super(message, values);
    }
    
    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SecurityException(String message) {
        super(message);
    }
    
    public SecurityException(Throwable cause, String message, Object... values) {
        super(cause, message, values);
    }
    
    public SecurityException(Throwable cause) {
        super(cause);
    }
    
}
