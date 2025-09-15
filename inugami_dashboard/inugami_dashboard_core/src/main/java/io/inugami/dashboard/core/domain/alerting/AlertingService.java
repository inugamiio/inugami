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
package io.inugami.dashboard.core.domain.alerting;

import io.inugami.dashboard.api.domain.alerting.IAlertingDao;
import io.inugami.dashboard.api.domain.alerting.IAlertingService;
import io.inugami.dashboard.api.domain.alerting.dto.AlertingFilters;
import io.inugami.dashboard.api.domain.alerting.dto.AlertingSearchRequestDTO;
import io.inugami.dashboard.api.domain.alerting.exception.AlertingErrors;
import io.inugami.framework.interfaces.models.event.AlertingModel;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static io.inugami.dashboard.api.domain.alerting.exception.AlertingErrors.READ_NOT_FOUND;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertNotEmpty;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertNotNull;

@RequiredArgsConstructor
@Service
public class AlertingService implements IAlertingService {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private final IAlertingDao alertingDao;

    // =================================================================================================================
    // CREATE
    // =================================================================================================================

    @Override
    public Collection<AlertingModel> create(final Collection<AlertingModel> values) {
        assertNotEmpty(AlertingErrors.CREATE_INVALID_DATA, values);
        return alertingDao.create(values);
    }

    // =================================================================================================================
    // READ
    // =================================================================================================================
    @Override
    public SearchResponse<AlertingModel> search(final AlertingSearchRequestDTO searchRequest) {
        return alertingDao.search(searchRequest, AlertingFilters.FILTERS);
    }

    @Override
    public AlertingModel getById(final String id, final Boolean full) {
        assertNotEmpty(AlertingErrors.READ_INVALID_DATA, id);
        final var value = alertingDao.getById(id, full);
        assertNotNull(READ_NOT_FOUND, value);
        return value;
    }

    @Override
    public boolean contains(final Collection<String> uids) {
        assertNotEmpty(AlertingErrors.READ_INVALID_DATA, uids);
        return alertingDao.contains(uids);
    }

    @Override
    public Collection<AlertingModel> getByIds(final Collection<String> ids) {
        assertNotEmpty(AlertingErrors.READ_INVALID_DATA, ids);
        return alertingDao.getByIds(ids);
    }

    // =================================================================================================================
    // UPDATE
    // =================================================================================================================
    @Override
    public Collection<AlertingModel> update(final Collection<AlertingModel> values, final boolean force) {
        assertNotEmpty(AlertingErrors.UPDATE_INVALID_DATA, values);
        return alertingDao.update(values);
    }

    // =================================================================================================================
    // DELETE
    // =================================================================================================================
    @Override
    public void delete(final Collection<String> ids) {
        assertNotEmpty(AlertingErrors.DELETE_INVALID_DATA, ids);
        alertingDao.delete(ids);
    }
}
