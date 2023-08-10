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
package io.inugami.configuration.models.plugins;

import io.inugami.api.alertings.AlertingProvider;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.handlers.Handler;
import io.inugami.api.listeners.EngineListener;
import io.inugami.api.models.Gav;
import io.inugami.api.models.plugins.ManifestInfo;
import io.inugami.api.processors.Processor;
import io.inugami.api.providers.Provider;
import io.inugami.configuration.models.EventConfig;
import io.inugami.configuration.models.plugins.front.PluginFrontConfig;
import lombok.Getter;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Plugin
 *
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
@Getter
@SuppressWarnings({"java:S3655", "java:S107"})
public class Plugin implements Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1620535173987042446L;

    private                 boolean                          enabled = true;
    private final           PluginConfiguration              config;
    private final           List<EventConfig>                events;
    private final           boolean                          eventConfigPresent;
    private final           ManifestInfo                     manifest;
    private final           Map<String, Map<String, String>> properties;
    private final transient List<EngineListener>             listeners;
    private final transient List<Processor>                  processors;
    private final transient List<Provider>                   providers;
    private final transient List<Handler>                    handlers;
    private final transient List<AlertingProvider>           alertingProviders;
    private final           Gav                              gav;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    //@formatter:off
    public Plugin(final PluginConfiguration config,
                  final List<EventConfig> events,
                  final List<EngineListener> listeners,
                  final List<Processor> processors,
                  final List<Handler> handlers,
                  final List<Provider> providers,
                  final List<AlertingProvider> alertingProviders,
                  final ManifestInfo manifest,
                  final Map<String, Map<String, String>> propertiesPaths) {
        //@formatter:on
        Asserts.assertNotNull(config);
        Asserts.assertNotNull(config.getGav());
        this.gav = config.getGav();
        this.enabled = config.isEnable();
        this.config = config;
        this.eventConfigPresent = (events != null) && !events.isEmpty();
        this.manifest = manifest;
        this.properties = propertiesPaths;

        //@formatter:off
        this.events = events == null ? null : Collections.unmodifiableList(events);
        this.listeners = listeners == null ? null : Collections.unmodifiableList(listeners);
        this.processors = processors == null ? null : Collections.unmodifiableList(processors);
        this.handlers = handlers == null ? null : Collections.unmodifiableList(handlers);
        this.providers = providers == null ? null : Collections.unmodifiableList(providers);
        this.alertingProviders = alertingProviders == null ? null : Collections.unmodifiableList(alertingProviders);
        //@formatter:on

    }

    public Plugin(final String groupId, final String artifactId) {
        gav = new Gav(groupId, artifactId);
        config = null;
        events = null;
        eventConfigPresent = false;
        manifest = null;
        properties = null;
        listeners = null;
        processors = null;
        providers = null;
        handlers = null;
        alertingProviders = null;

    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return config.getGav().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;

        if (!result && (obj != null) && (obj instanceof Plugin)) {
            result = getGav().equals(((Plugin) obj).getGav());
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Plugin[");
        builder.append(getGav().getHash());
        builder.append(']');
        return builder.toString();
    }

    // =========================================================================
    // DELEGATES
    // =========================================================================


    public List<Resource> getResources() {
        return config.getResources();
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String buildChannelName() {
        String result = null;
        if (getFrontConfig().isPresent()) {
            result = getFrontConfig().get().getPluginBaseName();
        } else {
            //@formatter:off
            result = String.join(":", getGav().getGroupId(), getGav().getArtifactId());
            //@formatter:on
        }
        return result;
    }


    public void enablePlugin() {
        this.enabled = true;
    }

    public void disablePlugin() {
        this.enabled = false;
    }

    public Optional<List<EventConfig>> getEvents() {
        return events == null ? Optional.empty() : Optional.of(events);
    }

    public Optional<List<EngineListener>> getListeners() {
        return listeners == null ? Optional.empty() : Optional.of(listeners);
    }

    public Optional<List<Processor>> getProcessors() {
        return processors == null ? Optional.empty() : Optional.of(processors);
    }

    public Optional<List<Provider>> getProviders() {
        return providers == null ? Optional.empty() : Optional.of(providers);
    }


    public Optional<PluginFrontConfig> getFrontConfig() {
        return config.getFrontConfig();
    }

    public Optional<Long> getTimeout() {
        return Optional.ofNullable(config.getTimeout());
    }


    public File getWorkspace() {
        return manifest == null ? null : manifest.getWorkspace();
    }

    public Optional<List<AlertingProvider>> getAlertingProviders() {
        return Optional.ofNullable(alertingProviders);
    }

    public Optional<Map<String, Map<String, String>>> getProperties() {
        return Optional.ofNullable(properties);
    }

    public Optional<List<Handler>> getHandlers() {
        return handlers == null ? Optional.empty() : Optional.of(handlers);
    }

}
