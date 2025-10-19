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
package io.inugami.dashboard.api.domain.plugin;

import io.inugami.framework.configuration.models.HandlerConfig;
import io.inugami.framework.configuration.models.ListenerModel;
import io.inugami.framework.configuration.models.ProviderConfig;
import io.inugami.framework.interfaces.alertings.AlertingProvider;
import io.inugami.framework.interfaces.alertings.AlertingProviderModel;
import io.inugami.framework.interfaces.handlers.Handler;
import io.inugami.framework.interfaces.listeners.EngineListener;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import io.inugami.framework.interfaces.providers.Provider;

import java.util.List;
import java.util.Map;

public interface IPluginLoaderService {

    List<AlertingProvider> loadAlertings(final List<AlertingProviderModel> alertings,
                                         final Map<String, String> globalProperties,
                                         final ManifestInfo manifest);

    List<EngineListener> loadListeners(final List<ListenerModel> listeners,
                                       final Map<String, String> globalProperties,
                                       final ManifestInfo manifest);

    List<Processor> loadProcessors(final List<ProcessorModel> processors,
                                   final Map<String, String> globalProperties,
                                   final ManifestInfo manifest);

    List<Provider> loadProviders(final List<ProviderConfig> providers,
                                 final Map<String, String> globalProperties,
                                 final ManifestInfo manifest);

    List<Handler> loadHandlers(final List<HandlerConfig> handlers,
                               final Map<String, String> globalProperties,
                               final ManifestInfo manifest);

}
