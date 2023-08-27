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
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import io.inugami.api.models.Gav;
import io.inugami.api.models.data.basic.JsonObject;

import java.util.List;

/**
 * ComponentsModel
 *
 * @author patrickguillerm
 * @since 30 août 2018
 */
@SuppressWarnings({"java:S1700"})
@XStreamAlias("components")
public class Components implements JsonObject {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 8367886592682309031L;

    private Gav gav;

    @XStreamImplicit
    private List<ComponentModel> components;

    // =========================================================================
    // OVERRIDE
    // =========================================================================
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append(convertToJson());
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ((gav == null) ? 0 : gav.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;

        if (!result && obj != null) {
            Gav otherGav = null;
            if (obj instanceof Components) {
                otherGav = ((Components) obj).getGav();
            }

            result = isSameGav(otherGav);
        }

        return result;
    }

    public boolean isSameGav(Gav otherGav) {
        return gav == null ? otherGav == null : gav.equals(otherGav);
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public List<ComponentModel> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentModel> components) {
        this.components = components;
    }

    public Gav getGav() {
        return gav;
    }

    public void setGav(Gav gav) {
        this.gav = gav;
    }

}
