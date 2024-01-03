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
 * Commonly used in unit test, the <strong>FunctionMustThrow</strong> interface design a function
 * who will throw an exception. This approach is very helpful to verify error code returned.
 *
 * @author patrickguillerm
 * @since 20 déc. 2017
 */
public interface FunctionMustThrow {
    default void mustThrow(final VoidFunctionWithException function) throws TechnicalException {
        try {
            function.process();
            throw new TechnicalException("Error function must throw exception!");
        }
        catch (final Exception e) {
            // noting to do
        }
    }
}
