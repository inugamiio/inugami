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
package io.inugami.interfaces.monitoring.models;

import io.inugami.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.interfaces.monitoring.senders.MonitoringSender;
import io.inugami.interfaces.monitoring.sensors.MonitoringSensor;
import io.inugami.interfaces.processors.ConfigHandler;
import lombok.*;

import java.util.List;

/**
 * Monitoring
 *
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@Setter
@Getter
public class Monitoring {

    private boolean                           enable;
    private String                            env;
    private String                            asset;
    private String                            hostname;
    private String                            instanceName;
    private String                            instanceNumber;
    private String                            applicationVersion;
    private Headers                           headers;
    private int                               maxSensorsTasksThreads;
    private ConfigHandler<String, String>     properties;
    private List<MonitoringSender>            senders;
    private List<MonitoringSensor>            sensors;
    private List<MonitoringFilterInterceptor> interceptors;


}
