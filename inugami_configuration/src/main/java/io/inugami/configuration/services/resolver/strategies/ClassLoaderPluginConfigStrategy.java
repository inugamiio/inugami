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
package io.inugami.configuration.services.resolver.strategies;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.loggers.Loggers;
import io.inugami.configuration.models.EventConfig;
import io.inugami.configuration.models.plugins.EventsFileModel;
import io.inugami.configuration.models.plugins.PluginConfiguration;
import io.inugami.configuration.models.plugins.components.config.Components;
import io.inugami.configuration.services.PluginConfigurationLoader;
import io.inugami.configuration.services.resolver.ConfigurationResolverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * ClassLoaderPluginConfigStrategy
 *
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
public class ClassLoaderPluginConfigStrategy implements PluginConfigResolverStrategy {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String CONFIG_FILE_NAME = "META-INF/plugin-configuration.xml";

    private final static String COMPONENTS_CONFIG_FILE_NAME = "META-INF/plugin-components.xml";

    private final static String CONFIG_FILE_NAME_JSON = "META-INF/plugin-configuration.json";

    private final static Logger LOGGER = LoggerFactory.getLogger(ClassLoaderPluginConfigStrategy.class);

    private final int MAX_CLASSLOADER_PARENT = 4;

    private final PluginConfigurationLoader loader;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public ClassLoaderPluginConfigStrategy(final PluginConfigurationLoader pluginLoader) {
        this.loader = pluginLoader;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Optional<List<PluginConfiguration>> resolve() throws ConfigurationResolverException {
        Optional<List<PluginConfiguration>> result = Optional.empty();
        final List<PluginConfiguration>     config = process();
        if (config != null) {
            result = Optional.of(config);
        }
        return result;
    }

    @Override
    public Optional<EventConfig> resolveEventFile(final PluginConfiguration config,
                                                  final EventsFileModel eventFile) throws TechnicalException {

        Optional<EventConfig> result = Optional.empty();

        final EventConfig resultConfig = processResolveEventFile(config, eventFile);
        if (resultConfig != null) {
            result = Optional.of(resultConfig);
        }
        return result;
    }

    public List<Components> loadComponents() {
        final List<Components> result = new ArrayList<>();
        List<URL>              urls   = null;
        try {
            urls = processResolveFile(COMPONENTS_CONFIG_FILE_NAME);
        } catch (final ConfigurationResolverException e) {
            if (Loggers.DEBUG.isDebugEnabled()) {
                Loggers.DEBUG.warn(e.getMessage(), e);
            } else {
                Loggers.DEBUG.warn(e.getMessage());
            }
        }

        if (urls != null) {
            for (final URL url : urls) {
                try {
                    final Components config = loader.loadComponentsConfiguration(url);
                    result.add(config);
                } catch (final ConfigurationResolverException e) {
                    Loggers.DEBUG.error("unable to load component configuration : {} - {}", url, e.getMessage());
                }
            }
        }
        return result;
    }

    // =========================================================================
    // PRIVATE API
    // =========================================================================
    private List<PluginConfiguration> process() throws ConfigurationResolverException {
        List<PluginConfiguration> result = null;
        final List<URL>           urls   = resolveFiles();
        if (urls != null) {
            result = load(urls);
        }
        return result;
    }

    private EventConfig processResolveEventFile(final PluginConfiguration pluginConfig,
                                                final EventsFileModel eventFile) throws TechnicalException {
        EventConfig           result    = null;
        final List<URL>       files     = processResolveFile("META-INF/" + eventFile.getName());
        Optional<EventConfig> configOpt = Optional.empty();
        if ((files != null) && !files.isEmpty()) {
            final URL url = files.get(0);
            configOpt = loader.loadEventConfigFromUrl(url, pluginConfig.getGav(), eventFile);
        }

        if (configOpt.isPresent()) {
            result = configOpt.get();
        }
        return result;
    }
    // =========================================================================
    // RESOLVE FILES
    // =========================================================================

    protected List<URL> resolveFiles() throws ConfigurationResolverException {
        return processResolveFile(CONFIG_FILE_NAME);
    }

    private List<URL> processResolveFile(final String fileName) throws ConfigurationResolverException {
        final List<URL> result      = new ArrayList<>();
        int             step        = 1;
        ClassLoader     classLoader = this.getClass().getClassLoader();

        //@formatter:off
        while ((step < MAX_CLASSLOADER_PARENT)
                && (classLoader != null)
                && !classLoader.equals(System.class.getClassLoader())) {
            //@formatter:on

            final List<URL> urls = resolveUrls(classLoader, fileName);
            if (urls != null) {
                result.addAll(urls);
                urls.forEach(url -> LOGGER.debug("found configuration : {}", url));
            }

            classLoader = classLoader.getParent();
            step++;
        }

        return result;
    }

    private List<URL> resolveUrls(final ClassLoader classLoader,
                                  final String fileName) throws ConfigurationResolverException {
        Asserts.assertNotNull(classLoader);
        List<URL>        result = null;
        Enumeration<URL> urls   = null;
        try {
            urls = classLoader.getResources(fileName);
        } catch (final IOException e) {
            throw new ConfigurationResolverException(e.getMessage(), e);
        }

        if (urls != null) {
            result = new ArrayList<>();
            while (urls.hasMoreElements()) {
                result.add(urls.nextElement());
            }
        }
        return result;
    }

    // =========================================================================
    // LOAD CONFIGURATION
    // =========================================================================
    protected List<PluginConfiguration> load(final List<URL> urls) throws ConfigurationResolverException {
        final List<PluginConfiguration> result = new ArrayList<>();

        for (final URL url : urls) {
            Optional<PluginConfiguration> config = Optional.empty();
            try {
                config = loader.loadFromUrl(url);

            } catch (final TechnicalException e) {
                throw new ConfigurationResolverException(e.getMessage(), e);
            }
            if (config.isPresent()) {
                result.add(config.get());
            }
        }

        return result;
    }

}
