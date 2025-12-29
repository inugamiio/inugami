package  io.inugami.dashboard.interfaces.domain.plugin;

import  io.inugami.framework.interfaces.rest.*;
import  io.swagger.v3.oas.annotations.media.*;
import  io.swagger.v3.oas.annotations.responses.*;
import  java.lang.annotation.*;
import  lombok.experimental.UtilityClass;


@UtilityClass
public class PluginRestClientDOC  {
     @ApiResponses({
         @ApiResponse(
         	responseCode="200",
         	description="Successful operation",
         	content={
                      @Content(
                      	mediaType="application/json",
                      	examples={                 
                                       @ExampleObject(
                                       	name="Nominal",
                                       	value="""
                                                [ {
                                                  "config" : {
                                                    "alertings" : [ ],
                                                    "components" : [ ],
                                                    "dependencies" : [ ],
                                                    "enable" : true,
                                                    "eventsFiles" : [ ],
                                                    "frontProperties" : [ ],
                                                    "handlers" : [ ],
                                                    "listeners" : [ ],
                                                    "processors" : [ ],
                                                    "properties" : { },
                                                    "providers" : [ ],
                                                    "resources" : [ ],
                                                    "security" : [ ]
                                                  },
                                                  "enabled" : false,
                                                  "eventConfigPresent" : false,
                                                  "events" : [ {
                                                    "enable" : true,
                                                    "events" : [ {
                                                      "type" : "Event",
                                                      "name" : "event-name",
                                                      "fromFirstTime" : "-10min",
                                                      "until" : null,
                                                      "provider" : "provider",
                                                      "mapper" : "mapper",
                                                      "processors" : [ {
                                                        "configs" : { },
                                                        "name" : "processor_name"
                                                      } ],
                                                      "alertings" : [ ],
                                                      "scheduler" : "0 0/5 * * * ?",
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
                                                    } ],
                                                    "gav" : {
                                                      "artifactId" : "inu-test",
                                                      "groupId" : "io.inugami.plugin",
                                                      "hash" : "io.inugami.plugin:inu-test:4.3.0:jar",
                                                      "qualifier" : "jar",
                                                      "version" : "4.3.0"
                                                    },
                                                    "name" : "events",
                                                    "simpleEvents" : [ {
                                                      "type" : "SimpleEvent",
                                                      "name" : "event-name",
                                                      "fromFirstTime" : "-10min",
                                                      "until" : null,
                                                      "provider" : "provider",
                                                      "mapper" : "mapper",
                                                      "processors" : [ {
                                                        "configs" : { },
                                                        "name" : "processor_name"
                                                      } ],
                                                      "alertings" : [ ],
                                                      "query" : "query",
                                                      "parent" : null,
                                                      "scheduler" : "0 0/5 * * * ?"
                                                    } ]
                                                  } ],
                                                  "gav" : {
                                                    "artifactId" : "inu-test",
                                                    "groupId" : "io.inugami.plugin",
                                                    "hash" : "io.inugami.plugin:inu-test:4.3.0:jar",
                                                    "qualifier" : "jar",
                                                    "version" : "4.3.0"
                                                  }
                                                } ]
                                       """
                                       )
                      
                      	}
                      )
         		}
         	)
     })
     @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
     @Retention(RetentionPolicy.RUNTIME)
     @Inherited
     public @interface DocFindAllPlugin  {}

     @ApiResponses({
         @ApiResponse(
         	responseCode="200",
         	description="Successful operation",
         	content={
                      @Content(
                      	mediaType="application/json",
                      	examples={                 
                                       @ExampleObject(
                                       	name="Nominal",
                                       	value="""
                                                {
                                                  "events" : [ {
                                                    "data" : {
                                                      "alerts" : [ ],
                                                      "channel" : "SSE_inugami",
                                                      "data" : [ {
                                                        "timestamp" : 1766064662604,
                                                        "value" : 15.5
                                                      } ]
                                                    },
                                                    "errorCode" : {
                                                      "statusCode" : 500,
                                                      "errorCode" : "ENGINE-0_6",
                                                      "errorType" : "technical",
                                                      "message" : "error on read application configuration",
                                                      "exploitationError" : false,
                                                      "rollbackRequire" : false,
                                                      "retryable" : false
                                                    },
                                                    "name" : "simple-event",
                                                    "status" : "SUCCESS"
                                                  } ],
                                                  "gav" : {
                                                    "artifactId" : "inu-test",
                                                    "groupId" : "io.inugami.plugin",
                                                    "hash" : "io.inugami.plugin:inu-test:null"
                                                  }
                                                }
                                       """
                                       )
                      
                      	}
                      )
         		}
         	)
     })
     @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
     @Retention(RetentionPolicy.RUNTIME)
     @Inherited
     public @interface DocFindPluginDataByGav  {}


}