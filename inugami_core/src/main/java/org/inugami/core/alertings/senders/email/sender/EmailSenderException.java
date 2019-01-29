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
package org.inugami.core.alertings.senders.email.sender;

import org.inugami.core.services.senders.SenderException;

/**
 * EmailSenderException
 * 
 * @author patrick_guillerm
 * @since 16 mars 2018
 */
public class EmailSenderException extends SenderException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 7765413990807838558L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public EmailSenderException() {
        super();
    }
    
    public EmailSenderException(final String message, final Object... values) {
        super(message, values);
    }
    
    public EmailSenderException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public EmailSenderException(final String message) {
        super(message);
    }
    
    public EmailSenderException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }
    
    public EmailSenderException(final Throwable cause) {
        super(cause);
    }
}
