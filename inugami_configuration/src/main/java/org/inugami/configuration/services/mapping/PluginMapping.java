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
package org.inugami.configuration.services.mapping;

import java.util.List;

import org.inugami.api.exceptions.services.MappingException;
import org.inugami.api.models.events.Event;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.models.events.TargetConfig;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.configuration.models.plugins.PluginConfiguration;
import org.inugami.configuration.services.mapping.transformers.EventTransformer;
import org.inugami.configuration.services.mapping.transformers.PluginConfigurationTransformer;
import org.inugami.configuration.services.mapping.transformers.PluginTransformer;
import org.inugami.configuration.services.mapping.transformers.SimpleEventTransformer;
import org.inugami.configuration.services.mapping.transformers.TargetTransformer;

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
