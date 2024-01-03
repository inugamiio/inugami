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
package io.inugami.interfaces.exceptions;

@SuppressWarnings({"java:S5669"})
public class CheckedException extends Exception implements ExceptionWithErrorCode {


    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final    long      serialVersionUID = -7629698090833397665L;
    private final transient ErrorCode errorCode;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CheckedException(final int code, final String message, final Throwable cause) {
        this(DefaultErrorCode.builder().statusCode(code).build(), cause, message);
    }

    public CheckedException(final ErrorCode errorCode, final Throwable cause) {
        super(errorCode == null ? DefaultErrorCode.buildUndefineError().getMessage() : errorCode.getMessage(), cause);
        this.errorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
    }


    public CheckedException() {
        this(null, null, null);
    }

    public CheckedException(final String message, final Throwable cause) {
        this(null, cause, message, null);
    }

    public CheckedException(final String message, final Object... values) {
        this(null, null, message, values);
    }

    public CheckedException(final Throwable cause, final String message, final Object... values) {
        this(null, cause, message, values);
    }

    public CheckedException(final String message) {
        this(null, null, message, null);
    }

    public CheckedException(final Throwable cause) {
        this(null, cause, cause.getMessage(), null);
    }

    public CheckedException(final ErrorCode errorCode) {
        this(errorCode, null, errorCode == null ? null : errorCode.getMessage(), null);
    }

    public CheckedException(final ErrorCode errorCode, final String message) {
        this(errorCode, null, message, null);
    }


    public CheckedException(final ErrorCode errorCode, final Throwable cause, final String message,
                            final Object... values) {
        super(MessagesFormatter.format(message, values), cause);
        this.errorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
    }


    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public int getCode() {
        return errorCode.getStatusCode();
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
