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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.monitoring.MonitoringLoaderSpi;
import io.inugami.api.monitoring.models.Monitoring;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.commons.files.FilesUtils;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import io.inugami.monitoring.config.models.DefaultHeaderInformation;
import io.inugami.monitoring.config.models.HeaderInformationsConfig;
import io.inugami.monitoring.config.models.InterceptorConfig;
import io.inugami.monitoring.config.models.InterceptorsConfig;
import io.inugami.monitoring.config.models.MonitoringConfig;
import io.inugami.monitoring.config.models.MonitoringSenderConfig;
import io.inugami.monitoring.config.models.MonitoringSensorConfig;
import io.inugami.monitoring.config.models.PropertiesConfig;
import io.inugami.monitoring.config.models.PropertyConfigModel;
import io.inugami.monitoring.config.models.SpecificHeader;
import io.inugami.monitoring.config.models.SpecificHeaders;

import com.thoughtworks.xstream.XStream;

/**
 * ConfigurationLoader
 * 
 * @author patrickguillerm
 * @since Jan 15, 2019
 */
public final class ConfigurationLoader implements MonitoringLoaderSpi {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static XStream XSTREAM_MAIN = initXStream();
    
    public final Monitoring      configuration;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ConfigurationLoader() {
        configuration = initializeConfiguration();
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
    
    private Monitoring initializeConfiguration() {
        final File configFile = resolveConfigFilePath();
        MonitoringConfig result = null;
        
        if (configFile == null) {
            result = new MonitoringConfig();
        }
        else {
            result = loadConfiguration(configFile);
        }
        
        if (result == null) {
            return null;
        }
        
        final ConfigHandler<String, String> configHandler = buildConfigHandler(result);
        try {
            result.postProcessing(configHandler);
        }
        catch (final TechnicalException e) {
            throw new FatalException(e.getMessage(), e);
        }
        
        return MonitoringDataBuilder.build(result, configHandler);
    }
    
    protected static MonitoringConfig loadConfiguration(final File configFile) {
        MonitoringConfig result = null;
        if ((configFile != null) && configFile.exists() && configFile.canRead()) {
            result = (MonitoringConfig) XSTREAM_MAIN.fromXML(configFile);
        }
        return result;
    }
    
    // =========================================================================
    // RESOLVE CONFIG FILE
    // =========================================================================
    private static File resolveConfigFilePath() {
        File result = null;
        final String specificFile = JvmKeyValues.MONITORING_FILE.get();
        if (specificFile != null) {
            result = new File(specificFile);
        }
        else if ((specificFile == null) && (JvmKeyValues.JVM_HOME_PATH.get() != null)) {
            result = FilesUtils.buildFile(new File(JvmKeyValues.JVM_HOME_PATH.get()), "monitoring.xml");
        }
        else {
            Loggers.INIT.info("no monitoring configuration file define");
        }
        return result;
    }
    
    // =========================================================================
    // BUILDER
    // =========================================================================
    private static ConfigHandler<String, String> buildConfigHandler(final MonitoringConfig config) {
        final Map<String, String> result = new HashMap<>();
        
        //@formatter:off
        final PropertiesConfig properties = config.getProperties()==null?new PropertiesConfig():config.getProperties();
        properties.getProperties()
                  .stream()
                  .filter(item -> item.getKey() !=null)
                  .filter(item -> item.getValue() !=null)
                  .forEach(item-> result.put(item.getKey(), item.getValue()));
        //@formatter:on
        
        System.getProperties().forEach((key, value) -> result.put(String.valueOf(key), String.valueOf(value)));
        
        return new ConfigHandlerHashMap(result);
    }
    
}
