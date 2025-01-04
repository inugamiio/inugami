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

import com.thoughtworks.xstream.XStream;
import io.inugami.api.alertings.AlertingProviderModel;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.exceptions.services.MappingException;
import io.inugami.api.functionnals.FilterFunction;
import io.inugami.api.functionnals.ValidatorFunction;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.AlertingModel;
import io.inugami.api.models.events.Event;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.models.events.TargetConfig;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.Config;
import io.inugami.api.processors.ProcessorModel;
import io.inugami.commons.connectors.config.HttpDefaultConfig;
import io.inugami.commons.connectors.config.HttpHeaderField;
import io.inugami.commons.files.FilesUtils;
import io.inugami.configuration.exceptions.ConfigurationException;
import io.inugami.configuration.exceptions.NotEventConfigurationException;
import io.inugami.configuration.exceptions.NotPluginConfigurationException;
import io.inugami.configuration.models.EventConfig;
import io.inugami.configuration.models.HandlerConfig;
import io.inugami.configuration.models.ListenerModel;
import io.inugami.configuration.models.ProviderConfig;
import io.inugami.configuration.models.app.*;
import io.inugami.configuration.models.plugins.*;
import io.inugami.configuration.models.plugins.components.config.*;
import io.inugami.configuration.models.plugins.front.PluginFrontConfig;
import io.inugami.configuration.services.functions.ConfigLoaderTranstypeFunction;
import io.inugami.configuration.services.mapping.PluginFrontConfigMapping;
import io.inugami.configuration.services.resolver.ConfigurationResolverException;
import io.inugami.configuration.services.validators.ApplicationConfigValidator;
import io.inugami.configuration.services.validators.PluginConfigurationValidator;
import io.inugami.configuration.services.validators.PluginEventsConfigValidator;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    //@formatter:off
    private static final  Class<?>[] TYPES_MAIN = {
            List.class,
            ArrayList.class,
            String.class,
            Boolean.class,
            ApplicationConfig.class,
            SecurityConfiguration.class,
            Config.class,
            PropertyModel.class,
            UsersConfig.class,
            UserConfig.class,
            HttpDefaultConfig.class,
            HttpHeaderField.class,
            MatcherConfig.class,
            RolesMappeurConfig.class,
            RoleMappeurConfig.class,
            UserConfig.class
    };

    private static final  Class<?>[] TYPES = {
            List.class,
            ArrayList.class,
            String.class,
            Boolean.class,
            PluginConfiguration.class,
            Resource.class,
            ResourceCss.class,
            ResourceJavaScript.class,
            ResourcePage.class,
            PropertyModel.class,
            ClassBehavior.class,
            ListenerModel.class,
            ProcessorModel.class,
            ProviderConfig.class,
            PropertyModel.class,
            Gav.class,
            Dependency.class,
            Config.class,
            EventConfig.class,
            Event.class,
            SimpleEvent.class,
            RoleMappeurConfig.class,
            EventsFileModel.class,
            RolesMappeurConfig.class,
            MatcherConfig.class,
            UserConfig.class,
            AlertingProviderModel.class,
            HandlerConfig.class
    };


    private static final  Class<?>[] CLASSES_EVENT = {
            List.class,
            ArrayList.class,
            String.class,
            Boolean.class,
            EventConfig.class,
            Event.class,
            SimpleEvent.class,
            ClassBehavior.class,
            Gav.class,
            AlertingModel.class,
            PropertyModel.class,
            ProcessorModel.class,
            TargetConfig.class
    };


    private static final  Class<?>[] CLASSES_COMPONENTS = {
            List.class,
            ArrayList.class,
            String.class,
            Boolean.class,
            Gav.class,
            Components.class,
            ComponentModel.class,
            ComponentViewModel.class,
            ComponentDescription.class,
            ComponentDescriptionContentModel.class,
            ComponentFieldsModel.class,
            ComponentEvent.class,
            ComponentScreenshot.class
    };


    //@formatter:on    
    private static final XStream XSTREAM_MAIN = initXStream(TYPES_MAIN);

    private static final XStream XSTREAM = initXStream(TYPES);

    private static final XStream XSTREAM_EVENT = initXStream(CLASSES_EVENT);

    private static final XStream XSTREAM_COMPONENTS          = initXStream(CLASSES_COMPONENTS);
    public static final  String  LOAD_CONFIGURATION_FROM_URL = "load configuration from url : {}";


    // =========================================================================
    // INIT
    // =========================================================================
    private static XStream initXStream(final Class<?>... types) {
        final XStream result = new XStream();
        result.autodetectAnnotations(true);
        result.processAnnotations(types);
        XStream.setupDefaultSecurity(result);
        result.allowTypes(types);
        return result;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public Optional<PluginConfiguration> loadFromUrl(final URL url) throws TechnicalException {
        LOGGER.info(LOAD_CONFIGURATION_FROM_URL, url);
        Asserts.assertNotNull("can't load null url!", url);
        Optional<PluginConfiguration> result = Optional.empty();

        final PluginConfiguration config = (PluginConfiguration) XSTREAM.fromXML(url);
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
        //@formatter:off
        return processLoadFromFile(url.getPath(),
                                   () -> XSTREAM_MAIN.fromXML(url),
                                   obj -> obj instanceof ApplicationConfig,
                                   raw -> (ApplicationConfig) raw,
                                   config -> new ApplicationConfigValidator(config, url.toString()).validate());
        //@formatter:on
    }

    public Optional<ApplicationConfig> loadApplicationConfig(final File file) throws TechnicalException {
        //@formatter:off
        assertFileRead(file);
        return processLoadFromFile(file.getAbsolutePath(),
                                   () -> XSTREAM_MAIN.fromXML(file),
                                   obj ->obj instanceof ApplicationConfig,
                                   raw -> (ApplicationConfig) raw,
                                   config -> new ApplicationConfigValidator(config, file.getAbsolutePath()).validate());
        //@formatter:on
    }

    //@formatter:off
    private <T> Optional<T> processLoadFromFile(final String file,
                                                final Supplier<Object> reader,
                                                final FilterFunction<Object> filter,
                                                final ConfigLoaderTranstypeFunction<T> transtype,
                                                final ValidatorFunction<T, TechnicalException> validator) throws TechnicalException {
        //@formatter:on
        LOGGER.info(LOAD_CONFIGURATION_FROM_URL, file);
        T            result    = null;
        final Object xmlLoaded = reader.get();
        if (filter.accept(xmlLoaded)) {
            result = transtype.process(xmlLoaded);
            validator.validate(result);
        } else {
            throw new ConfigurationException("{0} isn't valid configuration file!", file);
        }
        return Optional.ofNullable(result);
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
        assertFileRead(file);
        LOGGER.info(LOAD_CONFIGURATION_FROM_URL, file.getAbsolutePath());

        final Object xmlLoaded = XSTREAM.fromXML(file);
        if (!(xmlLoaded instanceof PluginConfiguration)) {
            throw new NotPluginConfigurationException(file, xmlLoaded.getClass().getName());
        }
        final PluginConfiguration config = (PluginConfiguration) xmlLoaded;

        new PluginConfigurationValidator(config, file.getAbsolutePath()).validate();

        final Optional<PluginConfiguration> result = Optional.of(config);
        config.setConfigFile(file.getAbsolutePath());

        return result;
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
        assertFileRead(file);
        Asserts.assertNotNull("GAV definition is mandatory!", gav);

        final Object xmlLoaded = XSTREAM_EVENT.fromXML(file);
        return loadingEventFile(gav, file.getAbsolutePath(), file.getName(), xmlLoaded);
    }

    public Optional<EventConfig> loadEventConfigFromUrl(final URL url, final Gav gav,
                                                        final EventsFileModel eventFile) throws TechnicalException {
        Asserts.assertNotNull("URL definition is mandatory!", url);
        Asserts.assertNotNull("GAV definition is mandatory!", gav);
        Asserts.assertNotNull("Event file name is mandatory!", eventFile);
        final Object xmlLoaded = XSTREAM_EVENT.fromXML(url);
        return loadingEventFile(gav, url.toString(), eventFile.getName(), xmlLoaded);
    }

    private Optional<EventConfig> loadingEventFile(final Gav gav, final String file, final String fileNameFull,
                                                   final Object xmlLoaded) throws TechnicalException {

        final String fileName = fileNameFull.substring(0, fileNameFull.lastIndexOf('.'));

        if (!(xmlLoaded instanceof EventConfig)) {
            throw new NotEventConfigurationException(file, xmlLoaded.getClass().getName());
        }

        final EventConfig eventConfig = (EventConfig) xmlLoaded;
        final Gav eventGav = gav.toBuilder()
                                .qualifier(fileName)
                                .build();
        //@formatter:off
        final EventConfig eventConfigResult = new EventConfig(eventConfig, file, fileName, eventGav);
        //@formatter:on
        new PluginEventsConfigValidator(eventConfigResult, file).validate();
        new PluginConfigurationInitilizer(eventConfigResult).process();


        return Optional.of(eventConfigResult);
    }

    private PluginFrontConfig loadFrontConfig(final URL url) throws ConfigurationResolverException {

        PluginFrontConfig result = null;
        final URL         realUrl;
        try {
            realUrl = new URL(url.toString().replaceAll("[.]xml", ".json"));
        } catch (final MalformedURLException e) {
            throw new ConfigurationResolverException(e.getMessage(), e);
        }
        String content = null;
        try {
            content = readFromUrl(realUrl);
        } catch (final FileNotFoundException fileNotFoundErr) {
            // nothing to do
        } catch (final IOException e) {
            throw new ConfigurationResolverException(e.getMessage(), e);
        }

        if (content != null) {
            //@formatter:off
            try {
                result = new PluginFrontConfigMapping().unmarshalling(content);
            } catch (final MappingException error) {
                Loggers.CONFIG.error("error on unmarshalling file : {}  ({})", realUrl, error.getMessage());
                throw new ConfigurationResolverException("error on unmarshalling file : {0}  ({1})", realUrl, error.getMessage());
            }
            //@formatter:on
        }

        return result;
    }

    // =========================================================================
    // LOAD COMPOENTS CONFIGS
    // =========================================================================
    public Components loadComponentsConfiguration(final URL url) throws ConfigurationResolverException {
        Asserts.assertNotNull("components configuration file url is mandatory!", url);
        final Object rawData = XSTREAM_COMPONENTS.fromXML(url);
        if (!(rawData instanceof Components)) {
            throw new ConfigurationResolverException("can't read components configuration : {0}", url);
        }
        return (Components) rawData;
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private void assertFileRead(final File file) throws PluginConfigurationLoaderException {
        Asserts.assertNotNull("can't load null file!", file);
        if (!file.exists()) {
            throw new PluginConfigurationLoaderException("file {0} doesn't exists!", file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new PluginConfigurationLoaderException("can't read file {0} !", file.getAbsolutePath());
        }
    }

    private String readFromUrl(final URL realUrl) throws IOException {
        final StringBuilder result = new StringBuilder();
        InputStreamReader   reader = null;
        try {
            reader = new InputStreamReader(realUrl.openStream());
        } catch (final IOException e) {
            if (Loggers.IO.isDebugEnabled()) {
                Loggers.IO.warn(e.getMessage());
            }

            FilesUtils.close(reader);
            return null;
        }

        final BufferedReader in = new BufferedReader(reader);
        String               inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
        } catch (final IOException e) {
            Loggers.IO.error(e.getMessage(), e);
            throw e;
        } finally {
            FilesUtils.close(in);
            FilesUtils.close(reader);
        }

        return result.toString();
    }

    // =========================================================================
    // EXCEPTION
    // =========================================================================
    /* package */ class PluginConfigurationLoaderException extends TechnicalException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = -7628343990372670043L;

        public PluginConfigurationLoaderException(final String message, final Object... values) {
            super(message, values);
        }
    }

}
