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
package io.inugami.framework.interfaces.marshalling.serializers;


import io.inugami.framework.interfaces.models.JsonBuilder;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodSerializer extends StdSerializer<Method> {

    public MethodSerializer(final Class<Method> t) {
        super(t);
    }

    @Override
    public void serialize(final Method method,
                          final JsonGenerator jsonGenerator,
                          final SerializationContext ctxt) throws JacksonException {
        if (method == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeString(renderAsJson(method));
        }
    }

    private static String renderAsJson(final Method method) {
        final JsonBuilder json = new JsonBuilder();
        json.write(method.getReturnType() == null ? "void" : method.getReturnType().getName())
            .write(" ")
            .write(method.getDeclaringClass().getName())
            .write(method.getName())
            .openTuple();

        final Parameter[] params   = method.getParameters();
        final int         nbParams = params.length;
        for (int i = 0; i < nbParams; i++) {
            json.write(params[i].getClass().getName());
            if (i < nbParams - 1) {
                json.addSeparator();
            }
        }
        json.closeTuple();
        return json.toString();
    }


}
