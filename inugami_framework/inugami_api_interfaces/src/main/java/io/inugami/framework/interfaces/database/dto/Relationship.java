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
package io.inugami.framework.interfaces.database.dto;

import lombok.*;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.inugami.framework.interfaces.database.NodeUtils.sortProperties;

@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class Relationship implements  Comparable<Relationship> , Serializable{

    private static final long serialVersionUID = 973646684487506001L;

    @EqualsAndHashCode.Include
    private String                    from;
    @EqualsAndHashCode.Include
    private String                    to;
    @EqualsAndHashCode.Include
    private String                    type;
    private Map<String, Serializable> properties;

    public void sort() {
        properties = sortProperties(properties);
    }

    @Override
    public int compareTo(final Relationship other) {
        return compareTo(buildHash(), other == null ? null : other.buildHash());
    }

    public String buildHash() {
        return new StringBuilder().append(from)
                                  .append("-[").append(type).append("]->")
                                  .append(to)
                                  .toString();
    }

    public static class RelationshipBuilder {
        public RelationshipBuilder property(final String key, final Serializable value) {
            if (this.properties == null) {
                this.properties = new LinkedHashMap<>();
            }
            if (key != null && value != null) {
                this.properties.put(key, value);
            }
            this.properties = sortProperties(this.properties);
            return this;
        }

        public RelationshipBuilder properties(final Map<String, Serializable> properties) {
            if (this.properties == null) {
                this.properties = new LinkedHashMap<>();
            }
            if (properties != null) {
                this.properties.putAll(properties);
            }
            this.properties = sortProperties(this.properties);
            return this;
        }
    }
    public static int compareTo(final String value, final String ref) {
        if (value == null && ref != null) {
            return 1;
        } else if (value != null && ref == null) {
            return -1;
        } else if (value == null && ref == null) {
            return 0;
        } else {
            return convertCompareToResult(value.compareTo(ref));
        }

    }

    private static int convertCompareToResult(final int result) {
        if (result == 0) {
            return 0;
        } else if (result < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
