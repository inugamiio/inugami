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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.functionnals.ApplyIfNotNull;
import io.inugami.api.mapping.Mapper;
import io.inugami.api.marshalling.JsonMarshaller;
import io.inugami.api.models.data.basic.Json;

import java.util.ArrayList;

/**
 * TransformAlertResultToEntity
 *
 * @author patrickguillerm
 * @since 20 janv. 2018
 */
public class TransformAlertResultToEntity implements Mapper<AlertEntity, AlertingResult>, ApplyIfNotNull {

    // =========================================================================
    // MAPPING AlertEntity -> AlertingResult
    // =========================================================================
    @Override
    public AlertEntity mapping(final AlertingResult data) {
        return data == null ? null : processMapping(data);
    }

    private AlertEntity processMapping(final AlertingResult data) {
        final AlertEntity result = new AlertEntity();
        //@formatter:off
        applyIfNotNull(data.getAlerteName(), result::setAlerteName);
        applyIfNotNull(data.getChannel(),    result::setChannel);
        applyIfNotNull(data.getLevel(),      result::setLevel);
        applyIfNotNull(data.getMessage(),    result::setLabel);
        applyIfNotNull(data.getSubLabel(),   result::setSubLabel);
        applyIfNotNull(data.getUrl(),        result::setUrl);
        applyIfNotNull(data.getData(),       value-> result.setData(cleanData(data)));
        applyIfNotNull(data.getProviders(),  value-> result.setProviders(new ArrayList<>(value)));
        //@formatter:on        
        result.setCreated(data.getCreated());
        result.setDuration(data.getDuration());

        return result;
    }

    private String cleanData(final AlertingResult data) {
        if (data == null || data.getData() == null) {
            return null;
        }

        if (data.getData() instanceof Json) {
            return ((Json) data.getData()).convertToJson();

        } else {
            final ObjectMapper objectMapper = JsonMarshaller.getInstance()
                                                            .getDefaultObjectMapper();
            try {
                return objectMapper.writeValueAsString(data.getData());
            } catch (JsonProcessingException e) {
                return null;
            }
        }

    }

}
