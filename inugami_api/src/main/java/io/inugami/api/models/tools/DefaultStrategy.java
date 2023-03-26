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
package io.inugami.api.models.tools;

import io.inugami.api.exceptions.tools.StrategyException;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.function.Function;

@AllArgsConstructor
@Builder(toBuilder = true)
public class DefaultStrategy<IN, OUT> implements Strategy<IN, OUT> {


    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Function<IN, Boolean> accept;
    private final Function<IN, OUT>     process;

    // =========================================================================
    // API
    // =========================================================================
    @Override
    public boolean accept(final IN inputData) {
        return accept == null ? true : accept.apply(inputData);
    }

    @Override
    public OUT process(final IN inputData) throws StrategyException {
        return process == null ? null : process.apply(inputData);
    }
}
