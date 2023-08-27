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
package io.inugami.commons.threads;

import java.util.concurrent.Callable;

/**
 * CallableTimeout
 * 
 * @author patrickguillerm
 * @since 24 mars 2018
 */
public interface CallableWithErrorResult<V> extends Callable<V> {
    default V getTimeoutResult() {
        return null;
    }
    
    default V getErrorResult(final Exception error) {
        return null;
    }
}
