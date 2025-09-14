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
package io.inugami.dashboard.infrastructure.database;

import io.inugami.dashboard.api.domain.alerting.IAlertingDao;
import io.inugami.dashboard.api.domain.alerting.dto.AlertingSearchRequestDTO;
import io.inugami.framework.interfaces.models.event.AlertingModel;
import io.inugami.framework.interfaces.models.search.QueryFilterDTO;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@Service
public class AlertingDao implements IAlertingDao {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final Map<String, AlertingModel> MOCK = new ConcurrentHashMap<>();

    // =================================================================================================================
    // CREATE
    // =================================================================================================================
    @Override
    public Collection<AlertingModel> create(final Collection<AlertingModel> values) {
        final List<AlertingModel> result = new ArrayList<>();
        if (values == null) {
            return result;
        }

        for (AlertingModel value : values) {
            final String uid = UUID.randomUUID().toString();
            final var currentValue = value.toBuilder()
                                          .uid(uid)
                                          .build();
            result.add(currentValue);
            MOCK.put(uid, currentValue);
        }
        return result;
    }


    // =================================================================================================================
    // READ
    // =================================================================================================================
    @Override
    public SearchResponse<AlertingModel> search(final AlertingSearchRequestDTO searchRequest,
                                                final Collection<QueryFilterDTO<?>> requestFilters) {
        return SearchResponse.<AlertingModel>builder()
                             .data(MOCK.entrySet()
                                       .stream()
                                       .map(Map.Entry::getValue)
                                       .toList())
                             .next(false)
                             .previous(false)
                             .nbFoundItems(MOCK.size())
                             .totalPages(1)
                             .build();
    }


    @Override
    public AlertingModel getById(final String id, final Boolean full) {
        if (id == null) {
            return null;
        }
        return MOCK.get(id);
    }

    @Override
    public boolean contains(final Collection<String> uids) {
        if (uids == null || uids.isEmpty()) {
            return false;
        }
        for (String uid : uids) {
            if (!MOCK.containsKey(uid)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Collection<AlertingModel> getByIds(final Collection<String> ids) {
        final List<AlertingModel> result = new ArrayList<>();
        if (ids == null) {
            return result;
        }
        for (String uid : ids) {
            final var value = MOCK.get(uid);
            applyIfNotNull(value, result::add);
        }
        return result;
    }

    // =================================================================================================================
    // UPDATE
    // =================================================================================================================
    @Override
    public Collection<AlertingModel> update(final Collection<AlertingModel> values) {
        final List<AlertingModel> result = new ArrayList<>();
        if (values == null) {
            return result;
        }

        for (AlertingModel value : values) {
            if (value.getUid() == null) {
                continue;
            }
            MOCK.put(value.getUid(), value);
            result.add(value);
        }

        return result;
    }

    // =================================================================================================================
    // DELETE
    // =================================================================================================================
    @Override
    public void delete(final Collection<String> ids) {
        if (ids == null) {
            return;
        }
        for (String id : ids) {
            MOCK.remove(id);
        }
    }
}
