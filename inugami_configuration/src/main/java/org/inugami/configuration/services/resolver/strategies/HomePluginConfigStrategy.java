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
package org.inugami.configuration.services.resolver.strategies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.inugami.api.exceptions.TechnicalException;
import org.inugami.configuration.exceptions.NotPluginConfigurationException;
import org.inugami.configuration.models.EventConfig;
import org.inugami.configuration.models.plugins.EventsFileModel;
import org.inugami.configuration.models.plugins.PluginConfiguration;
import org.inugami.configuration.services.PluginConfigurationLoader;
import org.inugami.configuration.services.resolver.ConfigurationResolverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassLoaderPluginConfigStrategy
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
public class HomePluginConfigStrategy implements PluginConfigResolverStrategy {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger             LOGGER       = LoggerFactory.getLogger(HomePluginConfigStrategy.class);
    
    private final static Pattern            FILE_PATTERN = Pattern.compile("^plugin_.*.xml$");
    
    public final File                       homePath;
    
    private final PluginConfigurationLoader loader;
    
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    
    public HomePluginConfigStrategy(final PluginConfigurationLoader pluginLoader, final File homePath) {
        this.loader = pluginLoader;
        this.homePath = homePath;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Optional<List<PluginConfiguration>> resolve() throws ConfigurationResolverException {
        Optional<List<PluginConfiguration>> result = Optional.empty();
        final List<PluginConfiguration> config = process();
        if (config != null) {
            result = Optional.of(config);
        }
        return result;
    }
    
    @Override
    public Optional<EventConfig> resolveEventFile(final PluginConfiguration config,
                                                  final EventsFileModel eventFile) throws ConfigurationResolverException {
        Optional<EventConfig> result = Optional.empty();
        
        final Optional<File> eventConfigFile = resolveEventConfigFile(eventFile);
        if (eventConfigFile.isPresent()) {
            try {
                result = loader.loadEventConfigFromFile(config.getGav(), eventConfigFile.get());
            }
            catch (final TechnicalException e) {
                throw new ConfigurationResolverException(e.getMessage(), e);
            }
        }
        
        return result;
    }
    
    private Optional<File> resolveEventConfigFile(final EventsFileModel eventFile) {
        Optional<File> result = Optional.empty();
        final String[] files = homePath.list((dir, name) -> name.equals(eventFile.getName()));
        if ((files != null) && (files.length > 0)) {
            result = Optional.of(new File(homePath.getAbsolutePath() + File.separator + files[0]));
        }
        
        return result;
    }
    
    // =========================================================================
    // PRIVATE API
    // =========================================================================
    private List<PluginConfiguration> process() throws ConfigurationResolverException {
        List<PluginConfiguration> result = null;
        final List<File> files = resolveFiles();
        if (files != null) {
            result = new ArrayList<>();
            for (final File file : files) {
                Optional<PluginConfiguration> config = Optional.empty();
                try {
                    config = loader.loadFromFile(file);
                }
                catch (final NotPluginConfigurationException except) {
                    LOGGER.error("found not plugin configuration file : type={} path:{}", except.getClassName(),
                                 except.getFile().getAbsolutePath());
                }
                catch (final TechnicalException e) {
                    throw new ConfigurationResolverException(e.getMessage(), e);
                }
                if (config.isPresent()) {
                    result.add(config.get());
                }
            }
        }
        
        return result;
    }
    
    protected List<File> resolveFiles() throws ConfigurationResolverException {
        List<File> result = null;
        final String[] files = homePath.list((dir, name) -> FILE_PATTERN.matcher(name).matches());
        if (files != null) {
            result = new ArrayList<>();
            for (final String name : files) {
                result.add(new File(homePath.getAbsolutePath() + "/" + name));
            }
        }
        return result;
    }
    
}
