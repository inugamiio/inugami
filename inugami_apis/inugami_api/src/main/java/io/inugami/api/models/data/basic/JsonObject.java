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
package io.inugami.api.models.data.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.marshalling.JsonMarshaller;
import io.inugami.api.models.ClonableObject;
import io.inugami.api.models.data.JsonObjectToJson;

import java.nio.charset.Charset;


/**
 * In Inugami all information should be able to be converted to the JSON format.
 * The <strong>JsonObject</strong> interface can track all serializable objects and offers
 * a generic solution for marshalling the current object.
 *
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
public interface JsonObject extends JsonObjectToJson, ClonableObject<JsonObject> {

    default <T extends JsonObject> T convertToObject(final byte[] data, final Charset encoding) {
        final String json = new String(data, encoding);
        final ObjectMapper objectMapper = JsonMarshaller.getInstance().getDefaultObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    default JsonObject cloneObj() {
        return null;
    }

}
