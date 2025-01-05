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

import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.configuration.models.app.ApplicationConfig;
import io.inugami.framework.configuration.models.components.Components;
import io.inugami.framework.configuration.models.front.PluginFrontConfig;
import io.inugami.framework.configuration.models.plugins.EventsFileModel;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.services.functions.ConfigLoaderTranstypeFunction;
import io.inugami.framework.configuration.services.resolver.ConfigurationResolverException;
import io.inugami.framework.configuration.services.validators.PluginConfigurationValidator;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.functionnals.FilterFunction;
import io.inugami.framework.interfaces.functionnals.ValidatorFunction;
import io.inugami.framework.interfaces.models.maven.Gav;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * PluginConfigurationLoader
 *
 * @author patrick_guillerm
 * @since 26 déc. 2016
 */
@SuppressWarnings({"java:S2139", "java:S1874", "java:S1612"})
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


        final PluginConfiguration config = null;
        // TODO:REFACTOR (PluginConfiguration) XSTREAM.fromXML(url);
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

    public Optional<ApplicationConfig> loadApplicationConfig(final URL url) throws TechnicalException {
        //TODO: REFACTOR
        return Optional.empty();
    }

    public Optional<ApplicationConfig> loadApplicationConfig(final File file) throws TechnicalException {
        //TODO: REFACTOR
        return Optional.empty();
    }

    //@formatter:off
    private <T> Optional<T> processLoadFromFile(final String file,
                                                final Supplier<Object> reader,
                                                final FilterFunction<Object> filter,
                                                final ConfigLoaderTranstypeFunction<T> transtype,
                                                final ValidatorFunction<T, TechnicalException> validator) throws TechnicalException {
        //TODO: REFACTOR
        return Optional.empty();
    }

    /**
     * Allow to load XML plugin configuration file
     *
     * @param file the file to load
     * @return probable Plugin configuration
     * @throws NotPluginConfigurationException if file loaded isn't a plugin
     *                                         configuration file
     * @throws TechnicalException              if other exception is occurs
     */
    public Optional<PluginConfiguration> loadFromFile(final File file) throws TechnicalException {
        //TODO: REFACTOR
        return Optional.empty();
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
        //TODO: REFACTOR
        return Optional.empty();
    }

    public Optional<EventConfig> loadEventConfigFromUrl(final URL url, final Gav gav,
                                                        final EventsFileModel eventFile) throws TechnicalException {
        //TODO: REFACTOR
        return Optional.empty();
    }

    private Optional<EventConfig> loadingEventFile(final Gav gav, final String file, final String fileNameFull,
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
        //TODO: REFACTOR
        return null;
    }



}
