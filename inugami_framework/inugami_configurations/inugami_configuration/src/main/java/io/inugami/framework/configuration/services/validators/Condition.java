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
package io.inugami.framework.configuration.services.validators;

/**
 * Condition
 *
 * @author patrick_guillerm
 * @since 29 déc. 2016
 */
public class Condition {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    final boolean error;

    final String message;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public Condition(final String message, final boolean hasError) {
        super();
        this.error = hasError;
        this.message = message;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public boolean hasError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
