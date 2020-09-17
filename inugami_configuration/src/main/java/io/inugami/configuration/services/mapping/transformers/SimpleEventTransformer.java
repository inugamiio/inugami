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

import io.inugami.api.models.events.SimpleEvent;

import flexjson.transformer.Transformer;

/**
 * SimpleEventTransformer
 * 
 * @author patrick_guillerm
 * @since 24 janv. 2017
 */
public class SimpleEventTransformer extends AbstractTransformerHelper<SimpleEvent> implements Transformer {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void process(final SimpleEvent object) {
        //@formatter:off
        fieldString("name"      , () -> object.getName());
        fieldString("query"     , () -> object.getQuery());
        fieldString("parent"    , () -> object.getParent().orElse(null));
        fieldString("from"      , () -> object.getFrom().orElse(null));
        fieldString("until"     , () -> object.getUntil().orElse(null));
        fieldString("mapper"    , () -> object.getMapper().orElse(null));
        fieldString("provider"  , () -> object.getProvider().isPresent() ? object.getProvider().get() : null);
        //@formatter:on
        if (object.getProcessors().isPresent()) {
            fieldList("processors", object.getProcessors().get());
        }
        else {
            fieldNull("processors");
        }
        
    }
}
