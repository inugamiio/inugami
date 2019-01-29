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
package org.inugami.core.context;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.spi.CDI;

import org.inugami.api.alertings.AlertingProvider;
import org.inugami.api.alertings.AlertingProviderModel;
import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.FatalException;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.handlers.Handler;
import org.inugami.api.listeners.EngineListener;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.plugins.ManifestInfo;
import org.inugami.api.processors.ClassBehavior;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.processors.Processor;
import org.inugami.api.processors.ProcessorModel;
import org.inugami.api.providers.Provider;
import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.security.EncryptionUtils;
import org.inugami.configuration.models.HandlerConfig;
import org.inugami.configuration.models.ListenerModel;
import org.inugami.configuration.models.ProviderConfig;
import org.inugami.configuration.models.plugins.PluginConfiguration;
import org.inugami.configuration.models.plugins.PropertyModel;
import org.inugami.core.context.loading.AlertsResourcesLoader;
import org.inugami.core.context.loading.AlertsResourcesLoaderFileSystem;
import org.inugami.core.context.loading.AlertsResourcesLoaderZip;
import org.inugami.core.context.loading.PropertiesResourcesLoader;
import org.inugami.core.context.loading.PropertiesResourcesLoaderZip;

/**
 * Loader.
 *
 * @author patrick_guillerm
 * @since 3 janv. 2017
 */
public class Loader {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The encryption utils. */
    private final EncryptionUtils           encryptionUtils                 = new EncryptionUtils();
    
    private final AlertsResourcesLoader     alertsResourcesLoaderZip        = new AlertsResourcesLoaderZip();
    
    private final AlertsResourcesLoader     alertsResourcesLoaderFileSystem = new AlertsResourcesLoaderFileSystem();
    
    private final PropertiesResourcesLoader propertiesResourcesLoaderZip    = new PropertiesResourcesLoaderZip();
    
    /** The cache. */
    private final Map<String, Object>       CACHE                           = new HashMap<>();
    
    private final ClassBehaviorFactory      classBehaviorFactory            = new ClassBehaviorFactory();
    
    // =========================================================================
    // METHODS
    // =========================================================================
    /**
     * Load listeners.
     *
     * @param config the config
     * @param globalProperties
     * @param manifest
     * @return the list
     * @throws TechnicalException the technical exception
     */
    public List<EngineListener> loadListeners(final List<ListenerModel> config,
                                              final ConfigHandler<String, String> globalProperties,
                                              final ManifestInfo manifest) throws TechnicalException {
        List<EngineListener> result = null;
        if (config != null) {
            result = load(config, globalProperties);
        }
        return result;
    }
    
    /**
     * Load processors.
     *
     * @param config the config
     * @param globalProperties
     * @param manifest
     * @return the list
     * @throws TechnicalException the technical exception
     */
    public List<Processor> loadProcessors(final List<ProcessorModel> config,
                                          final ConfigHandler<String, String> globalProperties,
                                          final ManifestInfo manifest) throws TechnicalException {
        List<Processor> result = null;
        if (config != null) {
            result = load(config, globalProperties);
        }
        return result;
    }
    
    public List<AlertingProvider> loadAlertings(final List<AlertingProviderModel> config,
                                                final ConfigHandler<String, String> globalProperties) throws TechnicalException {
        List<AlertingProvider> result = null;
        if ((config != null) && isCdiContextEnable()) {
            result = load(config, globalProperties);
        }
        return result;
    }
    
    public List<Handler> loadHandlers(final List<HandlerConfig> config,
                                      final ConfigHandler<String, String> globalProperties) throws TechnicalException {
        List<Handler> result = null;
        if (config != null) {
            result = load(config, globalProperties);
        }
        return result;
    }
    
    private boolean isCdiContextEnable() {
        boolean result = false;
        try {
            final CDI<?> cdiContext = CDI.current();
            result = true;
        }
        catch (final IllegalStateException e) {
            Loggers.INIT.error(e.getMessage());
        }
        
        return result;
    }
    
    // =========================================================================
    // PROVIDERS
    // =========================================================================
    /**
     * Load provider.
     *
     * @param config the config
     * @param globalProperties
     * @param manifest
     * @return the list
     * @throws TechnicalException the technical exception
     */
    public List<Provider> loadProvider(final List<ProviderConfig> config,
                                       final ConfigHandler<String, String> globalProperties,
                                       final ManifestInfo manifest) throws TechnicalException {
        List<Provider> result = null;
        if (config != null) {
            result = processLoadProvider(config, globalProperties, manifest);
        }
        return result;
    }
    
    private List<Provider> processLoadProvider(final List<? extends ClassBehavior> behaviors,
                                               final ConfigHandler<String, String> globalProperties,
                                               final ManifestInfo manifest) throws TechnicalException {
        final List<Provider> result = new ArrayList<>();
        
        for (final ClassBehavior behavior : behaviors) {
            final String hash = hashBehavior(behavior);
            assertBehavior(hash, behavior);
            behavior.setManifest(manifest);
            final Provider instance = buildBehaviorInstance(behavior, globalProperties);
            Asserts.notNull(String.format("can't instantiate behavior %s", behavior.getName()), instance);
            result.add(instance);
        }
        
        return result;
        
    }
    
    private <T> T buildBehaviorInstance(final ClassBehavior behavior,
                                        final ConfigHandler<String, String> globalProperties) throws LoaderException {
        T result;
        try {
            result = classBehaviorFactory.createBean(behavior, globalProperties);
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Loggers.XLLOG.error(buildMessage(e, behavior));
            throw new LoaderFatalException(e.getMessage(), e);
        }
        catch (final TechnicalException e) {
            throw new LoaderException(e.getMessage(), e);
        }
        
        return result;
    }
    
    // =========================================================================
    // PRIVATE
    // =========================================================================
    /**
     * Load.
     *
     * @param <T> the generic type
     * @param behaviors the behaviors
     * @param globalProperties
     * @return the list
     * @throws TechnicalException the technical exception
     */
    private <T> List<T> load(final List<? extends ClassBehavior> behaviors,
                             final ConfigHandler<String, String> globalProperties) throws TechnicalException {
        final List<T> result = new ArrayList<>();
        
        for (final ClassBehavior behavior : behaviors) {
            final String hash = hashBehavior(behavior);
            assertBehavior(hash, behavior);
            final T instance = buildBehaviorInstance(behavior, globalProperties);
            Asserts.notNull(String.format("can't instantiate behavior %s", behavior.getName()), instance);
            result.add(instance);
        }
        
        return result;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private String buildMessage(final Exception except, final ClassBehavior behavior) {
        final StringBuilder result = new StringBuilder();
        result.append("provider:");
        result.append(behavior.getName());
        result.append(" : ");
        
        if (except.getMessage() != null) {
            result.append(except.getMessage());
            result.append(' ');
        }
        
        if (except.getCause() != null) {
            result.append(except.getCause());
        }
        result.append(' ');
        result.append('(');
        result.append(behavior.getClassName());
        result.append(')');
        
        return result.toString();
    }
    
    /**
     * Hash behavior.
     *
     * @param behavior the behavior
     * @return the string
     */
    private String hashBehavior(final ClassBehavior behavior) {
        final StringBuilder result = new StringBuilder();
        result.append(behavior.getClass().getName());
        result.append('@');
        result.append(behavior.getName());
        result.append('@');
        result.append(behavior.getClassName());
        return encryptionUtils.encodeSha1(result.toString());
    }
    
    // =========================================================================
    // LOAD PROPERTIES
    // =========================================================================
    public Map<String, Map<String, String>> loadPropertiesLables(final ManifestInfo manifest) throws TechnicalException {
        Map<String, Map<String, String>> properties = null;
        if (manifest != null) {
            if (isJarResources(manifest)) {
                final File zip = FilesUtils.resolveJarFile(manifest.getManifestUrl());
                properties = propertiesResourcesLoaderZip.loadResources(zip);
            }
        }
        
        return properties;
    }
    
    public void loadAlertingResources(final ManifestInfo manifest) throws TechnicalException {
        if (manifest != null) {
            if (isJarResources(manifest)) {
                alertsResourcesLoaderZip.loadAlertingsResources(manifest.getManifestUrl());
            }
            else {
                alertsResourcesLoaderFileSystem.loadAlertingsResources(manifest.getManifestUrl());
            }
        }
        
    }
    
    private boolean isJarResources(final ManifestInfo manifest) {
        return alertsResourcesLoaderZip.isJarResources(manifest.getManifestUrl());
    }
    
    public Map<String, String> loadFrontProperties(final PluginConfiguration config) {
        final Map<String, String> result = new HashMap<>();
        if (config.getFrontProperties().isPresent()) {
            for (final PropertyModel property : config.getFrontProperties().get()) {
                result.put(property.getKey(), property.getValue());
            }
        }
        return result;
    }
    
    // =========================================================================
    // EXCEPTION
    // =========================================================================
    /**
     * Assert behavior.
     *
     * @param hash the hash
     * @param behavior the behavior
     * @throws LoaderException the loader exception
     */
    private void assertBehavior(final String hash, final ClassBehavior behavior) throws LoaderException {
        if (behavior.getClassName() == null) {
            throw new LoaderException("behavior \"{0}\" hasn't class name! ", behavior.getName());
        }
        if (CACHE.containsKey(hash)) {
            throw new LoaderException("behavior \"{0}\" already defined ! ", behavior.getName());
        }
    }
    
    private class LoaderException extends TechnicalException {
        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 5834339220694067530L;
        
        public LoaderException(final String message, final Object... values) {
            super(message, values);
        }
        
        public LoaderException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * The Class LoaderFatalException.
     */
    private class LoaderFatalException extends FatalException {
        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 8275883699726339471L;
        
        public LoaderFatalException(final String message, final Throwable cause) {
            super(message, cause);
        }
        
        public LoaderFatalException(final String message, final Object... values) {
            super(message, values);
        }
    }
    
}
