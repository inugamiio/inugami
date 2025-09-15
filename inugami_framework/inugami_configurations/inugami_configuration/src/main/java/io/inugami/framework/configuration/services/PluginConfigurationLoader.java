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
package io.inugami.framework.configuration.services;

import io.inugami.framework.api.marshalling.YamlMarshaller;
import io.inugami.framework.commons.files.FilesUtils;
import io.inugami.framework.configuration.exceptions.FatalConfigurationException;
import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.configuration.models.app.ApplicationConfig;
import io.inugami.framework.configuration.models.components.Components;
import io.inugami.framework.configuration.models.front.PluginFrontConfig;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.services.resolver.ConfigurationResolverException;
import io.inugami.framework.configuration.services.validators.PluginConfigurationValidator;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.tools.StringTools;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * PluginConfigurationLoader
 *
 * @author patrick_guillerm
 * @since 26 déc. 2016
 */
@SuppressWarnings({"java:S2139", "java:S1874", "java:S1612"})
@Slf4j
@NoArgsConstructor
public class PluginConfigurationLoader {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginConfigurationLoader.class);

    public static final String LOAD_CONFIGURATION_FROM_URL = "load configuration from url : {}";


    // =========================================================================
    // INIT
    // =========================================================================

    // =========================================================================
    // METHODS
    // =========================================================================
    public Optional<PluginConfiguration> loadFromUrl(final URL url) throws TechnicalException {
        LOGGER.info(LOAD_CONFIGURATION_FROM_URL, url);
        Asserts.assertNotNull("can't load null url!", url);
        Optional<PluginConfiguration> result = Optional.empty();


        final PluginConfiguration config = loadPluginConfiguration(url);
        if (config != null) {
            new PluginConfigurationValidator(config, url.toString()).validate();
            result = Optional.of(config);
            config.setConfigFile(url.toString());
            final PluginFrontConfig frontConfig = loadFrontConfig(url);
            if (frontConfig != null) {
                config.setFrontConfig(frontConfig);
            }
        }

        return result;
    }

    private PluginConfiguration loadPluginConfiguration(final URL url) {
        try {
            final String content = FilesUtils.readContent(url);
            return YamlMarshaller.getInstance().convertFromYaml(content, PluginConfiguration.class);
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
            return null;
        }
    }

    public Optional<ApplicationConfig> loadApplicationConfig(final URL url) {
        try {
            final String content = FilesUtils.readContent(url);
            final ApplicationConfig result = YamlMarshaller.getInstance()
                                                           .convertFromYaml(content, ApplicationConfig.class);
            return Optional.ofNullable(result);
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
            return Optional.empty();
        }
    }

    public Optional<ApplicationConfig> loadApplicationConfig(final File file) {
        final String            content = readFile(file);
        final ApplicationConfig result  = YamlMarshaller.getInstance()
                                                        .convertFromYaml(content, ApplicationConfig.class);
        return Optional.ofNullable(result);
    }


    /**
     * Allow to load XML plugin configuration file
     *
     * @param file the file to load
     * @return probable Plugin configuration
     * @throws TechnicalException              if other exception is occurs
     */
    public Optional<PluginConfiguration> loadFromFile(final File file) throws TechnicalException {
        if (file == null || !file.isFile() || !file.canRead()) {
            return Optional.empty();
        }
        final String              content = readFile(file);
        final PluginConfiguration result  = YamlMarshaller.getInstance()
                                                          .convertFromYaml(content, PluginConfiguration.class);
        return Optional.ofNullable(result);
    }

    /**
     * Allow to load event configuration from File.
     *
     * @param gav  the gav
     * @param file configuration file
     * @return probable event configuration
     * @throws TechnicalException if exception is occurs
     */
    public Optional<EventConfig> loadEventConfigFromFile(final Gav gav, final File file) throws TechnicalException {
        final String      content = readFile(file);
        final EventConfig result  = YamlMarshaller.getInstance().convertFromYaml(content, EventConfig.class);

        result.setGav(gav);
        return Optional.ofNullable(result);
    }

    public Optional<EventConfig> loadEventConfigFromUrl(final URL url,
                                                        final Gav gav,
                                                        final String eventFile) throws TechnicalException {
        //TODO: REFACTOR
        return Optional.empty();
    }

    private Optional<EventConfig> loadingEventFile(final Gav gav,
                                                   final String file,
                                                   final String fileNameFull,
                                                   final Object xmlLoaded) throws TechnicalException {

        //TODO: REFACTOR
        return Optional.empty();
    }

    private PluginFrontConfig loadFrontConfig(final URL url) throws ConfigurationResolverException {

        //TODO: REFACTOR
        return null;
    }

    // =========================================================================
    // LOAD COMPOENTS CONFIGS
    // =========================================================================
    public Components loadComponentsConfiguration(final URL url) throws ConfigurationResolverException {
        final String content;
        try {
            log.debug("loading Components : {}", url);
            content = FilesUtils.readContent(url);
            return YamlMarshaller.getInstance().convertFromYaml(content, Components.class);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }


    private String readFile(final File file) {
        if (file == null) {
            throw buildException("can't read file with null path");
        }
        if (!file.exists()) {
            throw buildException("file not found : {0}", file.getAbsolutePath());
        }
        if (!file.isFile() || !file.canRead()) {
            throw buildException("can't read file : {0}", file.getAbsolutePath());
        }
        try {
            return FilesUtils.readContent(file);
        } catch (Throwable e) {
            throw new FatalConfigurationException(e.getMessage(), e);
        }

    }

    private FatalConfigurationException buildException(final String message, final Object... values) {
        final String errorMessage = StringTools.format(message, values);
        log.error(errorMessage);
        return new FatalConfigurationException(errorMessage);
    }
}
