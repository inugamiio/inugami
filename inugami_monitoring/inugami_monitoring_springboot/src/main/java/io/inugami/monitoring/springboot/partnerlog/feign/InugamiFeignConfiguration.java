package io.inugami.monitoring.springboot.partnerlog.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Client;
import feign.Contract;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InugamiFeignConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public Client inugamiDefaultClient() {
        return new OkHttpClient();
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
    public Decoder inugamiFeignDecoder(final ObjectMapper objectMapper) {
        return FeignPartnerResponseDecoder.builder().decoder(new JacksonDecoder(objectMapper)).build();
    }
}
