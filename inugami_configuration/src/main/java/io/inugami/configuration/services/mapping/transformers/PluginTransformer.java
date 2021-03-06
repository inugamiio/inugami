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
package io.inugami.configuration.services.mapping.transformers;

import io.inugami.configuration.models.plugins.Plugin;

import flexjson.transformer.Transformer;

/**
 * OptionnalTransformer
 * 
 * @author patrick_guillerm
 * @since 20 janv. 2017
 */
public class PluginTransformer extends AbstractTransformerHelper<Plugin> implements Transformer {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void process(final Plugin value) {
        fieldBoolean("enable", () -> value.isEnabled());
        field("config", () -> new PluginConfigurationTransformer().transform(value.getConfig()));
        fieldList("events", value.getEvents());
    }
}
