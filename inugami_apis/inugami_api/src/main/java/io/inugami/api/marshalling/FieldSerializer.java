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
package io.inugami.api.marshalling;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.inugami.api.models.JsonBuilder;

import java.io.IOException;
import java.lang.reflect.Field;

public class FieldSerializer extends StdSerializer<Field> {

    protected FieldSerializer(final Class<Field> t) {
        super(t);
    }

    @Override
    public void serialize(final Field field,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        if (field == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeString(renderAsJson(field));
        }
    }

    private static String renderAsJson(final Field field) {
        final JsonBuilder json = new JsonBuilder();
        json.write(field.getDeclaringClass().getName())
            .dot()
            .write(field.getName())
            .openTuple()
            .write(field.getType().getName())
            .closeTuple();

        return json.toString();
    }
}
