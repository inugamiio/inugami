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
package io.inugami.dashboard.interfaces.domain.alerting;

import io.inugami.dashboard.interfaces.domain.alerting.dto.AlertingSearchRequestAPI;
import io.inugami.framework.interfaces.models.event.AlertingModel;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping(path = "alerting")
public interface AlertingRestClient {
    // =================================================================================================================
    // CREATE
    // =================================================================================================================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Collection<AlertingModel> create(@RequestBody final Collection<AlertingModel> values);

    // =================================================================================================================
    // READ
    // =================================================================================================================
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    SearchResponse<AlertingModel> search(final AlertingSearchRequestAPI searchRequest);

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    AlertingModel getById(@PathVariable final String id,
                          @RequestParam(name = "full", defaultValue = "false", required = false) final boolean full);

    // =================================================================================================================
    // UPDATE
    // =================================================================================================================
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Collection<AlertingModel> updateForce(@RequestBody final Collection<AlertingModel> values);

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Collection<AlertingModel> update(@RequestBody final Collection<AlertingModel> values);

    // =================================================================================================================
    // DELETE
    // =================================================================================================================
    @DeleteMapping()
    void delete(@RequestParam(name = "ids") final Collection<String> ids);

}
