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
package io.inugami.framework.api.marshalling;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.inugami.framework.interfaces.exceptions.Warning;

import java.io.IOException;

import static io.inugami.framework.interfaces.exceptions.Warning.*;

public class WarningSerializer extends StdSerializer<Warning> {


    public WarningSerializer(final Class<Warning> errorCodeClass) {
        super(errorCodeClass);
    }

    @Override
    public void serialize(final Warning value,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        if (value == null) {
            jsonGenerator.writeNull();
        } else {
            renderAsJson(value, jsonGenerator);
        }
    }

    private void renderAsJson(final Warning value, final JsonGenerator json) throws IOException {
        json.writeStartObject();

        if (value.getWarningCode() != null) {
            json.writeFieldName(WARNING_CODE);
            json.writeString(value.getWarningCode());
        }

        if (value.getMessage() != null) {
            json.writeFieldName(WARNING_MESSAGE);
            json.writeString(value.getMessage());
        }

        if (value.getMessageDetail() != null) {
            json.writeFieldName(WARNING_MESSAGE_DETAIL);
            json.writeString(value.getMessageDetail());
        }

        if (value.getWarningType() != null) {
            json.writeFieldName(WARNING_TYPE);
            json.writeString(value.getWarningType());
        }

        if (value.getCategory() != null) {
            json.writeFieldName(WARNING_CATEGORY);
            json.writeString(value.getCategory());
        }

        if (value.getDomain() != null) {
            json.writeFieldName(WARNING_DOMAIN);
            json.writeString(value.getDomain());
        }

        if (value.getSubDomain() != null) {
            json.writeFieldName(WARNING_SUB_DOMAIN);
            json.writeString(value.getSubDomain());
        }

        json.writeEndObject();
    }
}
