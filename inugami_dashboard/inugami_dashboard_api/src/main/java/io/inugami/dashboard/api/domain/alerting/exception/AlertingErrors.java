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
package io.inugami.dashboard.api.domain.alerting.exception;

import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;

import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.newBuilder;

public enum AlertingErrors implements ErrorCode {
    UNDEFINED(newBuilder().errorCode("ALERTING-0_0")
                          .message("Undefined error occurs")
                          .statusCode(500)
                          .errorTypeTechnical()),

    CREATE_INVALID_DATA(newBuilder().errorCode("ALERTING-1_0")
                                    .message("Alerting data are invalid")
                                    .statusCode(400)
                                    .errorTypeFunctional()),

    READ_INVALID_DATA(newBuilder().errorCode("ALERTING-2_0")
                                  .message("Alerting data are invalid")
                                  .statusCode(400)
                                  .errorTypeFunctional()),
    READ_NOT_FOUND(newBuilder().errorCode("ALERTING-2_1")
                                  .message("Alerting not found")
                                  .statusCode(404)
                                  .errorTypeFunctional()),

    UPDATE_INVALID_DATA(newBuilder().errorCode("ALERTING-3_0")
                                    .message("Alerting data are invalid")
                                    .statusCode(400)
                                    .errorTypeFunctional()),

    DELETE_INVALID_DATA(newBuilder().errorCode("ALERTING-4_0")
                                    .message("Alerting data are invalid")
                                    .statusCode(400)
                                    .errorTypeFunctional());

    private final ErrorCode errorCode;

    private AlertingErrors(final DefaultErrorCode.DefaultErrorCodeBuilder errorBuilder) {
        errorCode = errorBuilder.errorCode(this.name()).build();
    }

    @Override
    public ErrorCode getCurrentErrorCode() {
        return errorCode;
    }
}
