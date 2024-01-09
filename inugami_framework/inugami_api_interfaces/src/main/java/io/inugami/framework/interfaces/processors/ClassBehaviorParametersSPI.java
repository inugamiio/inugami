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
package io.inugami.framework.interfaces.processors;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;

/**
 * Inugami use a lot of SPI implementation, Some components required to be configured.
 * SPI implementation required to have on implementation a constructor without parameters.
 * In Java is not possible to create an final SPI implementation with some initialized fields.
 * To do it, the <strong>ClassBehaviorParametersSPI</strong> allows to build a newer SPI instance with given
 * configuration.
 */
public interface ClassBehaviorParametersSPI<T> {
    boolean accept(Class<?> clazz);

    T build(final ClassBehavior behavior, final ConfigHandler<String, String> config);
}
