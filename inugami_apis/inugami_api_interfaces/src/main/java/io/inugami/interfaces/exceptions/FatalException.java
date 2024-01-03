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
package io.inugami.interfaces.exceptions;

import io.inugami.interfaces.exceptions.ErrorCode;
import io.inugami.interfaces.exceptions.UncheckedException;

/**
 * FatalException
 * 
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */
public class FatalException extends UncheckedException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7204197977074790471L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public FatalException() {
    }

    public FatalException(String message, Throwable cause) {
        super(message, cause);
    }

    public FatalException(String message) {
        super(message);
    }

    public FatalException(Throwable cause) {
        super(cause);
    }

    public FatalException(String message, Object... values) {
        super(message, values);
    }

    public FatalException(Throwable cause, String message, Object... values) {
        super(cause, message, values);
    }

    public FatalException(ErrorCode errorCode, Throwable cause, String message, Object... values) {
        super(errorCode, cause, message, values);
    }

    public FatalException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
