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
package io.inugami.framework.interfaces.models.crud;

import io.inugami.framework.interfaces.models.search.QueryFilterDTO;
import io.inugami.framework.interfaces.models.search.SearchRequest;
import io.inugami.framework.interfaces.models.search.SearchResponse;

import java.util.Collection;

public interface CrudDao<T,PK,SR extends SearchRequest> {
    // =================================================================================================================
    // CREATE
    // =================================================================================================================
    Collection<T> create(final Collection<T> values);

    // =================================================================================================================
    // READ
    // =================================================================================================================
    SearchResponse<T> search(final SR searchRequest, final Collection<QueryFilterDTO<?>> requestFilters);

    T getById(final PK id, final Boolean full);

    boolean contains(final Collection<String> uids);

    Collection<T> getByIds(final Collection<PK> ids);

    // =================================================================================================================
    // UPDATE
    // =================================================================================================================
    Collection<T> update(final Collection<T> values);

    // =================================================================================================================
    // DELETE
    // =================================================================================================================
    void delete(final Collection<PK> ids);
}
