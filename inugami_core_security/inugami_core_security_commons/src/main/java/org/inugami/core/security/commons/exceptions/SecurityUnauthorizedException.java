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
package org.inugami.core.security.commons.exceptions;

import org.inugami.api.exceptions.TechnicalException;

/**
 * SecurityUnauthorizedException
 * 
 * @author patrick_guillerm
 * @since 14 déc. 2017
 */
public class SecurityUnauthorizedException extends TechnicalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -7950414715942578207L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SecurityUnauthorizedException() {
        super();
    }
    
    public SecurityUnauthorizedException(String message, Object... values) {
        super(message, values);
    }
    
    public SecurityUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SecurityUnauthorizedException(String message) {
        super(message);
    }
    
    public SecurityUnauthorizedException(Throwable cause, String message, Object... values) {
        super(cause, message, values);
    }
    
    public SecurityUnauthorizedException(Throwable cause) {
        super(cause);
    }
    
}
