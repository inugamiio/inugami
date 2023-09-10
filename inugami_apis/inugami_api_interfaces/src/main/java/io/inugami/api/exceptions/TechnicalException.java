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
package io.inugami.api.exceptions;

/**
 * TechnicalException
 * 
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */
public class TechnicalException extends CheckedException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 389031756408740003L;

    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================
    public TechnicalException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public TechnicalException() {
    }

    public TechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public TechnicalException(String message, Object... values) {
        super(message, values);
    }

    public TechnicalException(Throwable cause, String message, Object... values) {
        super(cause, message, values);
    }

    public TechnicalException(String message) {
        super(message);
    }

    public TechnicalException(Throwable cause) {
        super(cause);
    }

    public TechnicalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TechnicalException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public TechnicalException(ErrorCode errorCode, Throwable cause, String message, Object... values) {
        super(errorCode, cause, message, values);
    }
}
