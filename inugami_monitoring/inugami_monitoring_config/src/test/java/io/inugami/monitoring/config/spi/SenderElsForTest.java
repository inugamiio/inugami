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
import io.inugami.api.monitoring.senders.MonitoringSender;
import io.inugami.api.monitoring.senders.MonitoringSenderException;
import io.inugami.api.processors.ConfigHandler;

import java.util.List;

/**
 * SenderElsForTest
 *
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
public class SenderElsForTest implements MonitoringSender {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    // =========================================================================
    // SPI
    // =========================================================================
    @Override
    public MonitoringSender buildInstance(final ConfigHandler<String, String> configuration) {
        return this;
    }

    @Override
    public String getName() {
        return "ELS";
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void process(final List<GenericMonitoringModel> data) throws MonitoringSenderException {
        // TODO Auto-generated method stub

    }
}
