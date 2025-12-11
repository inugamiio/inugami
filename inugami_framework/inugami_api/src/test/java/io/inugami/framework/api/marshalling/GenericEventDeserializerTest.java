package io.inugami.framework.api.marshalling;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class GenericEventDeserializerTest {

    @Test
    void simpleEvent_deserialize() throws JsonProcessingException {
        final var json = """
                {
                  "type" : "SimpleEvent",
                  "name" : "simple-event",
                  "fromFirstTime" : "-15min",
                  "until" : "6h",
                  "provider" : "graphite",
                  "mapper" : "simpleEventMapper",
                  "processors" : [ {
                    "className" : "io.inugami.Processor",
                    "configs" : {
                      "key" : "value"
                    },
                    "manifest" : { },
                    "name" : "processor"
                  } ],
                  "alertings" : [ {
                    "level" : "ERROR",
                    "name" : "alert",
                    "provider" : "alertProvider"
                  } ],
                  "query" : "io.inugami.instance.*.error",
                  "parent" : "parent",
                  "scheduler" : "* * * * *"
                }
                """;

        final var result = JsonMarshaller.getInstance().getDefaultObjectMapper().readValue(json, GenericEvent.class);
        assertThat(result).isInstanceOf(SimpleEvent.class);
        assertText(JsonMarshaller.getInstance().getDefaultObjectMapper().readValue(json, GenericEvent.class),
                   """
                           {
                               "type" : "SimpleEvent",
                               "name" : "simple-event",
                               "fromFirstTime" : "-15min",
                               "until" : "6h",
                               "provider" : "graphite",
                               "mapper" : "simpleEventMapper",
                               "processors" : [ {
                                 "className" : "io.inugami.Processor",
                                 "configs" : {
                                   "key" : "value"
                                 },
                                 "name" : "processor"
                               } ],
                               "alertings" : [ {
                                 "level" : "ERROR",
                                 "name" : "alert",
                                 "provider" : "alertProvider"
                               } ],
                               "query" : "io.inugami.instance.*.error",
                               "parent" : "parent",
                               "scheduler" : "* * * * *"
                             }
                           """);
    }


    @Test
    void event_deserialize() throws JsonProcessingException {
        final var json = """
                {
                             "type" : "Event",
                             "name" : "simple-event",
                             "fromFirstTime" : "-15min",
                             "until" : "6h",
                             "provider" : "graphite",
                             "mapper" : "simpleEventMapper",
                             "processors" : [ {
                               "className" : "io.inugami.Processor",
                               "configs" : {
                                 "key" : "value"
                               },
                               "manifest" : { },
                               "name" : "processor"
                             } ],
                             "alertings" : [ {
                               "level" : "ERROR",
                               "name" : "alert",
                               "provider" : "alertProvider"
                             } ],
                             "scheduler" : "* * * * *",
                             "targets" : [ {
                               "name" : "target",
                               "fromFirstTime" : "-15min",
                               "until" : "6h",
                               "provider" : "graphite",
                               "mapper" : "simpleEventMapper",
                               "processors" : [ {
                                 "className" : "io.inugami.Processor",
                                 "configs" : {
                                   "key" : "value"
                                 },
                                 "manifest" : { },
                                 "name" : "processor"
                               } ],
                               "alertings" : [ {
                                 "level" : "ERROR",
                                 "name" : "alert",
                                 "provider" : "alertProvider"
                               } ],
                               "query" : "io.inugami.instance.*.error",
                               "parent" : "parent",
                               "scheduler" : "* * * * *"
                             } ]
                           }
                """;

        final var result = JsonMarshaller.getInstance().getDefaultObjectMapper().readValue(json, GenericEvent.class);
        assertThat(result).isInstanceOf(Event.class);
        assertText(JsonMarshaller.getInstance().getDefaultObjectMapper().readValue(json, GenericEvent.class),
                   """
                           {
                             "type" : "Event",
                             "name" : "simple-event",
                             "fromFirstTime" : "-15min",
                             "until" : "6h",
                             "provider" : "graphite",
                             "mapper" : "simpleEventMapper",
                             "processors" : [ {
                               "className" : "io.inugami.Processor",
                               "configs" : {
                                 "key" : "value"
                               },
                               "name" : "processor"
                             } ],
                             "alertings" : [ {
                               "level" : "ERROR",
                               "name" : "alert",
                               "provider" : "alertProvider"
                             } ],
                             "scheduler" : "* * * * *",
                             "targets" : [ {
                               "name" : null,
                               "fromFirstTime" : null,
                               "until" : null,
                               "provider" : null,
                               "mapper" : null,
                               "processors" : [ ],
                               "alertings" : [ ],
                               "query" : null,
                               "parent" : null,
                               "scheduler" : null
                             } ]
                           }
                           """);
    }
}