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
public class FatalException extends RuntimeException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7204197977074790471L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public FatalException() {
        super();
    }
    
    public FatalException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public FatalException(final String message) {
        super(message);
    }
    
    public FatalException(final Throwable cause) {
        super(cause);
    }
    
    public FatalException(final String message, final Object... values) {
        super(MessagesFormatter.format(message, values));
    }
    
    public FatalException(final Throwable cause, final String message, final Object... values) {
        super(MessagesFormatter.format(message, values), cause);
    }
    
}
