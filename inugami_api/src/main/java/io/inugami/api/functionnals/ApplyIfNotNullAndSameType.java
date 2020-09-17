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

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ApplyIfNotNull
 * 
 * @author patrick_guillerm
 * @since 15 janv. 2018
 */
public interface ApplyIfNotNullAndSameType {
    default <T, R> void ifNotNullAndSameType(final T data, final Function<? super T, ? extends R> mapper,
                                             final Consumer<R> consumer) {
        if (data != null) {
            final R typedData = mapper.apply(data);
            if (typedData != null) {
                consumer.accept(typedData);
            }
        }
    }
    
    default Function<Object, String> buildStringMapper() {
        return v -> v instanceof String ? (String) v : null;
    }
}
