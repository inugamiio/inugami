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
import lombok.ToString;

@ToString
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
public final class DefaultWarning implements Warning {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @EqualsAndHashCode.Include
    private final String warningCode;
    private final String message;
    private final String messageDetail;
    private final String warningType;
    private final String category;
    private final String domain;
    private final String subDomain;

    public static DefaultWarningBuilder fromWarningCode(final Warning warning) {
        final DefaultWarningBuilder builder = builder();
        if (warning != null) {
            builder.warningCode(warning.getWarningCode());
            builder.message(warning.getMessage());
            builder.messageDetail(warning.getMessageDetail());
            builder.warningType(warning.getWarningType());
            builder.category(warning.getCategory());
            builder.domain(warning.getDomain());
            builder.subDomain(warning.getSubDomain());
        }
        return builder;
    }

    public static class DefaultWarningBuilder {
        public DefaultWarning.DefaultWarningBuilder addMessageDetail(final String message, final Object... values) {
            if (message != null) {
                this.messageDetail = MessagesFormatter.format(message, values);
            }
            return this;
        }

        public DefaultWarning.DefaultWarningBuilder typeFunctional() {
            this.warningType = "functional";
            return this;
        }

        public DefaultWarning.DefaultWarningBuilder typeTechnical() {
            this.warningType = "technical";
            return this;
        }

        public DefaultWarning.DefaultWarningBuilder typeSecurity() {
            this.warningType = "security";
            return this;
        }

        public DefaultWarning.DefaultWarningBuilder typeFeature() {
            this.warningType = "feature";
            return this;
        }
    }

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
        return messageDetail;
    }

    @Override
    public String getWarningType() {
        return warningType;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public String getSubDomain() {
        return subDomain;
    }
}
