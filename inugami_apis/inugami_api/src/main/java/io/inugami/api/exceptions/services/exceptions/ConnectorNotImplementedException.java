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
package io.inugami.api.exceptions.services.exceptions;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.services.ConnectorException;

/**
 * ConnectorMarshallingException
 *
 * @author patrick_guillerm
 * @since 2022-12-04
 */
@SuppressWarnings({"java:S110"})
public class ConnectorNotImplementedException extends ConnectorException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 2726564879384632480L;


    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================

    public ConnectorNotImplementedException(final int code, final String message, final Throwable cause) {
        super(code, message, cause);
    }

    public ConnectorNotImplementedException(final int code, final String message) {
        super(code, message);
    }

    public ConnectorNotImplementedException() {
    }

    public ConnectorNotImplementedException(final ErrorCode errorCode, final Throwable cause, final String message,
                                            final Object... values) {
        super(errorCode, cause, message, values);
    }

    public ConnectorNotImplementedException(final ErrorCode errorCode, final Throwable cause) {
        super(errorCode, cause);
    }

    public ConnectorNotImplementedException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public ConnectorNotImplementedException(final String message, final Object... values) {
        super(message, values);
    }

    public ConnectorNotImplementedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConnectorNotImplementedException(final String message) {
        super(message);
    }

    public ConnectorNotImplementedException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public ConnectorNotImplementedException(final Throwable cause) {
        super(cause);
    }
}
