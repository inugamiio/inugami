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
package org.inugami.monitoring.core.interceptors.spi;

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.models.data.graphite.number.LongNumber;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.monitoring.api.data.GenericMonitoringModel;
import org.inugami.monitoring.api.data.GenericMonitoringModelBuilder;
import org.inugami.monitoring.api.data.ResponseData;
import org.inugami.monitoring.api.data.ResquestData;
import org.inugami.monitoring.api.exceptions.ErrorResult;
import org.inugami.monitoring.api.interceptors.MonitoringFilterInterceptor;
import org.inugami.monitoring.api.tools.GenericMonitoringModelTools;
import org.inugami.monitoring.core.sensors.services.ServiceValueTypes;
import org.inugami.monitoring.core.sensors.services.ServicesSensor;

/**
 * ServiceCounterInterceptor
 * 
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
public class ServiceCounterInterceptor implements MonitoringFilterInterceptor {
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ServiceCounterInterceptor() {
    }
    
    @Override
    public MonitoringFilterInterceptor buildInstance(ConfigHandler<String, String> configuration) {
        return new ServiceCounterInterceptor();
    }
    
    @Override
    public String getName() {
        return "servicesCounter";
    }
    // =========================================================================
    // METHODS
    // =========================================================================
    
    @Override
    public List<GenericMonitoringModel> onBegin(ResquestData request) {
        final GenericMonitoringModelBuilder builder = GenericMonitoringModelTools.initResultBuilder();
        
        builder.setValue(new LongNumber(1));
        builder.setCounterType(ServiceValueTypes.HITS.getKeywork());
        builder.setValueType("count");
        
        ServicesSensor.addData(GenericMonitoringModelTools.buildSingleResult(builder));
        return null;
    }
    
    @Override
    public List<GenericMonitoringModel> onDone(ResquestData request, ResponseData response, ErrorResult error) {
        final List<GenericMonitoringModel> result = new ArrayList<>();
        
        final GenericMonitoringModelBuilder builder = GenericMonitoringModelTools.initResultBuilder();
        ServiceValueTypes doneType = error == null ? ServiceValueTypes.DONE : ServiceValueTypes.ERROR;
        
        if (doneType == ServiceValueTypes.ERROR) {
            builder.setErrorCode(error.getErrorCode());
            builder.setErrorType(error.getErrorType());
        }
        builder.setCounterType(doneType.getKeywork());
        
        builder.setValue(new LongNumber(response.getDuration()));
        builder.setCounterType(ServiceValueTypes.RESPONSE_TIME.getKeywork());
        result.add(builder.build());
        
        builder.setValue(new LongNumber(1));
        builder.setCounterType(ServiceValueTypes.HITS.getKeywork());
        builder.setValueType("count");
        result.add(builder.build());
        
        ServicesSensor.addData(result);
        return null;
    }
    
}
