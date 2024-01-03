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
package io.inugami.interfaces.exceptions.services;

import io.inugami.interfaces.exceptions.ErrorCode;
import io.inugami.interfaces.exceptions.TechnicalException;

public class HandlerException extends TechnicalException {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -6414566846460005415L;

    
   
    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================
    public HandlerException() {
        super();
    }

    public HandlerException(ErrorCode errorCode, Throwable cause, String message, Object... values) {
        super(errorCode, cause, message, values);
    }

    public HandlerException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public HandlerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public HandlerException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public HandlerException(String message, Object... values) {
        super(message, values);
    }

    public HandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerException(String message) {
        super(message);
    }

    public HandlerException(Throwable cause, String message, Object... values) {
        super(cause, message, values);
    }

    public HandlerException(Throwable cause) {
        super(cause);
    }
    
    
}
