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

/**
 * In Java we have the Consumer and BiConsumer interfaces. But in specific use case,
 * we need  to have three parameters. The **TriConsumer** is designed for that.
 * However, if you have more than three parameters, the best approach is to
 * define a DTO object as parameter.
 * 
 * @author patrick_guillerm
 * @since 17 oct. 2017
 */
@FunctionalInterface
public interface TriConsumer<F, S, T> {
    void accept(final F firstValue, final S secondValue, final T thirdValue);
}
