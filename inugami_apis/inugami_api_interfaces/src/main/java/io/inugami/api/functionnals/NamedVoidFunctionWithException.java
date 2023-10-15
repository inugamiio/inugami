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

import lombok.RequiredArgsConstructor;

@SuppressWarnings({"java:S2166"})
@RequiredArgsConstructor
public class NamedVoidFunctionWithException implements VoidFunctionWithException {
    private final String                    name;
    private final VoidFunctionWithException action;

    public static VoidFunctionWithException of(final String name, final VoidFunctionWithException action) {
        return new NamedVoidFunctionWithException(name, action);
    }

    @Override
    public void process() throws Exception {
        if (action != null) {
            action.process();
        }
    }

    @Override
    public String toString() {
        return "NamedVoidFunctionWithException{" +
               "name='" + name + '\'' +
               '}';
    }
}
