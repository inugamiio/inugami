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
 * Basically, the **ValidatorFunction** is designed to create a validation strategy.
 * Your implementation should have a list of <strong>ValidatorFunction</strong> and execute
 * all of them. If one of them throw an exception, you know that your input is not valid.
 * 
 * @author patrick_guillerm
 * @since 15 déc. 2017
 */
@FunctionalInterface
public interface ValidatorFunction<T, E extends Exception> {
    void validate(final T input) throws E;
}
