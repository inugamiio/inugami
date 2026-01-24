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
package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.codec.Encoder;
import feign.okhttp.OkHttpClient;
import io.inugami.monitoring.core.context.MonitoringContext;
import io.inugami.monitoring.springboot.config.InugamiMonitoringProperties;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InugamiMonitoringFeignConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public Encoder encoder(final ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringEncoder(messageConverters);
    }

    @ConditionalOnMissingBean
    @Bean
    public OkHttpClient client(final InugamiMonitoringProperties properties) {
        return new OkHttpClient(new okhttp3.OkHttpClient.Builder()
                                        .addInterceptor(new OkClientAntiSsrfInterceptor(properties.getFeign()))
                                        .followRedirects(false)
                                        .build());
    }


    @ConditionalOnMissingBean
    @Bean
    public FeignPartnerRequestInterceptor feignPartnerRequestInterceptor(final MonitoringContext monitoringContext){
        return new FeignPartnerRequestInterceptor(monitoringContext);
    }
}
