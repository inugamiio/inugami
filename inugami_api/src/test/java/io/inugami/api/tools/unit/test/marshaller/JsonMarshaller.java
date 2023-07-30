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
package io.inugami.api.tools.unit.test.marshaller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
public class JsonMarshaller {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final SimpleModule INUGAMI_MODULE = initInugamiModule();


    private final ObjectMapper indentedObjectMapper;
    private final ObjectMapper defaultObjectMapper;

    private static final JsonMarshaller INSTANCE = new JsonMarshaller();


    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private static SimpleModule initInugamiModule() {
        final SimpleModule module = new SimpleModule();
        module.addSerializer(Method.class, new MethodSerializer(Method.class));
        module.addSerializer(Class.class, new ClassSerializer(Class.class));
        module.addSerializer(Field.class, new FieldSerializer(Field.class));
        return module;
    }

    public static JsonMarshaller getInstance() {
        return INSTANCE;
    }

    private JsonMarshaller() {
        defaultObjectMapper = new ObjectMapper();
        defaultObjectMapper.findAndRegisterModules();
        defaultObjectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

        defaultObjectMapper.registerModule(new ParameterNamesModule())
                           .registerModule(new Jdk8Module())
                           .registerModule(new JavaTimeModule());
        defaultObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        defaultObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        defaultObjectMapper.registerModule(INUGAMI_MODULE);


        indentedObjectMapper = new ObjectMapper();
        indentedObjectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        indentedObjectMapper.findAndRegisterModules();
        indentedObjectMapper.enable(SerializationFeature.INDENT_OUTPUT)
                            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

        indentedObjectMapper.registerModule(new ParameterNamesModule())
                            .registerModule(new Jdk8Module())
                            .registerModule(new JavaTimeModule());
        indentedObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        indentedObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        indentedObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        indentedObjectMapper.registerModule(INUGAMI_MODULE);
    }


}

