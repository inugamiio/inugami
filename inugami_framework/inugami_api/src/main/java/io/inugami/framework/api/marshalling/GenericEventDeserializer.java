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
import io.inugami.framework.interfaces.models.event.*;
import io.inugami.framework.interfaces.processors.ProcessorModel;

import java.io.IOException;
import java.util.*;

import static io.inugami.framework.api.marshalling.GenericEventSerializer.*;

public class GenericEventDeserializer extends StdDeserializer<GenericEvent> {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String CLASS_NAME  = "className";
    public static final String CONFIGS     = "configs";
    public static final String UID         = "uid";
    public static final String DESCRIPTION = "description";
    public static final String MESSAGE     = "message";
    public static final String LEVEL       = "level";
    public static final String CONDITION   = "condition";
    public static final String FUNCTION    = "function";

    // =================================================================================================================
    // CONSTRUCTOR
    // =================================================================================================================
    protected GenericEventDeserializer(final Class<?> type) {
        super(type);
    }

    // =================================================================================================================
    // DESERIALIZE
    // =================================================================================================================
    @Override
    public GenericEvent deserialize(final JsonParser json,
                                    final DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode     node = json.getCodec().readTree(json);
        final String type = Optional.ofNullable(node.get(TYPE)).map(JsonNode::asText).orElse(null);
        if (type == null) {
            return null;
        }
        if (EVENT.equalsIgnoreCase(type)) {
            return deserializeEvent(node);
        } else if (SIMPLE_EVENT.equalsIgnoreCase(type)) {
            return deserializeSimpleEvent(node);
        }
        return null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // deserializeSimpleEvent
    // -----------------------------------------------------------------------------------------------------------------
    private GenericEvent deserializeSimpleEvent(final JsonNode node) {
        return SimpleEvent.builder()
                          .name(Optional.ofNullable(node.get(NAME)).map(JsonNode::asText).orElse(null))
                          .fromFirstTime(Optional.ofNullable(node.get(FROM_FIRST_TIME))
                                                 .map(JsonNode::asText)
                                                 .orElse(null))
                          .until(Optional.ofNullable(node.get(UNTIL)).map(JsonNode::asText).orElse(null))
                          .provider(Optional.ofNullable(node.get(PROVIDER)).map(JsonNode::asText).orElse(null))
                          .mapper(Optional.ofNullable(node.get(MAPPER)).map(JsonNode::asText).orElse(null))
                          .query(Optional.ofNullable(node.get(QUERY)).map(JsonNode::asText).orElse(null))
                          .parent(Optional.ofNullable(node.get(PARENT)).map(JsonNode::asText).orElse(null))
                          .scheduler(Optional.ofNullable(node.get(SCHEDULER)).map(JsonNode::asText).orElse(null))
                          .processors(deserializeProcessor(node.get(PROCESSORS)))
                          .alertings(deserializeAlertings(node.get(ALERTINGS)))
                          .build();
    }


    // -----------------------------------------------------------------------------------------------------------------
    // deserializeEvent
    // -----------------------------------------------------------------------------------------------------------------
    private GenericEvent deserializeEvent(final JsonNode node) {
        return Event.builder()
                    .name(Optional.ofNullable(node.get(NAME)).map(JsonNode::asText).orElse(null))
                    .fromFirstTime(Optional.ofNullable(node.get(FROM_FIRST_TIME))
                                           .map(JsonNode::asText)
                                           .orElse(null))
                    .until(Optional.ofNullable(node.get(UNTIL)).map(JsonNode::asText).orElse(null))
                    .provider(Optional.ofNullable(node.get(PROVIDER)).map(JsonNode::asText).orElse(null))
                    .mapper(Optional.ofNullable(node.get(MAPPER)).map(JsonNode::asText).orElse(null))
                    .scheduler(Optional.ofNullable(node.get(SCHEDULER)).map(JsonNode::asText).orElse(null))
                    .processors(deserializeProcessor(node.get(PROCESSORS)))
                    .alertings(deserializeAlertings(node.get(ALERTINGS)))
                    .targets(deserializeTarget(node.get(TARGETS)))
                    .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // deserializeTarget
    // -----------------------------------------------------------------------------------------------------------------
    private Collection<? extends TargetConfig> deserializeTarget(final JsonNode node) {
        final List<TargetConfig> result = new ArrayList<>();
        if (node == null || !node.isArray()) {
            return result;
        }
        node.forEach(child -> result.add(TargetConfig.builder()
                                                     .name(Optional.ofNullable(node.get(NAME))
                                                                   .map(JsonNode::asText)
                                                                   .orElse(null))
                                                     .fromFirstTime(Optional.ofNullable(node.get(FROM_FIRST_TIME))
                                                                            .map(JsonNode::asText)
                                                                            .orElse(null))
                                                     .until(Optional.ofNullable(node.get(UNTIL))
                                                                    .map(JsonNode::asText)
                                                                    .orElse(null))
                                                     .provider(Optional.ofNullable(node.get(PROVIDER))
                                                                       .map(JsonNode::asText)
                                                                       .orElse(null))
                                                     .mapper(Optional.ofNullable(node.get(MAPPER))
                                                                     .map(JsonNode::asText)
                                                                     .orElse(null))
                                                     .query(Optional.ofNullable(node.get(QUERY))
                                                                    .map(JsonNode::asText)
                                                                    .orElse(null))
                                                     .parent(Optional.ofNullable(node.get(PARENT))
                                                                     .map(JsonNode::asText)
                                                                     .orElse(null))
                                                     .scheduler(Optional.ofNullable(node.get(SCHEDULER))
                                                                        .map(JsonNode::asText)
                                                                        .orElse(null))
                                                     .processors(deserializeProcessor(node.get(PROCESSORS)))
                                                     .alertings(deserializeAlertings(node.get(ALERTINGS)))
                                                     .build()));
        return result;
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================

    private Collection<? extends ProcessorModel> deserializeProcessor(final JsonNode node) {
        final List<ProcessorModel> processors = new ArrayList<>();
        if (node == null || !node.isArray()) {
            return processors;
        }
        node.forEach(child -> processors.add(ProcessorModel.builder()
                                                           .name(Optional.ofNullable(child.get(NAME))
                                                                         .map(JsonNode::asText)
                                                                         .orElse(null))
                                                           .className(Optional.ofNullable(child.get(CLASS_NAME))
                                                                              .map(JsonNode::asText)
                                                                              .orElse(null))
                                                           .configs(deserializeConfig(child.get(CONFIGS)))
                                                           .build()));
        return processors;
    }

    private Map<String, String> deserializeConfig(final JsonNode jsonNode) {
        final Map<String, String> result = new LinkedHashMap<>();
        if (jsonNode == null) {
            return result;
        }
        final Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();

        while (fields.hasNext()) {
            final Map.Entry<String, JsonNode> field = fields.next();
            Optional.ofNullable(field.getValue())
                    .map(JsonNode::asText)
                    .ifPresent(v -> result.put(field.getKey(), v));

        }
        return result;
    }

    private Collection<AlertingModel> deserializeAlertings(final JsonNode node) {
        final List<AlertingModel> result = new ArrayList<>();
        if (node == null || !node.isArray()) {
            return result;
        }
        node.forEach(child -> result.add(AlertingModel.builder()
                                                      .uid(Optional.ofNullable(child.get(UID))
                                                                   .map(JsonNode::asText)
                                                                   .orElse(null))
                                                      .name(Optional.ofNullable(child.get(NAME))
                                                                    .map(JsonNode::asText)
                                                                    .orElse(null))
                                                      .description(Optional.ofNullable(child.get(
                                                                                   DESCRIPTION))
                                                                           .map(JsonNode::asText)
                                                                           .orElse(null))
                                                      .provider(Optional.ofNullable(child.get(PROVIDER))
                                                                        .map(JsonNode::asText)
                                                                        .orElse(null))
                                                      .message(Optional.ofNullable(child.get(MESSAGE))
                                                                       .map(JsonNode::asText)
                                                                       .orElse(null))
                                                      .level(Optional.ofNullable(child.get(LEVEL))
                                                                     .map(JsonNode::asText)
                                                                     .orElse(null))
                                                      .condition(Optional.ofNullable(child.get(CONDITION))
                                                                         .map(JsonNode::asText)
                                                                         .orElse(null))
                                                      .function(Optional.ofNullable(child.get(FUNCTION))
                                                                        .map(JsonNode::asText)
                                                                        .orElse(null))
                                                      .build()));
        return result;
    }

}

