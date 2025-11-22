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

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder(toBuilder = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class Node implements  Comparable<Node>, Serializable {

    private static final long                      serialVersionUID = 7519867544798392684L;
    @EqualsAndHashCode.Include
    private              String                    type;
    private              String                    name;
    @EqualsAndHashCode.Include
    private              String                    uid;
    private              Map<String, Serializable> properties;

    @Override
    public int compareTo(final Node other) {
        return compareTo(buildHash(), other == null ? null : other.buildHash());
    }

    private String buildHash() {
        return new StringBuilder().append(uid)
                                  .append("<")
                                  .append(type)
                                  .append(">")
                                  .append("{")
                                  .append(properties)
                                  .append("}")
                                  .toString();
    }

    public void sort() {
        properties = sortProperties(properties);
    }

    public static class NodeBuilder {
        private Map<String, Serializable> properties;

        public NodeBuilder addProperty(final String key, final Serializable value) {
            if (this.properties == null) {
                this.properties = new LinkedHashMap<>();
            }
            if (key != null && value != null) {
                this.properties.put(key, value);
            }
            this.properties = sortProperties(properties);
            return this;
        }


        public NodeBuilder properties(final Map<String, Serializable> properties) {
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
