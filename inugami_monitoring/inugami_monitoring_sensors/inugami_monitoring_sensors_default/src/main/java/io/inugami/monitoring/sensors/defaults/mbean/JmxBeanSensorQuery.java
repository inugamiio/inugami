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
package io.inugami.monitoring.sensors.defaults.mbean;

import flexjson.JSON;
import io.inugami.api.models.data.basic.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@SuppressWarnings({"java:S5361"})
public final class JmxBeanSensorQuery implements JsonObject {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -1555887276893630060L;

    private String path;

    @JSON(name = "attribute")
    private String mbeanAttibute;

    @JSON(name = "method")
    private String mbeanMethod;


    public JmxBeanSensorQuery(final String path, final String attibute, final String method) {
        super();
        this.path = path;
        this.mbeanAttibute = attibute;
        this.mbeanMethod = method;
    }

    public JmxBeanSensorQuery(final JmxBeanSensorQuery query) {
        this.path = query.getPath().replaceAll("'", "\"");
        this.mbeanAttibute = query.getMbeanAttibute();
        this.mbeanMethod = query.getMbeanMethod();
    }


}
