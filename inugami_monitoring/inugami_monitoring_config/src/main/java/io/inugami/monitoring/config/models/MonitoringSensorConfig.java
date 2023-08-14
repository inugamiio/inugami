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
package io.inugami.monitoring.config.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.ApplyIfNull;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;
import lombok.*;

import java.io.Serializable;

/**
 * MonitoringSensorConfig
 *
 * @author patrickguillerm
 * @since Jan 15, 2019
 */
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@XStreamAlias("sensor")
public final class MonitoringSensorConfig implements PostProcessing<ConfigHandler<String, String>>, ApplyIfNull, Serializable {

    private static final long serialVersionUID = -774495566963928169L;

    @XStreamAsAttribute
    private String           name;
    @XStreamAsAttribute
    private String           interval;
    @XStreamAsAttribute
    private String           query;
    private PropertiesConfig properties;

    // =========================================================================
    // INIT
    // =========================================================================
    @Override
    public void postProcessing(final ConfigHandler<String, String> context) throws TechnicalException {
        interval = applyIfNull(interval, () -> "60000");
    }
}
