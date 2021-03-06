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


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.function.BiConsumer;

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
public class DefaultErrorCode implements Serializable, ErrorCode {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -3438361352478852714L;

    private final int    statusCode;
    @EqualsAndHashCode.Include
    private final String errorCode;

    private final String message;
    private final String messageDetail;
    private final String errorType;

    private final String payload;

    private final BiConsumer<String, Exception> errorHandler;

    public static ErrorCode buildUndefineError() {
        return DefaultErrorCode.builder().statusCode(500).errorCode("err-undefine").build();
    }

    public static DefaultErrorCode.DefaultErrorCodeBuilder newBuilder() {
        return DefaultErrorCode.builder();
    }

    public static class DefaultErrorCodeBuilder {
        public DefaultErrorCodeBuilder errorTypeTechnical() {
            this.errorType = "technical";
            return this;
        }

        public DefaultErrorCodeBuilder errorTypeFunctional() {
            this.errorType = "functional";
            return this;
        }

        public DefaultErrorCodeBuilder errorTypeConfiguration() {
            this.errorType = "configuration";
            return this;
        }

        public DefaultErrorCodeBuilder errorTypeSecurity() {
            this.errorType = "security";
            return this;
        }
    }

    @Override
    public ErrorCode getCurrentErrorCode() {
        return this;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessageDetail() {
        return messageDetail;
    }

    @Override
    public String getErrorType() {
        return errorType;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public BiConsumer<String, Exception> getErrorHandler() {
        return errorHandler;
    }
}
