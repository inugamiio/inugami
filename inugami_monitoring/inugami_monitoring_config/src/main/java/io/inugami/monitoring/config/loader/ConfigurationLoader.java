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

import com.thoughtworks.xstream.XStream;
import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.monitoring.MonitoringLoaderSpi;
import io.inugami.api.monitoring.models.Monitoring;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.processors.DefaultConfigHandler;
import io.inugami.commons.files.FilesUtils;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import io.inugami.monitoring.config.models.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ConfigurationLoader
 *
 * @author patrickguillerm
 * @since Jan 15, 2019
 */
@SuppressWarnings({"java:S1874", "java:S1181"})
public final class ConfigurationLoader implements MonitoringLoaderSpi {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final XStream XSTREAM_MAIN = initXStream();

    public final Monitoring configuration;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ConfigurationLoader() {
        configuration = initializeConfiguration(null);
    }

    public ConfigurationLoader(final ConfigHandler<String, String> springConfig) {
        configuration = initializeConfiguration(springConfig);
    }

    private static XStream initXStream() {
        //@formatter:off
        final Class<?>[] types = {
            List.class,
            ArrayList.class,
            DefaultHeaderInformation.class,
            HeaderInformationsConfig.class,
            MonitoringConfig.class,
            MonitoringSenderConfig.class,
            MonitoringSensorConfig.class,
            PropertyConfigModel.class,
            SpecificHeaders.class,
            SpecificHeader.class,
            InterceptorsConfig.class,
            InterceptorConfig.class
        };
        //@formatter:on

        final XStream result = new XStream();
        result.autodetectAnnotations(true);
        result.processAnnotations(types);
        result.alias("property", PropertyConfigModel.class);
        XStream.setupDefaultSecurity(result);
        result.allowTypes(types);
        return result;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Monitoring load() {
        return configuration;
    }

    private Monitoring initializeConfiguration(final ConfigHandler<String, String> springConfig) {
        final File                    configFile    = resolveConfigFilePath();
        ConfigHandler<String, String> configHandler = springConfig == null ? new DefaultConfigHandler() : springConfig;
        MonitoringConfig              result        = null;

        if (configFile == null) {
            result = new MonitoringConfig();
        } else {
            result = loadConfiguration(configFile);
            configHandler = buildConfigHandler(result, springConfig);
        }

        try {
            result.postProcessing(configHandler);
        } catch (final TechnicalException e) {
            throw new FatalException(e.getMessage(), e);
        }

        return MonitoringDataBuilder.build(result, configHandler);
    }

    protected static MonitoringConfig loadConfiguration(final File configFile) {
        try {
            if ((configFile != null) && configFile.exists() && configFile.canRead()) {
                return (MonitoringConfig) XSTREAM_MAIN.fromXML(configFile);
            }
        } catch (Throwable e) {
            Loggers.CONFIG.error(e.getMessage(), e);
        }

        return new MonitoringConfig();
    }

    // =========================================================================
    // RESOLVE CONFIG FILE
    // =========================================================================
    private static File resolveConfigFilePath() {
        File         result       = null;
        final String specificFile = JvmKeyValues.MONITORING_FILE.get();
        if (specificFile != null) {
            result = new File(specificFile);
        } else if (JvmKeyValues.JVM_HOME_PATH.get() != null) {
            result = FilesUtils.buildFile(new File(JvmKeyValues.JVM_HOME_PATH.get()), "monitoring.xml");
        } else {
            Loggers.INIT.info("no monitoring configuration file define");
        }
        return result;
    }

    // =========================================================================
    // BUILDER
    // =========================================================================
    private static ConfigHandler<String, String> buildConfigHandler(final MonitoringConfig config,
                                                                    final ConfigHandler<String, String> springConfig) {
        final Map<String, String> buffer = new HashMap<>();

        //@formatter:off
        final PropertiesConfig properties = config.getProperties()==null?new PropertiesConfig():config.getProperties();
        properties.getProperties()
                  .stream()
                  .filter(item -> item.getKey() !=null)
                  .filter(item -> item.getValue() !=null)
                  .forEach(item-> buffer.put(item.getKey(), item.getValue()));
        //@formatter:on

        System.getProperties().forEach((key, value) -> buffer.put(String.valueOf(key), String.valueOf(value)));

        final ConfigHandlerHashMap result = new ConfigHandlerHashMap(buffer);
        if (springConfig != null) {
            result.putAll(springConfig);
        }
        return result;
    }

}
