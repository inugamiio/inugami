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
package org.inugami.api.exceptions;

import java.util.Objects;

public class DefaultWarning implements Warning{

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String warningCode;
    private final String message;
    private final String warningType;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DefaultWarning(String warningCode,
                          String message,
                          String warningType) {
        this.warningCode = warningCode;
        this.message     = message;
        this.warningType = warningType;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultWarning that = (DefaultWarning) o;
        return Objects.equals(warningCode,
                              that.warningCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warningCode);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultWarning{");
        sb.append("warningCode='")
          .append(warningCode)
          .append('\'');
        sb.append(", message='")
          .append(message)
          .append('\'');
        sb.append(", warningType='")
          .append(warningType)
          .append('\'');
        sb.append('}');
        return sb.toString();
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getWarningCode() {
        return warningCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getWarningType() {
        return warningType;
    }
}
