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

public class ProcessingRunningException extends TechnicalException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 2707723648196026698L;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProcessingRunningException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ProcessingRunningException() {
    }

    public ProcessingRunningException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessingRunningException(String message, Object... values) {
        super(message, values);
    }

    public ProcessingRunningException(Throwable cause, String message, Object... values) {
        super(cause, message, values);
    }

    public ProcessingRunningException(String message) {
        super(message);
    }

    public ProcessingRunningException(Throwable cause) {
        super(cause);
    }

    public ProcessingRunningException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ProcessingRunningException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ProcessingRunningException(ErrorCode errorCode, Throwable cause, String message, Object... values) {
        super(errorCode, cause, message, values);
    }
}
