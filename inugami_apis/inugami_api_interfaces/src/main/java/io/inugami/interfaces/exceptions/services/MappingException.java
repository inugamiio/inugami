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
package io.inugami.interfaces.exceptions.services;

import io.inugami.interfaces.exceptions.TechnicalException;

/**
 * MappingException
 * 
 * @author patrick_guillerm
 * @since 19 janv. 2017
 */
public class MappingException extends TechnicalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8935788400204039487L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MappingException() {
        super();
    }
    
    public MappingException(final String message, final Object... values) {
        super(message, values);
    }
    
    public MappingException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public MappingException(final String message) {
        super(message);
    }
    
    public MappingException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }
    
    public MappingException(final Throwable cause) {
        super(cause);
    }
    
}
