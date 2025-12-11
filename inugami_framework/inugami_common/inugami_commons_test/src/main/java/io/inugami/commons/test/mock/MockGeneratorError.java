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
package io.inugami.commons.test.mock;

import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;

import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.newBuilder;

public enum MockGeneratorError implements ErrorCode {
    CONTEXT_REQUIRED(newBuilder().errorCode("MOCK_GEN-0_0")
                                    .message("Context required to generate mocks and OpenApi")
                                    .statusCode(500)
                                    .errorTypeTechnical()),
    REST_CLIENT_CLASS_REQUIRED(newBuilder().errorCode("MOCK_GEN-0_1")
                                 .message("Rest client class is required to generate OpenApi documentation")
                                 .statusCode(500)
                                 .errorTypeTechnical()),
    ENDPOINT_MAVEN_MODULE_PATH_REQUIRED(newBuilder().errorCode("MOCK_GEN-0_2")
                                           .message("Endpoint maven module path is required")
                                           .statusCode(500)
                                           .errorTypeTechnical()),
    ENDPOINT_MAVEN_MODULE_NOT_READABLE(newBuilder().errorCode("MOCK_GEN-0_3")
                                                    .message("Endpoint maven module can't be read")
                                                    .statusCode(500)
                                                    .errorTypeTechnical()),
    ENDPOINT_MAVEN_MODULE_NOT_WRITABLE(newBuilder().errorCode("MOCK_GEN-0_4")
                                                   .message("Endpoint maven module can't be write")
                                                   .statusCode(500)
                                                   .errorTypeTechnical())
    ;


    private final ErrorCode errorCode;

    MockGeneratorError(final DefaultErrorCode.DefaultErrorCodeBuilder errorBuilder) {
        errorCode = errorBuilder.build();
    }

    @Override
    public ErrorCode getCurrentErrorCode() {
        return errorCode;
    }
}
