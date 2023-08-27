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

import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Transformer;
import io.inugami.configuration.models.plugins.front.PluginFrontConfig;

/**
 * PluginFrontConfigTransformer
 *
 * @author patrick_guillerm
 * @since 20 janv. 2017
 */
//TODO: [321] refactoring
@SuppressWarnings({"java:S1854", "java:S1135", "java:S1481"})
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
