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

public class WarningBuilder {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String warningCode;
    private String message;
    private String warningType;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public WarningBuilder() {
    }

    public WarningBuilder(String warningCode,
                          String message,
                          String warningType) {
        this.warningCode = warningCode;
        this.message     = message;
        this.warningType = warningType;
    }

    public WarningBuilder(final Warning warning) {
        if (warning != null) {
            this.warningCode = warning.getWarningCode();
            this.message     = warning.getMessage();
            this.warningType = warning.getWarningType();
        }
    }

    public Warning build(){
        return new DefaultWarning(warningCode,message,warningType);
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarningBuilder{");
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

    public String getWarningCode() {
        return warningCode;
    }

    public WarningBuilder setWarningCode(String warningCode) {
        this.warningCode = warningCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public WarningBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getWarningType() {
        return warningType;
    }

    public WarningBuilder setWarningType(String warningType) {
        this.warningType = warningType;
        return this;
    }
}
