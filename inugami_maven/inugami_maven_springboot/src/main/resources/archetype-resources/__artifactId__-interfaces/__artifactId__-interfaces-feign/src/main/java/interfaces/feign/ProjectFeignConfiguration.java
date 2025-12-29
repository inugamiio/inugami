package ${package}.interfaces.feign;

import ${package}.interfaces.api.domain.user.UserRestClient;
import feign.Client;
import feign.Contract;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import io.inugami.framework.commons.spring.feign.FeignBuilder;
import io.inugami.framework.interfaces.rest.PartnerConfigurationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @since 2025-12-29
 */
@Configuration
public class ProjectFeignConfiguration {
    public static final String PACKAGE = "${package}.interfaces.feign";

    @ConditionalOnMissingBean
    @Bean
    public PartnerConfigurationDTO ${rootArtifactId}PartnerConfiguration(@Value("${symbol_dollar}{application.partners.${rootArtifactId}.baseUrl: http://localhost:8080}") final String baseUrl,
                                                               @Value("${symbol_dollar}{application.partners.${rootArtifactId}.user: #{null}}") final String user,
                                                               @Value("${symbol_dollar}{application.partners.${rootArtifactId}.password: #{null}}") final String password) {
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
    public UserRestClient userRestClient(final FeignBuilder inugamiFeignBuilder) {
        return inugamiFeignBuilder.buildClient(UserRestClient.class);
    }

}
