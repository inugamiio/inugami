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
package io.inugami.framework.configuration.services.mapping.transformers;

import flexjson.transformer.ObjectTransformer;
import flexjson.transformer.Transformer;
import io.inugami.configuration.models.plugins.PluginConfiguration;

/**
 * PluginConfigurationTransformer
 *
 * @author patrick_guillerm
 * @since 20 janv. 2017
 */
public class PluginConfigurationTransformer extends AbstractTransformerHelper<PluginConfiguration>
        implements Transformer {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void process(final PluginConfiguration value) {
        final Transformer optionnalTransformer = new OptionalTransformer();
        final Transformer objTransformer       = new ObjectTransformer();

        //@formatter:off
        field("gav",              ()-> objTransformer.transform(value.getGav()));
        fieldString("configFile", value::getConfigFile);

        
        field("frontConfig",      ()-> optionnalTransformer.transform(value.getFrontConfig()));
        
        
        fieldList("resources",    value.getResources());
        fieldList("providers",    value.getProviders());
        fieldList("listeners",    value.getListeners());
        fieldList("processors",   value.getProcessors());
        fieldList("eventsFiles",  value.getEventsFiles());
        fieldList("dependencies", value.getDependencies());
        //@formatter:on
    }

}
