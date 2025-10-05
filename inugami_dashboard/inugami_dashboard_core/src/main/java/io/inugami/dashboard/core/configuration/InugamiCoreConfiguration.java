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
package io.inugami.dashboard.core.configuration;

import com.sun.source.util.Plugin;
import io.inugami.dashboard.api.domain.engine.EngineListener;
import io.inugami.dashboard.core.domain.engine.EngineService;
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.configuration.models.app.ApplicationConfig;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.services.PluginConfigurationLoader;
import io.inugami.framework.configuration.services.resolver.ConfigurationResolver;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.inugami.dashboard.api.domain.engine.exception.EngineErrors.*;
import static io.inugami.framework.api.tools.ReflectionUtils.runSafe;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertNotNull;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertTrue;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@Slf4j
@EnableConfigurationProperties(InugamiConfiguration.class)
@Configuration
public class InugamiCoreConfiguration {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final String                 ENGINE_THREADS_EXECUTOR_SERVICE = "engineThreadsExecutorService";
    private             ThreadsExecutorService engineThreadsExecutorService;

    //==================================================================================================================
    // BEANS
    //==================================================================================================================
    @Bean
    public PluginConfigurationLoader pluginConfigurationLoader() {
        return new PluginConfigurationLoader();
    }

    @Bean
    public File inugamiConfigurationFile(final InugamiConfiguration configuration) {
        final String folderPath = configuration.getApplication().getWorkspace();
        assertNotNull(WORKSPACE_UNDEFINED, folderPath);

        log.info("use inugami workspace : {}", folderPath);
        final var folder = new File(folderPath);
        assertTrue(WORKSPACE_NOT_EXISTS, folder.exists());
        assertTrue(WORKSPACE_NOT_FOLDER, folder.isDirectory());
        assertTrue(WORKSPACE_CAN_READ, folder.canRead());
        final File applicationConfig = new File(folderPath + File.separator + "application-configuration.yaml");
        assertTrue(APPLICATION_CONFIG_NOT_EXISTS, applicationConfig.exists());
        assertTrue(APPLICATION_CONFIG_NOT_READABLE, applicationConfig.canRead());
        return applicationConfig;
    }

    @Bean
    public ConfigurationResolver configurationResolver() {
        return new ConfigurationResolver();
    }

    @Bean
    public ApplicationConfig applicationConfig(final PluginConfigurationLoader pluginConfigurationLoader,
                                               final ConfigurationResolver configurationResolver,
                                               final File inugamiConfigurationFile) {
        final ApplicationConfig result = configurationResolver.loadApplicationConfig(inugamiConfigurationFile);

        assertNotNull(APPLICATION_CONFIG_ERROR, result);

        return result;
    }

    @Bean
    public List<Plugin> plugins(final ConfigurationResolver configurationResolver) {
        final List<PluginConfiguration> pluginConfigurations = new ArrayList<>();
        try {
            pluginConfigurations.addAll(configurationResolver.resolvePluginsConfigurations().orElse(List.of()));
        } catch (TechnicalException e) {
        }
        //TODO: load plugin from configurations
        return List.of();
    }

    @Bean
    public ThreadsExecutorService engineThreadsExecutorService(final InugamiConfiguration configuration) {
        engineThreadsExecutorService = new ThreadsExecutorService(ENGINE_THREADS_EXECUTOR_SERVICE,
                                                                  configuration.getEngine().getMaxThreads(),
                                                                  false,
                                                                  configuration.getEngine().getTimeout());

        return engineThreadsExecutorService;
    }

    @Bean
    public EngineService engineService(final List<Plugin> plugins,
                                       final Collection<EngineListener> listeners,
                                       final ThreadsExecutorService engineThreadsExecutorService,
                                       final Clock clock) {
        return EngineService.builder()
                            .plugins(plugins)
                            .listeners(listeners)
                            .threadsExecutor(engineThreadsExecutorService)
                            .clock(clock)
                            .build();
    }

    //==================================================================================================================
    // LIFECYCLE
    //==================================================================================================================
    @PreDestroy
    public void shutdown() {
        applyIfNotNull(engineThreadsExecutorService, ThreadsExecutorService::shutdown);
    }
}
