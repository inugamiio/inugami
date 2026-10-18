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
package io.inugami.framework.api.marshalling;


import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

@SuppressWarnings({"java:S3740"})
public class ClassSerializer extends StdSerializer<Class> {

    protected ClassSerializer(final Class<Class> t) {
        super(t);
    }


    @Override
    public void serialize(final Class value,
                          final JsonGenerator gen,
                          final SerializationContext ctxt) throws JacksonException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.getName());
        }
    }
}
