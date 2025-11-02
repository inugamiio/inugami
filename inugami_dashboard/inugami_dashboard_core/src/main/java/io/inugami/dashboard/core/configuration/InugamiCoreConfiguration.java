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

import io.inugami.dashboard.api.domain.engine.EngineListener;
import io.inugami.dashboard.api.domain.plugin.IPluginService;
import io.inugami.dashboard.core.domain.engine.EngineService;
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.configuration.models.app.ApplicationConfig;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.configuration.services.PluginConfigurationLoader;
import io.inugami.framework.configuration.services.resolver.ConfigurationResolver;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.inugami.dashboard.api.domain.engine.exception.EngineErrors.*;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertNotNull;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertTrue;

@Slf4j
@EnableConfigurationProperties(InugamiConfiguration.class)
@Configuration
public class InugamiCoreConfiguration {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final  String                       ENGINE_THREADS_EXECUTOR_SERVICE          = "engineThreadsExecutorService";
    public static final  String                       ENGINE_THREADS_EXECUTOR_SERVICE_INTERNAL = "engineThreadsExecutorServiceInternal";
    private static final List<ThreadsExecutorService> THREAD_POOLS                             = new ArrayList<>();


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
    public List<Plugin> plugins(final IPluginService pluginService) {
        return pluginService.loadPlugins();
    }


    @Bean
    public EngineService engineService(final InugamiConfiguration configuration,
                                       final List<Plugin> plugins,
                                       final Collection<EngineListener> listeners,
                                       final Clock clock,
                                       final ZoneOffset zoneOffset) {
        long timeout = configuration.getEngine().getTimeout();
        if (timeout < 1000L) {
            timeout = InugamiConfiguration.InugamiConfigurationEngine.DEFAULT_TIMEOUT;
        }
        int maxThreads = configuration.getEngine().getMaxThreads();
        if (maxThreads < 1) {
            maxThreads = 1;
        }
        if (maxThreads > InugamiConfiguration.InugamiConfigurationEngine.DEFAULT_MAX_THREAD) {
            maxThreads = InugamiConfiguration.InugamiConfigurationEngine.DEFAULT_MAX_THREAD;
        }

        final var engineThreadsExecutorService = new ThreadsExecutorService(ENGINE_THREADS_EXECUTOR_SERVICE,
                                                                            maxThreads,
                                                                            false,
                                                                            timeout);

        final var internalThreadPool = new ThreadsExecutorService(ENGINE_THREADS_EXECUTOR_SERVICE_INTERNAL,
                                                                  5,
                                                                  false,
                                                                  timeout);
        THREAD_POOLS.addAll(List.of(engineThreadsExecutorService, internalThreadPool));

        final var result = EngineService.builder()
                                        .plugins(plugins)
                                        .listeners(listeners)
                                        .threadsExecutor(engineThreadsExecutorService)
                                        .threadsExecutorInternal(internalThreadPool)
                                        .clock(clock)
                                        .zoneOffset(zoneOffset)
                                        .timeout(configuration.getEngine().getTimeout())
                                        .build()
                                        .init();
        THREAD_POOLS.addAll(result.getPluginsThreadPool());
        return result;
    }

    //==================================================================================================================
    // LIFECYCLE
    //==================================================================================================================
    @PreDestroy
    public void shutdown() {
        final List<ThreadsExecutorService> threadsPools = new ArrayList<>(THREAD_POOLS);
        Collections.reverse(threadsPools);
        threadsPools.forEach(ThreadsExecutorService::shutdown);
    }
}
