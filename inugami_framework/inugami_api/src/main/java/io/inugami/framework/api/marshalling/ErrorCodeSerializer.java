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

import java.io.IOException;

public class ErrorCodeSerializer extends StdSerializer<ErrorCode> {

    public static final String STATUS_CODE        = "statusCode";
    public static final String CATEGORY           = "category";
    public static final String DOMAIN             = "domain";
    public static final String ERROR_CODE         = "errorCode";
    public static final String ERROR_TYPE         = "errorType";
    public static final String FIELD              = "field";
    public static final String MESSAGE            = "message";
    public static final String MESSAGE_DETAIL     = "messageDetail";
    public static final String PAYLOAD            = "payload";
    public static final String SUB_DOMAIN         = "subDomain";
    public static final String URL                = "url";
    public static final String EXPLOITATION_ERROR = "exploitationError";
    public static final String ROLLBACK_REQUIRE   = "rollbackRequire";
    public static final String RETRYABLE          = "retryable";

    public ErrorCodeSerializer(final Class<ErrorCode> errorCodeClass) {
        super(errorCodeClass);
    }

    @Override
    public void serialize(final ErrorCode value,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        if (value == null) {
            jsonGenerator.writeNull();
        } else {
            renderAsJson(value, jsonGenerator);
        }
    }

    private void renderAsJson(final ErrorCode value, final JsonGenerator json) throws IOException {
        json.writeStartObject();
        json.writeFieldName(STATUS_CODE);
        json.writeNumber(value.getStatusCode());

        if (value.getCategory() != null) {
            json.writeFieldName(CATEGORY);
            json.writeString(value.getCategory());
        }

        if (value.getDomain() != null) {
            json.writeFieldName(DOMAIN);
            json.writeString(value.getDomain());
        }
        if (value.getErrorCode() != null) {
            json.writeFieldName(ERROR_CODE);
            json.writeString(value.getErrorCode());
        }
        if (value.getErrorType() != null) {
            json.writeFieldName(ERROR_TYPE);
            json.writeString(value.getErrorType());
        }
        if (value.getField() != null) {
            json.writeFieldName(FIELD);
            json.writeString(value.getField());
        }
        if (value.getMessage() != null) {
            json.writeFieldName(MESSAGE);
            json.writeString(value.getMessage());
        }
        if (value.getMessageDetail() != null) {
            json.writeFieldName(MESSAGE_DETAIL);
            json.writeString(value.getMessageDetail());
        }
        if (value.getPayload() != null) {
            json.writeFieldName(PAYLOAD);
            json.writeString(value.getPayload());
        }
        if (value.getSubDomain() != null) {
            json.writeFieldName(SUB_DOMAIN);
            json.writeString(value.getSubDomain());
        }
        if (value.getUrl() != null) {
            json.writeFieldName(URL);
            json.writeString(value.getUrl());
        }

        json.writeBooleanField(EXPLOITATION_ERROR,value.isExploitationError());
        json.writeBooleanField(ROLLBACK_REQUIRE,value.isRollbackRequire());
        json.writeBooleanField(RETRYABLE,value.isRetryable());

        json.writeEndObject();
    }
}
