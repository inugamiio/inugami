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
package io.inugami.framework.interfaces.monitoring.sensors;

import java.util.List;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.spi.NamedSpi;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;

/**
 * Sensor
 *
 * @author patrickguillerm
 * @since Jan 15, 2019
 */
public interface MonitoringSensor extends NamedSpi {

    MonitoringSensor buildInstance(final long interval, final String query,
                                   ConfigHandler<String, String> configuration);

    long getInterval();

    List<GenericMonitoringModel> process();

    default void shutdown() {
    }

}
