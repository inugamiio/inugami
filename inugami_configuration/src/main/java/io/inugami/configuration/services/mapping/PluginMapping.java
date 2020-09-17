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
package io.inugami.configuration.services.mapping;

import java.util.List;

import io.inugami.api.exceptions.services.MappingException;
import io.inugami.api.models.events.Event;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.models.events.TargetConfig;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.configuration.models.plugins.PluginConfiguration;
import io.inugami.configuration.services.mapping.transformers.EventTransformer;
import io.inugami.configuration.services.mapping.transformers.PluginConfigurationTransformer;
import io.inugami.configuration.services.mapping.transformers.PluginTransformer;
import io.inugami.configuration.services.mapping.transformers.SimpleEventTransformer;
import io.inugami.configuration.services.mapping.transformers.TargetTransformer;

import flexjson.JSONSerializer;

/**
 * PluginMapping
 * 
 * @author patrick_guillerm
 * @since 20 janv. 2017
 */
public class PluginMapping {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public String marshalling(final List<Plugin> values) throws MappingException {
        //@formatter:off
        return new JSONSerializer()
                        .transform(new PluginTransformer(), Plugin.class)
                        .transform(new PluginConfigurationTransformer(), PluginConfiguration.class)
                        .transform(new EventTransformer(), Event.class)
                        .transform(new TargetTransformer(), TargetConfig.class)
                        .transform(new SimpleEventTransformer(), SimpleEvent.class)
                        .exclude("*.class")
                        .deepSerialize(values);
        //@formatter:on
    }
}
