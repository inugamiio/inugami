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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import io.inugami.api.alertings.AlertingProviderModel;
import io.inugami.api.models.Gav;
import io.inugami.api.processors.ProcessorModel;
import io.inugami.configuration.models.HandlerConfig;
import io.inugami.configuration.models.ListenerModel;
import io.inugami.configuration.models.ProviderConfig;
import io.inugami.configuration.models.app.SecurityConfiguration;
import io.inugami.configuration.models.plugins.components.config.Components;
import io.inugami.configuration.models.plugins.front.PluginFrontConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PluginConfiguration
 *
 * @author patrick_guillerm
 * @since 22 déc. 2016
 */
@SuppressWarnings({"java:S2326"})
@XStreamAlias("plugin")
public class PluginConfiguration implements Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -6084670832420392014L;

    @XStreamAsAttribute
    private Boolean                     enable;
    @XStreamOmitField
    private String                      configFile;
    @XStreamOmitField
    private PluginFrontConfig           frontConfig;
    @XStreamAsAttribute
    private Long                        timeout;
    private Gav                         gav;
    private List<Resource>              resources;
    private List<ProviderConfig>        providers;
    private List<HandlerConfig>         handlers;
    private List<ListenerModel>         listeners;
    private List<ProcessorModel>        processors;
    private List<PropertyModel>         properties;
    @XStreamAlias("front-properties")
    private List<PropertyModel>         frontProperties;
    private List<AlertingProviderModel> alertings;
    @XStreamAlias("events-files")
    private List<EventsFileModel>       eventsFiles;
    private List<Dependency>            dependencies;
    @XStreamImplicit
    private List<SecurityConfiguration> security;
    @XStreamOmitField
    private Components                  components;

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PluginConfiguration [gav=");
        builder.append(gav);
        builder.append(", enable=");
        builder.append(enable);
        builder.append(", frontConfig=");
        builder.append(frontConfig);
        builder.append(", resources=");
        builder.append(renderList(resources));
        builder.append(", providers=");
        builder.append(renderList(providers));
        builder.append(", handlers=");
        builder.append(renderList(handlers));
        builder.append(", listeners=");
        builder.append(renderList(listeners));
        builder.append(", processors=");
        builder.append(renderList(processors));
        builder.append(", alertings=");
        builder.append(renderList(alertings));
        builder.append(", eventsFiles=");
        builder.append(renderList(eventsFiles));
        builder.append(", dependencies=");
        builder.append(renderList(dependencies));
        builder.append(", components=");
        builder.append(components == null ? "null" : renderList(components.getComponents()));
        builder.append("]");
        return builder.toString();
    }

    private <T> String renderList(final List<? extends Object> values) {
        final StringBuilder result = new StringBuilder();
        if (values == null) {
            result.append("null");
        } else {
            result.append('[');
            for (final Object value : values) {
                result.append(value);
                result.append(',');
            }
            result.append(']');
        }
        return result.toString();
    }

    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = (prime * result) + ((gav == null) ? 0 : gav.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;

        if (!result && (obj != null)) {
            Gav otherGav = null;
            if (obj instanceof PluginConfiguration) {
                otherGav = ((PluginConfiguration) obj).getGav();
            }

            result = gav == null ? otherGav == null : gav.equals(otherGav);
        }

        return result;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================

    public Gav getGav() {
        return gav;
    }

    public void setGav(final Gav gav) {
        this.gav = gav;
    }

    public List<Resource> getResources() {
        if (resources == null) {
            resources = new ArrayList<>();
        }
        return resources;
    }

    public List<HandlerConfig> getHandlers() {
        return handlers;
    }

    public void setHandlers(final List<HandlerConfig> handlers) {
        this.handlers = handlers;
    }

    public void setResources(final List<Resource> resources) {
        this.resources = resources;
    }

    public List<ProviderConfig> getProviders() {
        return providers;
    }

    public void setProviders(final List<ProviderConfig> providers) {
        this.providers = providers;
    }

    public List<ListenerModel> getListeners() {
        return listeners;
    }

    public void setListeners(final List<ListenerModel> listeners) {
        this.listeners = listeners;
    }

    public List<ProcessorModel> getProcessors() {
        return processors;
    }

    public void setProcessors(final List<ProcessorModel> processors) {
        this.processors = processors;
    }

    public List<EventsFileModel> getEventsFiles() {
        return eventsFiles;
    }

    public void setEventsFiles(final List<EventsFileModel> eventsFiles) {
        this.eventsFiles = eventsFiles;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(final String configFile) {
        this.configFile = configFile;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(final List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public Optional<PluginFrontConfig> getFrontConfig() {
        return frontConfig == null ? Optional.empty() : Optional.of(frontConfig);
    }

    public void setFrontConfig(final PluginFrontConfig frontConfig) {
        this.frontConfig = frontConfig;
    }

    public boolean isEnable() {
        return enable == null || enable;
    }

    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }

    public List<PropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(final List<PropertyModel> properties) {
        this.properties = properties;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }

    public Boolean getEnable() {
        return enable;
    }

    public List<SecurityConfiguration> getSecurity() {
        return security;
    }

    public void setSecurity(final List<SecurityConfiguration> security) {
        this.security = security;
    }

    public List<AlertingProviderModel> getAlertings() {
        return alertings;
    }

    public void setAlertings(final List<AlertingProviderModel> alertings) {
        this.alertings = alertings;
    }

    public Optional<List<PropertyModel>> getFrontProperties() {
        return Optional.ofNullable(frontProperties);
    }

    public void setFrontProperties(final List<PropertyModel> frontProperties) {
        this.frontProperties = frontProperties;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(final Components components) {
        this.components = components;
    }

}
