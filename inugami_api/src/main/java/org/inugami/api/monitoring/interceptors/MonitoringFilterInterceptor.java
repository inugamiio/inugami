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
package org.inugami.api.monitoring.interceptors;

import java.util.List;

import org.inugami.api.monitoring.MonitoringInitializer;
import org.inugami.api.monitoring.data.ResponseData;
import org.inugami.api.monitoring.data.ResquestData;
import org.inugami.api.monitoring.exceptions.ErrorResult;
import org.inugami.api.monitoring.models.GenericMonitoringModel;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.spi.NamedSpi;

/**
 * MonitoringFilterInterceptor
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
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
