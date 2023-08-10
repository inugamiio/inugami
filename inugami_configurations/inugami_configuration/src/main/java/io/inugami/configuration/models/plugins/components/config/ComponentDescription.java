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

import java.io.Serializable;
import java.util.List;

/**
 * ComponentDescription
 *
 * @author patrickguillerm
 * @since 30 août 2018
 */
@XStreamAlias("description")
public class ComponentDescription implements Serializable {

    private static final long                                   serialVersionUID = -1483992189580390931L;
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @XStreamImplicit
    private              List<ComponentDescriptionContentModel> descriptions;

    @XStreamImplicit
    private List<ComponentScreenshot> screenshots;

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ComponentDescription [descriptions=");
        builder.append(descriptions);
        builder.append(", screenshots=");
        builder.append(screenshots);
        builder.append("]");
        return builder.toString();
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public List<ComponentDescriptionContentModel> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(final List<ComponentDescriptionContentModel> descriptions) {
        this.descriptions = descriptions;
    }

    public List<ComponentScreenshot> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(final List<ComponentScreenshot> screenshots) {
        this.screenshots = screenshots;
    }
}
