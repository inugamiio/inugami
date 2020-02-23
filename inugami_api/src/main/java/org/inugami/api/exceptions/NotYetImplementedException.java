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
package org.inugami.api.exceptions;

/**
 * FatalException
 * 
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */
public class NotYetImplementedException extends FatalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2334824783473867813L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public NotYetImplementedException() {
    }

    public NotYetImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotYetImplementedException(String message) {
        super(message);
    }

    public NotYetImplementedException(Throwable cause) {
        super(cause);
    }

    public NotYetImplementedException(String message, Object... values) {
        super(message, values);
    }

    public NotYetImplementedException(Throwable cause, String message, Object... values) {
        super(cause, message, values);
    }

    public NotYetImplementedException(ErrorCode errorCode, Throwable cause, String message, Object... values) {
        super(errorCode, cause, message, values);
    }
}
