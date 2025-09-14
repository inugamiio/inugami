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
package io.inugami.dashboard.interfaces.core.domain.alerting;

import io.inugami.dashboard.api.domain.alerting.IAlertingService;
import io.inugami.dashboard.interfaces.domain.alerting.AlertingRestClient;
import io.inugami.dashboard.interfaces.domain.alerting.dto.AlertingSearchRequestAPI;
import io.inugami.dashboard.interfaces.core.domain.alerting.mapper.AlertingSearchRequestAPIMapper;
import io.inugami.framework.interfaces.models.event.AlertingModel;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
public class AlertingRestController implements AlertingRestClient {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private final IAlertingService               alertingService;
    private final AlertingSearchRequestAPIMapper alertingSearchRequestAPIMapper;

    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    @Override
    public Collection<AlertingModel> create(final Collection<AlertingModel> values) {
        return alertingService.create(values);
    }

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Override
    public SearchResponse<AlertingModel> search(final AlertingSearchRequestAPI searchRequest) {
        return alertingService.search(alertingSearchRequestAPIMapper.convertToRestDTO(searchRequest));
    }

    @Override
    public AlertingModel getById(final String id, final boolean full) {
        return alertingService.getById(id, full);
    }

    //==================================================================================================================
    // UPDATE
    //==================================================================================================================

    @Override
    public Collection<AlertingModel> updateForce(final Collection<AlertingModel> values) {
        return alertingService.update(values, true);
    }

    @Override
    public Collection<AlertingModel> update(final Collection<AlertingModel> values) {
        return alertingService.update(values, false);
    }

    //==================================================================================================================
    // DELETE
    //==================================================================================================================

    @Override
    public void delete(final Collection<String> ids) {
        alertingService.delete(ids);
    }
}
