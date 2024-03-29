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
package io.inugami.configuration.exceptions;

/**
 * NotApplicationConfigurationException
 *
 * @author patrick_guillerm
 * @since 15 déc. 2017
 */
@SuppressWarnings({"java:S110"})
public class NotApplicationConfigurationException extends ConfigurationException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -4596687159150903509L;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public NotApplicationConfigurationException() {
        super();
    }

    public NotApplicationConfigurationException(final String message, final Object... values) {
        super(message, values);
    }

    public NotApplicationConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotApplicationConfigurationException(final String message) {
        super(message);
    }

    public NotApplicationConfigurationException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public NotApplicationConfigurationException(final Throwable cause) {
        super(cause);
    }

}
