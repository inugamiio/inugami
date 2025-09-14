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
package io.inugami.dashboard.interfaces.core.domain.alerting.mapper;

import io.inugami.dashboard.api.domain.alerting.dto.AlertingSearchRequestDTO;
import io.inugami.dashboard.interfaces.domain.alerting.dto.AlertingSearchRequestAPI;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AlertingSearchRequestAPIMapper {

    @Mapping(target = "createdDate", expression = "java(io.inugami.framework.interfaces.models.search.SearchFiltersUtils.convertToLocalDateTime(value, value::getCreatedDate))")
    @Mapping(target = "LastModifiedDate", expression = "java(io.inugami.framework.interfaces.models.search.SearchFiltersUtils.convertToLocalDateTime(value, value::getLastModifiedDate))")
    AlertingSearchRequestDTO convertToRestDTO(final AlertingSearchRequestAPI value);
}
