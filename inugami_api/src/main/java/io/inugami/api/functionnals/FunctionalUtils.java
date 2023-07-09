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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.LongConsumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FunctionalUtils {

    // =================================================================================================================
    // APPLY IF NOT NULL
    // =================================================================================================================
    public static <T> boolean applyIfNotNull(final T data, final Consumer<T> consumer) {
        if (data != null) {
            consumer.accept(data);
            return true;
        }
        return false;
    }

    public static <T> boolean applyIfNotNull(final T data, final T defaultValue, final Consumer<T> consumer) {
        return applyIfNotNull(data == null ? defaultValue : data, consumer);
    }

    // =================================================================================================================
    // APPLY IF CHANGE
    // =================================================================================================================
    public static boolean applyIfChange(final boolean ref, final boolean newValue, final Consumer<Boolean> consumer) {
        if (ref != newValue) {
            consumer.accept(newValue);
            return true;
        }
        return false;
    }

    public static boolean applyIfChange(final short ref, final short newValue, final Consumer<Short> consumer) {
        if (ref != newValue) {
            consumer.accept(newValue);
            return true;
        }
        return false;
    }

    public static boolean applyIfChange(final int ref, final int newValue, final Consumer<Integer> consumer) {
        if (ref != newValue) {
            consumer.accept(newValue);
            return true;
        }
        return false;
    }

    public static boolean applyIfChange(final float ref, final float newValue, final Consumer<Float> consumer) {
        if (ref != newValue) {
            consumer.accept(newValue);
            return true;
        }
        return false;
    }

    public static boolean applyIfChange(final long ref, final long newValue, final LongConsumer consumer) {
        if (ref != newValue) {
            consumer.accept(newValue);
            return true;
        }
        return false;
    }

    public static boolean applyIfChange(final double ref, final double newValue, final DoubleConsumer consumer) {
        if (ref != newValue) {
            consumer.accept(newValue);
            return true;
        }
        return false;
    }

    public static <T extends Object> boolean applyIfChange(final T ref, final T newValue, final Consumer<T> consumer) {
        if (hasChange(ref, newValue)) {
            consumer.accept(newValue);
            return true;
        }
        return false;
    }


    public static <T extends Object> boolean applyIfChangeAndNotNull(final T ref, final T newValue, final Consumer<T> consumer) {
        if (hasChange(ref, newValue)) {
            return applyIfNotNull(newValue, consumer);
        }
        return false;
    }

    private static <T extends Object> boolean hasChange(final T ref, final T newValue) {
        if (ref == null && newValue == null) {
            return false;
        } else if (ref == null || newValue == null) {
            return true;
        }
        return !ref.equals(newValue);
    }
}
