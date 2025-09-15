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
package io.inugami.framework.interfaces.models.search;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MatchType {
    EQUALS,
    CONTAINS,
    START_WITH,
    END_WITH,
    LESS,
    HIGHER,
    LESS_OR_EQUALS,
    HIGHER_OR_EQUALS,
    LOWER_OR_EQUALS,
    BETWEEN,
    IN;


    private static final List<MatchType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

    public static List<MatchType> getTypes() {
        return VALUES;
    }

    public static MatchType getEnum(final String value) {
        if (value == null) {
            return null;
        }
        final String currentValue = value.trim().toUpperCase();
        
        return VALUES.stream()
                     .filter(item -> item.name().equals(currentValue))
                     .findFirst()
                     .orElse(null);
    }
}
