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
package io.inugami.interfaces.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * SaveEntitiesResult
 *
 * @author patrick_guillerm
 * @since 15 mars 2018
 */
@ToString
@Getter
@NoArgsConstructor
public class SaveEntitiesResult<E> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final List<E> newEntities = new ArrayList<>();

    private final List<E> mergeEntities = new ArrayList<>();


    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public SaveEntitiesResult<E> addNewEntity(final E entity) {
        if (entity != null) {
            newEntities.add(entity);
        }
        return this;
    }

    public SaveEntitiesResult<E> addAllNewEntities(final List<E> uids) {
        if (uids != null) {
            newEntities.addAll(uids);
        }
        return this;
    }

    public SaveEntitiesResult<E> addMergeEntity(final E uid) {
        if (uid != null) {
            mergeEntities.add(uid);
        }
        return this;
    }

    public SaveEntitiesResult<E> addAllMergeEntities(final List<E> uids) {
        if (uids != null) {
            mergeEntities.addAll(uids);
        }
        return this;
    }


}
