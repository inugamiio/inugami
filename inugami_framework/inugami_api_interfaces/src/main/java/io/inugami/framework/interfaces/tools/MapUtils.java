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
package io.inugami.framework.interfaces.tools;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class MapUtils {

    public static <K extends Comparable<K>, V> Map<K, V> initMapAndSort(final Map<K, V> value) {
        final Map<K, V> result = new LinkedHashMap<>();
        if (value != null) {
            final List<K> keys = new ArrayList<>(value.keySet());
            Collections.sort(keys);
            for (K key : keys) {
                result.put(key, value.get(key));
            }
        }
        return result;
    }

    public static <K, V> Map<K, V> initMap(final Map<K, V> value) {
        final Map<K, V> result = new LinkedHashMap<>();
        if (value != null) {
            for (Map.Entry<K, V> entry : value.entrySet()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}
