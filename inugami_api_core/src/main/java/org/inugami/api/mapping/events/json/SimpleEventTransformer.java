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
package org.inugami.api.mapping.events.json;

import org.inugami.api.models.JsonBuilder;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.models.events.TargetConfig;

import flexjson.JSONContext;
import flexjson.transformer.AbstractTransformer;

/**
 * SimpleEventTransformer
 * 
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
public class SimpleEventTransformer extends AbstractTransformer {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final transient GenericEventTransformer genericTransfo = new GenericEventTransformer();
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {
        if ((object != null) && ((object instanceof SimpleEvent) || (object instanceof TargetConfig))) {
            process((SimpleEvent) object);
        }
        else {
            getContext().write(JsonBuilder.VALUE_NULL);
        }
    }
    
    // =========================================================================
    // PROCESS
    // =========================================================================
    private void process(final SimpleEvent event) {
        final JSONContext ctx = getContext();
        ctx.writeOpenObject();
        genericTransfo.transform(event, ctx);
        genericTransfo.writeString("query", event.getQuery(), ctx);
        genericTransfo.writeString("scheduler", event.getScheduler(), ctx);
        genericTransfo.writeString("parent", event.getParent(), ctx);
        ctx.writeCloseObject();
        
    }
}
