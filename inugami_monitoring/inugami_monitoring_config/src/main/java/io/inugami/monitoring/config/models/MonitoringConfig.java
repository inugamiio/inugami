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
package io.inugami.monitoring.config.models;

import lombok.*;

import java.io.Serializable;

/**
 * MonitoringConfig
 *
 * @author patrickguillerm
 * @since Jan 14, 2019
 */
@Getter
@Setter
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringConfig implements Serializable {

    private static final long    serialVersionUID = -8578418466667776898L;
    @Builder.Default
    private              Boolean enable           = Boolean.TRUE;
    private              String  env;
    private              String  asset;
    private              String  hostname;
    private              String  instanceName;
    private              String  instanceNumber;
    private              String  maxSensorsTasksThreads;
    private              String  applicationVersion;

    @Builder.Default
    private HeaderInformationsConfig header       = HeaderInformationsConfig.builder().build();
    @Builder.Default
    private PropertiesConfig         properties   = PropertiesConfig.builder().build();
    @Builder.Default
    private MonitoringSendersConfig  senders      = MonitoringSendersConfig.builder().build();
    @Builder.Default
    private SensorsConfig            sensors      = SensorsConfig.builder().build();
    @Builder.Default
    private InterceptorsConfig       interceptors = InterceptorsConfig.builder().build();


}
