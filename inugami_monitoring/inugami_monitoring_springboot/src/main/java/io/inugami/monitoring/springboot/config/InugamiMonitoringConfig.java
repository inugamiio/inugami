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



import io.inugami.framework.commons.spring.configuration.ConfigConfiguration;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.exceptions.ErrorCodeResolver;
import io.inugami.framework.interfaces.feature.IFeatureService;
import io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.framework.interfaces.monitoring.models.Monitoring;
import io.inugami.monitoring.core.context.MonitoringBootstrapService;
import io.inugami.monitoring.core.context.MonitoringContext;
import io.inugami.monitoring.core.spi.IoLogInterceptor;
import io.inugami.monitoring.core.spi.MdcInterceptor;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.function.Consumer;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;


@SuppressWarnings({"java:S1450"})
@Import({
        IoLogFilter.class,
        SpringRestMethodResolver.class,
        ConfigConfiguration.class
})
@Slf4j
@Configuration
public class InugamiMonitoringConfig {
    public static final String INUGAMI_MONITORING_CONFIG = "io.inugami.monitoring.springboot";
    public static final String INUGAMI                   = "io.inugami";
    MonitoringContext monitoringContext;
    private Monitoring monitoring;


    // ========================================================================
    // BEANS
    // ========================================================================
    @Bean
    public Monitoring initMonitoringContext(final ConfigHandler<String, String> springConfig) {
        Monitoring config = MonitoringBootstrapService.CONTEXT.get().getConfig();
        MonitoringContext monitoringContext = MonitoringBootstrapService.CONTEXT.get();
        final ConfigHandler<String, String> currentConfiguration = ConfigConfiguration.CONFIGURATION;
        applyIfNotNull(springConfig, currentConfiguration::putAll);
        config.setProperties(mergeProperties(config.getProperties(),currentConfiguration));
        initializeInterceptors();
        return config;
    }

    private ConfigHandler<String, String> mergeProperties(final ConfigHandler<String, String> properties,
                                                          final ConfigHandler<String, String> currentConfiguration) {
        if(properties==null){
            return currentConfiguration;
        }
        if(currentConfiguration!=null){
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

    // ========================================================================
    // TOOLS
    // ========================================================================
    private void initializeInterceptors() {

        addInterceptorIfNoPresent(MdcInterceptor.class, ctx -> ctx.getInterceptors().add(new MdcInterceptor()));
        addInterceptorIfNoPresent(IoLogInterceptor.class,
                                  ctx -> ctx.getInterceptors()
                                            .add(new IoLogInterceptor(ConfigConfiguration.CONFIGURATION)));


    }


    private <T extends MonitoringFilterInterceptor> void addInterceptorIfNoPresent(final Class<T> interceptorClass,
                                                                                   final Consumer<MonitoringContext> appender) {

        final Optional<MonitoringFilterInterceptor> interceptor = monitoring.getInterceptors()
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
