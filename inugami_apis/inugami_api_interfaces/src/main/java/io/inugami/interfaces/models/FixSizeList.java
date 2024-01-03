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
package io.inugami.interfaces.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;

/**
 * FixSizeList
 *
 * @author pguillerm
 * @since 3 sept. 2017
 */
@SuppressWarnings({"java:S2160"})
public class FixSizeList<T> extends ArrayList<T> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -5757831411643911290L;

    private final int capacity;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public FixSizeList(final int initialCapacity) {
        super(initialCapacity);
        this.capacity = initialCapacity;
    }

    // =========================================================================
    // ADD
    // =========================================================================
    @Override
    public boolean add(final T e) {
        if (size() >= capacity) {
            remove(0);
        }
        return super.add(e);
    }

    @Override
    public void add(final int index, final T element) {
        throw new IllegalAccessError("You can't insert element!");
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        boolean result = false;
        if (c != null) {
            c.forEach(this::add);
            result = true;
        }
        return result;
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        throw new IllegalAccessError("You can't insert elements!");
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================

    @Override
    public T set(final int index, final T element) {
        throw new IllegalAccessError("You can't use set!");
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new IllegalAccessError("You can't use retainAll!");
    }

    @Override
    public void replaceAll(final UnaryOperator<T> operator) {
        throw new IllegalAccessError("You can't use replaceAll!");
    }

}
