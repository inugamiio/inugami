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
package io.inugami.dashboard.api.domain.engine.exception;

import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;

import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.newBuilder;

public enum EngineErrors implements ErrorCode {
    WORKSPACE_UNDEFINED(newBuilder().errorCode("ENGINE-0_0")
                                    .message("workspace not defined")
                                    .statusCode(500)
                                    .errorTypeTechnical()),
    WORKSPACE_NOT_EXISTS(newBuilder().errorCode("ENGINE-0_1")
                                     .message("workspace isn't folder")
                                     .statusCode(500)
                                     .errorTypeTechnical()),
    WORKSPACE_NOT_FOLDER(newBuilder().errorCode("ENGINE-0_2")
                                     .message("workspace isn't folder")
                                     .statusCode(500)
                                     .errorTypeTechnical()),
    WORKSPACE_CAN_READ(newBuilder().errorCode("ENGINE-0_3")
                                   .message("workspace can't be read")
                                   .statusCode(500)
                                   .errorTypeTechnical()),
    APPLICATION_CONFIG_NOT_EXISTS(newBuilder().errorCode("ENGINE-0_4")
                                              .message("application configuration doesn't exists")
                                              .statusCode(500)
                                              .errorTypeTechnical()),
    APPLICATION_CONFIG_NOT_READABLE(newBuilder().errorCode("ENGINE-0_5")
                                                .message("application configuration can't be read")
                                                .statusCode(500)
                                                .errorTypeTechnical()),
    APPLICATION_CONFIG_ERROR(newBuilder().errorCode("ENGINE-0_6")
                                                .message("error on read application configuration")
                                                .statusCode(500)
                                                .errorTypeTechnical());


    private final ErrorCode errorCode;

    private EngineErrors(final DefaultErrorCode.DefaultErrorCodeBuilder errorBuilder) {
        errorCode = errorBuilder.build();
    }

    @Override
    public ErrorCode getCurrentErrorCode() {
        return errorCode;
    }
}

