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
package io.inugami.framework.api.mapping.events.json;

import flexjson.JSONContext;
import flexjson.transformer.AbstractTransformer;
import io.inugami.framework.interfaces.models.JsonBuilder;
import io.inugami.framework.interfaces.models.event.Event;

/**
 * EventTransformer
 *
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
public class EventTransformer extends AbstractTransformer {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final GenericEventTransformer genericTransfo = new GenericEventTransformer();

    private final SimpleEventTransformer transformer = new SimpleEventTransformer();

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {
        if (object instanceof Event) {
            process((Event) object);
        } else {
            getContext().write(JsonBuilder.VALUE_NULL);
        }
    }

    // =========================================================================
    // PROCESS
    // =========================================================================
    private void process(final Event event) {
        final JSONContext ctx = getContext();
        ctx.writeOpenObject();
        genericTransfo.transform(event, ctx);
        genericTransfo.writeString("scheduler", event.getScheduler(), ctx);
        ctx.writeComma();
        genericTransfo.writeListWithTransfo("targets", event.getTargets(), ctx, transformer);
        ctx.writeCloseObject();
    }

}
