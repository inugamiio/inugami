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
package io.inugami.configuration.exceptions;

import io.inugami.api.exceptions.FatalException;

/**
 * ConfigurationException
 * 
 * @author patrick_guillerm
 * @since 15 déc. 2017
 */
public class FatalConfigurationException extends FatalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -7499333227245366881L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public FatalConfigurationException() {
        super();
    }
    
    public FatalConfigurationException(final String message, final Object... values) {
        super(message, values);
    }
    
    public FatalConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public FatalConfigurationException(final String message) {
        super(message);
    }
    
    public FatalConfigurationException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }
    
    public FatalConfigurationException(final Throwable cause) {
        super(cause);
    }
    
}
