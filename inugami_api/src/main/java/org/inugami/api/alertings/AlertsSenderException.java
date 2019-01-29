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
package org.inugami.api.alertings;

import org.inugami.api.exceptions.TechnicalException;

/**
 * AlertsSenderException
 * 
 * @author patrickguillerm
 * @since 20 janv. 2018
 */
public class AlertsSenderException extends TechnicalException {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -4732187457495657430L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public AlertsSenderException() {
        super();
    }
    
    public AlertsSenderException(final String message, final Object... values) {
        super(message, values);
    }
    
    public AlertsSenderException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public AlertsSenderException(final String message) {
        super(message);
    }
    
    public AlertsSenderException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }
    
    public AlertsSenderException(final Throwable cause) {
        super(cause);
    }
}
