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
package io.inugami.framework.interfaces.marshalling;

import io.inugami.framework.interfaces.exceptions.CheckedException;
import io.inugami.framework.interfaces.exceptions.ErrorCode;

public class JsonMarshallerSPIException extends CheckedException {
    public JsonMarshallerSPIException(final int code, final String message, final Throwable cause) {
        super(code, message, cause);
    }

    public JsonMarshallerSPIException(final ErrorCode errorCode, final Throwable cause) {
        super(errorCode, cause);
    }

    public JsonMarshallerSPIException() {
    }

    public JsonMarshallerSPIException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JsonMarshallerSPIException(final String message, final Object... values) {
        super(message, values);
    }

    public JsonMarshallerSPIException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public JsonMarshallerSPIException(final String message) {
        super(message);
    }

    public JsonMarshallerSPIException(final Throwable cause) {
        super(cause);
    }

    public JsonMarshallerSPIException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public JsonMarshallerSPIException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }

    public JsonMarshallerSPIException(final ErrorCode errorCode,
                                      final Throwable cause,
                                      final String message,
                                      final Object... values) {
        super(errorCode, cause, message, values);
    }
}
