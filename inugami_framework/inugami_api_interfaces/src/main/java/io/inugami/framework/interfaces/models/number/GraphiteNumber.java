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
package io.inugami.framework.interfaces.models.number;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * GraphiteNumber
 *
 * @author patrick_guillerm
 * @since 17 août 2018
 */
public interface GraphiteNumber extends Serializable {
    GraphiteNumber add(GraphiteNumber number);

    GraphiteNumber sub(GraphiteNumber number);

    long toLong();

    double toDouble();

    String rendering();

    boolean isDecimal();

    BigDecimal toBigDecimal();

    GraphiteNumber cloneNumber();

    default int compare(final GraphiteNumber number) {
        final BigDecimal current = toBigDecimal();
        final BigDecimal other   = number == null ? null : number.toBigDecimal();
        int              result  = 0;

        if (current == null && other == null) {
            result = 0;
        } else if (other == null) {
            result = -1;
        } else if (current == null) {
            result = 1;
        } else {
            result = current.compareTo(other);
        }
        return result;
    }
}
