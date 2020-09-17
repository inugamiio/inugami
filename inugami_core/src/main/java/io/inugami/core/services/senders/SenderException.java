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
package io.inugami.core.services.senders;

import io.inugami.api.exceptions.TechnicalException;

/**
 * SenderException
 * 
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
public class SenderException extends TechnicalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -9029154618744950588L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SenderException() {
        super();
    }
    
    public SenderException(final String message, final Object... values) {
        super(message, values);
    }
    
    public SenderException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public SenderException(final String message) {
        super(message);
    }
    
    public SenderException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }
    
    public SenderException(final Throwable cause) {
        super(cause);
    }
    
}
