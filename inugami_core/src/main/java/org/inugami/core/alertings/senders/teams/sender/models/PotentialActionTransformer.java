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
package org.inugami.core.alertings.senders.teams.sender.models;

import org.inugami.configuration.services.mapping.transformers.AbstractTransformerHelper;

import flexjson.transformer.Transformer;

/**
 * PotentialActionTransformer
 * 
 * @author patrick_guillerm
 * @since 22 août 2018
 */
public class PotentialActionTransformer extends AbstractTransformerHelper<PotentialAction> implements Transformer {
    
    @Override
    public void process(final PotentialAction value) {
        fieldString("name", () -> value.getName());
        fieldString("@type", () -> value.getType());
        
        if (value instanceof PotentialActionOpenUri) {
            final PotentialActionOpenUri openUri = (PotentialActionOpenUri) value;
            fieldList("targets", openUri.getTargets());
        }
        
    }
    
}
