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

import java.util.List;
import java.util.Optional;

import org.inugami.api.functionnals.ApplyIfNotNull;
import org.inugami.api.models.JsonBuilder;
import org.inugami.api.models.events.GenericEvent;

import flexjson.JSONContext;
import flexjson.transformer.Transformer;

/**
 * EventTransformer
 * 
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
public class GenericEventTransformer implements ApplyIfNotNull {
    
    private static final String NULL = "null";
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public <T extends GenericEvent> void transform(final T event, final JSONContext ctx) {
        
        ctx.writeName("name");
        if (event.getName() == null) {
            ctx.write(JsonBuilder.VALUE_NULL);
        }
        else {
            ctx.writeQuoted(event.getName());
        }
        
        writeString("from", event.getFrom(), ctx);
        writeString("until", event.getUntil(), ctx);
        writeString("provider", event.getProvider(), ctx);
        writeString("mapper", event.getMapper(), ctx);
        ctx.writeComma();
        writeList("processors", event.getProcessors(), ctx);
        ctx.writeComma();
        writeList("alertings", event.getAlertings(), ctx);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    public void writeString(final String name, final Object data, final JSONContext ctx) {
        writeString(name, data, ctx, false);
    }
    
    public void writeString(final String name, final Object data, final JSONContext ctx, final boolean first) {
        if (data != null) {
            if (data instanceof String) {
                if (!first) {
                    ctx.writeComma();
                }
                ctx.writeName(name);
                ctx.writeQuoted((String) data);
            }
            else if (data instanceof Optional) {
                final Optional<?> opt = (Optional<?>) data;
                writeString(name, opt.orElse(null), ctx, first);
            }
        }
        else {
            if (!first) {
                ctx.writeComma();
            }
            ctx.writeName(name);
            ctx.write(NULL);
        }
    }
    
    public <T> void writeList(final String name, final Optional<List<T>> optional, final JSONContext ctx) {
        if (optional.isPresent()) {
            final List<T> data = optional.get();
            ctx.writeName(name);
            ctx.writeOpenArray();
            for (int i = 0; i < data.size(); i++) {
                if (i != 0) {
                    ctx.writeComma();
                }
                ctx.transform(data.get(i));
            }
            ctx.writeCloseArray();
        }
    }
    
    public <T> void writeListWithTransfo(final String name, final List<T> values, final JSONContext ctx,
                                         final Transformer transformer) {
        if (values != null) {
            ctx.writeName(name);
            ctx.writeOpenArray();
            for (int i = 0; i < values.size(); i++) {
                if (i != 0) {
                    ctx.writeComma();
                }
                if (transformer == null) {
                    ctx.transform(values.get(i));
                }
                else {
                    transformer.transform(values.get(i));
                }
            }
            ctx.writeCloseArray();
        }
    }
    
}
