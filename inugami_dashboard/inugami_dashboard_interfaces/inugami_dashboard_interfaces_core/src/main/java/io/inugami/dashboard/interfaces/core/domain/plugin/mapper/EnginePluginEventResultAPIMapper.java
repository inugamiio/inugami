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
package io.inugami.dashboard.interfaces.core.domain.plugin.mapper;

import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.interfaces.domain.plugin.dto.EnginePluginEventResultAPI;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Mapper
public interface EnginePluginEventResultAPIMapper {
@Mapping(target = "error" , expression = "java(EnginePluginEventResultAPIMapperUtils.convertException(value))")
    EnginePluginEventResultAPI convertToApi(EnginePluginEventResultDTO value);

    default Collection<EnginePluginEventResultAPI> convertToApi(Collection<EnginePluginEventResultDTO> values) {
        return Optional.ofNullable(values)
                       .orElse(List.of())
                       .stream()
                       .map(this::convertToApi)
                       .toList();
    }
}
