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
package org.inugami.commons.threads;

/**
 * TimeoutTaskException
 * 
 * @author patrickguillerm
 * @since 25 mars 2018
 */
public class TimeoutTaskException extends Exception {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 4991745549592164932L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public TimeoutTaskException() {
        super();
    }
    
    public TimeoutTaskException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public TimeoutTaskException(final String message) {
        super(message);
    }
    
    public TimeoutTaskException(final Throwable cause) {
        super(cause);
    }
}
