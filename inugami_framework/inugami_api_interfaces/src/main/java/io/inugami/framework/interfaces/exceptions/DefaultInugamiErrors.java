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
package io.inugami.framework.interfaces.exceptions;

import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.newBuilder;

public enum DefaultInugamiErrors implements ErrorCode {

    UNDEFINED(newBuilder().errorCode("err-undefine")
                          .errorTypeTechnical()),

    ZIP_BOMB(newBuilder().errorCode("ZIP-0_0")
                         .message("zip file is too big to be unzipped")
                         .errorTypeSecurity()),

    ZIP_SLIP(newBuilder().errorCode("ZIP-0_1")
                         .message("zip slip detected")
                         .errorTypeSecurity()),
    HTTP_SSRF(newBuilder().errorCode("HTTP-0_")
                         .message("SSRF request detected")
                         .errorTypeSecurity());


    private final ErrorCode errorCode;

    private DefaultInugamiErrors(final DefaultErrorCode.DefaultErrorCodeBuilder errorBuilder) {
        errorCode = errorBuilder.build();
    }

    @Override
    public ErrorCode getCurrentErrorCode() {
        return errorCode;
    }
}
