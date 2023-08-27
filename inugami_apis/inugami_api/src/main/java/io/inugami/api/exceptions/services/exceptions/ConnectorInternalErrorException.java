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
public class ConnectorInternalErrorException extends ConnectorException {


    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -3651438599463990816L;


    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================

    public ConnectorInternalErrorException(final int code, final String message, final Throwable cause) {
        super(code, message, cause);
    }

    public ConnectorInternalErrorException(final int code, final String message) {
        super(code, message);
    }

    public ConnectorInternalErrorException() {
    }

    public ConnectorInternalErrorException(final ErrorCode errorCode, final Throwable cause, final String message,
                                           final Object... values) {
        super(errorCode, cause, message, values);
    }

    public ConnectorInternalErrorException(final ErrorCode errorCode, final Throwable cause) {
        super(errorCode, cause);
    }

    public ConnectorInternalErrorException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public ConnectorInternalErrorException(final String message, final Object... values) {
        super(message, values);
    }

    public ConnectorInternalErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConnectorInternalErrorException(final String message) {
        super(message);
    }

    public ConnectorInternalErrorException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public ConnectorInternalErrorException(final Throwable cause) {
        super(cause);
    }
}