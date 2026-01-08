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
package io.inugami.framework.interfaces.monitoring.models;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSender;
import io.inugami.framework.interfaces.monitoring.sensors.MonitoringSensor;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
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
public class Monitoring implements Serializable {
    private static final long serialVersionUID = -772837418019698246L;
    @Builder.Default
    private boolean                           enable       = true;
    private String                            env;
    private String                            asset;
    private String                            hostname;
    private String                            instanceName;
    private String                            instanceNumber;
    private String                            applicationVersion;
    private int                               maxSensorsTasksThreads;
    private CurrentApplicationDTO             currentApplication;
    private ConfigHandler<String, String>     properties;
    @Builder.Default
    private List<MonitoringSender>            senders      = new ArrayList<>();
    @Builder.Default
    private List<MonitoringSensor>            sensors      = new ArrayList<>();
    @Builder.Default
    private List<MonitoringFilterInterceptor> interceptors = new ArrayList<>();


}
