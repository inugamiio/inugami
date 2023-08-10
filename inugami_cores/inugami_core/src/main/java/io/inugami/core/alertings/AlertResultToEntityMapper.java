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
package io.inugami.core.alertings;

import flexjson.JSONSerializer;
import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.mapping.Mapper;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.basic.StringJson;

import java.util.List;
import java.util.Optional;

/**
 * AlertResultToEntity
 *
 * @author patrick_guillerm
 * @since 7 mars 2018
 */
public class AlertResultToEntityMapper implements Mapper<AlertEntity, AlertingResult> {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public AlertEntity mapping(final AlertingResult data) {
        return AlertEntity.builder()
                          .alerteName(data.getAlerteName())
                          .level(data.getLevel())
                          .levelType(data.getLevelType())
                          .levelNumber(data.getLevelNumber())
                          .label(data.getMessage())
                          .subLabel(data.getSubLabel())
                          .duration(data.getDuration())
                          .data(convertToJson(data.getData()))
                          .channel(data.getChannel())
                          .created(data.getCreated())
                          .url(data.getUrl())
                          .ttl(data.getCreated() + (data.getDuration() * 1000))
                          .providers(Optional.ofNullable(data.getProviders()).orElse(List.of()))
                          .build();
    }

    private String convertToJson(final Object data) {
        String result = null;
        if (data != null) {
            if (data instanceof String) {
                result = new StringJson((String) data).convertToJson();
            } else if (data instanceof JsonObject) {
                result = ((JsonObject) data).convertToJson();
            } else {
                result = new JSONSerializer().exclude("*.class").deepSerialize(data);
            }
        }
        return result;
    }

}
