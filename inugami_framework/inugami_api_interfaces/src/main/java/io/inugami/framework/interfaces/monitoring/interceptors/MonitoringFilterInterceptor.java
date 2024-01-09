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
package io.inugami.framework.interfaces.monitoring.interceptors;

import io.inugami.framework.interfaces.monitoring.MonitoringInitializer;
import io.inugami.framework.interfaces.monitoring.data.ResponseData;
import io.inugami.framework.interfaces.monitoring.data.ResquestData;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.spi.NamedSpi;
import io.inugami.framework.interfaces.monitoring.ErrorResult;

import java.util.List;

/**
 * In Java application we can uses request filters. But on JavaEE or Spring (or other) it's not the same approach.
 * The <strong>MonitoringFilterInterceptor</strong> is designed to unify the filter principle.
 *
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@SuppressWarnings({"java:S1168"})
public interface MonitoringFilterInterceptor extends NamedSpi, MonitoringInitializer {
    MonitoringFilterInterceptor buildInstance(ConfigHandler<String, String> configuration);

    default List<GenericMonitoringModel> onBegin(final ResquestData request) {
        return null;
    }

    default List<GenericMonitoringModel> onDone(final ResquestData request, final ResponseData response,
                                                final ErrorResult error) {
        return null;
    }

}
