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

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.inugami.framework.interfaces.database.NodeUtils.sortProperties;

@Builder(toBuilder = true)
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class PropertiesResources implements Serializable {

    private static final long serialVersionUID = -1001566852709954084L;

    @EqualsAndHashCode.Include
    private String type;

    private String              encoding;
    @EqualsAndHashCode.Include
    private String              propertiesPath;
    private String              propertiesUrl;
    @ToString.Exclude
    private String              propertiesUrlAuthorization;
    @ToString.Exclude
    private Map<String, String> properties;

    public static class PropertiesResourcesBuilder {

        PropertiesResourcesBuilder properties(final Map<String, String> properties) {
            if (this.properties == null) {
                this.properties = new LinkedHashMap<>();
            }
            if (properties != null) {
                this.properties.putAll(properties);
            }
            this.properties = sortProperties(this.properties);
            return this;
        }

        PropertiesResourcesBuilder addProperty(final String key, final String value) {
            if (this.properties == null) {
                this.properties = new LinkedHashMap<>();
            }
            if (key != null && value != null) {
                this.properties.put(key, value);
            }
            this.properties = sortProperties(this.properties);
            return this;
        }
    }
}
