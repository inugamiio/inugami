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
 * TechnicalException
 * 
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */
public class TechnicalException extends Exception {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 389031756408740003L;
    
    public static final int   ERR_CODE         = 500;
    
    private final int         code;
    
    // =========================================================================
    // PROTECTED CONSTRUCTORS
    // =========================================================================
    protected TechnicalException(final int code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================
    
    public TechnicalException() {
        this(ERR_CODE, null, null);
    }
    
    public TechnicalException(final String message, final Throwable cause) {
        this(ERR_CODE, message, cause);
    }
    
    public TechnicalException(final String message, final Object... values) {
        this(ERR_CODE, MessagesFormatter.format(message, values), null);
    }
    
    public TechnicalException(final Throwable cause, final String message, final Object... values) {
        this(ERR_CODE, MessagesFormatter.format(message, values), cause);
    }
    
    public TechnicalException(final String message) {
        this(ERR_CODE, message, null);
    }
    
    public TechnicalException(final Throwable cause) {
        this(ERR_CODE, null, cause);
    }
    
    // =========================================================================
    // CODE
    // =========================================================================
    public int getCode() {
        return code;
    }
    
}
