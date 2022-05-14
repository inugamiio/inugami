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

public interface Warning {
    Warning getCurrentWaring();

    default String getWarningCode() {
        return getCurrentWaring() == null ? "warn" : getCurrentWaring().getWarningCode();
    }

    default String getMessage() {
        return getCurrentWaring() == null ? null : getCurrentWaring().getMessage();
    }

    default String getMessageDetail() {
        return getCurrentWaring() == null ? null : getCurrentWaring().getMessageDetail();
    }

    default String getWarningType() {
        return getCurrentWaring() == null ? "functional" : getCurrentWaring().getWarningType();
    }

    default Warning addDetail(final String detail, Object... values) {
        return toBuilder().addMessageDetail(detail, values).build();
    }
    default DefaultWarning.DefaultWarningBuilder toBuilder() {
        DefaultWarning.DefaultWarningBuilder  builder = null;
        if ( getCurrentWaring() == null) {
            builder = DefaultWarning.builder();
        }
        else {
            builder = DefaultWarning.fromWarningCode(getCurrentWaring());
        }
        return builder;
    }
}
