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
package io.inugami.dashboard.infrastructure.internal;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@EnableConfigurationProperties(InugamiInfrastructureInternalProperties.class)
@Configuration
public class InugamiInfrastructureInternalConfiguration {

    @Bean
    public Config hazelcastConfig(final InugamiInfrastructureInternalProperties properties) {
        Config config = new Config();
        config.getNetworkConfig()
              .setPort(properties.getHazelcast().getPort())
              .setPortAutoIncrement(Optional.ofNullable(properties.getHazelcast().getAuto())
                                            .orElse(Boolean.TRUE)
                                            .booleanValue());
        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(final Config config) {
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
        return instance;
    }

    @Bean
    public ClientConfig clientConfig(final InugamiInfrastructureInternalProperties properties) {
        ClientConfig cfg = ClientConfig.load();
        cfg.setClusterName(properties.getHazelcast().getCluster());
        return cfg;
    }
}
