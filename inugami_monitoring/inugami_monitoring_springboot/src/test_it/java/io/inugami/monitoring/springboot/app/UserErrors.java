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
package io.inugami.monitoring.springboot.app;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;

import static io.inugami.api.exceptions.DefaultErrorCode.newBuilder;

public enum UserErrors implements ErrorCode {

    READ_USER_ID_INVALID(newBuilder()
                                 .errorCode("USER-1_0")
                                 .statusCode(400)
                                 .message("user's id isn't valid")
                                 .errorTypeFunctional()
                                 .field("id")
                                 .domain(UserErrors.DOMAIN)
                                 .subDomain("read")
                                 .category("GET")),

    READ_USER_NOT_FOUND(newBuilder()
                                .errorCode("USER-1_1")
                                .statusCode(404)
                                .message("user not found")
                                .errorTypeFunctional()
                                .domain(UserErrors.DOMAIN)
                                .subDomain("read")
                                .category("GET"));

    // =================================================================================================================
    // IMPL
    // =================================================================================================================
    private static final String DOMAIN = "user";

    private UserErrors(final DefaultErrorCode.DefaultErrorCodeBuilder errorBuilder) {
        errorCode = errorBuilder.build();
    }

    private final ErrorCode errorCode;

    @Override
    public ErrorCode getCurrentErrorCode() {
        return errorCode;
    }
}
