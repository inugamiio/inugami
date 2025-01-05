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

import io.inugami.framework.configuration.models.HandlerConfig;
import io.inugami.framework.configuration.models.ListenerModel;
import io.inugami.framework.configuration.models.ProviderConfig;
import io.inugami.framework.configuration.models.app.SecurityConfiguration;
import io.inugami.framework.configuration.models.components.Components;
import io.inugami.framework.configuration.models.front.PluginFrontConfig;
import io.inugami.framework.interfaces.alertings.AlertingProviderModel;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * PluginConfiguration
 *
 * @author patrick_guillerm
 * @since 22 déc. 2016
 */
@SuppressWarnings({"java:S2326"})
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PluginConfiguration implements Serializable {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long                        serialVersionUID = -6084670832420392014L;
    @ToString.Include
    private              Boolean                     enable;
    @ToString.Include
    private              String                      configFile;
    private              PluginFrontConfig           frontConfig;
    private              Long                        timeout;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              Gav                         gav;
    @Singular("resources")
    private              List<Resource>              resources;
    @Singular("providers")
    private              List<ProviderConfig>        providers;
    @Singular("handlers")
    private              List<HandlerConfig>         handlers;
    @Singular("listeners")
    private              List<ListenerModel>         listeners;
    @Singular("processors")
    private              List<ProcessorModel>        processors;
    @Singular("properties")
    private              List<PropertyModel>         properties;
    @Singular("frontProperties")
    private              List<PropertyModel>         frontProperties;
    @Singular("alertings")
    private              List<AlertingProviderModel> alertings;
    @Singular("eventsFiles")
    private              List<EventsFileModel>       eventsFiles;
    @Singular("dependencies")
    private              List<Dependency>            dependencies;
    @Singular("security")
    private              List<SecurityConfiguration> security;
    private              Components                  components;

}
