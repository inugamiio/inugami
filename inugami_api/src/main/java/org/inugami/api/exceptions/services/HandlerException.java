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
package org.inugami.api.exceptions.services;

import org.inugami.api.exceptions.MessagesFormatter;
import org.inugami.api.exceptions.TechnicalException;

public class HandlerException extends TechnicalException {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -6414566846460005415L;
    
    // =========================================================================
    // PROTECTED CONSTRUCTORS
    // =========================================================================
    protected HandlerException(final int code, final String message, final Throwable cause) {
        super(code, message, cause);
    }
    
    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================
    public HandlerException() {
        this(ERR_CODE, null, null);
    }
    
    public HandlerException(final String message, final Throwable cause) {
        this(ERR_CODE, message, cause);
    }
    
    public HandlerException(final String message, final Object... values) {
        this(ERR_CODE, MessagesFormatter.format(message, values), null);
    }
    
    public HandlerException(final Throwable cause, final String message, final Object... values) {
        this(ERR_CODE, MessagesFormatter.format(message, values), cause);
    }
    
    public HandlerException(final String message) {
        this(ERR_CODE, message, null);
    }
    
    public HandlerException(final Throwable cause) {
        this(ERR_CODE, null, cause);
    }
}
