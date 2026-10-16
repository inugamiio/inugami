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
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.Warning;
import io.inugami.framework.interfaces.marshalling.JacksonMarshallerSpi;
import io.inugami.framework.interfaces.marshalling.ModuleRegisterSpi;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.spi.SpiLoader;
import lombok.Getter;
import tools.jackson.databind.*;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"java:S1874"})
@Getter
public class JsonMarshaller {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final SimpleModule        INUGAMI_MODULE   = initInugamiModule();
    private static final List<JacksonModule> EXTERNAL_MODULES = loadExternalModules();

    private static List<JacksonModule> loadExternalModules() {
        final List<ModuleRegisterSpi> moduleLoaders = SpiLoader.getInstance().loadSpiService(ModuleRegisterSpi.class);

        return moduleLoaders.stream()
                            .map(ModuleRegisterSpi::extractModules)
                            .filter(Objects::nonNull)
                            .flatMap(List::stream)
                            .toList();
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
        module.addSerializer(GenericEvent.class, new GenericEventSerializer(GenericEvent.class));
        module.addSerializer(ErrorCode.class, new ErrorCodeSerializer(ErrorCode.class));
        module.addSerializer(Throwable.class, new ThrowableSerializer(Throwable.class));
        module.addDeserializer(ErrorCode.class, new ErrorCodeDeserializer(ErrorCode.class));
        module.addDeserializer(GenericEvent.class, new GenericEventDeserializer(GenericEvent.class));
        module.addSerializer(Warning.class, new WarningSerializer(Warning.class));
        module.addDeserializer(Warning.class, new WarningDeserializer(Warning.class));

        return module;
    }

    public static JsonMarshaller getInstance() {
        return INSTANCE;
    }

    private JsonMarshaller() {
        final JacksonMarshallerSpi defaultBuilder = new DefaultObjectMapperBuilder();
        final JacksonMarshallerSpi builder = SpiLoader.getInstance()
                                                      .loadSpiServiceByPriority(JacksonMarshallerSpi.class,
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
    private static class DefaultObjectMapperBuilder implements JacksonMarshallerSpi {

        @Override
        public ObjectMapper buildObjectMapper() {
            var builder = JsonMapper.builder()
                                    .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                                    .disable(MapperFeature.SORT_CREATOR_PROPERTIES_FIRST)
                                    .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                                    .disable(tools.jackson.databind.cfg.EnumFeature.WRITE_ENUMS_USING_TO_STRING)
                                    .disable(tools.jackson.databind.cfg.EnumFeature.READ_ENUMS_USING_TO_STRING)
                                    .changeDefaultPropertyInclusion(include -> include.withValueInclusion(JsonInclude.Include.NON_NULL)
                                                                                      .withContentInclusion(JsonInclude.Include.NON_NULL))
                                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                    .addModule(INUGAMI_MODULE);

            EXTERNAL_MODULES.forEach(builder::addModule);
            return builder.build();
        }

        @Override
        public ObjectMapper buildIndentedObjectMapper() {
            var builder = JsonMapper.builder()
                                    .enable(SerializationFeature.INDENT_OUTPUT)
                                    .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                                    .disable(tools.jackson.databind.cfg.EnumFeature.WRITE_ENUMS_USING_TO_STRING)
                                    .disable(tools.jackson.databind.cfg.EnumFeature.READ_ENUMS_USING_TO_STRING)
                                    .disable(MapperFeature.SORT_CREATOR_PROPERTIES_FIRST)
                                    .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                                    .changeDefaultPropertyInclusion(include -> include.withValueInclusion(JsonInclude.Include.NON_NULL)
                                                                                      .withContentInclusion(JsonInclude.Include.NON_NULL))
                                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                    .addModule(INUGAMI_MODULE);

            EXTERNAL_MODULES.forEach(builder::addModule);
            return builder.build();
        }
    }
}