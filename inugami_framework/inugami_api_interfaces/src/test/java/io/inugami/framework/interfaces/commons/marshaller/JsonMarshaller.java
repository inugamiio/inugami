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
package io.inugami.framework.interfaces.commons.marshaller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.inugami.framework.interfaces.spi.SpiLoader;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings({"java:S1874"})
@Getter
public class JsonMarshaller {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final SimpleModule INUGAMI_MODULE = initInugamiModule();
    private final        ObjectMapper indentedObjectMapper;

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
        final JsonMarshallerSpi builder = SpiLoader.getInstance().loadSpiServiceByPriority(JsonMarshallerSpi.class,
                                                                                           new DefaultObjectMapperBuilder());
        
        indentedObjectMapper = builder.buildIndentedObjectMapper();
    }


    // =========================================================================
    // API
    // =========================================================================
    private static class DefaultObjectMapperBuilder implements JsonMarshallerSpi {

        @Override
        public ObjectMapper buildObjectMapper() {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);


            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(INUGAMI_MODULE);

            return objectMapper;
        }

        @Override
        public ObjectMapper buildIndentedObjectMapper() {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.findAndRegisterModules();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
                        .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);


            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(INUGAMI_MODULE);

            return objectMapper;
        }
    }
}

