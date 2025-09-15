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
package io.inugami.framework.api.connectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.connectors.HttpPayloadMarshaller;
import io.inugami.framework.interfaces.exceptions.connector.ConnectorMarshallingException;

public class JsonHttpPayloadMarshaller implements HttpPayloadMarshaller {

    @Override
    public String convertToPayload(final Object object) throws ConnectorMarshallingException {
        try {
            return JsonMarshaller.getInstance().getDefaultObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ConnectorMarshallingException(e);
        }
    }
}
