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
package io.inugami.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
public class DefaultWarning implements Warning {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @EqualsAndHashCode.Include
    private final String warningCode;
    private final String message;
    private final String messageDetail;
    private final String warningType;


    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public Warning getCurrentWaring() {
        return this;
    }


    @Override
    public String getWarningCode() {
        return warningCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessageDetail() {
        return null;
    }

    @Override
    public String getWarningType() {
        return warningType;
    }
}
