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
package io.inugami.core.context;

import io.inugami.api.alertings.AlertingProvider;
import io.inugami.api.alertings.AlertingProviderModel;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.handlers.Handler;
import io.inugami.api.listeners.EngineListener;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.plugins.ManifestInfo;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.processors.Processor;
import io.inugami.api.processors.ProcessorModel;
import io.inugami.api.providers.Provider;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.security.EncryptionUtils;
import io.inugami.configuration.models.HandlerConfig;
import io.inugami.configuration.models.ListenerModel;
import io.inugami.configuration.models.ProviderConfig;
import io.inugami.configuration.models.plugins.PluginConfiguration;
import io.inugami.configuration.models.plugins.PropertyModel;
import io.inugami.core.context.loading.*;

import javax.enterprise.inject.spi.CDI;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Loader.
 *
 * @author patrick_guillerm
 * @since 3 janv. 2017
 */
@SuppressWarnings({"java:S3655", "java:S1172", "java:S1854", "java:S2139", "java:S1481"})
public class Loader {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The encryption utils.
     */
    private final EncryptionUtils encryptionUtils = new EncryptionUtils();

    private final AlertsResourcesLoader alertsResourcesLoaderZip = new AlertsResourcesLoaderZip();

    private final AlertsResourcesLoader alertsResourcesLoaderFileSystem = new AlertsResourcesLoaderFileSystem();

    private final PropertiesResourcesLoader propertiesResourcesLoaderZip = new PropertiesResourcesLoaderZip();

    /**
     * The cache.
     */
    private static final Map<String, Object> CACHE = new LinkedHashMap<>();

    private final ClassBehaviorFactory classBehaviorFactory = new ClassBehaviorFactory();

    // =========================================================================
    // METHODS
    // =========================================================================


    public List<EngineListener> loadListeners(final List<ListenerModel> config,
                                              final ConfigHandler<String, String> globalProperties,
                                              final ManifestInfo manifest) throws TechnicalException {
        List<EngineListener> result = null;
        if (config != null) {
            result = load(config, globalProperties);
        }
        return result;
    }


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
        } catch (final IllegalStateException e) {
            Loggers.INIT.error(e.getMessage());
        }

        return result;
    }

    // =========================================================================
    // PROVIDERS
    // =========================================================================

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
            Asserts.assertNotNull(String.format("can't instantiate behavior %s", behavior.getName()), instance);
            result.add(instance);
        }

        return result;

    }

    private <T> T buildBehaviorInstance(final ClassBehavior behavior,
                                        final ConfigHandler<String, String> globalProperties) throws LoaderException {
        final T result;
        try {
            result = classBehaviorFactory.createBean(behavior, globalProperties);
        } catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Loggers.XLLOG.error(buildMessage(e, behavior));
            throw new LoaderFatalException(e.getMessage(), e);
        } catch (final TechnicalException e) {
            throw new LoaderException(e.getMessage(), e);
        }

        return result;
    }

    // =========================================================================
    // PRIVATE
    // =========================================================================

    private <T> List<T> load(final List<? extends ClassBehavior> behaviors,
                             final ConfigHandler<String, String> globalProperties) throws TechnicalException {
        final List<T> result = new ArrayList<>();

        for (final ClassBehavior behavior : behaviors) {
            final String hash = hashBehavior(behavior);
            assertBehavior(hash, behavior);
            final T instance = buildBehaviorInstance(behavior, globalProperties);
            Asserts.assertNotNull(String.format("can't instantiate behavior %s", behavior.getName()), instance);
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
        if (manifest != null && isJarResources(manifest)) {
            final File zip = FilesUtils.resolveJarFile(manifest.getManifestUrl());
            properties = propertiesResourcesLoaderZip.loadResources(zip);
        }

        return properties;
    }

    public void loadAlertingResources(final ManifestInfo manifest) throws TechnicalException {
        if (manifest != null) {
            if (isJarResources(manifest)) {
                alertsResourcesLoaderZip.loadAlertingsResources(manifest.getManifestUrl());
            } else {
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

    private void assertBehavior(final String hash, final ClassBehavior behavior) throws LoaderException {
        if (behavior.getClassName() == null) {
            throw new LoaderException("behavior \"{0}\" hasn't class name! ", behavior.getName());
        }
        if (CACHE.containsKey(hash)) {
            throw new LoaderException("behavior \"{0}\" already defined ! ", behavior.getName());
        }
    }

    private class LoaderException extends TechnicalException {

        /**
         * The Constant serialVersionUID.
         */
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
    @SuppressWarnings({"java:S110"})
    private class LoaderFatalException extends FatalException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = 8275883699726339471L;

        public LoaderFatalException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public LoaderFatalException(final String message, final Object... values) {
            super(message, values);
        }
    }

}
