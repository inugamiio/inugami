package  ${package}.interfaces.api.domain.user;

import  io.inugami.framework.interfaces.rest.*;
import  io.swagger.v3.oas.annotations.media.*;
import  io.swagger.v3.oas.annotations.responses.*;
import  java.lang.annotation.*;
import  lombok.experimental.UtilityClass;


@UtilityClass
public class UserRestClientDOC  {
     @PotentialErrors({
          @PotentialError(errorCode="USER-1_0", httpStatus=400, type="functional", domain="USER", errorMessage="invalid data for user creation", errorMessageDetail="invalid data for user creation"),
          @PotentialError(errorCode="USER-1_1", httpStatus=400, type="functional", domain="USER", errorMessage="uid is forbidden for user creation", errorMessageDetail="uid is forbidden for user creation"),
          @PotentialError(errorCode="USER-1_2", httpStatus=400, type="functional", domain="USER", errorMessage="invalid firstname", errorMessageDetail="invalid firstname"),
          @PotentialError(errorCode="USER-1_3", httpStatus=400, type="functional", domain="USER", errorMessage="invalid lastname", errorMessageDetail="invalid lastname"),
          @PotentialError(errorCode="USER-1_4", httpStatus=400, type="functional", domain="USER", errorMessage="invalid email", errorMessageDetail="invalid email")
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
                                                  "audit" : {
                                                    "createdBy" : "system",
                                                    "createdDate" : "2023-05-27T12:00:00",
                                                    "lastModifiedBy" : "user-1",
                                                    "lastModifiedDate" : "2023-06-01T12:00:00",
                                                    "version" : 1
                                                  },
                                                  "email" : "john.smith@mock.org",
                                                  "firstName" : "John",
                                                  "lastName" : "Smith",
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
                                                "USER-1_0  |  functional  |  USER        |  invalid data for user creation",
                                                "USER-1_1  |  functional  |  USER        |  uid is forbidden for user creation",
                                                "USER-1_2  |  functional  |  USER        |  invalid firstname",
                                                "USER-1_3  |  functional  |  USER        |  invalid lastname",
                                                "USER-1_4  |  functional  |  USER        |  invalid email"
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
                                                      "domain" : "USER",
                                                      "errorCode" : "USER-1_0",
                                                      "errorType" : "functional",
                                                      "message" : "invalid data for user creation",
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
          @PotentialError(errorCode="USER-2_0", httpStatus=400, type="functional", domain="USER", errorMessage="uid required to find user", errorMessageDetail="uid required to find user"),
          @PotentialError(errorCode="USER-2_1", httpStatus=404, type="functional", domain="USER", errorMessage="user not found", errorMessageDetail="user not found")
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
                                                  "audit" : {
                                                    "createdBy" : "system",
                                                    "createdDate" : "2023-05-27T12:00:00",
                                                    "lastModifiedBy" : "user-1",
                                                    "lastModifiedDate" : "2023-06-01T12:00:00",
                                                    "version" : 1
                                                  },
                                                  "email" : "john.smith@mock.org",
                                                  "firstName" : "John",
                                                  "lastName" : "Smith",
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
                                                "USER-2_0  |  functional  |  USER        |  uid required to find user"
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
                                                      "domain" : "USER",
                                                      "errorCode" : "USER-2_0",
                                                      "errorType" : "functional",
                                                      "message" : "uid required to find user",
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
                                                "USER-2_1  |  functional  |  USER        |  user not found"
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
                                                      "domain" : "USER",
                                                      "errorCode" : "USER-2_1",
                                                      "errorType" : "functional",
                                                      "message" : "user not found",
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
                                                    "audit" : {
                                                      "createdBy" : "system",
                                                      "createdDate" : "2023-05-27T12:00:00",
                                                      "lastModifiedBy" : "user-1",
                                                      "lastModifiedDate" : "2023-06-01T12:00:00",
                                                      "version" : 1
                                                    },
                                                    "email" : "john.smith@mock.org",
                                                    "firstName" : "John",
                                                    "lastName" : "Smith",
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

     @PotentialErrors({
          @PotentialError(errorCode="USER-3_0", httpStatus=400, type="functional", domain="USER", errorMessage="invalid data for user modification", errorMessageDetail="invalid data for user modification"),
          @PotentialError(errorCode="USER-3_1", httpStatus=404, type="functional", domain="USER", errorMessage="users not found", errorMessageDetail="users not found"),
          @PotentialError(errorCode="USER-3_2", httpStatus=400, type="functional", domain="USER", errorMessage="uid is required for user modification", errorMessageDetail="uid is required for user modification"),
          @PotentialError(errorCode="USER-3_3", httpStatus=400, type="functional", domain="USER", errorMessage="invalid firstname", errorMessageDetail="invalid firstname"),
          @PotentialError(errorCode="USER-3_4", httpStatus=400, type="functional", domain="USER", errorMessage="invalid lastname", errorMessageDetail="invalid lastname"),
          @PotentialError(errorCode="USER-3_5", httpStatus=400, type="functional", domain="USER", errorMessage="invalid email", errorMessageDetail="invalid email")
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
                                                  "audit" : {
                                                    "createdBy" : "system",
                                                    "createdDate" : "2023-05-27T12:00:00",
                                                    "lastModifiedBy" : "user-1",
                                                    "lastModifiedDate" : "2023-06-01T12:00:00",
                                                    "version" : 1
                                                  },
                                                  "email" : "john.smith@mock.org",
                                                  "firstName" : "John",
                                                  "lastName" : "Smith",
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
                                                "USER-3_0  |  functional  |  USER        |  invalid data for user modification",
                                                "USER-3_2  |  functional  |  USER        |  uid is required for user modification",
                                                "USER-3_3  |  functional  |  USER        |  invalid firstname",
                                                "USER-3_4  |  functional  |  USER        |  invalid lastname",
                                                "USER-3_5  |  functional  |  USER        |  invalid email"
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
                                                      "domain" : "USER",
                                                      "errorCode" : "USER-3_0",
                                                      "errorType" : "functional",
                                                      "message" : "invalid data for user modification",
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
                                                "USER-3_1  |  functional  |  USER        |  users not found"
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
                                                      "domain" : "USER",
                                                      "errorCode" : "USER-3_1",
                                                      "errorType" : "functional",
                                                      "message" : "users not found",
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
     public @interface DocUpdateForce  {}

     @PotentialErrors({
          @PotentialError(errorCode="USER-3_0", httpStatus=400, type="functional", domain="USER", errorMessage="invalid data for user modification", errorMessageDetail="invalid data for user modification"),
          @PotentialError(errorCode="USER-3_1", httpStatus=404, type="functional", domain="USER", errorMessage="users not found", errorMessageDetail="users not found"),
          @PotentialError(errorCode="USER-3_2", httpStatus=400, type="functional", domain="USER", errorMessage="uid is required for user modification", errorMessageDetail="uid is required for user modification"),
          @PotentialError(errorCode="USER-3_3", httpStatus=400, type="functional", domain="USER", errorMessage="invalid firstname", errorMessageDetail="invalid firstname"),
          @PotentialError(errorCode="USER-3_4", httpStatus=400, type="functional", domain="USER", errorMessage="invalid lastname", errorMessageDetail="invalid lastname"),
          @PotentialError(errorCode="USER-3_5", httpStatus=400, type="functional", domain="USER", errorMessage="invalid email", errorMessageDetail="invalid email")
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
                                                  "audit" : {
                                                    "createdBy" : "system",
                                                    "createdDate" : "2023-05-27T12:00:00",
                                                    "lastModifiedBy" : "user-1",
                                                    "lastModifiedDate" : "2023-06-01T12:00:00",
                                                    "version" : 1
                                                  },
                                                  "email" : "john.smith@mock.org",
                                                  "firstName" : "John",
                                                  "lastName" : "Smith",
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
                                                "USER-3_0  |  functional  |  USER        |  invalid data for user modification",
                                                "USER-3_2  |  functional  |  USER        |  uid is required for user modification",
                                                "USER-3_3  |  functional  |  USER        |  invalid firstname",
                                                "USER-3_4  |  functional  |  USER        |  invalid lastname",
                                                "USER-3_5  |  functional  |  USER        |  invalid email"
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
                                                      "domain" : "USER",
                                                      "errorCode" : "USER-3_0",
                                                      "errorType" : "functional",
                                                      "message" : "invalid data for user modification",
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
                                                "USER-3_1  |  functional  |  USER        |  users not found"
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
                                                      "domain" : "USER",
                                                      "errorCode" : "USER-3_1",
                                                      "errorType" : "functional",
                                                      "message" : "users not found",
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
     public @interface DocUpdate  {}

     @PotentialErrors({
          @PotentialError(errorCode="USER-4_0", httpStatus=400, type="functional", domain="USER", errorMessage="uid required to delete users", errorMessageDetail="uid required to delete users"),
          @PotentialError(errorCode="USER-4_1", httpStatus=404, type="functional", domain="USER", errorMessage="users not found", errorMessageDetail="users not found")
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
                                       	name="Nominal"
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
                                                "USER-4_0  |  functional  |  USER        |  uid required to delete users"
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
                                                      "domain" : "USER",
                                                      "errorCode" : "USER-4_0",
                                                      "errorType" : "functional",
                                                      "message" : "uid required to delete users",
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
                                                "USER-4_1  |  functional  |  USER        |  users not found"
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
                                                      "domain" : "USER",
                                                      "errorCode" : "USER-4_1",
                                                      "errorType" : "functional",
                                                      "message" : "users not found",
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
     public @interface DocDelete  {}


}