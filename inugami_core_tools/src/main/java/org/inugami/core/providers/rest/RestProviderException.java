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
package org.inugami.core.providers.rest;

import org.inugami.api.exceptions.services.ProviderException;

/**
 * RestProviderException
 * 
 * @author patrick_guillerm
 * @since 9 mai 2017
 */
public class RestProviderException extends ProviderException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -219425270644281236L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public RestProviderException() {
        super();
    }
    
    public RestProviderException(final String message, final Object... values) {
        super(message, values);
    }
    
    public RestProviderException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public RestProviderException(final String message) {
        super(message);
    }
    
    public RestProviderException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }
    
    public RestProviderException(final Throwable cause) {
        super(cause);
    }
}
