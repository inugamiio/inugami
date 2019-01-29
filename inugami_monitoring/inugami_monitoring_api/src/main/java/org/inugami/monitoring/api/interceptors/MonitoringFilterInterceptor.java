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
package org.inugami.monitoring.api.interceptors;

import java.util.List;

import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.spi.NamedSpi;
import org.inugami.monitoring.api.data.GenericMonitoringModel;
import org.inugami.monitoring.api.data.ResponseData;
import org.inugami.monitoring.api.data.ResquestData;
import org.inugami.monitoring.api.exceptions.ErrorResult;

/**
 * MonitoringFilterInterceptor
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
public interface MonitoringFilterInterceptor extends NamedSpi {
    MonitoringFilterInterceptor buildInstance(ConfigHandler<String, String> configuration);
    
    default List<GenericMonitoringModel> onBegin(ResquestData request) {
        return null;
    }
    
    default List<GenericMonitoringModel> onDone(ResquestData request, ResponseData response, ErrorResult error) {
        return null;
    }
    
}
