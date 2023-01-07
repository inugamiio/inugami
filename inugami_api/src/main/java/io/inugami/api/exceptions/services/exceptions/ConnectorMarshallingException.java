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
public class ConnectorMarshallingException extends ConnectorException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -7033618161331116635L;
    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================

    public ConnectorMarshallingException(final int code, final String message, final Throwable cause) {
        super(code, message, cause);
    }

    public ConnectorMarshallingException() {
    }

    public ConnectorMarshallingException(final ErrorCode errorCode, final Throwable cause,
                                         final String message, final Object... values) {
        super(errorCode, cause, message, values);
    }

    public ConnectorMarshallingException(final ErrorCode errorCode, final Throwable cause) {
        super(errorCode, cause);
    }

    public ConnectorMarshallingException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public ConnectorMarshallingException(final String message, final Object... values) {
        super(message, values);
    }

    public ConnectorMarshallingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConnectorMarshallingException(final String message) {
        super(message);
    }

    public ConnectorMarshallingException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public ConnectorMarshallingException(final Throwable cause) {
        super(cause);
    }
}
