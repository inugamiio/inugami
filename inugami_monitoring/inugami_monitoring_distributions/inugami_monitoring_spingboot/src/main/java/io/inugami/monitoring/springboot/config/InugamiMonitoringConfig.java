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
import io.inugami.commons.spring.configuration.ConfigConfiguration;
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




    private void initializeInterceptors(final MonitoringBootstrap monitoringBootstrap) {

        addInterceptorIfNoPresent(MdcInterceptor.class, ctx -> ctx.getInterceptors().add(new MdcInterceptor()));
        addInterceptorIfNoPresent(IoLogInterceptor.class,
                                  ctx -> ctx.getInterceptors().add(new IoLogInterceptor(ConfigConfiguration.CONFIGURATION)));


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


}
