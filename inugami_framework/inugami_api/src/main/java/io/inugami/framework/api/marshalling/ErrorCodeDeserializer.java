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

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;

import java.io.IOException;
import java.util.Optional;

import static io.inugami.framework.api.marshalling.ErrorCodeSerializer.*;

public class ErrorCodeDeserializer extends StdDeserializer<ErrorCode> {


    public ErrorCodeDeserializer(final Class<ErrorCode> errorCodeClass) {
        super(errorCodeClass);
    }


    @Override
    public ErrorCode deserialize(final JsonParser json,
                                 final DeserializationContext ctxt) throws IOException, JacksonException {
        final var builder = DefaultErrorCode.builder();
        JsonNode  node    = json.getCodec().readTree(json);


        builder.statusCode(Optional.ofNullable(node.get(STATUS_CODE)).map(JsonNode::intValue).orElse(500));
        builder.category(Optional.ofNullable(node.get(CATEGORY)).map(JsonNode::asText).orElse(null));
        builder.domain(Optional.ofNullable(node.get(DOMAIN)).map(JsonNode::asText).orElse(null));
        builder.errorCode(Optional.ofNullable(node.get(ERROR_CODE)).map(JsonNode::asText).orElse(null));
        builder.errorType(Optional.ofNullable(node.get(ERROR_TYPE)).map(JsonNode::asText).orElse(null));
        builder.field(Optional.ofNullable(node.get(FIELD)).map(JsonNode::asText).orElse(null));
        builder.message(Optional.ofNullable(node.get(MESSAGE)).map(JsonNode::asText).orElse(null));
        builder.messageDetail(Optional.ofNullable(node.get(MESSAGE_DETAIL)).map(JsonNode::asText).orElse(null));
        builder.payload(Optional.ofNullable(node.get(PAYLOAD)).map(JsonNode::asText).orElse(null));
        builder.subDomain(Optional.ofNullable(node.get(SUB_DOMAIN)).map(JsonNode::asText).orElse(null));
        builder.url(Optional.ofNullable(node.get(URL)).map(JsonNode::asText).orElse(null));
        builder.exploitationError(Optional.ofNullable(node.get(EXPLOITATION_ERROR)).map(JsonNode::asBoolean).orElse(false));
        builder.rollback(Optional.ofNullable(node.get(ROLLBACK_REQUIRE)).map(JsonNode::asBoolean).orElse(false));
        builder.retryable(Optional.ofNullable(node.get(RETRYABLE)).map(JsonNode::asBoolean).orElse(false));
        return builder.build();
    }
}
