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
package io.inugami.api.dao;

import io.inugami.api.exceptions.TechnicalException;

/**
 * DaoException
 * 
 * @author patrick_guillerm
 * @since 9 janv. 2018
 */
public class DaoException extends TechnicalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 1437325562162419252L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DaoException() {
        super();
    }
    
    public DaoException(final String message, final Object... values) {
        super(message, values);
    }
    
    public DaoException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DaoException(final String message) {
        super(message);
    }
    
    public DaoException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }
    
    public DaoException(final Throwable cause) {
        super(cause);
    }
    
}
