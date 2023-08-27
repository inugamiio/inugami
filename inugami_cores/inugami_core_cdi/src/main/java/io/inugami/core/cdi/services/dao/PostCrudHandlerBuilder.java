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
package io.inugami.core.cdi.services.dao;

import java.util.List;
import java.util.function.Consumer;

/**
 * PostCrudHandlerBuilder
 *
 * @author patrickguillerm
 * @since 21 janv. 2018
 */
public class PostCrudHandlerBuilder<E> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private Consumer<List<E>> onFindAll;

    private Consumer<List<E>> onFind;

    private Consumer<Integer> onCount;

    private Consumer<E> onGet;

    private Consumer<List<E>> onSave;

    private Consumer<List<E>> onMerge;

    private Consumer<List<E>> onRegister;

    private Consumer<String> onDeleteItem;

    private Consumer<List<String>> onDelete;

    // =========================================================================
    // BUILDER
    // =========================================================================
    public PostCrudHandler<E> build() {
        return new PostCrudHandlerGeneric<>(onFindAll, onFind, onCount, onGet, onSave, onMerge, onRegister,
                                            onDeleteItem, onDelete);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public PostCrudHandlerBuilder<E> addOnFindAll(Consumer<List<E>> function) {
        onFindAll = function;
        return this;
    }

    public PostCrudHandlerBuilder<E> addOnFind(Consumer<List<E>> function) {
        onFind = function;
        return this;
    }

    public PostCrudHandlerBuilder<E> addOnCount(Consumer<Integer> function) {
        onCount = function;
        return this;
    }

    public PostCrudHandlerBuilder<E> addOnGet(Consumer<E> function) {
        onGet = function;
        return this;
    }

    public PostCrudHandlerBuilder<E> addOnSave(Consumer<List<E>> function) {
        onSave = function;
        return this;
    }

    public PostCrudHandlerBuilder<E> addOnMerge(Consumer<List<E>> function) {
        onMerge = function;
        return this;
    }

    public PostCrudHandlerBuilder<E> addOnRegister(Consumer<List<E>> function) {
        onRegister = function;
        return this;
    }

    public PostCrudHandlerBuilder<E> addOnDeleteItem(Consumer<String> function) {
        onDeleteItem = function;
        return this;
    }

    public PostCrudHandlerBuilder<E> addOnDelete(Consumer<List<String>> function) {
        onDelete = function;
        return this;
    }

}
