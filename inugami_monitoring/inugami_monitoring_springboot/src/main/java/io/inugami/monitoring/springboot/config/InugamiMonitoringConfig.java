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
package io.inugami.monitoring.springboot.config;


import io.inugami.framework.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.framework.api.tools.RunSafeUtils;
import io.inugami.framework.commons.spring.configuration.ConfigConfiguration;
import io.inugami.framework.configuration.services.ConfigHandlerHashMap;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.exceptions.ErrorCodeResolver;
import io.inugami.framework.interfaces.feature.IFeatureService;
import io.inugami.framework.interfaces.monitoring.MonitoringLoaderSpi;
import io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.framework.interfaces.monitoring.models.CurrentApplicationDTO;
import io.inugami.framework.interfaces.monitoring.models.Monitoring;
import io.inugami.framework.interfaces.monitoring.models.MonitoringContextDTO;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSender;
import io.inugami.framework.interfaces.monitoring.sensors.MonitoringSensor;
import io.inugami.framework.interfaces.spi.SpiLoaderServiceSPI;
import io.inugami.monitoring.core.context.MonitoringBootstrapService;
import io.inugami.monitoring.core.context.MonitoringContext;
import io.inugami.monitoring.core.interceptors.FilterInterceptor;
import io.inugami.monitoring.core.spi.IoLogInterceptor;
import io.inugami.monitoring.springboot.actuator.FailSafeStatusAggregator;
import io.inugami.monitoring.springboot.actuator.VersionHealthIndicator;
import io.inugami.monitoring.springboot.actuator.feature.FeatureIndicator;
import io.inugami.monitoring.springboot.exception.SpringDefaultErrorCodeResolver;
import io.inugami.monitoring.springboot.filter.IoLogFilter;
import io.inugami.monitoring.springboot.request.SpringRestMethodResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.StatusAggregator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;


@SuppressWarnings({"java:S1450", "java:S2209"})
@EnableConfigurationProperties({InugamiMonitoringProperties.class})
@Import({
        IoLogFilter.class,
        SpringRestMethodResolver.class,
        ConfigConfiguration.class,
        InugamiMonitoringInterceptableConfig.class,
        InugamiMonitoringJavaRestMethodTrackerConfig.class,
        InugamiMonitoringExceptionResolverConfig.class,
        InugamiMonitoringResponseListenerConfig.class,
        InugamiMonitoringFilterInterceptorConfig.class,
        InugamiMonitoringPurgeStrategyConfig.class,
        InugamiMonitoringMdcCleanerConfig.class
})
@Slf4j
@Configuration
public class InugamiMonitoringConfig {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String INUGAMI_MONITORING_CONFIG = "io.inugami.monitoring.springboot";
    public static final String INUGAMI                   = "io.inugami";
    public static final String QUERY                     = "query";
    public static final String EMPTY                     = "";

    // =================================================================================================================
    // BEANS
    // =================================================================================================================
    @Bean
    public MonitoringBootstrapService monitoringBootstrapService(final SpiLoaderServiceSPI spiLoaderServiceSPI,
                                                                 final List<MonitoringLoaderSpi> loaders) {
        // spiLoaderServiceSPI must be initialized before MonitoringBootstrapService
        return MonitoringBootstrapService.builder()
                                         .loader(loaders.stream()
                                                        .findFirst()
                                                        .orElse(() -> Monitoring.builder().build()))
                                         .build()
                                         .initialize();
    }

    @Bean
    public MonitoringContextDTO monitoringContextDTO(final CurrentApplicationDTO currentApplication,
                                                     final Clock clock) {
        return MonitoringContextDTO.builder()
                                   .currentApplication(currentApplication)
                                   .clock(clock)
                                   .build();
    }

    @Bean
    public Monitoring initMonitoringContext(final ConfigHandler<String, String> springConfig,
                                            final MonitoringBootstrapService monitoringBootstrapService,
                                            final SpiLoaderServiceSPI spiLoaderServiceSPI,
                                            final InugamiMonitoringProperties monitoringProperties,
                                            final MonitoringContextDTO monitoringContextDTO) {
        final MonitoringContext monitoringContext = monitoringBootstrapService.getContext();
        final Monitoring        config            = monitoringContext.getConfig();
        config.setMaxSensorsTasksThreads(20);
        monitoringContext.setCurrentApplication(monitoringContextDTO.getCurrentApplication());
        final ConfigHandler<String, String> currentConfiguration = ConfigConfiguration.CONFIGURATION;
        applyIfNotNull(springConfig, currentConfiguration::putAll);
        config.setProperties(mergeProperties(config.getProperties(), currentConfiguration));
        initializeInterceptors(monitoringContext, config);
        initializeSensors(config, spiLoaderServiceSPI, monitoringProperties, currentConfiguration,monitoringContextDTO);
        initializeSenders(config, spiLoaderServiceSPI, monitoringProperties, currentConfiguration,monitoringContextDTO);
        monitoringContext.initialize(null);
        return config;
    }


    private void initializeSensors(final Monitoring config,
                                   final SpiLoaderServiceSPI spiLoaderServiceSPI,
                                   final InugamiMonitoringProperties monitoringProperties,
                                   final ConfigHandler<String, String> currentConfiguration,
                                   final MonitoringContextDTO monitoringContextDTO) {
        final List<MonitoringSensor> sensors =
                RunSafeUtils.runSafeOrElse(() -> spiLoaderServiceSPI.loadServices(MonitoringSensor.class), List.of());
        if (sensors.isEmpty()) {
            return;
        }
        final Map<String, Map<String, String>> sensorConfig = monitoringProperties.getSenders();
        for (MonitoringSensor sensor : sensors) {
            final Map<String, String> senderConfig = sensorConfig.get(Optional.ofNullable(sensor.getName())
                                                                              .orElse(EMPTY));

            final Map<String, String> currentConfig = new LinkedHashMap<>();
            applyIfNotNull(currentConfiguration, currentConfig::putAll);
            applyIfNotNull(senderConfig, currentConfig::putAll);

            config.getSensors().add(sensor.buildInstance(monitoringProperties.getInternal().getInterval(),
                                                         Optional.ofNullable(currentConfig.get(QUERY)).orElse(EMPTY),
                                                         new ConfigHandlerHashMap(currentConfig),
                                                         monitoringContextDTO));
        }
    }

    private void initializeSenders(final Monitoring config,
                                   final SpiLoaderServiceSPI spiLoaderServiceSPI,
                                   final InugamiMonitoringProperties monitoringProperties,
                                   final ConfigHandler<String, String> currentConfiguration,
                                   final MonitoringContextDTO monitoringContextDTO) {
        final List<MonitoringSender> senders =
                RunSafeUtils.runSafeOrElse(() -> spiLoaderServiceSPI.loadServices(MonitoringSender.class), List.of());
        if (senders.isEmpty()) {
            return;
        }
        final Map<String, Map<String, String>> sendersConfig = monitoringProperties.getSenders();
        for (MonitoringSender sender : senders) {
            final Map<String, String> senderConfig = sendersConfig.get(Optional.ofNullable(sender.getName())
                                                                               .orElse(EMPTY));
            final Map<String, String> currentConfig = new LinkedHashMap<>();
            applyIfNotNull(currentConfiguration, currentConfig::putAll);
            applyIfNotNull(senderConfig, currentConfig::putAll);

            config.getSenders().add(sender.buildInstance(new ConfigHandlerHashMap(currentConfig),monitoringContextDTO));
        }
    }


    @Bean
    public FilterInterceptor filterInterceptor(final SpiLoaderServiceSPI spiLoaderServiceSPI,
                                               final Monitoring monitoring,
                                               final CurrentApplicationDTO currentApplication) {
        // monitoring must be initialized before FilterInterceptor
        final FilterInterceptor filter = new FilterInterceptor(spiLoaderServiceSPI, currentApplication);
        DefaultApplicationLifecycleSPI.register(filter);
        filter.onApplicationStarted(null);
        return filter;
    }

    private ConfigHandler<String, String> mergeProperties(final ConfigHandler<String, String> properties,
                                                          final ConfigHandler<String, String> currentConfiguration) {
        if (properties == null) {
            return currentConfiguration;
        }
        if (currentConfiguration != null) {
            properties.putAll(currentConfiguration);
        }
        return properties;
    }


    @Bean
    public ErrorCodeResolver springDefaultErrorCodeResolver() {
        return new SpringDefaultErrorCodeResolver();
    }


    @Bean
    @ConditionalOnProperty(name = "inugami.monitoring.actuator.version.enabled", havingValue = "true", matchIfMissing = true)
    public HealthIndicator versionHealthIndicator(@Value("${application.groupId:#{null}}") final String groupId,
                                                  @Value("${application.artifactId:#{null}}") final String artifactId,
                                                  @Value("${application.version:#{null}}") final String version,
                                                  @Value("${application.commitId:#{null}}") final String commitId,
                                                  @Value("${application.commitDate:#{null}}") final String commitDate) {
        return VersionHealthIndicator.builder()
                                     .groupId(groupId)
                                     .artifactId(artifactId)
                                     .version(version)
                                     .commitId(commitId)
                                     .commitDate(commitDate)
                                     .build();
    }

    @Bean
    public HealthIndicator featureIndicator(final IFeatureService service) {
        return FeatureIndicator.builder()
                               .featureService(service)
                               .build();
    }

    @Bean
    @ConditionalOnProperty(name = "inugami.monitoring.actuator.fail.safe.enabled", havingValue = "true", matchIfMissing = true)
    public StatusAggregator failSafeStatusAggregator() {
        return new FailSafeStatusAggregator();
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private void initializeInterceptors(final MonitoringContext monitoringContext,
                                        final Monitoring monitoring) {

        addInterceptorIfNoPresent(IoLogInterceptor.class,
                                  ctx -> ctx.getInterceptors()
                                            .add(new IoLogInterceptor(ConfigConfiguration.CONFIGURATION)),
                                  monitoringContext,
                                  monitoring);


    }


    private <T extends MonitoringFilterInterceptor> void addInterceptorIfNoPresent(final Class<T> interceptorClass,
                                                                                   final Consumer<MonitoringContext> appender,
                                                                                   final MonitoringContext monitoringContext,
                                                                                   final Monitoring monitoring) {
        final Optional<MonitoringFilterInterceptor> interceptor = Optional.ofNullable(monitoring.getInterceptors())
                                                                          .orElse(List.of())
                                                                          .stream()
                                                                          .filter(i -> i.getClass()
                                                                                        .isInstance(
                                                                                                interceptorClass))
                                                                          .findFirst();

        if (!interceptor.isPresent()) {
            appender.accept(monitoringContext);
        }
    }


}
