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

/**
 * TechnicalException
 * 
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */
public class ConnectorException extends TechnicalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 389031756408740003L;
    
    // =========================================================================
    // PROTECTED CONSTRUCTORS
    // =========================================================================
    protected ConnectorException(final int code, final String message, final Throwable cause) {
        super(code, message, cause);
    }
    
    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================
    
    public ConnectorException() {
        this(ERR_CODE, null, null);
    }
    
    public ConnectorException(final String message, final Throwable cause) {
        this(ERR_CODE, message, cause);
    }
    
    public ConnectorException(final String message, final Object... values) {
        this(ERR_CODE, MessagesFormatter.format(message, values), null);
    }
    
    public ConnectorException(final Throwable cause, final String message, final Object... values) {
        this(ERR_CODE, MessagesFormatter.format(message, values), cause);
    }
    
    public ConnectorException(final String message) {
        this(ERR_CODE, message, null);
    }
    
    public ConnectorException(final Throwable cause) {
        this(ERR_CODE, null, cause);
    }
    
}
