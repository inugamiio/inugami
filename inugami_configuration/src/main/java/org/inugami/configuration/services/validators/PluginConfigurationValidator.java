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
package org.inugami.configuration.services.validators;

import static org.inugami.configuration.services.validators.ValidatorProcessor.condition;
import static org.inugami.configuration.services.validators.ValidatorProcessor.isEmpty;

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.alertings.AlertingProviderModel;
import org.inugami.api.exceptions.MessagesFormatter;
import org.inugami.api.processors.Config;
import org.inugami.api.processors.ProcessorModel;
import org.inugami.configuration.exceptions.ConfigurationException;
import org.inugami.configuration.models.ListenerModel;
import org.inugami.configuration.models.ProviderConfig;
import org.inugami.configuration.models.plugins.Dependency;
import org.inugami.configuration.models.plugins.EventsFileModel;
import org.inugami.configuration.models.plugins.PluginConfiguration;
import org.inugami.configuration.models.plugins.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PluginConfigurationValidator
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
public class PluginConfigurationValidator implements Validator {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger       LOGGER    = LoggerFactory.getLogger(PluginConfigurationValidator.class);
    
    private final PluginConfiguration config;
    
    private final ValidatorProcessor  validator = new ValidatorProcessor();
    
    private final String              path;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public PluginConfigurationValidator(final PluginConfiguration config, final String path) {
        this.config = config;
        this.path = path;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void validate() throws ConfigurationException {
        validatePluginConfig(config, path);
    }
    
    private void validatePluginConfig(final PluginConfiguration config,
                                      final String filePath) throws ConfigurationException {
        
        final List<Condition> conditions = new ArrayList<>();
        //@formatter:off
        conditions.add(condition("No GAV present in configuration file"      , config.getGav() == null));
        conditions.add(condition("No groupId define in configuration file"   , isEmpty(config.getGav().getGroupId())));
        conditions.add(condition("No artifactId define in configuration file", isEmpty(config.getGav().getArtifactId())));
        conditions.add(condition("No version define in configuration file"   , isEmpty(config.getGav().getVersion())));
        //@formatter:on
        
        conditions.addAll(validateResources(config));
        conditions.addAll(validateProviders(config));
        conditions.addAll(validateProcessors(config));
        conditions.addAll(validateListeners(config));
        conditions.addAll(validateEventsFiles(config));
        conditions.addAll(validateDependencies(config));
        conditions.addAll(validateAlertings(config));
        
        //@formatter:off
        validator.validate( filePath,
                            conditions,
                            (condition, path) -> MessagesFormatter.format(condition.getMessage()+ " {0}", filePath));
        //@formatter:on
    }
    
    // =========================================================================
    // VALIDATORS
    // =========================================================================
    private List<Condition> validateResources(final PluginConfiguration config) {
        return validate(config.getResources(), (data, result) -> {
            for (final Resource item : data) {
                result.add(condition("resource path is mandatory", isEmpty(item.getPath())));
                result.add(condition("resource name is mandatory", isEmpty(item.getName())));
            }
        });
    }
    
    private List<Condition> validateProviders(final PluginConfiguration config) {
        return validate(config.getProviders(), (data, result) -> {
            for (final ProviderConfig item : data) {
                result.add(condition("provider type is mandatory", isEmpty(item.getClassName())));
                result.add(condition("provider name is mandatory", isEmpty(item.getName())));
                LOGGER.debug("provider : {} - {}", item.getName(), item.getClassName());
                result.addAll(validateConfigTags(item.getConfigs()));
            }
        });
    }
    
    private List<Condition> validateConfigTags(final List<Config> configs) {
        return validate(configs, (data, result) -> {
            for (final Config config : data) {
                result.add(condition("config key is mandatory", config.getKey() == null));
            }
        });
    }
    
    private List<Condition> validateProcessors(final PluginConfiguration config) {
        return validate(config.getProcessors(), (data, result) -> {
            for (final ProcessorModel item : data) {
                result.add(condition("processor className is mandatory", isEmpty(item.getClassName())));
                result.add(condition("processor name is mandatory", isEmpty(item.getName())));
                LOGGER.debug("processor : {} - {}", item.getName(), item.getClassName());
                result.addAll(validateConfigTags(item.getConfigs()));
            }
        });
    }
    
    private List<Condition> validateListeners(final PluginConfiguration config) {
        return validate(config.getListeners(), (data, result) -> {
            for (final ListenerModel item : data) {
                result.add(condition("listener className is mandatory", isEmpty(item.getClassName())));
                result.add(condition("listener name is mandatory", isEmpty(item.getName())));
                LOGGER.debug("listener : {} - {}", item.getName(), item.getClassName());
                result.addAll(validateConfigTags(item.getConfigs()));
            }
        });
    }
    
    private List<Condition> validateEventsFiles(final PluginConfiguration config) {
        return validate(config.getEventsFiles(), (data, result) -> {
            for (final EventsFileModel item : data) {
                result.add(condition("events-file name is mandatory", isEmpty(item.getName())));
            }
        });
    }
    
    private List<Condition> validateDependencies(final PluginConfiguration config) {
        return validate(config.getDependencies(), (data, result) -> {
            for (final Dependency item : data) {
                result.add(condition("dependency groupId is mandatory", isEmpty(item.getGroupId())));
                result.add(condition("dependency artifactId is mandatory", isEmpty(item.getArtifactId())));
                result.add(condition("dependency version is mandatory", isEmpty(item.getVersion())));
            }
        });
    }
    
    private List<Condition> validateAlertings(final PluginConfiguration config) {
        return validate(config.getAlertings(), (data, result) -> {
            for (final AlertingProviderModel item : data) {
                result.add(condition("Alerting name is mandatory", isEmpty(item.getName())));
                result.add(condition("Alerting name is mandatory", isEmpty(item.getClassName())));
                result.addAll(validateConfigTags(item.getConfigs()));
            }
        });
    }
    
}
