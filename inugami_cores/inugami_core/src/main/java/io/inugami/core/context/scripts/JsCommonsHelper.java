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
package io.inugami.core.context.scripts;

import lombok.experimental.UtilityClass;

/**
 * JsCommonsHelper
 *
 * @author patrick_guillerm
 * @since 27 déc. 2017
 */
@UtilityClass
class JsCommonsHelper {
    // =========================================================================
    // VALUES
    // =========================================================================
    static final String UNDEFINED = "undefined";

    // =========================================================================
    // NOT NULL
    // =========================================================================
    static boolean isNotNull(final String value) {
        return (value != null) && !value.trim().isEmpty() && !UNDEFINED.equals(value);
    }

    // =========================================================================
    // CONVERT
    // =========================================================================
    static String convertStrValue(final String value) {
        return ((value == null) || UNDEFINED.equals(value)) ? null : value;
    }
}
