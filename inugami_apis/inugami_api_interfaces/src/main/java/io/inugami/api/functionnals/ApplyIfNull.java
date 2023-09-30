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
package io.inugami.api.functionnals;

import java.util.function.Supplier;

/**
 * Same as <strong>ApplyIfNotNull</strong> but it will invoke the supplier only if the data is null.
 * 
 * @author patrickguillerm
 * @since Jan 14, 2019
 */
public interface ApplyIfNull {
    
    default <T> T applyIfNull(final T data, final Supplier<T> supplier) {
        T result = null;
        if (data == null) {
            result = supplier.get();
        }
        else {
            result = data;
        }
        return result;
    }
    
}