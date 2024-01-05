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

import lombok.*;

import java.math.BigDecimal;

/**
 * LongValue
 *
 * @author patrick_guillerm
 * @since 17 août 2018
 */
@SuppressWarnings({"java:S2153"})
@ToString
@EqualsAndHashCode
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LongNumber implements GraphiteNumber {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 5221104927236998757L;

    private long value;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public static LongNumber of(long value) {
        return new LongNumber(value);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public GraphiteNumber add(final GraphiteNumber number) {
        final long toAdd = number == null ? 0L : number.toLong();
        return new LongNumber(value + toAdd);
    }

    @Override
    public GraphiteNumber sub(final GraphiteNumber number) {
        final long toSub = number == null ? 0L : number.toLong();
        return new LongNumber(value - toSub);
    }

    @Override
    public String rendering() {
        return String.valueOf(value);
    }


    @Override
    public long toLong() {
        return value;
    }

    @Override
    public double toDouble() {
        return Double.valueOf(value).doubleValue();
    }

    @Override
    public GraphiteNumber cloneNumber() {
        return new LongNumber(value);
    }

    @Override
    public boolean isDecimal() {
        return false;
    }

    @Override
    public BigDecimal toBigDecimal() {
        return new BigDecimal(value);
    }

    @Override
    public GraphiteNumber cloneObj() {
        return new LongNumber(value);
    }
}
