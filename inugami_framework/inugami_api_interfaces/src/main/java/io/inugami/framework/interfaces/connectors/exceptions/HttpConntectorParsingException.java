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
package io.inugami.framework.interfaces.connectors.exceptions;

import io.inugami.framework.interfaces.exceptions.CheckedException;
import io.inugami.framework.interfaces.exceptions.ErrorCode;

public class HttpConntectorParsingException extends CheckedException {
    public HttpConntectorParsingException(final int code, final String message, final Throwable cause) {
        super(code, message, cause);
    }

    public HttpConntectorParsingException(final ErrorCode errorCode,
                                          final Throwable cause) {
        super(errorCode, cause);
    }

    public HttpConntectorParsingException() {
    }

    public HttpConntectorParsingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HttpConntectorParsingException(final String message, final Object... values) {
        super(message, values);
    }

    public HttpConntectorParsingException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public HttpConntectorParsingException(final String message) {
        super(message);
    }

    public HttpConntectorParsingException(final Throwable cause) {
        super(cause);
    }

    public HttpConntectorParsingException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public HttpConntectorParsingException(final ErrorCode errorCode,
                                          final String message) {
        super(errorCode, message);
    }

    public HttpConntectorParsingException(final ErrorCode errorCode,
                                          final Throwable cause,
                                          final String message,
                                          final Object... values) {
        super(errorCode, cause, message, values);
    }
}
