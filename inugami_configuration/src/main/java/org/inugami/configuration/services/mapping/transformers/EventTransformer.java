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
package org.inugami.configuration.services.mapping.transformers;

import org.inugami.api.models.events.Event;

import flexjson.transformer.Transformer;

/**
 * EventTransformer
 * 
 * @author patrick_guillerm
 * @since 24 janv. 2017
 */
public class EventTransformer extends AbstractTransformerHelper<Event> implements Transformer {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void process(final Event object) {
        //@formatter:off
        fieldString("name"      , () -> object.getName());
        fieldString("from"      , () -> object.getFrom().orElse(null));
        fieldString("until"     , () -> object.getUntil().orElse(null));
        fieldString("mapper"    , () -> object.getMapper().orElse(null));
        fieldString("provider"  , () -> object.getProvider().orElse(null));
        fieldList(  "targets"   , object.getTargets());
        //@formatter:on
        if (object.getProcessors().isPresent()) {
            fieldList("processors", object.getProcessors().get());
        }
        else {
            fieldNull("processors");
        }
    }
}
