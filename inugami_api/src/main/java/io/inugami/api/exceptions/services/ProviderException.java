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
package io.inugami.api.exceptions.services;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.MessagesFormatter;
import io.inugami.api.exceptions.TechnicalException;

/**
 * ProcessorException
 * 
 * @author patrick_guillerm
 * @since 6 oct. 2016
 */
public class ProviderException extends TechnicalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4976777345663055137L;

  
    

    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================
    public ProviderException() {
        super();
    }

    public ProviderException(ErrorCode errorCode, Throwable cause, String message, Object... values) {
        super(errorCode, cause, message, values);
    }

    public ProviderException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ProviderException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ProviderException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ProviderException(String message, Object... values) {
        super(message, values);
    }

    public ProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProviderException(String message) {
        super(message);
    }

    public ProviderException(Throwable cause, String message, Object... values) {
        super(cause, message, values);
    }

    public ProviderException(Throwable cause) {
        super(cause);
    }
    
}
