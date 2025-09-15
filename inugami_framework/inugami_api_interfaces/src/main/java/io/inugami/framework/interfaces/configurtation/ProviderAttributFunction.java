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
package io.inugami.framework.interfaces.configurtation;

/**
 * ProviderAttributFunction
 *
 * @author patrick_guillerm
 * @since 17 août 2017
 */
@FunctionalInterface
public interface ProviderAttributFunction {

    default String getName() {
        return this.getClass().getSimpleName().substring(0, 1).toLowerCase()
               + this.getClass().getSimpleName().substring(1);
    }

    String apply(FunctionData data);
}
