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
package io.inugami.monitoring.config.loader;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.Headers;
import io.inugami.api.monitoring.models.Monitoring;
import io.inugami.api.monitoring.senders.MonitoringSender;
import io.inugami.api.monitoring.sensors.MonitoringSensor;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.spi.SpiLoader;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import io.inugami.monitoring.config.models.*;

import java.util.*;
import java.util.stream.Collectors;

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
        Asserts.assertNotNull("monitoring configuration is mandatory", config);
        Asserts.assertNotNull("configuration handler is mandatory", configHandler);
        final SpiLoader spiloader = SpiLoader.getInstance();

        final List<MonitoringFilterInterceptor> interceptors = buildInterceptors(config.getInterceptors(), configHandler, spiloader);
        final List<MonitoringSender>            senders      = buildSenders(config.getSenders(), configHandler, spiloader);
        final List<MonitoringSensor>            sensors      = buildSensors(config.getSensors(), configHandler, spiloader);
        final Headers                           header       = buildHeaders(config.getHeader(), configHandler);

        return Monitoring.builder()
                         .enable(config.getEnable() == null || config.getEnable().booleanValue())
                         .env(configHandler.applyProperties(config.getEnv()))
                         .asset(configHandler.applyProperties(config.getAsset()))
                         .hostname(configHandler.applyProperties(config.getHostname()))
                         .instanceName(configHandler.applyProperties(config.getInstanceName()))
                         .instanceNumber(configHandler.applyProperties(config.getInstanceNumber()))
                         .properties(configHandler)
                         .senders(senders)
                         .sensors(sensors)
                         .headers(header)
                         .maxSensorsTasksThreads(Integer.parseInt(config.getMaxSensorsTasksThreads()))
                         .applicationVersion(configHandler.applyProperties(config.getApplicationVersion()))
                         .interceptors(interceptors)
                         .build();
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

        return Headers.builder()
                      .correlationId(configHandler.applyProperties(defaultConfig.getCorrelationId()))
                      .requestId(configHandler.applyProperties(defaultConfig.getRequestId()))
                      .traceId(configHandler.applyProperties(defaultConfig.getTraceId()))
                      .conversationId(configHandler.applyProperties(defaultConfig.getConversationId()))
                      .token(configHandler.applyProperties(defaultConfig.getToken()))
                      .deviceIdentifier(configHandler.applyProperties(defaultConfig.getDeviceIdentifier()))
                      .deviceType(configHandler.applyProperties(defaultConfig.getDeviceType()))
                      .deviceClass(configHandler.applyProperties(defaultConfig.getDeviceClass()))
                      .deviceSystem(configHandler.applyProperties(defaultConfig.getDeviceSystem()))
                      .deviceVersion(configHandler.applyProperties(defaultConfig.getDeviceVersion()))
                      .deviceOsVersion(configHandler.applyProperties(defaultConfig.getDeviceOsVersion()))
                      .deviceNetworkType(configHandler.applyProperties(defaultConfig.getDeviceNetworkType()))
                      .deviceNetworkSpeedDown(configHandler.applyProperties(defaultConfig.getDeviceNetworkSpeedDown()))
                      .deviceNetworkSpeedUp(configHandler.applyProperties(defaultConfig.getDeviceNetworkSpeedUp()))
                      .deviceNetworkSpeedLatency(configHandler.applyProperties(defaultConfig.getDeviceNetworkSpeedLatency()))
                      .deviceIp(configHandler.applyProperties(defaultConfig.getDeviceIp()))
                      .userAgent(configHandler.applyProperties(defaultConfig.getUserAgent()))
                      .language(configHandler.applyProperties(defaultConfig.getLanguage()))
                      .country(configHandler.applyProperties(defaultConfig.getCountry()))

                      .warning(configHandler.applyProperties(defaultConfig.getWarning()))
                      .errorCode(configHandler.applyProperties(defaultConfig.getErrorCode()))
                      .errorException(configHandler.applyProperties(defaultConfig.getErrorException()))
                      .errorMessage(configHandler.applyProperties(defaultConfig.getErrorMessage()))
                      .errorMessageDetail(configHandler.applyProperties(defaultConfig.getErrorMessageDetail()))
                      .frontVersion(configHandler.applyProperties(defaultConfig.getFrontVersion()))
                      .callFrom(configHandler.applyProperties(defaultConfig.getCallFrom()))
                      .specificHeaders(specificHeaders)
                      .build();


    }

    // =========================================================================
    // BUILD INTERCEPTOR
    // =========================================================================
    private static List<MonitoringFilterInterceptor> buildInterceptors(final InterceptorsConfig interceptors,
                                                                       final ConfigHandler<String, String> configHandler,
                                                                       final SpiLoader spiloader) {
        final InterceptorsConfig interceptorsConfig = interceptors == null ? new InterceptorsConfig() : interceptors;

        return Optional.ofNullable(interceptorsConfig.getInterceptors())
                       .orElse(List.of())
                       .stream()
                       .map(item -> buildInterceptor(item, configHandler, spiloader))
                       .filter(Objects::nonNull)
                       .collect(Collectors.toList());

    }

    private static MonitoringFilterInterceptor buildInterceptor(final InterceptorConfig config,
                                                                final ConfigHandler<String, String> configHandler,
                                                                final SpiLoader spiloader) {
        final MonitoringFilterInterceptor spiInstance = loadSpiInstance(config.getName(), MonitoringFilterInterceptor.class, spiloader);
        //@formatter:off
        return spiInstance == null ? null
                : spiInstance.buildInstance(buildCongifHandler(config.getProperties(), configHandler));
        //@formatter:on
    }

    // =========================================================================
    // BUILD SENDERS
    // =========================================================================
    public static List<MonitoringSender> buildSenders(final MonitoringSendersConfig monitoringSendersConfig,
                                                      final ConfigHandler<String, String> configHandler,
                                                      final SpiLoader spiloader) {

        final MonitoringSendersConfig sendersConfig =
                monitoringSendersConfig == null ? new MonitoringSendersConfig() : monitoringSendersConfig;
        //@formatter:off
        return Optional.ofNullable(sendersConfig.getSenders())
                .orElse(new ArrayList<>())
                            .stream()
                            .map(item -> buildSender(item, configHandler, spiloader))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
        //@formatter:on
    }

    public static MonitoringSender buildSender(final MonitoringSenderConfig senderConfig,
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
    public static List<MonitoringSensor> buildSensors(final SensorsConfig sensorsConfig,
                                                      final ConfigHandler<String, String> configHandler,
                                                      final SpiLoader spiloader) {

        final SensorsConfig sensors = sensorsConfig == null ? new SensorsConfig() : sensorsConfig;

        return Optional.ofNullable(sensors.getSensors())
                       .orElse(new ArrayList<>())
                       .stream()
                       .map(item -> buildSensor(item, configHandler, spiloader))
                       .filter(Objects::nonNull)
                       .collect(Collectors.toList());
    }

    public static MonitoringSensor buildSensor(final MonitoringSensorConfig sensorConfig,
                                               final ConfigHandler<String, String> configHandler,
                                               final SpiLoader spiloader) {

        //@formatter:off
        MonitoringSensor       result      = null;
        final MonitoringSensor spiInstance = loadSpiInstance(sensorConfig.getName(), MonitoringSensor.class, spiloader);
        if (spiInstance != null) {
            final ConfigHandler<String, String> localConfig = buildCongifHandler(sensorConfig.getProperties(), configHandler);
            String                              interval    = localConfig.applyProperties(sensorConfig.getInterval());
            if (interval == null) {
                interval = "60000";
            }

            result = spiInstance.buildInstance(Long.parseLong(interval), sensorConfig.getQuery(), localConfig);
        }
        return result;
        //@formatter:on
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private static <T> T loadSpiInstance(final String name,
                                         final Class<? extends T> spiClass,
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
