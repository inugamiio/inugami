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
package io.inugami.configuration.services.resolver;

import io.inugami.api.exceptions.TechnicalException;

/**
 * ConfigurationResolverException.
 *
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
public class ConfigurationResolverException extends TechnicalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3605938183100057907L;
    
    // =========================================================================
    // CONSTRUCTORS
    /**
     * Instantiates a new configuration resolver exception.
     */
    // =========================================================================
    public ConfigurationResolverException() {
        super();
    }
    
    /**
     * Instantiates a new configuration resolver exception.
     *
     * @param message the message
     * @param values the values
     */
    public ConfigurationResolverException(final String message, final Object... values) {
        super(message, values);
    }
    
    /**
     * Instantiates a new configuration resolver exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public ConfigurationResolverException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Instantiates a new configuration resolver exception.
     *
     * @param message the message
     */
    public ConfigurationResolverException(final String message) {
        super(message);
    }
    
    /**
     * Instantiates a new configuration resolver exception.
     *
     * @param cause the cause
     * @param message the message
     * @param values the values
     */
    public ConfigurationResolverException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }
    
    /**
     * Instantiates a new configuration resolver exception.
     *
     * @param cause the cause
     */
    public ConfigurationResolverException(final Throwable cause) {
        super(cause);
    }
    
}
