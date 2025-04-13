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


import java.util.Optional;
import java.util.function.Consumer;

import static io.inugami.api.functionnals.FunctionalUtils.applyIfNotNull;

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

    private MonitoringBootstrap monitoringBootstrap;


    // ========================================================================
    // BEANS
    // ========================================================================
    @Bean
    public MonitoringBootstrap initMonitoringContext(final ConfigHandler<String, String> springConfig) {
        Monitoring config = MonitoringBootstrap.CONTEXT.getConfig();

        final ConfigHandler<String, String> currentConfiguration = ConfigConfiguration.CONFIGURATION;
        applyIfNotNull(springConfig, currentConfiguration::putAll);
        config.refreshConfig(currentConfiguration);
        config.setHeaders(Headers.buildFromConfig(currentConfiguration));
        monitoringBootstrap = new MonitoringBootstrap();

        initializeInterceptors();
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
