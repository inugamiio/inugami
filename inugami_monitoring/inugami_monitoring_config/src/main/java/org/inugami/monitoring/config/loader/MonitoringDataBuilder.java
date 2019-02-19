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
package org.inugami.monitoring.config.loader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.commons.spi.SpiLoader;
import org.inugami.configuration.services.ConfigHandlerHashMap;
import org.inugami.monitoring.api.data.config.Headers;
import org.inugami.monitoring.api.data.config.Monitoring;
import org.inugami.monitoring.api.interceptors.MonitoringFilterInterceptor;
import org.inugami.monitoring.api.senders.MonitoringSender;
import org.inugami.monitoring.api.sensors.MonitoringSensor;
import org.inugami.monitoring.config.models.DefaultHeaderInformation;
import org.inugami.monitoring.config.models.HeaderInformationsConfig;
import org.inugami.monitoring.config.models.InterceptorConfig;
import org.inugami.monitoring.config.models.InterceptorsConfig;
import org.inugami.monitoring.config.models.MonitoringConfig;
import org.inugami.monitoring.config.models.MonitoringSenderConfig;
import org.inugami.monitoring.config.models.MonitoringSendersConfig;
import org.inugami.monitoring.config.models.MonitoringSensorConfig;
import org.inugami.monitoring.config.models.PropertiesConfig;
import org.inugami.monitoring.config.models.SensorsConfig;
import org.inugami.monitoring.config.models.SpecificHeader;

/**
 * MonitoringDataBuilder
 * 
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
final class MonitoringDataBuilder {
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private MonitoringDataBuilder() {
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    static Monitoring build(final MonitoringConfig config, final ConfigHandler<String, String> configHandler) {
        Asserts.notNull("monitoring configuration is mandatory", config);
        Asserts.notNull("configuration handler is mandatory", configHandler);
        final SpiLoader spiloader = new SpiLoader();
        
        //@formatter:off
        final List<MonitoringFilterInterceptor> interceptors = buildInterceptors(config.getInterceptors(), configHandler, spiloader);
        final List<MonitoringSender> senders = buildSenders(config.getSenders(), configHandler, spiloader);
        final List<MonitoringSensor> sensors = buildSensors(config.getSensors(), configHandler, spiloader);
        final Headers header = buildHeaders(config.getHeader(), configHandler);
        
        return  new Monitoring(config.getEnable()==null?true:config.getEnable().booleanValue(),
                               configHandler.applyProperties(config.getEnv()),
                               configHandler.applyProperties(config.getAsset()),
                               configHandler.applyProperties(config.getHostname()),
                               configHandler.applyProperties(config.getInstanceName()),
                               configHandler.applyProperties(config.getInstanceNumber()),
                               configHandler,
                               senders,
                               sensors,
                               header,
                               Integer.parseInt(config.getMaxSensorsTasksThreads()),
                               configHandler.applyProperties(config.getApplicationVersion()),
                               interceptors);
        //@formatter:on
    }
    
    // =========================================================================
    // BUILD HEADERS
    // =========================================================================
    private static Headers buildHeaders(final HeaderInformationsConfig config,
                                        final ConfigHandler<String, String> configHandler) {
        
        final Set<String> specificHeaders = new HashSet<>();
        if ((config.getSpecificHeader() != null) && (config.getSpecificHeader().getSpecificHeader() != null)) {
            for (final SpecificHeader item : config.getSpecificHeader().getSpecificHeader()) {
                specificHeaders.add(configHandler.applyProperties(item.getName()));
            }
        }
        final DefaultHeaderInformation defaultConfig = config.getDefaultHeader();
        //@formatter:off
        return new Headers(configHandler.applyProperties(defaultConfig.getCorrelationId()),
                           configHandler.applyProperties(defaultConfig.getRequestId()),
                           configHandler.applyProperties(defaultConfig.getConversationId()),
                           configHandler.applyProperties(defaultConfig.getToken()),
                           configHandler.applyProperties(defaultConfig.getDeviceIdentifier()),
                           configHandler.applyProperties(defaultConfig.getDeviceType()),
                           configHandler.applyProperties(defaultConfig.getDeviceClass()),
                           configHandler.applyProperties(defaultConfig.getDeviceVersion()),
                           configHandler.applyProperties(defaultConfig.getDeviceOsVersion()),
                           configHandler.applyProperties(defaultConfig.getDeviceNetworkType()),
                           configHandler.applyProperties(defaultConfig.getDeviceNetworkSpeedDown()),
                           configHandler.applyProperties(defaultConfig.getDeviceNetworkSpeedUp()),
                           configHandler.applyProperties(defaultConfig.getDeviceNetworkSpeedLatency()),
                           configHandler.applyProperties(defaultConfig.getDeviceIp()),
                           configHandler.applyProperties(defaultConfig.getUserAgent()),
                           configHandler.applyProperties(defaultConfig.getLanguage()),
                           configHandler.applyProperties(defaultConfig.getCountry()),
                           specificHeaders);
      //@formatter:on
    }
    
    // =========================================================================
    // BUILD INTERCEPTOR
    // =========================================================================
    private static List<MonitoringFilterInterceptor> buildInterceptors(final InterceptorsConfig interceptors,
                                                                       final ConfigHandler<String, String> configHandler,
                                                                       final SpiLoader spiloader) {
        final InterceptorsConfig interceptorsConfig = interceptors == null ? new InterceptorsConfig() : interceptors;
        //@formatter:off
        return interceptorsConfig.getInterceptors()
                            .stream()
                            .map(item->buildInterceptor(item,configHandler,spiloader))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
        //@formatter:on
    }
    
    private static MonitoringFilterInterceptor buildInterceptor(final InterceptorConfig config,
                                                                final ConfigHandler<String, String> configHandler,
                                                                final SpiLoader spiloader) {
        final MonitoringFilterInterceptor spiInstance = loadSpiInstance(config.getName(),
                                                                        MonitoringFilterInterceptor.class, spiloader);
        //@formatter:off
        return spiInstance == null ? null
                                   : spiInstance.buildInstance(buildCongifHandler(config.getProperties(),configHandler));
        //@formatter:on
    }
    
    // =========================================================================
    // BUILD SENDERS
    // =========================================================================
    private static List<MonitoringSender> buildSenders(final MonitoringSendersConfig monitoringSendersConfig,
                                                       final ConfigHandler<String, String> configHandler,
                                                       final SpiLoader spiloader) {
        
        final MonitoringSendersConfig sendersConfig = monitoringSendersConfig == null ? new MonitoringSendersConfig()
                                                                                      : monitoringSendersConfig;
        //@formatter:off
        return sendersConfig.getSenders()
                            .stream()
                            .map(item->buildSender(item,configHandler,spiloader))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
        //@formatter:on
    }
    
    private static MonitoringSender buildSender(final MonitoringSenderConfig senderConfig,
                                                final ConfigHandler<String, String> configHandler,
                                                final SpiLoader spiloader) {
        //@formatter:off
        final MonitoringSender spiInstance = loadSpiInstance(senderConfig.getName(), MonitoringSender.class, spiloader);
        return spiInstance == null ? null
                                   : spiInstance.buildInstance(buildCongifHandler(senderConfig.getProperties(), configHandler));
        //@formatter:on
    }
    
    // =========================================================================
    // BUILD SENSORS
    // =========================================================================
    private static List<MonitoringSensor> buildSensors(final SensorsConfig sensorsConfig,
                                                       final ConfigHandler<String, String> configHandler,
                                                       final SpiLoader spiloader) {
        
        final SensorsConfig sensors = sensorsConfig == null ? new SensorsConfig() : sensorsConfig;
        //@formatter:off
        return sensors.getSensors()
                      .stream()
                      .map(item->buildSensor(item,configHandler,spiloader))
                      .filter(Objects::nonNull)
                      .collect(Collectors.toList());
        //@formatter:on
    }
    
    private static MonitoringSensor buildSensor(final MonitoringSensorConfig sensorConfig,
                                                final ConfigHandler<String, String> configHandler,
                                                final SpiLoader spiloader) {
        
        //@formatter:off
        MonitoringSensor result =null;
        final MonitoringSensor spiInstance = loadSpiInstance(sensorConfig.getName(), MonitoringSensor.class, spiloader);
        if(spiInstance !=null) {
            final ConfigHandler<String, String> localConfig = buildCongifHandler(sensorConfig.getProperties(),configHandler);
            String interval = localConfig.applyProperties(sensorConfig.getInterval());
            if (interval == null) {
                interval = "60000";
            }
            
            result=spiInstance.buildInstance(Long.parseLong(interval), sensorConfig.getQuery(),localConfig) ;
        }
        return result;
        //@formatter:on
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private static <T> T loadSpiInstance(final String name, final Class<? extends T> spiClass,
                                         final SpiLoader spiloader) {
        return name == null ? null : spiloader.loadSpiService(name, spiClass, false);
    }
    
    private static ConfigHandler<String, String> buildCongifHandler(final PropertiesConfig properties,
                                                                    final ConfigHandler<String, String> baseConfig) {
        if ((properties == null) || properties.getProperties().isEmpty()) {
            return baseConfig;
        }
        final Map<String, String> config = new HashMap<>();
        
        baseConfig.entrySet().forEach(entry -> config.put(entry.getKey(), entry.getValue()));
        properties.getProperties().forEach(item -> config.put(item.getKey(), item.getValue()));
        
        return new ConfigHandlerHashMap(config);
    }
    
}
