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
package io.inugami.framework.interfaces.connectors.config;

import io.inugami.framework.interfaces.connectors.ConnectorListener;
import lombok.*;
import okhttp3.OkHttpClient;

import java.time.Clock;
import java.util.List;
import java.util.function.Supplier;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class HttpBasicConnectorConfiguration {
    private String                  partnerName;
    private Integer                 timeoutConnecting = 10000;
    private Integer                 timeoutWriting    = 30000;
    private Integer                 timeoutReading    = 60000;
    private List<ConnectorListener> listeners;
    private String                  baseUrl;
    private int                     retry             = 3;
    private Supplier<OkHttpClient>  clientBuilder;
    private Clock                   clock;
}
