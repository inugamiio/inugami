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
package io.inugami.monitoring.config.spi;

import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.monitoring.sensors.MonitoringSensor;
import io.inugami.api.processors.ConfigHandler;

import java.util.List;

/**
 * CpuSensorforTest
 *
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
@SuppressWarnings({"java:S2187"})
public class MBeanSensorForTest implements MonitoringSensor {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String path;

    private String query;

    // =========================================================================
    // SPI
    // =========================================================================
    @Override
    public MonitoringSensor buildInstance(final long interval, final String query,
                                          final ConfigHandler<String, String> configuration) {
        this.path = configuration.grab("path");
        this.query = query;
        return this;
    }

    @Override
    public String getName() {
        return "mbean";
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public List<GenericMonitoringModel> process() {
        // TODO Auto-generated method stub
        return null;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public long getInterval() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

}
