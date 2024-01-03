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
package io.inugami.interfaces.exceptions.connector;

import io.inugami.interfaces.exceptions.ErrorCode;
import io.inugami.interfaces.exceptions.services.ConnectorException;

/**
 * ConnectorMarshallingException
 *
 * @author patrick_guillerm
 * @since 2022-12-04
 */
@SuppressWarnings({"java:S110"})
public class ConnectorForbiddenException extends ConnectorException {


    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -8966394669967388453L;

    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================

    public ConnectorForbiddenException(final int code, final String message, final Throwable cause) {
        super(code, message, cause);
    }

    public ConnectorForbiddenException(final int code, final String message) {
        super(code, message);
    }

    public ConnectorForbiddenException() {
    }

    public ConnectorForbiddenException(final ErrorCode errorCode, final Throwable cause, final String message,
                                       final Object... values) {
        super(errorCode, cause, message, values);
    }

    public ConnectorForbiddenException(final ErrorCode errorCode, final Throwable cause) {
        super(errorCode, cause);
    }

    public ConnectorForbiddenException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public ConnectorForbiddenException(final String message, final Object... values) {
        super(message, values);
    }

    public ConnectorForbiddenException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConnectorForbiddenException(final String message) {
        super(message);
    }

    public ConnectorForbiddenException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public ConnectorForbiddenException(final Throwable cause) {
        super(cause);
    }
}
