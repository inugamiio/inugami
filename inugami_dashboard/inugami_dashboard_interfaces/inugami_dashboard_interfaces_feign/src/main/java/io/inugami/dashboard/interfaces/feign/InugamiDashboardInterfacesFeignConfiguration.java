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
package io.inugami.dashboard.interfaces.feign;

import feign.Client;
import feign.Contract;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import io.inugami.dashboard.interfaces.domain.administration.PingRestClient;
import io.inugami.dashboard.interfaces.domain.alerting.AlertingRestClient;
import io.inugami.dashboard.interfaces.domain.event.EventRestClient;
import io.inugami.dashboard.interfaces.domain.plugin.PluginRestClient;
import io.inugami.framework.commons.spring.feign.FeignBuilder;
import io.inugami.framework.interfaces.rest.PartnerConfigurationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@EnableFeignClients
@Configuration
public class InugamiDashboardInterfacesFeignConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public PartnerConfigurationDTO inugamiPartnerConfiguration(@Value("${application.partners.inugami.baseUrl: http://localhost:8080}") final String baseUrl,
                                                               @Value("${application.partners.inugami.user: #{null}}") final String user,
                                                               @Value("${application.partners.inugami.password: #{null}}") final String password) {
        return PartnerConfigurationDTO.builder()
                                      .baseUrl(baseUrl)
                                      .user(user)
                                      .password(password)
                                      .build();
    }

    @Bean
    public FeignBuilder inugamiFeignBuilder(final Client client,
                                            final Encoder encoder,
                                            final Decoder decoder,
                                            final PartnerConfigurationDTO inugamiPartnerConfiguration,
                                            final List<RequestInterceptor> interceptors,
                                            final Contract contract) {
        return FeignBuilder.builder()
                           .client(client)
                           .encoder(encoder)
                           .decoder(decoder)
                           .partnerConfiguration(inugamiPartnerConfiguration)
                           .interceptors(interceptors)
                           .contract(contract)
                           .build()
                           .init();
    }

    //==================================================================================================================
    // CLIENTS
    //==================================================================================================================
    @Bean
    public AlertingRestClient alertingRestClient(final FeignBuilder inugamiFeignBuilder) {
        return inugamiFeignBuilder.buildClient(AlertingRestClient.class);
    }

    @Bean
    public EventRestClient eventRestClient(final FeignBuilder inugamiFeignBuilder) {
        return inugamiFeignBuilder.buildClient(EventRestClient.class);
    }

    @Bean
    public PingRestClient pingRestClient(final FeignBuilder inugamiFeignBuilder) {
        return inugamiFeignBuilder.buildClient(PingRestClient.class);
    }

    @Bean
    public PluginRestClient pluginRestClient(final FeignBuilder inugamiFeignBuilder) {
        return inugamiFeignBuilder.buildClient(PluginRestClient.class);
    }
}
