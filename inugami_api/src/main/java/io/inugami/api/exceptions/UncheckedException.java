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
package io.inugami.api.exceptions;

public class UncheckedException extends RuntimeException implements ExceptionWithErrorCode {

    private static final long serialVersionUID = 9117748018680221194L;
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ErrorCode errorCode;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public UncheckedException() {
        this(null, null, null, null);
    }

    public UncheckedException(final String message, final Throwable cause) {
        this(null, cause, message, null);
    }

    public UncheckedException(final String message) {
        this(null, null, message, null);
    }

    public UncheckedException(final Throwable cause) {
        this(null, cause, null, null);
    }

    public UncheckedException(final String message, final Object... values) {
        this(null, null, message, values);
    }

    public UncheckedException(final Throwable cause, final String message, final Object... values) {
        this(null, cause, message, null);
    }

    public UncheckedException(final ErrorCode errorCode, final String message) {
        this(errorCode, null, message, null);
    }

    public UncheckedException(final ErrorCode errorCode, final Throwable cause, final String message,
                              final Object... values) {
        super(MessagesFormatter.format(message, values), cause);
        this.errorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
