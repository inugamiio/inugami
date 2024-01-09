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

public class YamlProcessingException extends CheckedException {
    public YamlProcessingException(final int code, final String message, final Throwable cause) {
        super(code, message, cause);
    }

    public YamlProcessingException(final ErrorCode errorCode,
                                   final Throwable cause) {
        super(errorCode, cause);
    }

    public YamlProcessingException() {
    }

    public YamlProcessingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public YamlProcessingException(final String message, final Object... values) {
        super(message, values);
    }

    public YamlProcessingException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public YamlProcessingException(final String message) {
        super(message);
    }

    public YamlProcessingException(final Throwable cause) {
        super(cause);
    }

    public YamlProcessingException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public YamlProcessingException(final ErrorCode errorCode,
                                   final String message) {
        super(errorCode, message);
    }

    public YamlProcessingException(final ErrorCode errorCode,
                                   final Throwable cause,
                                   final String message,
                                   final Object... values) {
        super(errorCode, cause, message, values);
    }
}
