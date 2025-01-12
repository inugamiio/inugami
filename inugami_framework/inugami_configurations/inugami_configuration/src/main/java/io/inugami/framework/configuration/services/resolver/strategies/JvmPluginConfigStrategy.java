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
package io.inugami.framework.configuration.services.resolver.strategies;

import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.configuration.models.plugins.EventsFileModel;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.services.PluginConfigurationLoader;
import io.inugami.framework.configuration.services.resolver.ConfigurationResolverException;
import io.inugami.framework.interfaces.configurtation.JvmKeyValues;
import io.inugami.framework.interfaces.exceptions.TechnicalException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * ClassLoaderPluginConfigStrategy
 *
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
public class JvmPluginConfigStrategy implements PluginConfigResolverStrategy {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Pattern PATTERN = Pattern.compile("^" + JvmKeyValues.PLUGIN_PREFIX.getKey() + ".*");

    private final PluginConfigurationLoader loader;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    public JvmPluginConfigStrategy(final PluginConfigurationLoader pluginLoader) {
        this.loader = pluginLoader;
    }

    @Override
    public Optional<EventConfig> resolveEventFile(final PluginConfiguration config, final EventsFileModel eventFile) {
        // mustn't load event file from JVM properties
        return Optional.empty();
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

    // =========================================================================
    // PRIVATE API
    // =========================================================================
    protected List<File> resolveFiles() {
        final List<File> result = new ArrayList<>();

        //@formatter:off
        System.getProperties()
              .keySet()
              .stream()
              .filter(key -> PATTERN.matcher(String.valueOf(key)).matches())
              .forEach(key -> result.add(new File(System.getProperties().getProperty(String.valueOf(key)))));
        //@formatter:on
        return result.isEmpty() ? null : result;
    }

    private List<PluginConfiguration> process() throws ConfigurationResolverException {
        List<PluginConfiguration> result = null;
        final List<File>          files  = resolveFiles();
        if (files != null) {
            result = new ArrayList<>();
            for (final File file : files) {
                Optional<PluginConfiguration> config = Optional.empty();
                try {
                    config = loader.loadFromFile(file);
                } catch (final TechnicalException e) {
                    throw new ConfigurationResolverException(e.getMessage(), e);
                }
                if (config.isPresent()) {
                    result.add(config.get());
                }
            }
        }
        return result;
    }

}
