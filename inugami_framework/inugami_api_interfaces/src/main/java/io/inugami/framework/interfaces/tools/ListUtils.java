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
import java.util.function.Consumer;
import java.util.function.Function;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@UtilityClass
public class ListUtils {

    public static final int DEFAULT_BUCKET_SIZE = 100;

    // =================================================================================================================
    // TO LIST
    // =================================================================================================================
    public static <I, O> List<O> toList(final Function<I, O> convertor, final I... values) {
        final List<O> result = new ArrayList<>();
        for (I value : values) {
            final O item = convertor.apply(value);
            applyIfNotNull(item, result::add);
        }
        return result;
    }


    public static <T> List<T> toList(final T... values) {
        final List<T> result = new ArrayList<>();
        for (T value : values) {
            applyIfNotNull(value, result::add);
        }
        return result;
    }


    // =================================================================================================================
    // BUCKETS
    // =================================================================================================================
    public static <T> List<Collection<T>> split(final Collection<T> values) {
        return split(values, DEFAULT_BUCKET_SIZE);
    }

    public static <T> List<Collection<T>> split(final Collection<T> values, final int size) {
        final int currentSize = size <= 0 ? DEFAULT_BUCKET_SIZE : size;
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        if (values.size() < currentSize) {
            return List.of(values);
        }

        final List<Collection<T>> result = new ArrayList<>();
        final List<T>             bucket = new ArrayList<>();
        for (final T item : values) {
            bucket.add(item);
            if (bucket.size() == currentSize) {
                result.add(new ArrayList<>(bucket));
                bucket.clear();
            }
        }
        if (!bucket.isEmpty()) {
            result.add(new ArrayList<>(bucket));
        }
        return result;
    }

    public static <T> void processOverBucket(final Collection<T> values,
                                             final Consumer<Collection<T>> consumer) {
        processOverBucket(values, DEFAULT_BUCKET_SIZE, consumer);
    }

    public static <T> void processOverBucket(final Collection<T> values,
                                             final int size,
                                             final Consumer<Collection<T>> consumer) {
        if (consumer == null) {
            return;
        }
        final var buckets = split(values, size);
        for (var bucket : buckets) {
            consumer.accept(bucket);
        }
    }


    // =================================================================================================================
    // TO SET
    // =================================================================================================================
    public static <T> Set<T> toSet(T... values) {
        return new LinkedHashSet<>(Arrays.asList(values));
    }

    public static <T> boolean isEmpty(final Collection<T> values) {
        return values == null || values.isEmpty();
    }

    public static <T> boolean isNotEmpty(final Collection<T> values) {
        return values != null && !values.isEmpty();
    }

}
