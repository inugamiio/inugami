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
 * When a consumer uses data, exception can occur. To manage this use case, the <strong>ConsumerWithException&lt;T&gt;</strong>
 * has been designed.
 *
 * @author patrick_guillerm
 * @since 15 mars 2018
 */
@FunctionalInterface
public interface ConsumerWithException<T> {
    void process(final T value) throws Exception;
}
