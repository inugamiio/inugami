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
public class ConnectorRetryableException extends ConnectorException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 1809398636498204809L;

    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================

    public ConnectorRetryableException(final int code, final String message, final Throwable cause) {
        super(code, message, cause);
    }

    public ConnectorRetryableException(final int code, final String message) {
        super(code, message);
    }

    public ConnectorRetryableException() {
    }

    public ConnectorRetryableException(final ErrorCode errorCode, final Throwable cause, final String message,
                                       final Object... values) {
        super(errorCode, cause, message, values);
    }

    public ConnectorRetryableException(final ErrorCode errorCode, final Throwable cause) {
        super(errorCode, cause);
    }

    public ConnectorRetryableException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public ConnectorRetryableException(final String message, final Object... values) {
        super(message, values);
    }

    public ConnectorRetryableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConnectorRetryableException(final String message) {
        super(message);
    }

    public ConnectorRetryableException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public ConnectorRetryableException(final Throwable cause) {
        super(cause);
    }
}
