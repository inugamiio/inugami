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
package io.inugami.framework.configuration.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.Config;

/**
 * Caller
 *
 * @author patrick_guillerm
 * @since 22 déc. 2016
 */
@SuppressWarnings({"java:S2160"})
@XStreamAlias("provider")
public class ProviderConfig extends ClassBehavior {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -3725988814035334991L;

    @XStreamAsAttribute
    private String type;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProviderConfig() {
        super();
    }

    public ProviderConfig(final String name, final String className, final Config... configs) {
        super(name, className, configs);
    }

    public ProviderConfig(final String name, final String className, final String type, final Config... configs) {
        super(name, className, configs);
        this.type = type;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public void setType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
