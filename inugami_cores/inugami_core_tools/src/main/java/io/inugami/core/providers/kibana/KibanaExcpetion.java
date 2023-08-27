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
package io.inugami.core.providers.kibana;

import io.inugami.api.exceptions.services.ProviderException;

/**
 * KibanaExcpetion
 *
 * @author patrick_guillerm
 * @since 20 sept. 2017
 */
@SuppressWarnings({"java:S110"})
public class KibanaExcpetion extends ProviderException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -8151818842544465506L;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public KibanaExcpetion() {
        super();
    }

    public KibanaExcpetion(final String message, final Object... values) {
        super(message, values);
    }

    public KibanaExcpetion(final String message, final Throwable cause) {
        super(message, cause);
    }

    public KibanaExcpetion(final String message) {
        super(message);
    }

    public KibanaExcpetion(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public KibanaExcpetion(final Throwable cause) {
        super(cause);
    }
}
