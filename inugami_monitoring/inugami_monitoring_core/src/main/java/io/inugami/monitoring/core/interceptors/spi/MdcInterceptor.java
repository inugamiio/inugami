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
package io.inugami.monitoring.core.interceptors.spi;

import io.inugami.api.exceptions.WarningContext;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.processors.ConfigHandler;

import java.util.ArrayList;
import java.util.List;

public class MdcInterceptor implements MonitoringFilterInterceptor {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public MonitoringFilterInterceptor buildInstance(final ConfigHandler<String, String> configuration) {
        return this;
    }

    @Override
    public List<GenericMonitoringModel> onBegin(final ResquestData request) {
        MdcService.getInstance().initialize();
        WarningContext.getInstance().setWarnings(new ArrayList<>());
        return null;
    }

}
