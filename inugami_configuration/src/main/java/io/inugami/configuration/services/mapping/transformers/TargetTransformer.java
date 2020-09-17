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

import io.inugami.api.models.events.TargetConfig;

import flexjson.transformer.Transformer;

/**
 * TargetTransformer
 * 
 * @author patrick_guillerm
 * @since 24 janv. 2017
 */
public class TargetTransformer extends AbstractTransformerHelper<TargetConfig> implements Transformer {
    
    @Override
    public void process(final TargetConfig object) {
        new SimpleEventTransformer().transform(object);
    }
}
