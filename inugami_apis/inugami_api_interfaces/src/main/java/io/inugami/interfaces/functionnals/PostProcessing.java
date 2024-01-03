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

import io.inugami.interfaces.exceptions.TechnicalException;

/**
 * The <strong>PostProcessing&lt;T&gt;</strong> interface is designed to materialize a visitor pattern.
 * In Inugami, it is used to manipulate data after it has been recovered.
 *
 * @author patrickguillerm
 * @since 17 déc. 2017
 */
@FunctionalInterface
public interface PostProcessing<T> {
    void postProcessing(final T context) throws TechnicalException;
}
