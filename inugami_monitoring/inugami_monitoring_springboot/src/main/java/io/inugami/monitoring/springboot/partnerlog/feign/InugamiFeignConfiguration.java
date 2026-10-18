package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.Client;
import feign.Contract;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.jackson3.Jackson3Decoder;
import feign.okhttp.OkHttpClient;
import io.inugami.monitoring.springboot.config.InugamiMonitoringProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Configuration
public class InugamiFeignConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public Client inugamiDefaultClient(final InugamiMonitoringProperties properties) {
        return new OkHttpClient(new okhttp3.OkHttpClient.Builder()
                                        .addInterceptor(new OkClientAntiSsrfInterceptor(properties.getFeign()))
                                        .followRedirects(false)
                                        .build());
    }

    @Bean
    @ConditionalOnMissingBean
    public Contract inugamiSpringMvcContract() {
        return new InugamiSpringMvcContract();
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorDecoder feignPartnerErrorDecoder(@Autowired(required = false) final List<FeignPartnerErrorResolver> errorResolvers) {
        return new FeignPartnerErrorDecoder(errorResolvers);
    }


    @Bean
    @ConditionalOnMissingBean
    public Decoder inugamiFeignDecoder(final JsonMapper objectMapper) {
        return FeignPartnerResponseDecoder.builder()
                                          .decoder(new Jackson3Decoder(objectMapper))
                                          .build();
    }
}
