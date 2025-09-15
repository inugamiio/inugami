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

import io.inugami.monitoring.core.interceptable.DefaultInterceptableIdentifier;
import io.inugami.monitoring.core.spi.H2Interceptable;
import io.inugami.monitoring.springboot.request.SpringRestMethodTracker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InugamiMonitoringJavaRestMethodTrackerConfig {
    @ConditionalOnMissingBean
    @Bean
    public SpringRestMethodTracker springRestMethodTracker(){
        return new SpringRestMethodTracker();
    }

}
