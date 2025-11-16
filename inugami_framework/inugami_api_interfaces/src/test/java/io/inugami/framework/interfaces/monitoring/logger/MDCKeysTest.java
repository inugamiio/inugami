package io.inugami.framework.interfaces.monitoring.logger;

import io.inugami.framework.interfaces.testing.commons.SkipLineMatcher;
import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

class MDCKeysTest {

    @Test
    void assertEnum() {
        UnitTestHelper.assertEnum(MDCKeys.class,
                                  """
                                          {
                                            "appClass" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "appClass"
                                            },
                                            "appClassShortName" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "appClassShortName"
                                            },
                                            "appMethod" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "appMethod"
                                            },
                                            "appService" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "appService"
                                            },
                                            "appSubService" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "appSubService"
                                            },
                                            "applicationVersion" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "applicationVersion"
                                            },
                                            "asset" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "asset"
                                            },
                                            "artifactId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "artifactId"
                                            },
                                            "authProtocol" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "authProtocol"
                                            },
                                            "callFrom" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "callFrom"
                                            },
                                            "callType" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "callType"
                                            },
                                            "conversation_id" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "conversation_id"
                                            },
                                            "correlation_id" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "correlation_id"
                                            },
                                            "commitId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "commitId"
                                            },
                                            "commitDate" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "commitDate"
                                            },
                                            "country" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "country"
                                            },
                                            "customerId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "customerId"
                                            },
                                            "deviceClass" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "deviceClass"
                                            },
                                            "deviceIdentifier" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "deviceIdentifier"
                                            },
                                            "deviceIp" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "deviceIp"
                                            },
                                            "deviceNetworkSpeedDown" : {
                                              "defaultValue" : 0.0,
                                              "type" : "java.lang.Double",
                                              "currentName" : "deviceNetworkSpeedDown"
                                            },
                                            "deviceNetworkSpeedLatency" : {
                                              "defaultValue" : 0.0,
                                              "type" : "java.lang.Double",
                                              "currentName" : "deviceNetworkSpeedLatency"
                                            },
                                            "deviceNetworkSpeedUp" : {
                                              "defaultValue" : 0.0,
                                              "type" : "java.lang.Double",
                                              "currentName" : "deviceNetworkSpeedUp"
                                            },
                                            "deviceNetworkType" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "deviceNetworkType"
                                            },
                                            "deviceType" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "deviceType"
                                            },
                                            "domain" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "appDomain"
                                            },
                                            "duration" : {
                                              "defaultValue" : 0,
                                              "type" : "java.lang.Long",
                                              "currentName" : "duration"
                                            },
                                            "env" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "env"
                                            },
                                            "errorCategory" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "errorCategory"
                                            },
                                            "errorCode" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "errorCode"
                                            },
                                            "errorExploitationError" : {
                                              "defaultValue" : false,
                                              "type" : "java.lang.Boolean",
                                              "currentName" : "errorExploitationError"
                                            },
                                            "errorField" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "errorField"
                                            },
                                            "errorMessage" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "errorMessage"
                                            },
                                            "errorMessageDetail" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "errorMessageDetail"
                                            },
                                            "errorRetryable" : {
                                              "defaultValue" : false,
                                              "type" : "java.lang.Boolean",
                                              "currentName" : "errorRetryable"
                                            },
                                            "errorRollback" : {
                                              "defaultValue" : false,
                                              "type" : "java.lang.Boolean",
                                              "currentName" : "errorRollback"
                                            },
                                            "errorStatus" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "errorStatus"
                                            },
                                            "errorType" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "errorType"
                                            },
                                            "errorUrl" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "errorUrl"
                                            },
                                            "exceptionName" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "exceptionName"
                                            },
                                            "errorDomain" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "errorDomain"
                                            },
                                            "errorSubDomain" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "errorSubDomain"
                                            },
                                            "flags" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "flags"
                                            },
                                            "from" : {
                                              "defaultValue" : "2025-11-16T15:13:22.836987421",
                                              "type" : "java.time.LocalDateTime",
                                              "currentName" : "from"
                                            },
                                            "fromTimestamp" : {
                                              "defaultValue" : 0,
                                              "type" : "java.lang.Long",
                                              "currentName" : "fromTimestamp"
                                            },
                                            "functionalUid" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "functionalUid"
                                            },
                                            "globalStatus" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "globalStatus"
                                            },
                                            "groupId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "groupId"
                                            },
                                            "healthStatus" : {
                                              "defaultValue" : "up",
                                              "type" : "java.lang.String",
                                              "currentName" : "healthStatus"
                                            },
                                            "hostname" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "hostname"
                                            },
                                            "httpStatus" : {
                                              "defaultValue" : 0,
                                              "type" : "java.lang.Integer",
                                              "currentName" : "httpStatus"
                                            },
                                            "instanceName" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "instanceName"
                                            },
                                            "instanceNumber" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "instanceNumber"
                                            },
                                            "language" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "language"
                                            },
                                            "lifecycle" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "lifecycle"
                                            },
                                            "majorVersion" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "majorVersion"
                                            },
                                            "messageId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "messageId"
                                            },
                                            "methodInCause" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "methodInCause"
                                            },
                                            "orderId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "orderId"
                                            },
                                            "osVersion" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "osVersion"
                                            },
                                            "parentSpanId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "parentSpanId"
                                            },
                                            "partner" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "partner"
                                            },
                                            "partnerRequestCharset" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "partnerRequestCharset"
                                            },
                                            "partnerResponseCharset" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "partnerResponseCharset"
                                            },
                                            "partnerResponseDuration" : {
                                              "defaultValue" : 0,
                                              "type" : "java.lang.Long",
                                              "currentName" : "partnerResponseDuration"
                                            },
                                            "partnerResponseMessage" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "partnerResponseMessage"
                                            },
                                            "partnerResponseStatus" : {
                                              "defaultValue" : 0,
                                              "type" : "java.lang.Integer",
                                              "currentName" : "partnerResponseStatus"
                                            },
                                            "partnerService" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "partnerService"
                                            },
                                            "partnerSubService" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "partnerSubService"
                                            },
                                            "partnerType" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "partnerType"
                                            },
                                            "partnerUrl" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "partnerUrl"
                                            },
                                            "partnerVerb" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "partnerVerb"
                                            },
                                            "price" : {
                                              "defaultValue" : 0.0,
                                              "type" : "java.lang.Double",
                                              "currentName" : "price"
                                            },
                                            "principal" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "principal"
                                            },
                                            "productId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "productId"
                                            },
                                            "processId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "processId"
                                            },
                                            "processName" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "processName"
                                            },
                                            "processStatus" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "processStatus"
                                            },
                                            "quantity" : {
                                              "defaultValue" : 0.0,
                                              "type" : "java.lang.Double",
                                              "currentName" : "quantity"
                                            },
                                            "remoteAddress" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "remoteAddress"
                                            },
                                            "requestHeaders" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "requestHeaders"
                                            },
                                            "request_id" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "request_id"
                                            },
                                            "reservationNumber" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "reservationNumber"
                                            },
                                            "responseHeaders" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "responseHeaders"
                                            },
                                            "service" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "service"
                                            },
                                            "sessionId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "sessionId"
                                            },
                                            "size" : {
                                              "defaultValue" : 0.0,
                                              "type" : "java.lang.Double",
                                              "currentName" : "size"
                                            },
                                            "traceId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "traceId"
                                            },
                                            "until" : {
                                              "defaultValue" : "2025-11-16T15:13:22.83987888",
                                              "type" : "java.time.LocalDateTime",
                                              "currentName" : "until"
                                            },
                                            "untilTimestamp" : {
                                              "defaultValue" : 0,
                                              "type" : "java.lang.Long",
                                              "currentName" : "untilTimestamp"
                                            },
                                            "uri" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "appUri"
                                            },
                                            "url" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "appUrl"
                                            },
                                            "urlPattern" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "urlPattern"
                                            },
                                            "userAgent" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "userAgent"
                                            },
                                            "userId" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "userId"
                                            },
                                            "status" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "status"
                                            },
                                            "subDomain" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "appSubDomain"
                                            },
                                            "verb" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "appVerb"
                                            },
                                            "version" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "version"
                                            },
                                            "warning" : {
                                              "defaultValue" : "xxxx",
                                              "type" : "java.lang.String",
                                              "currentName" : "warning"
                                            }
                                          }
                                          """, SkipLineMatcher.of(217, 442));
    }
}