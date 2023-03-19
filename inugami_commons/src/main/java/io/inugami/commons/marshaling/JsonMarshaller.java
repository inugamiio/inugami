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
package io.inugami.commons.marshaling;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.inugami.api.spi.SpiLoader;
import lombok.Getter;

@Getter
public final class JsonMarshaller  {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ObjectMapper defaultObjectMapper;
    private final ObjectMapper indentedObjectMapper;

    private static final JsonMarshaller INSTANCE = new JsonMarshaller();
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public static JsonMarshaller getInstance(){
        return INSTANCE;
    }
    private JsonMarshaller(){
        final JsonMarshallerSpi defaultBuilder = new DefaultObjectMapperBuilder();
        final JsonMarshallerSpi builder = SpiLoader.getInstance().loadSpiServiceByPriority(JsonMarshallerSpi.class,
                                                                                defaultBuilder);

        ObjectMapper mapper = builder.buildObjectMapper();
        if (mapper == null){
            mapper = defaultBuilder.buildObjectMapper();
        }
        defaultObjectMapper= mapper;

        ObjectMapper indentedMapper = builder.buildIndentedObjectMapper();
        if (indentedMapper == null){
            indentedMapper = defaultBuilder.buildIndentedObjectMapper();
        }
        indentedObjectMapper= indentedMapper;
    }



    // =========================================================================
    // API
    // =========================================================================
    private static class DefaultObjectMapperBuilder implements JsonMarshallerSpi {

        @Override
        public ObjectMapper buildObjectMapper() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

            objectMapper.registerModule(new ParameterNamesModule())
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule());
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return objectMapper;
        }

        @Override
        public ObjectMapper buildIndentedObjectMapper() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.findAndRegisterModules();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
                        .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

            objectMapper.registerModule(new ParameterNamesModule())
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule());
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return objectMapper;
        }
    }
}
