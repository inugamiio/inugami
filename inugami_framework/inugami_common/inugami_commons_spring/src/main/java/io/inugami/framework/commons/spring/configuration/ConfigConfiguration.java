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
package io.inugami.framework.commons.spring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.commons.marshaling.XmlJaxbMarshallerSpiFactory;
import io.inugami.framework.commons.spring.SpringSpiLoaderServiceSPI;
import io.inugami.framework.commons.spring.feature.FeatureConfiguration;
import io.inugami.framework.configuration.services.ConfigHandlerHashMap;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.marshalling.XmlJaxbMarshallerSpi;
import io.inugami.framework.interfaces.monitoring.models.CurrentApplicationDTO;
import io.inugami.framework.interfaces.spi.SpiLoader;
import io.inugami.framework.interfaces.spi.SpiLoaderServiceSPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.Clock;
import java.time.ZoneOffset;

@SuppressWarnings({"java:S2386"})
@Slf4j
@EnableAspectJAutoProxy
@Import({
        FeatureConfiguration.class
})
@Configuration
public class ConfigConfiguration {
    public static final String INUGAMI = Inugami.BASE_PACKAGE;

    public static final ConfigHandler<String, String> CONFIGURATION = new ConfigHandlerHashMap();


    @ConditionalOnMissingBean
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @ConditionalOnMissingBean
    @Bean
    public ZoneOffset zoneOffset() {
        return ZoneOffset.UTC;
    }

    @Bean
    public ConfigHandler<String, String> config() {
        return CONFIGURATION;
    }

    @Bean
    public CurrentApplicationDTO currentApplication(@Value("${env:DEV}") final String env,
                                                    @Value("${application.hostname:localhost}") final String hostname,
                                                    @Value("${application.instanceName:#{null}}") final String instanceName,
                                                    @Value("${application.instanceNumber:#{null}}") final String instanceNumber,
                                                    @Value("${application.groupId:#{null}}") final String groupId,
                                                    @Value("${application.name:#{null}}") final String asset,
                                                    @Value("${application.artifactId:#{null}}") final String artifactId,
                                                    @Value("${application.version:#{null}}") final String version,
                                                    @Value("${application.commitId:#{null}}") final String commitId,
                                                    @Value("${application.commitDate:#{null}}") final String commitDate) {
        return CurrentApplicationDTO.builder()
                                    .env(env)
                                    .asset(asset)
                                    .instanceName(instanceName)
                                    .instanceNumber(instanceNumber)
                                    .groupId(groupId)
                                    .artifactId(artifactId)
                                    .version(version)
                                    .commitId(commitId)
                                    .commitDate(commitDate)
                                    .build();
    }

    public static void initializeConfig(final ConfigurableEnvironment resolver) {

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
                                            .springKey("application.hostname")
                                            .inugamiKey("hostname")
                                            .build(),
                         SpringConfigBinding.builder()
                                            .springKey("application.instanceName")
                                            .inugamiKey("instanceName")
                                            .build(),
                         SpringConfigBinding.builder()
                                            .springKey("application.instanceNumber")
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

    static void initializeConfig(final ConfigurableEnvironment resolver,
                                 final SpringConfigBinding... bindings) {
        for (final SpringConfigBinding binding : bindings) {
            final String value = orDefault(binding.getSpringKey(),
                                           binding.getDefaultValue() == null ? "" : binding.getDefaultValue(),
                                           resolver);
            CONFIGURATION.put(binding.getInugamiKey() == null ? binding.getSpringKey() : binding.getInugamiKey(),
                              value);
        }
    }

    static String orDefault(final String key, final String defaultValue,
                            final ConfigurableEnvironment resolver) {
        String value = null;
        try {
            value = resolver.getProperty(key);
        } catch (final Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
        return value == null ? defaultValue : value;
    }

    @ConditionalOnMissingBean
    @Bean
    public XmlJaxbMarshallerSpi xmlJaxbMarshallerSpi() {
        return XmlJaxbMarshallerSpiFactory.getInstance();
    }

    @ConditionalOnMissingBean
    @Bean
    public ObjectMapper objectMapper() {
        return JsonMarshaller.getInstance().getDefaultObjectMapper();
    }

    @ConditionalOnMissingBean
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(final ObjectMapper objectMapper) {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @ConditionalOnMissingBean
    @Bean
    public SpelExpressionParser spelExpressionParser() {
        return new SpelExpressionParser();
    }

    @Primary
    @Bean
    public SpiLoaderServiceSPI spiLoaderServiceSPI() {
        final var result = new SpringSpiLoaderServiceSPI();
        SpiLoader.getInstance().reloadLoaderService(result);
        return result;
    }
}
