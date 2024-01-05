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
package io.inugami.framework.interfaces.tools.strategy;

import io.inugami.framework.interfaces.exceptions.TechnicalException;

/**
 * StrategyException
 * 
 * @author patrick_guillerm
 * @since 26 mai 2017
 */
public class StrategyException extends TechnicalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7892157507482242165L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public StrategyException() {
        super();
    }
    
    public StrategyException(final String message, final Object... values) {
        super(message, values);
    }
    
    public StrategyException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public StrategyException(final String message) {
        super(message);
    }
    
    public StrategyException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }
    
    public StrategyException(final Throwable cause) {
        super(cause);
    }
    
}
