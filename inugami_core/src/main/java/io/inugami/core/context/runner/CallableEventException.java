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
package io.inugami.core.context.runner;

import io.inugami.api.exceptions.FatalException;

/**
 * CallableEventException
 * 
 * @author patrick_guillerm
 * @since 9 août 2017
 */
final class CallableEventException extends FatalException {
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private static final long serialVersionUID = -8590497932459056234L;
    
    public CallableEventException(final String message, final Object... values) {
        super(message, values);
    }
    
    public CallableEventException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
