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
package io.inugami.monitoring.springboot.app.monitoring;

import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.processors.ConfigHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class FunctionalInterceptor implements MonitoringFilterInterceptor {
    public static final AtomicReference<String> VALUE       = new AtomicReference<>();
    public static final String                  INTERCEPTED = "intercepted";

    public static void clean() {
        VALUE.set(null);
    }

    @Override
    public MonitoringFilterInterceptor buildInstance(final ConfigHandler<String, String> configuration) {
        return this;
    }

    @Override
    public List<GenericMonitoringModel> onBegin(final ResquestData request) {
        VALUE.set(INTERCEPTED);
        return null;
    }
}
