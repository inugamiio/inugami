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
package io.inugami.configuration.models.plugins.components.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * ComponentDescriptionContent
 *
 * @author patrickguillerm
 * @since 30 août 2018
 */
@XStreamAlias("description")
public class ComponentDescriptionContentModel implements Serializable {

    private static final long   serialVersionUID = -1057183917570860667L;
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @XStreamAsAttribute
    private              String type;

    @XStreamAsAttribute
    private String path;

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && obj != null && obj instanceof ComponentDescriptionContentModel) {
            final ComponentDescriptionContentModel other = (ComponentDescriptionContentModel) obj;
            result = path == null ? other.getPath() == null : path.equals(other.getPath());
        }

        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ComponentDescriptionContentModel [type=");
        builder.append(type);
        builder.append(", path=");
        builder.append(path);
        builder.append("]");
        return builder.toString();
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }
}
