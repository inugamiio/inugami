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
import java.util.List;
import java.util.Map;

import static io.inugami.framework.interfaces.database.NodeUtils.sortProperties;


@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public final class Resource implements Serializable {
    private static final long serialVersionUID = -5683988981944933305L;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String              target;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String              path;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String              gav;
    private List<Include>       includes;
    private List<Exclude>       excludes;
    private boolean             filtering;
    private Map<String, String> properties;

    public static class ResourceBuilder {
        ResourceBuilder property(final String key, final String value) {
            if (this.properties == null) {
                this.properties = new LinkedHashMap<>();
            }
            if (key != null && value != null) {
                this.properties.put(key, value);
            }
            this.properties = sortProperties(this.properties);
            return this;
        }

        ResourceBuilder properties(final Map<String, String> properties) {
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
}
