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
package io.inugami.framework.commons.threads;

import io.inugami.framework.interfaces.exceptions.TechnicalException;

/**
 * CancelledTaskException
 *
 * @author patrick_guillerm
 * @since 23 mars 2018
 */
public class CancelledTaskException extends TechnicalException {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long serialVersionUID = 2736920579285310134L;

    // =================================================================================================================
    // CONSTRUCTORS
    // =================================================================================================================
    public CancelledTaskException() {
        super();
    }

    public CancelledTaskException(final String message, final Object... values) {
        super(message, values);
    }

    public CancelledTaskException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CancelledTaskException(final String message) {
        super(message);
    }

    public CancelledTaskException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public CancelledTaskException(final Throwable cause) {
        super(cause);
    }
}
