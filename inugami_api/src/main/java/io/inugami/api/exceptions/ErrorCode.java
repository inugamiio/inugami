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

import java.util.function.BiConsumer;

public interface ErrorCode {


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


    default String getPayload() {
        return getCurrentErrorCode() == null ? null : getCurrentErrorCode().getPayload();
    }


    default BiConsumer<String, Exception> getErrorHandler() {
        return getCurrentErrorCode() == null ? null : getCurrentErrorCode().getErrorHandler();
    }

    default ErrorCode addDetail(final String detail, Object... values) {
        return toBuilder().addMessageDetail(detail, values).build();
    }


    default DefaultErrorCode.DefaultErrorCodeBuilder toBuilder() {
        DefaultErrorCode.DefaultErrorCodeBuilder builder = null;
        if (getCurrentErrorCode() == null) {
            builder = DefaultErrorCode.fromErrorCode(DefaultErrorCode.buildUndefineError());
        }
        else {
            builder = DefaultErrorCode.fromErrorCode(getCurrentErrorCode());
        }
        return builder;
    }

}
