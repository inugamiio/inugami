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
package io.inugami.monitoring.core.sensors;


import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.models.number.GraphiteNumber;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;

import java.util.List;

/**
 * ServicesSensorAggregator
 *
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
public interface ServicesSensorAggregator {
    boolean accept(GenericMonitoringModel data, ConfigHandler<String, String> configuration);

    List<GenericMonitoringModel> compute(GenericMonitoringModel data, List<GraphiteNumber> values,
                                         ConfigHandler<String, String> configuration);
}
