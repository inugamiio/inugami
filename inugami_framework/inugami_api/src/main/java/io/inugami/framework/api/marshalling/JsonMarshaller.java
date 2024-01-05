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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.inugami.framework.interfaces.spi.SpiLoader;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S1874"})
@Getter
public class JsonMarshaller {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final SimpleModule INUGAMI_MODULE   = initInugamiModule();
    private static final List<Module> EXTERNAL_MODULES = loadExternalModules();

    private static List<Module> loadExternalModules() {
        final List<ModuleRegisterSpi> moduleLoaders = SpiLoader.getInstance().loadSpiService(ModuleRegisterSpi.class);


        return moduleLoaders.stream()
                            .map(ModuleRegisterSpi::extractModules)
                            .filter(Objects::nonNull)
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
    }

    private final ObjectMapper defaultObjectMapper;
    private final ObjectMapper indentedObjectMapper;

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
        final JsonMarshallerSpi defaultBuilder = new DefaultObjectMapperBuilder();
        final JsonMarshallerSpi builder = SpiLoader.getInstance().loadSpiServiceByPriority(JsonMarshallerSpi.class,
                                                                                           defaultBuilder);

        ObjectMapper mapper = builder.buildObjectMapper();
        if (mapper == null) {
            mapper = defaultBuilder.buildObjectMapper();
        }
        defaultObjectMapper = mapper;

        ObjectMapper indentedMapper = builder.buildIndentedObjectMapper();
        if (indentedMapper == null) {
            indentedMapper = defaultBuilder.buildIndentedObjectMapper();
        }
        indentedObjectMapper = indentedMapper;
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


            for (Module module : EXTERNAL_MODULES) {
                objectMapper.registerModule(module);
            }

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

            // TODO: refactor
            /*
            objectMapper.registerModule(new ParameterNamesModule())
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule());

             */

            for (Module module : EXTERNAL_MODULES) {
                objectMapper.registerModule(module);
            }

            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(INUGAMI_MODULE);

            return objectMapper;
        }
    }
}

