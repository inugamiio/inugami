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
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.ExceptionWithErrorCode;

import java.io.IOException;
import java.util.Optional;

public class ThrowableSerializer extends StdSerializer<Throwable> {
    private static final ErrorCodeSerializer ERROR_CODE_SERIALIZER = new ErrorCodeSerializer(ErrorCode.class);
    public static final  String              EMPTY                 = "";
    public static final  String              CAUSE                 = "cause";
    public static final  String              MESSAGE               = "message";
    public static final String ERROR_CODE = "errorCode";

    protected ThrowableSerializer(final Class<Throwable> type) {
        super(type);
    }

    @Override
    public void serialize(final Throwable value,
                          final JsonGenerator json,
                          final SerializerProvider provider) throws IOException {
        if (value == null) {
            json.writeNull();
        } else {
            renderAsJson(value, json, provider);
        }
    }

    private void renderAsJson(final Throwable value,
                              final JsonGenerator json,
                              final SerializerProvider provider) throws IOException {
        json.writeStartObject();
        json.writeFieldName(MESSAGE);
        json.writeString(Optional.ofNullable(value.getMessage()).orElse(EMPTY));

        if (value instanceof ExceptionWithErrorCode errorCode && errorCode.getErrorCode() != null) {
            json.writeFieldName(ERROR_CODE);
            ERROR_CODE_SERIALIZER.serialize(errorCode.getErrorCode(), json, provider);
        }

        if (value.getCause() != null) {
            json.writeFieldName(CAUSE);
            json.writeStartObject();
            json.writeFieldName(MESSAGE);
            json.writeString(value.getCause().getMessage());
            if (value.getCause() instanceof ExceptionWithErrorCode causeErrorCode && causeErrorCode.getErrorCode() != null) {
                json.writeFieldName(ERROR_CODE);
                ERROR_CODE_SERIALIZER.serialize(causeErrorCode.getErrorCode(), json, provider);
            }
            json.writeEndObject();

        }

        json.writeEndObject();
    }

}
