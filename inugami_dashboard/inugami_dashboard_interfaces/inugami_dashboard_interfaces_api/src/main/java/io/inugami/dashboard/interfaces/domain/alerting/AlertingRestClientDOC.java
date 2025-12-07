package  io.inugami.dashboard.interfaces.domain.alerting;

import  io.inugami.framework.interfaces.rest.*;
import  io.swagger.v3.oas.annotations.media.*;
import  io.swagger.v3.oas.annotations.responses.*;
import  java.lang.annotation.*;
import  lombok.experimental.UtilityClass;


@UtilityClass
public class AlertingRestClientDOC  {
     @PotentialErrors({
          @PotentialError(errorCode="ALERTING-1_0", httpStatus=400, type="functional", errorMessage="Alerting data are invalid", errorMessageDetail="Alerting data are invalid")
     })
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
                                                  "condition" : "value > 5",
                                                  "description" : "lorem ipsum",
                                                  "function" : "handlerFunction",
                                                  "message" : "sorry",
                                                  "name" : "simple-alert",
                                                  "provider" : "graphite",
                                                  "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                                                } ]
                                       """
                                       )
                      
                      	}
                      )
         		}
         	)
         ,
         @ApiResponse(
         	responseCode="400",
         	description="Functional error on executing operation",
         	content={
                      @Content(
                      	mediaType="application/json",
                      	examples={                 
                                       @ExampleObject(
                                       	name="Error codes",
                                       	value="""
                                                [
                                                "ALERTING-1_0  |  functional  |  Alerting data are invalid"
                                                ]
                                       """
                                       ),
                                       @ExampleObject(
                                       	name="Response with error code",
                                       	value="""
                                                {
                                                  "details" : {
                                                    "errorCode" : {
                                                      "statusCode" : 400,
                                                      "errorCode" : "ALERTING-1_0",
                                                      "errorType" : "functional",
                                                      "message" : "Alerting data are invalid",
                                                      "exploitationError" : false,
                                                      "rollbackRequire" : false,
                                                      "retryable" : false
                                                    }
                                                  },
                                                  "errors" : [ ],
                                                  "parameters" : [ ],
                                                  "status" : 400
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
     public @interface DocCreate  {}

     @PotentialErrors({
          @PotentialError(errorCode="ALERTING-2_0", httpStatus=400, type="functional", errorMessage="Alerting data are invalid", errorMessageDetail="Alerting data are invalid"),
          @PotentialError(errorCode="ALERTING-2_1", httpStatus=404, type="functional", errorMessage="Alerting not found", errorMessageDetail="Alerting not found")
     })
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
                                                  "condition" : "value > 5",
                                                  "description" : "lorem ipsum",
                                                  "function" : "handlerFunction",
                                                  "message" : "sorry",
                                                  "name" : "simple-alert",
                                                  "provider" : "graphite",
                                                  "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                                                }
                                       """
                                       )
                      
                      	}
                      )
         		}
         	)
         ,
         @ApiResponse(
         	responseCode="400",
         	description="Functional error on executing operation",
         	content={
                      @Content(
                      	mediaType="application/json",
                      	examples={                 
                                       @ExampleObject(
                                       	name="Error codes",
                                       	value="""
                                                [
                                                "ALERTING-2_0  |  functional  |  Alerting data are invalid"
                                                ]
                                       """
                                       ),
                                       @ExampleObject(
                                       	name="Response with error code",
                                       	value="""
                                                {
                                                  "details" : {
                                                    "errorCode" : {
                                                      "statusCode" : 400,
                                                      "errorCode" : "ALERTING-2_0",
                                                      "errorType" : "functional",
                                                      "message" : "Alerting data are invalid",
                                                      "exploitationError" : false,
                                                      "rollbackRequire" : false,
                                                      "retryable" : false
                                                    }
                                                  },
                                                  "errors" : [ ],
                                                  "parameters" : [ ],
                                                  "status" : 400
                                                }
                                       """
                                       )
                      
                      	}
                      )
         		}
         	)
         ,
         @ApiResponse(
         	responseCode="404",
         	description="Functional error on executing operation",
         	content={
                      @Content(
                      	mediaType="application/json",
                      	examples={                 
                                       @ExampleObject(
                                       	name="Error codes",
                                       	value="""
                                                [
                                                "ALERTING-2_1  |  functional  |  Alerting not found"
                                                ]
                                       """
                                       ),
                                       @ExampleObject(
                                       	name="Response with error code",
                                       	value="""
                                                {
                                                  "details" : {
                                                    "errorCode" : {
                                                      "statusCode" : 404,
                                                      "errorCode" : "ALERTING-2_1",
                                                      "errorType" : "functional",
                                                      "message" : "Alerting not found",
                                                      "exploitationError" : false,
                                                      "rollbackRequire" : false,
                                                      "retryable" : false
                                                    }
                                                  },
                                                  "errors" : [ ],
                                                  "parameters" : [ ],
                                                  "status" : 404
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
     public @interface DocGetById  {}

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
                                                  "data" : [ {
                                                    "condition" : "value > 5",
                                                    "description" : "lorem ipsum",
                                                    "function" : "handlerFunction",
                                                    "message" : "sorry",
                                                    "name" : "simple-alert",
                                                    "provider" : "graphite",
                                                    "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                                                  } ],
                                                  "nbFoundItems" : 1,
                                                  "next" : false,
                                                  "page" : 0,
                                                  "pageSize" : 10,
                                                  "previous" : false,
                                                  "totalPages" : 1
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
     public @interface DocSearch  {}

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
                                                  "condition" : "value > 5",
                                                  "description" : "lorem ipsum",
                                                  "function" : "handlerFunction",
                                                  "message" : "sorry",
                                                  "name" : "simple-alert",
                                                  "provider" : "graphite",
                                                  "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
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
     public @interface DocUpdateForce  {}

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
                                                  "condition" : "value > 5",
                                                  "description" : "lorem ipsum",
                                                  "function" : "handlerFunction",
                                                  "message" : "sorry",
                                                  "name" : "simple-alert",
                                                  "provider" : "graphite",
                                                  "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
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
     public @interface DocUpdate  {}

     @ApiResponses({
         @ApiResponse(
         	responseCode="200",
         	description="Successful operation",
         	content={
                      @Content(
                      	mediaType="application/json",
                      	examples={                 
                                       @ExampleObject(
                                       	name="Nominal"
                                       )
                      
                      	}
                      )
         		}
         	)
     })
     @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
     @Retention(RetentionPolicy.RUNTIME)
     @Inherited
     public @interface DocDelete  {}


}