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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.inugami.framework.interfaces.exceptions.DefaultWarning;
import io.inugami.framework.interfaces.exceptions.Warning;

import java.io.IOException;
import java.util.Optional;

import static io.inugami.framework.interfaces.exceptions.Warning.*;

@SuppressWarnings({"java:S1130"})
public class WarningDeserializer extends StdDeserializer<Warning> {


    public WarningDeserializer(final Class<Warning> t) {
        super(t);
    }


    @Override
    public Warning deserialize(final JsonParser json,
                               final DeserializationContext ctxt) throws IOException, JacksonException {
        final var builder = DefaultWarning.builder();
        JsonNode  node    = json.getCodec().readTree(json);

        builder.warningCode(Optional.ofNullable(node.get(WARNING_CODE)).map(JsonNode::asText).orElse(null));
        builder.message(Optional.ofNullable(node.get(WARNING_MESSAGE)).map(JsonNode::asText).orElse(null));
        builder.messageDetail(Optional.ofNullable(node.get(WARNING_MESSAGE_DETAIL)).map(JsonNode::asText).orElse(null));
        builder.warningType(Optional.ofNullable(node.get(WARNING_TYPE)).map(JsonNode::asText).orElse(null));
        builder.category(Optional.ofNullable(node.get(WARNING_CATEGORY)).map(JsonNode::asText).orElse(null));
        builder.domain(Optional.ofNullable(node.get(WARNING_DOMAIN)).map(JsonNode::asText).orElse(null));
        builder.subDomain(Optional.ofNullable(node.get(WARNING_SUB_DOMAIN)).map(JsonNode::asText).orElse(null));

        return builder.build();
    }
}
