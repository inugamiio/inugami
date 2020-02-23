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

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiConsumer;

public class DefaultErrorCode implements Serializable, ErrorCode {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -3438361352478852714L;

    private final int statusCode;

    private final String errorCode;

    private final String message;

    private final String errorType;

    private final String payload;

    private final BiConsumer<String, Exception> errorHandler;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DefaultErrorCode(final int statusCode,
                            final String errorCode,
                            final String message,
                            final String errorType,
                            final String payload,
                            final BiConsumer<String, Exception> errorHandler) {
        super();
        this.statusCode   = statusCode;
        this.errorCode    = errorCode;
        this.message      = message;
        this.errorType    = errorType;
        this.payload      = payload;
        this.errorHandler = errorHandler;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;

        if (!result && obj != null && obj instanceof ErrorCode) {
            DefaultErrorCode other = (DefaultErrorCode) obj;
            result = Objects.equals(errorCode,
                                    other.getErrorCode());
        }

        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DefaultErrorCode [statusCode=");
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

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getPayload() {
        return payload;
    }

    public BiConsumer<String, Exception> getErrorHandler() {
        return errorHandler;
    }


}
