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
package io.inugami.framework.configuration.models.plugins;

import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.configuration.models.front.PluginFrontConfig;
import io.inugami.framework.interfaces.alertings.AlertingProvider;
import io.inugami.framework.interfaces.handlers.Handler;
import io.inugami.framework.interfaces.listeners.EngineListener;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.providers.Provider;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static io.inugami.framework.interfaces.exceptions.Asserts.assertNotNull;

/**
 * Plugin
 *
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuppressWarnings({"java:S3655", "java:S107"})
public final class Plugin implements Serializable {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long serialVersionUID = 1620535173987042446L;

    private           boolean                          enabled = true;
    private           PluginConfiguration              config;
    private           List<EventConfig>                events;
    private           boolean                          eventConfigPresent;
    private           ManifestInfo                     manifest;
    private           Map<String, Map<String, String>> properties;
    private           PluginFrontConfig                frontConfig;
    private transient List<EngineListener>             listeners;
    private transient List<Processor>                  processors;
    private transient List<Provider>                   providers;
    private transient List<Handler>                    handlers;
    private transient List<AlertingProvider>           alertingProviders;
    @ToString.Include
    @EqualsAndHashCode.Include
    private           Gav                              gav;

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String buildChannelName() {
        String result = null;
        if (frontConfig != null) {
            result = frontConfig.getPluginBaseName();
        } else {
            assertNotNull("Plugin GAV required!", gav);
            result = String.join(":", gav.getGroupId(), gav.getArtifactId());
        }
        return result;
    }

}
