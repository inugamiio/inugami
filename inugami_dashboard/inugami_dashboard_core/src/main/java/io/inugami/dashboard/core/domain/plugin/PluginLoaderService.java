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
package io.inugami.dashboard.core.domain.plugin;

import io.inugami.dashboard.api.domain.plugin.IPluginLoaderService;
import io.inugami.framework.configuration.models.HandlerConfig;
import io.inugami.framework.configuration.models.ListenerModel;
import io.inugami.framework.configuration.models.ProviderConfig;
import io.inugami.framework.configuration.services.ConfigHandlerHashMap;
import io.inugami.framework.interfaces.alertings.AlertingProvider;
import io.inugami.framework.interfaces.alertings.AlertingProviderModel;
import io.inugami.framework.interfaces.configurtation.BehaviourComponents;
import io.inugami.framework.interfaces.handlers.Handler;
import io.inugami.framework.interfaces.listeners.EngineListener;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.spi.SpiLoaderServiceSPI;
import io.inugami.framework.interfaces.tools.NamedComponent;
import io.inugami.framework.interfaces.tools.PostConstructConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class PluginLoaderService implements IPluginLoaderService {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private final SpiLoaderServiceSPI spiLoaderService;

    //==================================================================================================================
    // LOADERS
    //==================================================================================================================

    //---[ loadAlertings ]----------------------------------------------------------------------------------------------
    @Override
    public List<AlertingProvider> loadAlertings(final List<AlertingProviderModel> alertings,
                                                final Map<String, String> globalProperties,
                                                final ManifestInfo manifest) {
        return loadComponents(alertings, globalProperties, manifest, AlertingProvider.class);
    }


    //---[ loadListeners ]----------------------------------------------------------------------------------------------
    @Override
    public List<EngineListener> loadListeners(final List<ListenerModel> listeners,
                                              final Map<String, String> globalProperties,
                                              final ManifestInfo manifest) {
        return loadComponents(listeners, globalProperties, manifest, EngineListener.class);
    }


    //---[ loadProcessors ]---------------------------------------------------------------------------------------------
    @Override
    public List<Processor> loadProcessors(final List<ProcessorModel> processors,
                                          final Map<String, String> globalProperties,
                                          final ManifestInfo manifest) {
        return loadComponents(processors, globalProperties, manifest, Processor.class);
    }


    //---[ loadProvider ]-----------------------------------------------------------------------------------------------
    @Override
    public List<Provider> loadProviders(final List<ProviderConfig> providers,
                                        final Map<String, String> globalProperties,
                                        final ManifestInfo manifest) {
        return loadComponents(providers, globalProperties, manifest, Provider.class);
    }


    //---[ loadHandlers ]-----------------------------------------------------------------------------------------------
    @Override
    public List<Handler> loadHandlers(final List<HandlerConfig> handlers,
                                      final Map<String, String> globalProperties,
                                      final ManifestInfo manifest) {
        return loadComponents(handlers, globalProperties, manifest, Handler.class);
    }


    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    protected <T extends NamedComponent, C extends BehaviourComponents> List<T> loadComponents(final List<C> configurations,
                                                                                               final Map<String, String> globalProperties,
                                                                                               final ManifestInfo manifest,
                                                                                               final Class<T> serviceClass) {
        final List<T> result    = new ArrayList<>();
        final List<T> providers = getLoadServices(serviceClass);

        for (BehaviourComponents config : Optional.ofNullable(configurations).orElse(List.of())) {
            T instance = chooseProvider(providers, config.getName(), config.getClassName());
            if (instance != null) {
                if (instance instanceof PostConstructConfig postConstructInstance) {
                    final Map<String, String> instanceConfig = new LinkedHashMap<>(globalProperties);
                    applyIfNotNull(config.getConfigs(), instanceConfig::putAll);
                    postConstructInstance.postConstruct(new ConfigHandlerHashMap(instanceConfig), manifest);
                }
                result.add(instance);
            }
        }
        return result;
    }

    protected <T> List<T> getLoadServices(final Class<T> serviceClass) {
        try {
            return spiLoaderService.loadServices(serviceClass);
        } catch (Throwable e) {
            return List.of();
        }
    }

    protected <T extends NamedComponent> T chooseProvider(final List<T> providers,
                                                          final String name,
                                                          final String className) {
        for (T provider : providers) {
            if (provider.getName().equalsIgnoreCase(name) ||
                provider.getClass().getName().equalsIgnoreCase(className)) {
                return provider;
            }
        }
        return null;
    }
}
