package io.inugami.framework.api.marshalling;

import io.inugami.framework.interfaces.models.event.AlertingModel;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.event.TargetConfig;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertText;

class GenericEventSerializerTest {

    @Test
    void simpleEvent_serialize() {
        assertText(SimpleEvent.builder()
                              .name("simple-event")
                              .fromFirstTime("-15min")
                              .from("-5h")
                              .until("6h")
                              .scheduler("* * * * *")
                              .provider("graphite")
                              .mapper("simpleEventMapper")
                              .processors(List.of(ProcessorModel.builder()
                                                                .name("processor")
                                                                .className("io.inugami.Processor")
                                                                .configs(Map.of("key", "value"))
                                                                .manifest(ManifestInfo.builder()
                                                                                      .build())
                                                                .build()))
                              .alertings(AlertingModel.builder()
                                                      .name("alert")
                                                      .provider("alertProvider")
                                                      .level("ERROR")
                                                      .build())
                              .query("io.inugami.instance.*.error")
                              .parent("parent")
                              .build(),
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
                           """);
    }

    @Test
    void event_serialize() {
        assertText(Event.builder()
                        .name("simple-event")
                        .fromFirstTime("-15min")
                        .from("-5h")
                        .until("6h")
                        .scheduler("* * * * *")
                        .provider("graphite")
                        .mapper("simpleEventMapper")
                        .processors(List.of(ProcessorModel.builder()
                                                          .name("processor")
                                                          .className("io.inugami.Processor")
                                                          .configs(Map.of("key", "value"))
                                                          .manifest(ManifestInfo.builder()
                                                                                .build())
                                                          .build()))
                        .alertings(AlertingModel.builder()
                                                .name("alert")
                                                .provider("alertProvider")
                                                .level("ERROR")
                                                .build())
                        .targets(List.of(TargetConfig.builder()
                                                     .name("target")
                                                     .fromFirstTime("-15min")
                                                     .from("-5h")
                                                     .until("6h")
                                                     .scheduler("* * * * *")
                                                     .provider("graphite")
                                                     .mapper("simpleEventMapper")
                                                     .processors(List.of(ProcessorModel.builder()
                                                                                       .name("processor")
                                                                                       .className("io.inugami.Processor")
                                                                                       .configs(Map.of("key", "value"))
                                                                                       .manifest(ManifestInfo.builder()
                                                                                                             .build())
                                                                                       .build()))
                                                     .alertings(AlertingModel.builder()
                                                                             .name("alert")
                                                                             .provider("alertProvider")
                                                                             .level("ERROR")
                                                                             .build())
                                                     .query("io.inugami.instance.*.error")
                                                     .parent("parent")
                                                     .build()))
                        .build(),
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
                           """);
    }
}