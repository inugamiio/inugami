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
package org.inugami.api.exceptions;

import java.util.function.BiConsumer;

public class ErrorCodeBuilder {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private int statusCode;

    private String errorCode;

    private String message;

    private String errorType;

    private String                        payload;
    private BiConsumer<String, Exception> errorHandler;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ErrorCodeBuilder() {

    }

    public ErrorCodeBuilder(final ErrorCode errorCode) {
        if (errorCode != null) {
            this.statusCode   = errorCode.getStatusCode();
            this.errorCode    = errorCode.getErrorCode();
            this.message      = errorCode.getMessage();
            this.errorType    = errorCode.getErrorType();
            this.payload      = errorCode.getPayload();
            this.errorHandler = errorHandler;
        }
    }

    public static ErrorCodeBuilder newBuilder() {
        return new ErrorCodeBuilder();
    }

    public static ErrorCode buildUndefineError() {
        return new ErrorCodeBuilder().setStatusCode(500)
                                     .setErrorCode("undefine error")
                                     .build();
    }

    public ErrorCode build() {
        return new DefaultErrorCode(statusCode,
                                    errorCode,
                                    message,
                                    errorType,
                                    payload,
                                    errorHandler);
    }
    // =========================================================================
    // OVERRIDES
    // =========================================================================

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ErrorCodeBuilder [statusCode=");
        builder.append(statusCode);
        builder.append(", errorCode=");
        builder.append(errorCode);
        builder.append(", message=");
        builder.append(message);
        builder.append(", errorType=");
        builder.append(errorType);
        builder.append(", payload=");
        builder.append(payload);
        builder.append("]");
        return builder.toString();
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public int getStatusCode() {
        return statusCode;
    }

    public ErrorCodeBuilder setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ErrorCodeBuilder setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCodeBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getErrorType() {
        return errorType;
    }

    public ErrorCodeBuilder setErrorType(String errorType) {
        this.errorType = errorType;
        return this;
    }

    public String getPayload() {
        return payload;
    }

    public ErrorCodeBuilder setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public BiConsumer<String, Exception> getErrorHandler() {
        return errorHandler;
    }

    public ErrorCodeBuilder setErrorHandler(BiConsumer<String, Exception> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }
}
