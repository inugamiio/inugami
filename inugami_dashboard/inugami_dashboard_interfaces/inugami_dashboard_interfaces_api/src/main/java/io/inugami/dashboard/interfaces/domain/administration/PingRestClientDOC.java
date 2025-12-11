package  io.inugami.dashboard.interfaces.domain.administration;

import  io.inugami.framework.interfaces.rest.*;
import  io.swagger.v3.oas.annotations.media.*;
import  io.swagger.v3.oas.annotations.responses.*;
import  java.lang.annotation.*;
import  lombok.experimental.UtilityClass;


@UtilityClass
public class PingRestClientDOC  {
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
                                                  "applicationName" : "inugami",
                                                  "now" : "2023-06-01T12:00:00"
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
     public @interface DocPing  {}


}