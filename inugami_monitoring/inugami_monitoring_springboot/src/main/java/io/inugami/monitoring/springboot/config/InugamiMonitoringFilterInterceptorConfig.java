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

import io.inugami.monitoring.core.spi.CorsInterceptable;
import io.inugami.monitoring.core.spi.IoLogInterceptor;
import io.inugami.monitoring.core.spi.MdcInterceptor;
import io.inugami.monitoring.core.spi.ServiceCounterInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InugamiMonitoringFilterInterceptorConfig {
    @ConditionalOnMissingBean
    @Bean
    public ServiceCounterInterceptor serviceCounterInterceptor() {
        return new ServiceCounterInterceptor();
    }

    @ConditionalOnMissingBean
    @Bean
    public MdcInterceptor mdcInterceptor() {
        return new MdcInterceptor();
    }

    @ConditionalOnMissingBean
    @Bean
    public CorsInterceptable corsInterceptable() {
        return new CorsInterceptable();
    }

    @ConditionalOnMissingBean
    @Bean
    public IoLogInterceptor ioLogInterceptor() {
        return new IoLogInterceptor();
    }


}
