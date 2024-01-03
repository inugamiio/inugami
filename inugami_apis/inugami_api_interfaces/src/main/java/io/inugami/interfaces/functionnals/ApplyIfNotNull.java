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
package io.inugami.interfaces.functionnals;

import java.util.function.Consumer;

/**
 * Most facet than interface, The <strong>ApplyIfNotNull</strong> interface allow to prevent NullPopinterException.
 * 
 * @author patrick_guillerm
 * @since 15 janv. 2018
 */
public interface ApplyIfNotNull {
    default <T> void applyIfNotNull(final T data, final Consumer<T> consumer) {
        if (data != null) {
            consumer.accept(data);
        }
    }
    
    default <T> void applyIfNotNull(final T data, final T defaultValue, final Consumer<T> consumer) {
        consumer.accept(data == null ? defaultValue : data);
    }
}
