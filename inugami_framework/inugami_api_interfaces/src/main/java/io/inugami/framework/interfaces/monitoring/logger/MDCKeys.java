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
package io.inugami.framework.interfaces.monitoring.logger;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public enum MDCKeys {
    appClass,
    appClassShortName,
    appMethod,
    appService,
    appSubService,
    applicationVersion,
    asset,
    artifactId,
    authProtocol,
    callFrom,
    callType,
    conversation_id,
    correlation_id,
    commitId,
    commitDate,
    country,
    customerId,
    deviceClass,
    deviceIdentifier,
    deviceIp,
    deviceNetworkSpeedDown(Double.valueOf(0.0)),
    deviceNetworkSpeedLatency(Double.valueOf(0.0)),
    deviceNetworkSpeedUp(Double.valueOf(0.0)),
    deviceNetworkType,
    deviceType,
    domain("xxxx", "appDomain"),
    duration(Long.valueOf(0)),
    env,
    errorCategory,
    errorCode,
    errorExploitationError(Boolean.FALSE),
    errorField,
    errorMessage,
    errorMessageDetail,
    errorRetryable(Boolean.FALSE),
    errorRollback(Boolean.FALSE),
    /**
     * errorStatus is <strong>int value</strong>
     */
    errorStatus,
    errorType,
    errorUrl,
    exceptionName,
    errorDomain,
    errorSubDomain,
    flags,
    from(LocalDateTime.now()),
    fromTimestamp(Long.valueOf(0)),
    functionalUid,
    globalStatus,
    groupId,
    healthStatus("up"),
    hostname,
    httpStatus(Integer.valueOf(0)),
    instanceName,
    instanceNumber,
    language,
    lifecycle,
    majorVersion,
    messageId,
    methodInCause,
    orderId,
    osVersion,
    parentSpanId,
    partner,
    partnerRequestCharset,
    partnerResponseCharset,
    partnerResponseDuration(Long.valueOf(0)),
    partnerResponseMessage,
    partnerResponseStatus(Integer.valueOf(0)),
    partnerService,
    partnerSubService,
    partnerType,
    partnerUrl,
    partnerVerb,
    price(Double.valueOf(0.0)),
    principal,
    productId,
    processId,
    processName,
    processStatus,
    quantity(Double.valueOf(0.0)),
    remoteAddress,
    requestHeaders,
    request_id,
    reservationNumber,
    responseHeaders,
    service,
    sessionId,
    size(Double.valueOf(0.0)),
    traceId,
    until(LocalDateTime.now()),
    untilTimestamp(Long.valueOf(0)),
    uri("xxxx", "appUri"),
    url("xxxx", "appUrl"),
    urlPattern,
    userAgent,
    userId,
    status,
    subDomain("xxxx", "appSubDomain"),
    verb("xxxx", "appVerb"),
    version,
    warning;

    private static final String                        DEFAULT_STRING_VALUE = "xxxx";
    public static final  MDCKeys[]                     VALUES               = values();
    private final        Serializable                  defaultValue;
    private final        Class<? extends Serializable> type;

    private final String currentName;


    private MDCKeys() {
        this.defaultValue = DEFAULT_STRING_VALUE;
        type = String.class;
        currentName = this.name();
    }

    private MDCKeys(final Serializable defaultValue) {
        this.defaultValue = defaultValue;
        type = defaultValue.getClass();
        currentName = this.name();
    }

    private MDCKeys(final Serializable defaultValue, final String name) {
        this.defaultValue = defaultValue;
        type = defaultValue.getClass();
        currentName = name;
    }
}
