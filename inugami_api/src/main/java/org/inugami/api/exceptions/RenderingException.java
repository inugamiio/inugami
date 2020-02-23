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
 * RenderingException
 * 
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
public class RenderingException extends TechnicalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -6981840418424439927L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public RenderingException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public RenderingException() {
    }

    public RenderingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RenderingException(String message, Object... values) {
        super(message, values);
    }

    public RenderingException(Throwable cause, String message, Object... values) {
        super(cause, message, values);
    }

    public RenderingException(String message) {
        super(message);
    }

    public RenderingException(Throwable cause) {
        super(cause);
    }

    public RenderingException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RenderingException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public RenderingException(ErrorCode errorCode, Throwable cause, String message, Object... values) {
        super(errorCode, cause, message, values);
    }
}
