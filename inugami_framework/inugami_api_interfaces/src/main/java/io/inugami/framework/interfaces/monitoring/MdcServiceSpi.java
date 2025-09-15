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
package io.inugami.framework.interfaces.monitoring;

import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.functionnals.VoidFunctionWithException;
import io.inugami.framework.interfaces.models.Tuple;
import io.inugami.framework.interfaces.monitoring.logger.MDCKeys;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.inugami.framework.interfaces.monitoring.models.IoInfoDTO;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

public interface MdcServiceSpi {
    // =========================================================================
    // COMMONS
    // =========================================================================
    MdcServiceSpi setMdc(final Map<String, Serializable> values);

    MdcServiceSpi setMdc(final MDCKeys key, final Serializable value);

    MdcServiceSpi setMdc(final String key, final Serializable value);

    MdcServiceSpi addMdc(final Tuple<String, Serializable>... keys);

    MdcServiceSpi addMdc(final Collection<Tuple<String, Serializable>> keys);

    String getMdc(final MDCKeys key);

    String getMdc(final String key);

    boolean getBoolean(final MDCKeys key);

    boolean getBoolean(final String key);

    int getInt(final MDCKeys key);

    int getInt(final String key);

    long getLong(final MDCKeys key);

    long getLong(final String key);

    double getDouble(final MDCKeys key);

    double getDouble(final String key);

    Charset getCharset(final MDCKeys key);

    Charset getCharset(final String key);

    LocalDateTime getLocalDateTime(final MDCKeys key);

    LocalDateTime getLocalDateTime(final String key);

    Map<String, String> getAllMdc();

    Map<String, Serializable> getAllMdcExtended();

    MdcServiceSpi remove(final MDCKeys... keys);

    MdcServiceSpi remove(final String... keys);

    MdcServiceSpi clear();

    // =========================================================================
    // FIELDS
    // =========================================================================
    MdcServiceSpi appClass(final String value);

    String appClass();

    MdcServiceSpi appClassShortName(final String value);

    String appClassShortName();

    MdcServiceSpi domain(final String value);

    String domain();

    MdcServiceSpi subDomain(final String value);

    String subDomain();

    MdcServiceSpi appMethod(final String value);

    String appMethod();

    MdcServiceSpi appService(final String value);

    String appService();

    MdcServiceSpi appSubService(final String value);

    String appSubService();

    MdcServiceSpi applicationVersion(final String value);

    String applicationVersion();

    MdcServiceSpi asset(final String value);

    String asset();

    MdcServiceSpi authProtocol(final String value);

    String authProtocol();

    MdcServiceSpi callFrom(final String value);

    String callFrom();

    MdcServiceSpi callType(final String value);

    String callType();

    MdcServiceSpi conversationId(final String value);

    String conversationId();

    MdcServiceSpi correlationId(final String value);

    String correlationId();

    MdcServiceSpi country(final String value);

    String country();

    MdcServiceSpi customerId(final String value);

    String customerId();

    MdcServiceSpi deviceClass(final String value);

    String deviceClass();

    MdcServiceSpi deviceIdentifier(final String value);

    String deviceIdentifier();

    MdcServiceSpi deviceIp(final String value);

    String deviceIp();

    MdcServiceSpi deviceNetworkSpeedDown(final double value);

    double deviceNetworkSpeedDown();

    MdcServiceSpi deviceNetworkSpeedLatency(final double value);

    double deviceNetworkSpeedLatency();

    MdcServiceSpi deviceNetworkSpeedUp(final double value);

    double deviceNetworkSpeedUp();

    MdcServiceSpi deviceNetworkType(final String value);

    String deviceNetworkType();

    MdcServiceSpi deviceType(final String value);

    String deviceType();

    MdcServiceSpi duration(final long value);

    long duration();

    MdcServiceSpi env(final String value);

    String env();

    MdcServiceSpi errorCategory(final String value);

    String errorCategory();

    MdcServiceSpi errorCode(final String value);

    boolean hasError();

    MdcServiceSpi errorCode(final ErrorCode errorCode);

    MdcServiceSpi errorCodeRemove();

    ErrorCode errorCode();

    MdcServiceSpi errorExploitationError(final boolean value);

    boolean errorExploitationError();

    MdcServiceSpi errorField(final String... value);

    String errorField();

    MdcServiceSpi errorMessage(final String value);

    String errorMessage();

    MdcServiceSpi errorMessageDetail(final String value);

    String errorMessageDetail();

    MdcServiceSpi errorRetryable(final boolean value);

    boolean errorRetryable();

    MdcServiceSpi errorRollback(final boolean value);

    boolean errorRollback();

    MdcServiceSpi errorStatus(final int value);

    int errorStatus();

    MdcServiceSpi errorType(final String value);

    String errorType();

    MdcServiceSpi errorUrl(final String value);

    String errorUrl();

    MdcServiceSpi exceptionName(final String value);

    MdcServiceSpi errorDomain(final String value);

    String errorDomain();

    MdcServiceSpi errorSubDomain(final String value);

    String errorSubDomain();

    String exceptionName();

    MdcServiceSpi flags(final String value);

    String flags();

    MdcServiceSpi from(final LocalDateTime value);

    LocalDateTime from();

    MdcServiceSpi groupId(final String value);

    String groupId();

    MdcServiceSpi artifactId(final String value);

    String artifactId();

    MdcServiceSpi commitId(final String value);

    String commitId();

    MdcServiceSpi commitDate(final String value);

    String commitDate();

    MdcServiceSpi fromTimestamp(final long value);

    long fromTimestamp();

    MdcServiceSpi functionalUid(final String value);

    String functionalUid();

    MdcServiceSpi globalStatus(final String value);

    String globalStatus();

    void globalStatusSuccess();

    void globalStatusError();

    MdcServiceSpi removeGlobalStatus();

    MdcServiceSpi healthStatus(final String value);

    String healthStatus();

    MdcServiceSpi hostname(final String value);

    String hostname();

    MdcServiceSpi httpStatus(final int value);

    int httpStatus();

    MdcServiceSpi instanceName(final String value);

    String instanceName();

    MdcServiceSpi instanceNumber(final String value);

    String instanceNumber();

    MdcServiceSpi ioinfoIoLog(final IoInfoDTO info);

    MdcServiceSpi removeIoinfoIoLog();

    MdcServiceSpi ioinfoPartner(final IoInfoDTO info);

    MdcServiceSpi removeIoinfoPartner();

    MdcServiceSpi language(final String value);

    String language();

    MdcServiceSpi lifecycle(final String value);

    String lifecycle();

    MdcServiceSpi lifecycleIn();

    MdcServiceSpi lifecycleOut();

    Exception lifecycleOut(final VoidFunctionWithException function);

    MdcServiceSpi lifecycleRemove();

    MdcServiceSpi majorVersion(final String value);

    String majorVersion();

    MdcServiceSpi messageId(final String value);

    String messageId();

    MdcServiceSpi methodInCause(final String value);

    Map<String, String> getTrackingInformation();

    Map<String, String> getTrackingInformation(final Headers headers);

    MdcServiceSpi orderId(final String value);

    String orderId();

    MdcServiceSpi osVersion(final String value);

    String osVersion();

    MdcServiceSpi parentSpanId(final String value);

    String parentSpanId();

    MdcServiceSpi partner(final String value);

    String partner();

    MdcServiceSpi partnerRequestCharset(final String value);

    MdcServiceSpi partnerRequestCharset(final Charset value);

    Charset partnerRequestCharset();

    MdcServiceSpi partnerResponseCharset(final String value);

    MdcServiceSpi partnerResponseCharset(final Charset value);

    Charset partnerResponseCharset();

    MdcServiceSpi partnerResponseDuration(final long value);

    long partnerResponseDuration();

    MdcServiceSpi partnerResponseMessage(final String value);

    String partnerResponseMessage();

    MdcServiceSpi partnerResponseStatus(final int value);

    int partnerResponseStatus();

    MdcServiceSpi partnerService(final String value);

    String partnerService();

    MdcServiceSpi partnerSubService(final String value);

    String partnerSubService();

    MdcServiceSpi partnerType(final String value);

    String partnerType();

    MdcServiceSpi partnerUrl(final String value);

    String partnerUrl();

    MdcServiceSpi partnerVerb(final String value);

    String partnerVerb();

    MdcServiceSpi price(final double value);

    double price();

    MdcServiceSpi principal(final String value);

    String principal();

    MdcServiceSpi productId(final String value);

    String productId();

    MdcServiceSpi processId(final String value);

    String processId();

    MdcServiceSpi processName(final String value);

    String processName();

    MdcServiceSpi processStatus(final String value);

    String processStatus();

    MdcServiceSpi quantity(final double value);

    double quantity();

    MdcServiceSpi remoteAddress(final String value);

    String remoteAddress();

    MdcServiceSpi requestHeaders(final String value);

    String requestHeaders();

    MdcServiceSpi requestId(final String value);

    String requestId();

    MdcServiceSpi reservationNumber(final String value);

    String reservationNumber();

    MdcServiceSpi responseHeaders(final String value);

    String responseHeaders();

    MdcServiceSpi service(final String value);

    String service();

    MdcServiceSpi sessionId(final String value);

    String sessionId();

    MdcServiceSpi size(final double value);

    double size();

    MdcServiceSpi traceId(final String value);

    String traceId();

    MdcServiceSpi until(final LocalDateTime value);

    LocalDateTime until();

    MdcServiceSpi untilTimestamp(final long value);

    long untilTimestamp();

    MdcServiceSpi uri(final String value);

    String uri();

    MdcServiceSpi url(final String value);

    String url();

    MdcServiceSpi urlPattern(final String value);

    String urlPattern();

    MdcServiceSpi userAgent(final String value);

    String userAgent();

    MdcServiceSpi userId(final String value);

    String userId();

    MdcServiceSpi verb(final String value);

    MdcServiceSpi status(final int status);

    int status();

    String verb();

    MdcServiceSpi version(final String value);

    String version();

    MdcServiceSpi warning(final String value);

    String warning();


}
