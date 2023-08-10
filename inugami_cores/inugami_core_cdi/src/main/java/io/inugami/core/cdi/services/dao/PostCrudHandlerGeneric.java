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
 * PostCrudHandlerGeneric
 *
 * @author patrickguillerm
 * @since 21 janv. 2018
 */
@SuppressWarnings({"java:S107"})
final class PostCrudHandlerGeneric<E> implements PostCrudHandler<E> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Consumer<List<E>> onFindAll;

    private final Consumer<List<E>> onFind;

    private final Consumer<Integer> onCount;

    private final Consumer<E> onGet;

    private final Consumer<List<E>> onSave;

    private final Consumer<List<E>> onMerge;

    private final Consumer<List<E>> onRegister;

    private final Consumer<String> onDeleteItem;

    private final Consumer<List<String>> onDelete;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public PostCrudHandlerGeneric(Consumer<List<E>> onFindAll, Consumer<List<E>> onFind, Consumer<Integer> onCount,
                                  Consumer<E> onGet, Consumer<List<E>> onSave, Consumer<List<E>> onMerge,
                                  Consumer<List<E>> onRegister, Consumer<String> onDeleteItem,
                                  Consumer<List<String>> onDelete) {
        this.onFindAll = onFindAll;
        this.onFind = onFind;
        this.onCount = onCount;
        this.onGet = onGet;
        this.onSave = onSave;
        this.onMerge = onMerge;
        this.onRegister = onRegister;
        this.onDeleteItem = onDeleteItem;
        this.onDelete = onDelete;
    }
    // =========================================================================
    // METHODS
    // =========================================================================

    @Override
    public void onFindAll(List<E> result) {
        if (onFindAll != null) {
            onFindAll.accept(result);
        }
    }

    @Override
    public void onFind(List<E> result) {
        if (onFind != null) {
            onFind.accept(result);
        }
    }

    @Override
    public void onCount(int result) {
        if (onCount != null) {
            onCount.accept(result);
        }
    }

    @Override
    public void onGet(E result) {
        if (onGet != null) {
            onGet.accept(result);
        }
    }

    @Override
    public void onSave(List<E> listEntity) {
        if (onSave != null) {
            onSave.accept(listEntity);
        }
    }

    @Override
    public void onMerge(List<E> listEntity) {
        if (onMerge != null) {
            onMerge.accept(listEntity);
        }
    }

    @Override
    public void onDeleteItem(String uid) {
        if (onDeleteItem != null) {
            onDeleteItem.accept(uid);
        }
    }

    @Override
    public void onDelete(List<String> uids) {
        if (onDelete != null) {
            onDelete.accept(uids);
        }
    }

    @Override
    public void onRegister(List<E> listEntity) {
        if (onRegister != null) {
            onRegister.accept(listEntity);
        }

    }

}
