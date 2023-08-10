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
package io.inugami.api.exceptions.services;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.TechnicalException;
import lombok.Getter;
import lombok.Setter;

/**
 * TechnicalException
 *
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */
@SuppressWarnings({"java:S1165"})
@Setter
@Getter
public class ConnectorException extends TechnicalException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long   serialVersionUID = 389031756408740003L;
    private final        int    code;
    private transient    Object result;

    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================
    public ConnectorException(final int code, final String message, final Throwable cause) {
        super(code, message, cause);
        this.code = code;
    }

    public ConnectorException(final int code, final String message) {
        super(code, message, null);
        this.code = code;
    }

    public ConnectorException() {
        super();
        this.code = 500;
    }

    public ConnectorException(ErrorCode errorCode, Throwable cause, String message, Object... values) {
        super(errorCode, cause, message, values);
        this.code = errorCode == null ? 500 : errorCode.getStatusCode();
    }

    public ConnectorException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
        this.code = errorCode == null ? 500 : errorCode.getStatusCode();
    }

    public ConnectorException(ErrorCode errorCode) {
        super(errorCode);
        this.code = errorCode == null ? 500 : errorCode.getStatusCode();
    }

    public ConnectorException(String message, Object... values) {
        super(message, values);
        this.code = 500;
    }

    public ConnectorException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

    public ConnectorException(String message) {
        super(message);
        this.code = 500;
    }

    public ConnectorException(Throwable cause, String message, Object... values) {
        super(cause, message, values);
        this.code = 500;
    }

    public ConnectorException(Throwable cause) {
        super(cause);
        this.code = 500;
    }


}
