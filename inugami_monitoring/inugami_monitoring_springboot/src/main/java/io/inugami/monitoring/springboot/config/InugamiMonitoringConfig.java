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

import io.inugami.api.exceptions.ErrorCodeResolver;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.Headers;
import io.inugami.api.monitoring.models.Monitoring;
import io.inugami.commons.spring.configuration.ConfigConfiguration;
import io.inugami.commons.spring.exception.ExceptionHandlerService;
import io.inugami.monitoring.core.context.MonitoringBootstrap;
import io.inugami.monitoring.core.context.MonitoringContext;
import io.inugami.monitoring.core.interceptors.spi.IoLogInterceptor;
import io.inugami.monitoring.core.interceptors.spi.MdcInterceptor;
import io.inugami.monitoring.springboot.actuator.FailSafeStatusAggregator;
import io.inugami.monitoring.springboot.actuator.VersionHealthIndicator;
import io.inugami.monitoring.springboot.exception.DefaultExceptionHandlerService;
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

@Import({
        IoLogFilter.class,
        SpringRestMethodResolver.class
})
@Slf4j
@Configuration
public class InugamiMonitoringConfig {
    private MonitoringBootstrap monitoringBootstrap;


    // ========================================================================
    // BEANS
    // ========================================================================
    @Bean
    public MonitoringBootstrap initMonitoringContext() {
        final Monitoring config = MonitoringBootstrap.CONTEXT.getConfig();
        config.refreshConfig(ConfigConfiguration.CONFIGURATION);
        config.setHeaders(Headers.buildFromConfig(ConfigConfiguration.CONFIGURATION));
        monitoringBootstrap = new MonitoringBootstrap();

        initializeInterceptors(monitoringBootstrap);
        monitoringBootstrap.contextInitialized();
        return monitoringBootstrap;
    }

    @Bean
    public MonitoringContext monitoringContext(final MonitoringBootstrap monitoringBootstrap) {
        return MonitoringBootstrap.CONTEXT;
    }


    @Bean
    public ErrorCodeResolver springDefaultErrorCodeResolver() {
        return new SpringDefaultErrorCodeResolver();
    }

    @Bean
    @ConditionalOnProperty(name = "inugami.monitoring.exception.enabled", havingValue = "true", matchIfMissing = true)
    public ExceptionHandlerService exceptionHandlerService(@Value("${application.name:#{null}}") final String applicationName,
                                                           @Value("${application.version:#{null}}") final String applicationVersion,
                                                           @Value("${application.wiki:#{null}}") final String applicationWiki,
                                                           @Value("${inugami.monitoring.exception.show.detail.enabled:#{null}") final String showDetail) {


        return DefaultExceptionHandlerService.builder()
                                             .applicationName(applicationName)
                                             .applicationVersion(applicationVersion)
                                             .wikiPage(applicationWiki)
                                             .showAllDetail(showDetail == null ? true : Boolean.parseBoolean(showDetail))
                                             .build();
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
    @ConditionalOnProperty(name = "inugami.monitoring.actuator.fail.safe.enabled", havingValue = "true", matchIfMissing = true)
    public StatusAggregator failSafeStatusAggregator() {
        return new FailSafeStatusAggregator();
    }

    // ========================================================================
    // TOOLS
    // ========================================================================
    private void initializeInterceptors(final MonitoringBootstrap monitoringBootstrap) {

        addInterceptorIfNoPresent(MdcInterceptor.class, ctx -> ctx.getInterceptors().add(new MdcInterceptor()));
        addInterceptorIfNoPresent(IoLogInterceptor.class,
                                  ctx -> ctx.getInterceptors().add(new IoLogInterceptor(ConfigConfiguration.CONFIGURATION)));


    }


    private <T extends MonitoringFilterInterceptor> void addInterceptorIfNoPresent(final Class<T> interceptorClass,
                                                                                   final Consumer<MonitoringContext> appender) {

        final Optional<MonitoringFilterInterceptor> interceptor = MonitoringBootstrap.CONTEXT.getInterceptors()
                                                                                             .stream()
                                                                                             .filter(i -> i.getClass()
                                                                                                           .isInstance(
                                                                                                                   interceptorClass))
                                                                                             .findFirst();

        if (!interceptor.isPresent()) {
            appender.accept(MonitoringBootstrap.CONTEXT);
        }
    }


}
