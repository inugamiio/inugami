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

import io.inugami.api.monitoring.JavaRestMethodResolver;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.Headers;
import io.inugami.api.monitoring.models.Monitoring;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.commons.spring.InugamiSpringApplicationListener;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import io.inugami.monitoring.core.context.MonitoringBootstrap;
import io.inugami.monitoring.core.context.MonitoringContext;
import io.inugami.monitoring.core.interceptors.spi.IoLogInterceptor;
import io.inugami.monitoring.core.interceptors.spi.MdcInterceptor;
import io.inugami.monitoring.springboot.filter.IoLogFilter;
import io.inugami.monitoring.springboot.request.SpringRestMethodResolver;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import java.util.Optional;
import java.util.function.Consumer;

@Import({
        IoLogFilter.class,
        SpringRestMethodResolver.class
})
@Slf4j
@Configuration
public class InugamiMonitoringConfig {
    private       MonitoringBootstrap           monitoringBootstrap;
    private final ConfigHandler<String, String> configuration = new ConfigHandlerHashMap();


    @Bean
    public MonitoringBootstrap initMonitoringContext() {

        initializeConfig();

        final Monitoring config = MonitoringBootstrap.CONTEXT.getConfig();
        config.refreshConfig(configuration);
        config.setHeaders(Headers.buildFromConfig(configuration));
        monitoringBootstrap = new MonitoringBootstrap();

        initializeInterceptors(monitoringBootstrap);
        monitoringBootstrap.contextInitialized();
        return monitoringBootstrap;
    }

    private void initializeConfig() {
        final ConfigurableEnvironment resolver = InugamiSpringApplicationListener.getConfigurableEnvironment();

        initializeConfig(resolver,
                         SpringConfigBinding.builder()
                                            .springKey("inugami.monitoring.iolog.decorator.enabled")
                                            .inugamiKey("enableDecorator")
                                            .defaultValue("true")
                                            .build(),

                         SpringConfigBinding.builder()
                                            .springKey("inugami.monitoring.iolog.decorator.in")
                                            .inugamiKey("inputDecorator")
                                            .build(),

                         SpringConfigBinding.builder()
                                            .springKey("inugami.monitoring.iolog.decorator.out")
                                            .inugamiKey("outputDecorator")
                                            .build(),

                         SpringConfigBinding.fromKey("inugami.monitoring.enabled", "true"),
                         SpringConfigBinding.fromKey("env", "dev"),

                         SpringConfigBinding.builder()
                                            .springKey("application.name")
                                            .inugamiKey("asset")
                                            .defaultValue("application")
                                            .build(),
                         SpringConfigBinding.builder()
                                            .springKey("spring.application.hostname")
                                            .inugamiKey("hostname")
                                            .build(),
                         SpringConfigBinding.builder()
                                            .springKey("spring.application.instanceName")
                                            .inugamiKey("instanceName")
                                            .build(),
                         SpringConfigBinding.builder()
                                            .springKey("spring.application.instanceNumber")
                                            .inugamiKey("instanceNumber")
                                            .build(),
                         SpringConfigBinding.builder()
                                            .springKey("application.version")
                                            .inugamiKey("version")
                                            .build(),

                         SpringConfigBinding.fromKey("inugami.monitoring.headers.correlationId", "x-correlation-id"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.requestId", "x-b3-traceid"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.conversationId", "x-conversation-id"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.token", "Authorization"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.deviceIdentifier", "x-device-identifier"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.deviceType", "x-device-type"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.deviceClass", "x-device-class"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.deviceVersion", "x-device-version"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.deviceOsVersion", "x-device-os-version"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.deviceNetworkType", "x-device-network-type"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.deviceNetworkSpeedDown", "x-device-network-speed-down"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.deviceNetworkSpeedUp", "x-device-network-speed-up"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.deviceNetworkSpeedLatency", "x-device-network-speed-latency"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.deviceIp", "clientIp"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.userAgent", "User-Agent"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.language", "Accept-Language"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.country", "country"),
                         SpringConfigBinding.fromKey("inugami.monitoring.headers.specifics", "")
        );
    }

    private void initializeConfig(final ConfigurableEnvironment resolver,
                                  SpringConfigBinding... bindings) {
        for (SpringConfigBinding binding : bindings) {
            final String value = orDefault(binding.getSpringKey(),
                                           binding.getDefaultValue() == null ? "" : binding.getDefaultValue(),
                                           resolver);
            configuration.put(binding.getInugamiKey() == null ? binding.getSpringKey() : binding.getInugamiKey(),
                              value);
        }
    }


    private void initializeInterceptors(final MonitoringBootstrap monitoringBootstrap) {

        addInterceptorIfNoPresent(MdcInterceptor.class, ctx -> ctx.getInterceptors().add(new MdcInterceptor()));
        addInterceptorIfNoPresent(IoLogInterceptor.class,
                                  ctx -> ctx.getInterceptors().add(new IoLogInterceptor(configuration)));


    }

    private <T extends MonitoringFilterInterceptor> void addInterceptorIfNoPresent(final Class<T> interceptorClass,
                                                                                   Consumer<MonitoringContext> appender) {

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


    // =========================================================================
    // TOOLS
    // =========================================================================
    private String orDefault(final String key, final String defaultValue,
                             final ConfigurableEnvironment resolver) {
        String value = null;
        try {
            value = resolver.getProperty(key);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
        return value == null ? defaultValue : value;
    }

    @EqualsAndHashCode
    @ToString
    @Builder(toBuilder = true)
    @Getter
    private static class SpringConfigBinding {
        private final String springKey;
        private final String inugamiKey;
        private final String defaultValue;

        static SpringConfigBinding fromKey(final String key) {
            return new SpringConfigBinding(key, key, null);
        }

        static SpringConfigBinding fromKey(final String key, final String defaultValue) {
            return new SpringConfigBinding(key, key, defaultValue);
        }
    }
}
