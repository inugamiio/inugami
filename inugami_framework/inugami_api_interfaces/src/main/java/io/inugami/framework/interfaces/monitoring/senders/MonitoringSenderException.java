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
package io.inugami.framework.interfaces.monitoring.senders;

import io.inugami.framework.interfaces.exceptions.TechnicalException;

/**
 * MonitoringProviderException
 *
 * @author patrick_guillerm
 * @since 27 déc. 2018
 */
public class MonitoringSenderException extends TechnicalException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -2891954936658630035L;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public MonitoringSenderException() {
        super();
    }

    public MonitoringSenderException(String message, Object... values) {
        super(message, values);
    }

    public MonitoringSenderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MonitoringSenderException(String message) {
        super(message);
    }

    public MonitoringSenderException(Throwable cause, String message, Object... values) {
        super(cause, message, values);
    }

    public MonitoringSenderException(Throwable cause) {
        super(cause);
    }

}
