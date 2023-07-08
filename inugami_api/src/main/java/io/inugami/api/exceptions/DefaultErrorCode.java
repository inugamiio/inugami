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


import lombok.*;

import java.io.Serializable;
import java.util.function.BiConsumer;

@SuppressWarnings({"java:S1845"})
@ToString
@Getter
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

    private final String  message;
    private final String  messageDetail;
    private final String  errorType;
    private final boolean retryable;
    private final boolean rollback;
    private final String  payload;
    private final String  category;
    private final String  domain;
    private final String  subDomain;
    private final String  url;
    private final boolean exploitationError;

    private final String field;

    private final transient BiConsumer<String, Exception> errorHandler;

    public static ErrorCode buildUndefineError() {
        return DefaultErrorCode.builder()
                               .statusCode(500)
                               .errorCode("err-undefine")
                               .errorTypeTechnical()
                               .build();
    }

    public static DefaultErrorCode.DefaultErrorCodeBuilder buildUndefineErrorCode() {
        return fromErrorCode(buildUndefineError());
    }

    public static DefaultErrorCode.DefaultErrorCodeBuilder fromErrorCode(final ErrorCode errorCode) {
        final DefaultErrorCodeBuilder result           = DefaultErrorCode.builder();
        final ErrorCode               currentErrorCode = errorCode == null ? buildUndefineError() : errorCode;


        return result.errorCode(currentErrorCode.getErrorCode())
                     .statusCode(currentErrorCode.getStatusCode())
                     .message(currentErrorCode.getMessage())
                     .errorType(currentErrorCode.getErrorType())
                     .payload(currentErrorCode.getPayload())
                     .errorHandler(currentErrorCode.getErrorHandler())
                     .exploitationError(currentErrorCode.isExploitationError())
                     .retryable(currentErrorCode.isRetryable())
                     .rollback(currentErrorCode.isRollbackRequire())
                     .category(currentErrorCode.getCategory())
                     .field(currentErrorCode.getField())
                     .url(currentErrorCode.getUrl())
                     .domain(currentErrorCode.getDomain())
                     .subDomain(currentErrorCode.getSubDomain());
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

        public DefaultErrorCodeBuilder exploitationError() {
            this.exploitationError = true;
            return this;
        }

        public DefaultErrorCodeBuilder exploitationError(final boolean value) {
            this.exploitationError = value;
            return this;
        }

        public DefaultErrorCodeBuilder addMessageDetail(final String message, final Object... values) {
            if (message != null) {
                this.messageDetail = MessagesFormatter.format(message, values);
            }
            return this;
        }

        public DefaultErrorCodeBuilder rollbackRequire() {
            this.rollback = true;
            return this;
        }


        public DefaultErrorCodeBuilder isRetryable() {
            this.retryable = true;
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

    @Override
    public boolean isExploitationError() {
        return exploitationError;
    }

    @Override
    public boolean isRollbackRequire() {
        return rollback;
    }

    @Override
    public boolean isRetryable() {
        return retryable;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public String getSubDomain() {
        return subDomain;
    }


}
