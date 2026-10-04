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

import io.inugami.framework.interfaces.exceptions.ErrorCode;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

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
                          final JsonGenerator gen,
                          final SerializationContext ctxt) throws JacksonException {
        if (value == null) {
            gen.writeNull();
        } else {
            renderAsJson(value, gen);
        }
    }


    private void renderAsJson(final ErrorCode value, final JsonGenerator json) throws JacksonException {
        json.writeStartObject();
        json.writeName(STATUS_CODE);
        json.writeNumber(value.getStatusCode());

        if (value.getCategory() != null) {
            json.writeName(CATEGORY);
            json.writeString(value.getCategory());
        }

        if (value.getDomain() != null) {
            json.writeName(DOMAIN);
            json.writeString(value.getDomain());
        }
        if (value.getErrorCode() != null) {
            json.writeName(ERROR_CODE);
            json.writeString(value.getErrorCode());
        }
        if (value.getErrorType() != null) {
            json.writeName(ERROR_TYPE);
            json.writeString(value.getErrorType());
        }
        if (value.getField() != null) {
            json.writeName(FIELD);
            json.writeString(value.getField());
        }
        if (value.getMessage() != null) {
            json.writeName(MESSAGE);
            json.writeString(value.getMessage());
        }
        if (value.getMessageDetail() != null) {
            json.writeName(MESSAGE_DETAIL);
            json.writeString(value.getMessageDetail());
        }
        if (value.getPayload() != null) {
            json.writeName(PAYLOAD);
            json.writeString(value.getPayload());
        }
        if (value.getSubDomain() != null) {
            json.writeName(SUB_DOMAIN);
            json.writeString(value.getSubDomain());
        }
        if (value.getUrl() != null) {
            json.writeName(URL);
            json.writeString(value.getUrl());
        }

        // Remplacement de writeBooleanField par writeName + writeBoolean
        json.writeName(EXPLOITATION_ERROR);
        json.writeBoolean(value.isExploitationError());

        json.writeName(ROLLBACK_REQUIRE);
        json.writeBoolean(value.isRollbackRequire());

        json.writeName(RETRYABLE);
        json.writeBoolean(value.isRetryable());

        json.writeEndObject();
    }
}