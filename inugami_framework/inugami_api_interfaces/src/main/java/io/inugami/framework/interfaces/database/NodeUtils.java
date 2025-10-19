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
package io.inugami.framework.interfaces.database;

import io.inugami.framework.interfaces.database.dto.Node;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings({"java:S5361"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NodeUtils {

    public static final String NULL = "null";

    // =========================================================================
    // API
    // =========================================================================
    public static <K extends Comparable<K>, V> Map<K, V> sortProperties(final Map<K, V> properties) {
        final Map<K, V> result = new LinkedHashMap<>();
        if (properties == null) {
            return result;
        }

        final Map<K, V> buffer = new LinkedHashMap<>();
        buffer.putAll(properties);


        final List<K> keys = new ArrayList<>(buffer.keySet());
        Collections.sort(keys);
        for (final K key : keys) {
            result.put(key, buffer.get(key));
        }
        return result;
    }


    public static <T> void processIfNotNull(final T value, final Consumer<T> consumer) {
        if (value != null && consumer != null) {
            consumer.accept(value);
        }
    }

    public static void processIfNotEmpty(final String value, final Consumer<String> consumer) {
        if (value != null && !value.isEmpty() && consumer != null) {
            consumer.accept(value);
        }
    }

    public static <T> void processIfNotEmpty(final Collection<T> values, final Consumer<Collection<T>> consumer) {
        if (values != null && !values.isEmpty() && consumer != null) {
            consumer.accept(values);
        }
    }

    public static void processIfNotEmptyForce(final String value, final Consumer<String> consumer) {
        if (value != null && !value.isEmpty() && !NULL.equals(value) && consumer != null) {
            consumer.accept(value);
        }
    }

    public static String cleanLines(final String value) {
        return value == null ? null : value.replaceAll("\n", "\\\\n").replaceAll("\"", "\\\\\"");
    }

    public static boolean hasText(final String value) {
        return value != null && !value.trim().isEmpty();
    }


    public static String getStringValue(final String field, final Node node) {
        if (node == null || field == null || node.getProperties() == null) {
            return null;
        }

        final Serializable value = node.getProperties().get(field);
        return value == null ? null : String.valueOf(value);
    }
}
