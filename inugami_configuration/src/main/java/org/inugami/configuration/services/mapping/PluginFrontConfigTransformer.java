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

import org.inugami.configuration.models.plugins.front.PluginFrontConfig;

import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Transformer;

/**
 * PluginFrontConfigTransformer
 * 
 * @author patrick_guillerm
 * @since 20 janv. 2017
 */
public class PluginFrontConfigTransformer extends AbstractTransformer implements Transformer {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {
        getContext().writeOpenObject();
        if (object instanceof PluginFrontConfig) {
            final PluginFrontConfig value = (PluginFrontConfig) object;
        }
        getContext().writeCloseObject();
    }
}
