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
package io.inugami.api.exceptions;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public interface ErrorCode {


    String STATUS_CODE        = "statusCode";
    String ERROR_CODE         = "errorCode";
    String MESSAGE            = "message";
    String MESSAGE_DETAIL     = "messageDetail";
    String ERROR_TYPE         = "errorType";
    String PAYLOAD            = "payload";
    String EXPLOITATION_ERROR = "exploitationError";
    String ROLLBACK           = "rollback";
    String RETRYABLE          = "retryable";
    String FIELD              = "field";
    String URL                = "url";
    String DOMAIN             = "domain";
    String SUB_DOMAIN         = "subDomain";

    public ErrorCode getCurrentErrorCode();

    default int getStatusCode() {
        return getCurrentErrorCode() == null ? 500 : getCurrentErrorCode().getStatusCode();
    }


    default String getErrorCode() {
        return getCurrentErrorCode() == null ? "undefine" : getCurrentErrorCode().getErrorCode();
    }


    default String getMessage() {
        return getCurrentErrorCode() == null ? "error" : getCurrentErrorCode().getMessage();
    }


    default String getMessageDetail() {
        return getCurrentErrorCode() == null ? null : getCurrentErrorCode().getMessageDetail();
    }

    default boolean isExploitationError() {
        return getCurrentErrorCode() == null ? false : getCurrentErrorCode().isExploitationError();
    }

    default String getErrorType() {
        return getCurrentErrorCode() == null ? "technical" : getCurrentErrorCode().getErrorType();
    }


    default String getDomain() {
        return getCurrentErrorCode() == null ? null : getCurrentErrorCode().getDomain();
    }

    default String getSubDomain() {
        return getCurrentErrorCode() == null ? null : getCurrentErrorCode().getSubDomain();
    }

    default String getPayload() {
        return getCurrentErrorCode() == null ? null : getCurrentErrorCode().getPayload();
    }

    default String getUrl() {
        return getCurrentErrorCode() == null ? null : getCurrentErrorCode().getUrl();
    }


    default boolean isRollbackRequire() {
        return getCurrentErrorCode() == null ? false : getCurrentErrorCode().isRollbackRequire();
    }

    default boolean isRetryable() {
        return getCurrentErrorCode() == null ? false : getCurrentErrorCode().isRetryable();
    }

    default BiConsumer<String, Exception> getErrorHandler() {
        return getCurrentErrorCode() == null ? null : getCurrentErrorCode().getErrorHandler();
    }

    default ErrorCode addDetail(final String detail, final Object... values) {
        return toBuilder().addMessageDetail(detail, values).build();
    }

    default String getCategory() {
        return getCurrentErrorCode() == null ? null : getCurrentErrorCode().getCategory();
    }

    default String getField() {
        return getCurrentErrorCode() == null ? null : getCurrentErrorCode().getField();
    }


    default DefaultErrorCode.DefaultErrorCodeBuilder toBuilder() {
        DefaultErrorCode.DefaultErrorCodeBuilder builder = null;
        if (getCurrentErrorCode() == null) {
            builder = DefaultErrorCode.fromErrorCode(DefaultErrorCode.buildUndefineError());
        } else {
            builder = DefaultErrorCode.fromErrorCode(getCurrentErrorCode());
        }
        return builder;
    }

    default Map<String, Serializable> toMap() {
        final Map<String, Serializable> result = new LinkedHashMap<>();
        result.put(STATUS_CODE, getStatusCode());
        result.put(ERROR_CODE, getErrorCode());
        result.put(MESSAGE, getMessage());
        result.put(MESSAGE_DETAIL, getMessageDetail());
        result.put(ERROR_TYPE, getErrorType());
        result.put(PAYLOAD, getPayload());
        result.put(EXPLOITATION_ERROR, isExploitationError());
        result.put(ROLLBACK, isRollbackRequire());
        result.put(RETRYABLE, isRetryable());
        result.put(FIELD, getField());
        result.put(URL, getUrl());
        result.put(DOMAIN, getDomain());
        result.put(SUB_DOMAIN, getSubDomain());
        return result;
    }
}

