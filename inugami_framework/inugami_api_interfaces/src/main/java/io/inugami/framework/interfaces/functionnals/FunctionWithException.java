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
package io.inugami.framework.interfaces.functionnals;

/**
 * When a generic function is used, it can throw an exception.
 * To materialize this principle, the <strong>FunctionWithException</strong> is designed.
 * You can specify the input, output and exception types.
 * @param <I> the input type
 * @param <O> the output type
 * @param <E> the exception type (should extend from Throwable)
 */
@FunctionalInterface
public interface FunctionWithException<I, O, E extends Throwable> {

    O process(final I input) throws E;
}
