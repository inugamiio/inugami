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
package io.inugami.framework.api.metrics;

import io.inugami.framework.interfaces.metrics.DoubleNumberObject;
import io.inugami.framework.interfaces.metrics.LongNumberObject;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@UtilityClass
public class MetricsUtils {

    //==================================================================================================================
    // LONG
    //==================================================================================================================
    public static Long convertToLong(final Object value) {
        Long result = null;
        if (value instanceof Long v) {
            result = v;
        } else if (value instanceof Integer v) {
            result = Long.valueOf(v);
        } else if (value instanceof LongNumberObject v) {
            result = v.toLong();
        } else if (value instanceof Number v) {
            result = v.longValue();
        }
        return result;
    }

    public static List<Long> convertToLong(final List<Object> values) {
        final List<Long> result = new ArrayList<>();
        for (Object value : Optional.ofNullable(values).orElse(List.of())) {
            Long item = convertToLong(value);
            applyIfNotNull(item, result::add);
        }
        return result;
    }

    //==================================================================================================================
    // DOUBLE
    //==================================================================================================================
    public static Double convertToDouble(final Object value) {
        Double result = null;
        if (value instanceof Long v) {
            result = v >= Integer.MIN_VALUE || v <= Integer.MAX_VALUE ? Double.valueOf(v) : null;
        } else if (value instanceof Integer v) {
            result = Double.valueOf(v);
        } else if (value instanceof BigDecimal v) {
            result = v.doubleValue();
        } else if (value instanceof DoubleNumberObject v) {
            result = v.toDouble();
        } else if (value instanceof Number v) {
            result = v.doubleValue();
        }
        return result;
    }

    public static List<Double> convertToDouble(final List<Object> values) {
        final List<Double> result = new ArrayList<>();
        for (Object value : Optional.ofNullable(values).orElse(List.of())) {
            Double item = convertToDouble(value);
            applyIfNotNull(item, result::add);
        }
        return result;
    }

    public static boolean isDouble(final Object value) {
        return (value instanceof Double
                || value instanceof Float
                || value instanceof BigDecimal
                || value instanceof DoubleNumberObject);
    }
}
