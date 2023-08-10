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

import io.inugami.api.models.data.graphite.number.LongNumber;
import io.inugami.api.monitoring.data.ResponseData;
import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.exceptions.ErrorResult;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.monitoring.models.GenericMonitoringModelDTO;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.monitoring.api.tools.GenericMonitoringModelTools;
import io.inugami.monitoring.core.sensors.services.ServiceValueTypes;
import io.inugami.monitoring.core.sensors.services.ServicesSensor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * ServiceCounterInterceptor
 *
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
@SuppressWarnings({"java:S1117", "java:S1168", "java:S1181"})
@Slf4j
public class ServiceCounterInterceptor implements MonitoringFilterInterceptor {
    boolean enabled = false;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ServiceCounterInterceptor() {
    }

    public ServiceCounterInterceptor(final ConfigHandler<String, String> configuration) {
        enabled = configuration.grabBoolean("inugami.monitoring.interceptor.servicesCounter.enabled", false);
    }


    @Override
    public MonitoringFilterInterceptor buildInstance(final ConfigHandler<String, String> configuration) {
        return new ServiceCounterInterceptor(configuration);
    }

    @Override
    public String getName() {
        return "servicesCounter";
    }
    // =========================================================================
    // METHODS
    // =========================================================================

    @Override
    public List<GenericMonitoringModel> onBegin(final ResquestData request) {
        if (enabled) {
            final GenericMonitoringModelDTO.GenericMonitoringModelDTOBuilder builder = GenericMonitoringModelTools.initResultBuilder();

            ServicesSensor.addData(List.of(
                    builder.value(LongNumber.of(1L))
                           .counterType(ServiceValueTypes.HITS.getKeywork())
                           .valueType("count")
                           .build()
            ));
        }

        return null;
    }

    @Override
    public List<GenericMonitoringModel> onDone(final ResquestData request, final ResponseData response,
                                               final ErrorResult error) {
        if (enabled) {
            try {
                final List<GenericMonitoringModelDTO> result = new ArrayList<>();

                final GenericMonitoringModelDTO.GenericMonitoringModelDTOBuilder builder = GenericMonitoringModelTools.initResultBuilder();
                final ServiceValueTypes doneType =
                        error == null ? ServiceValueTypes.DONE : ServiceValueTypes.ERROR;

                if (error != null) {
                    builder.errorCode(error.getErrorCode());
                    builder.errorType(error.getErrorType());
                }

                builder.counterType(doneType.getKeywork());
                builder.addValue(response.getDuration());
                builder.counterType(ServiceValueTypes.RESPONSE_TIME.getKeywork());
                result.add(builder.build());

                builder.addValue(1);
                builder.counterType(ServiceValueTypes.HITS.getKeywork());
                builder.valueType("count");
                result.add(builder.build());

                ServicesSensor.addData(result);
            } catch (final Throwable e) {
                if (log.isTraceEnabled()) {
                    log.debug(e.getMessage(), e);
                }
            }
        }


        return null;
    }

}
