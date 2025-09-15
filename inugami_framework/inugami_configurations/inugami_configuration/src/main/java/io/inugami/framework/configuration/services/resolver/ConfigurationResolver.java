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
package io.inugami.framework.configuration.services.resolver;

import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.configuration.models.app.ApplicationConfig;
import io.inugami.framework.configuration.models.components.Components;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.services.PluginConfigurationLoader;
import io.inugami.framework.configuration.services.resolver.strategies.ClassLoaderPluginConfigStrategy;
import io.inugami.framework.configuration.services.resolver.strategies.HomePluginConfigStrategy;
import io.inugami.framework.configuration.services.resolver.strategies.JvmPluginConfigStrategy;
import io.inugami.framework.configuration.services.resolver.strategies.PluginConfigResolverStrategy;
import io.inugami.framework.interfaces.configurtation.JvmKeyValues;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.FatalException;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * ConfigurationResolver
 *
 * @author patrick_guillerm
 * @since 26 déc. 2016
 */
@SuppressWarnings({"java:S3655", "java:S2139"})
@Slf4j
public class ConfigurationResolver {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final File HOME_PATH = initHomePath();

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationResolver.class);

    private static final PluginConfigurationLoader PLUGIN_LOADER = new PluginConfigurationLoader();

    private static final ManifestResolver MANIFEST_RESOLVER = new ManifestResolver();

    private static final ClassLoaderPluginConfigStrategy classLoaderStrategy = new ClassLoaderPluginConfigStrategy(PLUGIN_LOADER);

    //@formatter:off
    private static final PluginConfigResolverStrategy[] pluginStrategies = {
            new JvmPluginConfigStrategy(PLUGIN_LOADER),
            new HomePluginConfigStrategy(PLUGIN_LOADER, HOME_PATH),
            classLoaderStrategy
    };
    //@formatter:on

    // =========================================================================
    // INITIALIZE
    // =========================================================================
    private static File initHomePath() {
        File         result         = null;
        final String jvmValueDefine = System.getProperty(JvmKeyValues.JVM_HOME_PATH.getKey());
        if (jvmValueDefine == null) {
            final String userHome = System.getProperty("user.home");
            Asserts.assertNotNull("Can't get user home information!", userHome);
            result = new File(userHome + File.separator + "." + JvmKeyValues.DEFAULT_APPLICATION_NAME);
        } else {
            result = new File(System.getProperty(JvmKeyValues.JVM_HOME_PATH.getKey()));
        }

        Asserts.assertNotNull(JvmKeyValues.DEFAULT_APPLICATION_NAME + " HOME isn't define!", result);
        if (!result.exists()) {
            result.mkdirs();
        }
        if (!result.canRead()) {
            throw new FatalException("can't read in user home : {0}", result.getAbsolutePath());
        }
        if (!result.canWrite()) {
            throw new FatalException("can't write in user home : {0}", result.getAbsolutePath());
        }

        return result;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public ApplicationConfig loadApplicationConfig(final URL url) {
        final Optional<ApplicationConfig> result = PLUGIN_LOADER.loadApplicationConfig(url);

        if (!result.isPresent()) {
            throw new FatalException("Can't load application configuration");
        }
        return result.get();
    }

    public ApplicationConfig loadApplicationConfig(final File file) {
        final Optional<ApplicationConfig> result = PLUGIN_LOADER.loadApplicationConfig(file);
        if (!result.isPresent()) {
            throw new FatalException("Can't load application configuration : {0}", file.getAbsolutePath());
        }
        return result.get();
    }

    public Optional<List<PluginConfiguration>> resolvePluginsConfigurations() throws TechnicalException {
        Optional<List<PluginConfiguration>> result = Optional.empty();

        final List<PluginConfiguration> configs = processResolvePluginsConfigurations();
        if (configs != null) {
            result = Optional.of(resolveDependencies(configs));
        }

        return result;
    }

    public Optional<List<EventConfig>> resolvePluginEventConfig(final PluginConfiguration config) throws TechnicalException {
        Optional<List<EventConfig>> result = Optional.empty();

        final List<EventConfig> eventsConfigs = processResolvePluginEventConfig(config);
        if (eventsConfigs != null) {
            result = Optional.of(eventsConfigs);
        }

        return result;
    }

    // =========================================================================
    // PROCESS
    // =========================================================================
    protected List<PluginConfiguration> processResolvePluginsConfigurations() throws ConfigurationResolverException {
        final List<ConfigurationResolverException> errors            = new ArrayList<>();
        final Map<Gav, PluginConfiguration>        configs           = new HashMap<>();
        final List<Components>                     componentsConfigs = classLoaderStrategy.loadComponents();

        for (final PluginConfigResolverStrategy strategy : pluginStrategies) {
            Optional<List<PluginConfiguration>> optConfig = Optional.empty();

            log.info("resolve from strategy :{}", strategy.getClass().getSimpleName());
            try {
                optConfig = strategy.resolve();
            } catch (final ConfigurationResolverException e) {
                errors.add(e);
            }

            if (optConfig.isPresent()) {
                final List<PluginConfiguration> plugins = optConfig.get();
                for (final PluginConfiguration plugin : plugins) {
                    addPluginToConfig(configs, plugin);
                    addComponents(plugin, componentsConfigs);
                }
            }
        }

        assertsNoErrors(errors);
        return convertMapToList(configs);
    }

    protected List<EventConfig> processResolvePluginEventConfig(final PluginConfiguration config) throws TechnicalException {
        Asserts.assertNotNull("plugin configuration mustn't be null", config);
        List<EventConfig> result = null;

        if ((config.getEventsFiles() != null) && !config.getEventsFiles().isEmpty()) {
            result = new ArrayList<>();
            for (final String eventFile : config.getEventsFiles()) {
                final EventConfig eventConfig = resolveEventFile(config, eventFile);
                LOGGER.debug("found event file : {}", eventConfig.getConfigFile());
                result.add(eventConfig);
            }

        }

        return result;
    }

    private EventConfig resolveEventFile(final PluginConfiguration config,
                                         final String eventFile) throws TechnicalException {
        Asserts.assertNotNull("plugin configuration mustn't be null!", config);
        Asserts.assertNotNull("event file mustn't be null!", eventFile);
        EventConfig result = null;
        for (final PluginConfigResolverStrategy strategy : pluginStrategies) {
            final Optional<EventConfig> eventConfigOpt = strategy.resolveEventFile(config, eventFile);
            if (eventConfigOpt.isPresent()) {
                result = eventConfigOpt.get();
                break;
            }
        }

        if (result == null) {
            // @formatter:off
            throw new ConfigurationResolverDependecyUnresolvedException(
                    "can't found event file \"{0}\" define in  plugin {1}", eventFile,
                    config.getGav().getHash());
            // @formatter:on
        }
        return result;
    }

    // =========================================================================
    // ADD configs
    // =========================================================================
    private void addPluginToConfig(final Map<Gav, PluginConfiguration> configs, final PluginConfiguration plugin) {
        if (configs.containsKey(plugin.getGav())) {
            if (plugin.getFrontConfig() != null) {
                final PluginConfiguration resolvedPlugin = configs.get(plugin.getGav());
                resolvedPlugin.setFrontConfig(plugin.getFrontConfig());
            }
        } else {
            Loggers.PLUGINS.info("plugin found : {}", plugin.getGav().getHash());
            configs.put(plugin.getGav(), plugin);
        }
    }

    private void addComponents(final PluginConfiguration plugin, final List<Components> componentsConfigs) {
        for (final Components compo : componentsConfigs) {
            if (compo.isSameGav(plugin.getGav())) {
                plugin.setComponents(compo.getComponents());
                break;
            }
        }
    }

    // =========================================================================
    // RESOLVE DEPENDENCIES
    // =========================================================================
    @SuppressWarnings({"java:S3776"})
    private List<PluginConfiguration> resolveDependencies(final List<PluginConfiguration> list) throws ConfigurationResolverDependecyUnresolvedException {
        LOGGER.info("check plugins dependencies...");
        final List<PluginConfiguration> result           = new ArrayList<>();
        final List<PluginConfiguration> withDependencies = new ArrayList<>();

        list.forEach(config -> {
            if ((config.getDependencies() == null) || config.getDependencies().isEmpty()) {
                result.add(config);
            } else {
                withDependencies.add(config);
            }
        });

        LOGGER.info("number plugins with dependencies : {}", withDependencies.size());
        while (!withDependencies.isEmpty()) {
            for (int index = 0; index < withDependencies.size(); index++) {
                final PluginConfiguration item = withDependencies.get(index);

                boolean allDependInResolved = true;
                for (final Gav depend : item.getDependencies()) {
                    allDependInResolved = foundDependency(result, depend);
                    if (matchDependency(item, depend)) {
                        throwUnresolved("extension can't depend him self ! ({0})", item);
                    }
                    if (!allDependInResolved && !foundDependency(withDependencies, depend)) {
                        throwUnresolved("can't resolve dependency :({0})", item);
                    }
                }

                if (allDependInResolved) {
                    result.add(item);
                    withDependencies.remove(item);
                }
            }
        }

        return result;
    }

    private boolean foundDependency(final List<PluginConfiguration> values, final Gav item) {
        boolean foundInResolved = false;
        for (final PluginConfiguration resolved : values) {
            if (matchDependency(resolved, item)) {
                foundInResolved = true;
                break;
            }
        }
        return foundInResolved;
    }

    private boolean matchDependency(final PluginConfiguration item, final Gav depend) {
        final boolean match = item.getGav().equalsWithoutVersion(depend);
        if (match && !item.getGav().getVersion().equals(depend.getVersion())) {
            Loggers.XLLOG.warn("plugin dependency error : {} -> {}", item.getGav(), depend);
        }
        return match;
    }

    // =========================================================================
    // MANIFEST RESOLVER
    // =========================================================================
    public ManifestInfo resolvePluginManifest(final PluginConfiguration config) {
        return MANIFEST_RESOLVER.resolvePluginManifest(config);
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private List<PluginConfiguration> convertMapToList(final Map<Gav, PluginConfiguration> configs) {
        Asserts.assertNotNull(configs);
        return configs.values().stream().collect(toList());
    }

    /**
     * Allow to asserts that no errors occurs
     *
     * @param errors the errors
     * @throws ConfigurationResolverException the configuration resolver
     *                                        exception
     */
    private void assertsNoErrors(final List<ConfigurationResolverException> errors) throws ConfigurationResolverException {
        if (!errors.isEmpty()) {
            final StringBuilder msg = new StringBuilder("some errors occurs in resolving plugins configurations:\n");
            errors.forEach(e -> appendMessageAndLog(msg, e));
            throw new ConfigurationResolverException(msg.toString());
        }
    }

    private void appendMessageAndLog(final StringBuilder msg, final ConfigurationResolverException e) {
        msg.append(e.getMessage()).append('\n');
        LOGGER.error(e.getMessage(), e);
    }

    private void throwUnresolved(final String message,
                                 final PluginConfiguration dependency) throws ConfigurationResolverDependecyUnresolvedException {
        throw new ConfigurationResolverDependecyUnresolvedException(message, dependency.getGav());
    }

    @SuppressWarnings({"java:S110"})
    private class ConfigurationResolverDependecyUnresolvedException extends FatalException {
        private static final long serialVersionUID = 4732065150403156551L;

        public ConfigurationResolverDependecyUnresolvedException(final String message, final Object... values) {
            super(message, values);
        }
    }

}
