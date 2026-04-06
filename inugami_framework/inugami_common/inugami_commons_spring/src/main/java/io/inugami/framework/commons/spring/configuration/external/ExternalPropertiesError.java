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
package io.inugami.framework.commons.spring.configuration.external;

import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;

import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.newBuilder;

public enum ExternalPropertiesError  implements ErrorCode {
        EXTERNAL_PROPERTIES_FOLDER_NOT_EXISTS(newBuilder().errorCode("EXT-PROP-0_0")
                                                          .message("external properties folder not exists")
                                                          .statusCode(500)
                                                          .errorTypeTechnical())
    ;
    private final ErrorCode errorCode;

    private ExternalPropertiesError(final DefaultErrorCode.DefaultErrorCodeBuilder errorBuilder) {
        errorCode = errorBuilder.build();
    }

    @Override
    public ErrorCode getCurrentErrorCode() {
        return errorCode;
    }
}
