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
package io.inugami.framework.interfaces.testing.commons.marshaller;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.inugami.framework.interfaces.marshalling.JacksonMarshallerSpi;
import io.inugami.framework.interfaces.marshalling.serializers.ClassSerializer;
import io.inugami.framework.interfaces.marshalling.serializers.FieldSerializer;
import io.inugami.framework.interfaces.marshalling.serializers.MethodSerializer;
import io.inugami.framework.interfaces.spi.SpiLoader;
import lombok.Getter;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

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
        final JacksonMarshallerSpi builder = SpiLoader.getInstance()
                                                      .loadSpiServiceByPriority(JacksonMarshallerSpi.class,
                                                                                new DefaultObjectMapperBuilder());

        indentedObjectMapper = builder.buildIndentedObjectMapper();
    }


    // =========================================================================
    // API
    // =========================================================================
    private static class DefaultObjectMapperBuilder implements JacksonMarshallerSpi {

        @Override
        public ObjectMapper buildObjectMapper() {
            return JsonMapper.builder()
                             .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                             .disable(MapperFeature.SORT_CREATOR_PROPERTIES_FIRST)
                             .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                             .changeDefaultPropertyInclusion(include -> include.withValueInclusion(JsonInclude.Include.NON_NULL)
                                                                               .withContentInclusion(JsonInclude.Include.NON_NULL)) // <-- Ajout du content inclusion
                             .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                             .addModule(INUGAMI_MODULE)
                             .build();
        }

        @Override
        public ObjectMapper buildIndentedObjectMapper() {
            return JsonMapper.builder()
                             .enable(SerializationFeature.INDENT_OUTPUT)
                             .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                             .disable(MapperFeature.SORT_CREATOR_PROPERTIES_FIRST)
                             .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                             .changeDefaultPropertyInclusion(include -> include.withValueInclusion(JsonInclude.Include.NON_NULL)
                                                                               .withContentInclusion(JsonInclude.Include.NON_NULL)) // <-- Ajout du content inclusion
                             .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                             .addModule(INUGAMI_MODULE)
                             .build();
        }
    }
}