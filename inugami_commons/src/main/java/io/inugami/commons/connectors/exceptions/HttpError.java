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
package io.inugami.commons.connectors.exceptions;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;

import static io.inugami.api.exceptions.DefaultErrorCode.newBuilder;

public enum HttpError implements ErrorCode {
    REQUEST_REQUIRE(newBuilder()
                            .statusCode(500)
                            .errorCode("HTTP-1")
                            .message("request is require")
    );


    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ErrorCode errorCode;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private HttpError(DefaultErrorCode.DefaultErrorCodeBuilder builder) {
        errorCode = builder.build();
    }

    // =========================================================================
    // API
    // =========================================================================
    @Override
    public ErrorCode getCurrentErrorCode() {
        return errorCode;
    }
}
