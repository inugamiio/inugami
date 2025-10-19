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
package io.inugami.framework.interfaces.database;

import java.util.List;
import java.util.Map;

public interface PropertiesConvertorSpi {
    boolean accept(final String type);

    Map<String, String> convert(final String content);

    default boolean matchType(final String type, final List<String> acceptedTypes) {
        if (type == null || acceptedTypes == null) {
            return false;
        }

        return acceptedTypes.stream()
                            .anyMatch(acceptedType -> acceptedType.equalsIgnoreCase(type));
    }
}
