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

import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.event.TargetConfig;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

public class GenericEventSerializer extends StdSerializer<GenericEvent> {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final String SCHEDULER       = "scheduler";
    public static final String PARENT          = "parent";
    public static final String QUERY           = "query";
    public static final String ALERTINGS       = "alertings";
    public static final String PROCESSORS      = "processors";
    public static final String MAPPER          = "mapper";
    public static final String PROVIDER        = "provider";
    public static final String UNTIL           = "until";
    public static final String FROM_FIRST_TIME = "fromFirstTime";
    public static final String NAME            = "name";
    public static final String TYPE            = "type";
    public static final String EVENT           = "Event";
    public static final String SIMPLE_EVENT    = "SimpleEvent";
    public static final String TARGETS         = "targets";

    //==================================================================================================================
    // CONSTRUCTOR
    //==================================================================================================================
    protected GenericEventSerializer(final Class<GenericEvent> t) {
        super(t);
    }

    //==================================================================================================================
    // SERIALIZE
    //==================================================================================================================
    @Override
    public void serialize(final GenericEvent value,
                          final JsonGenerator gen,
                          final SerializationContext ctxt) throws JacksonException {
        if (value == null) {
            gen.writeNull();
        } else {
            renderAsJson(value, gen);
        }
    }

    private void renderAsJson(final GenericEvent value,
                              final JsonGenerator json) throws JacksonException {
        if (value instanceof SimpleEvent simpleEvent) {
            writeSimpleEvent(simpleEvent, json);
        } else if (value instanceof TargetConfig targetConfig) {
            writeTargetConfig(targetConfig, json);
        } else if (value instanceof Event event) {
            writeEvent(event, json);
        } else {
            json.writeNull();
        }
    }


    private void writeSimpleEvent(final SimpleEvent value, final JsonGenerator json) throws JacksonException {
        json.writeStartObject();
        json.writeName(TYPE);
        json.writeString(SIMPLE_EVENT);
        json.writeName(NAME);
        json.writeString(value.getName());
        json.writeName(FROM_FIRST_TIME);
        json.writeString(value.getFromFirstTime());
        json.writeName(UNTIL);
        json.writeString(value.getUntil());
        json.writeName(PROVIDER);
        json.writeString(value.getProvider());
        json.writeName(MAPPER);
        json.writeString(value.getMapper());
        json.writeName(PROCESSORS);
        json.writePOJO(value.getProcessors());
        json.writeName(ALERTINGS);
        json.writePOJO(value.getAlertings());
        json.writeName(QUERY);
        json.writeString(value.getQuery());
        json.writeName(PARENT);
        json.writeString(value.getParent());
        json.writeName(SCHEDULER);
        json.writeString(value.getScheduler());
        json.writeEndObject();
    }

    private void writeTargetConfig(final TargetConfig value, final JsonGenerator json) throws JacksonException {
        json.writeStartObject();
        json.writeName(NAME);
        json.writeString(value.getName());
        json.writeName(FROM_FIRST_TIME);
        json.writeString(value.getFromFirstTime());
        json.writeName(UNTIL);
        json.writeString(value.getUntil());
        json.writeName(PROVIDER);
        json.writeString(value.getProvider());
        json.writeName(MAPPER);
        json.writeString(value.getMapper());
        json.writeName(PROCESSORS);
        json.writePOJO(value.getProcessors());
        json.writeName(ALERTINGS);
        json.writePOJO(value.getAlertings());
        json.writeName(QUERY);
        json.writeString(value.getQuery());
        json.writeName(PARENT);
        json.writeString(value.getParent());
        json.writeName(SCHEDULER);
        json.writeString(value.getScheduler());
        json.writeEndObject();
    }

    private void writeEvent(final Event value, final JsonGenerator json) throws JacksonException {
        json.writeStartObject();
        json.writeName(TYPE);
        json.writeString(EVENT);
        json.writeName(NAME);
        json.writeString(value.getName());
        json.writeName(FROM_FIRST_TIME);
        json.writeString(value.getFromFirstTime());
        json.writeName(UNTIL);
        json.writeString(value.getUntil());
        json.writeName(PROVIDER);
        json.writeString(value.getProvider());
        json.writeName(MAPPER);
        json.writeString(value.getMapper());
        json.writeName(PROCESSORS);
        json.writePOJO(value.getProcessors());
        json.writeName(ALERTINGS);
        json.writePOJO(value.getAlertings());
        json.writeName(SCHEDULER);
        json.writeString(value.getScheduler());
        json.writeName(TARGETS);
        if (value.getTargets() != null) {
            json.writeStartArray();
            for (var target : value.getTargets()) {
                writeTargetConfig(target, json);
            }
            json.writeEndArray();
        }
        json.writeEndObject();
    }
}