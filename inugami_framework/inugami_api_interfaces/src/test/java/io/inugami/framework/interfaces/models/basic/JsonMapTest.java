package io.inugami.framework.interfaces.models.basic;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class JsonMapTest {
    @Test
    void jsonMap() {
        assertDto(AssertDtoContext.<JsonMap>builder()
                                  .objectClass(JsonMap.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(JsonMap::new)
                                  .fullArgConstructor(this::buildJsonMap)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/models/basic/jsonMap/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/models/basic/jsonMap/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/models/basic/jsonMap/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(JsonMap instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().data(null).build());
        assertThat(instance.toBuilder().data(null).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().data(null).build().hashCode());
    }

    private JsonMap buildJsonMap() {
        Map<String, Json> data = new LinkedHashMap<>();
        data.put("value", Json.builder()
                              .value("value")
                              .build());
        return JsonMap.<String, Json>builder()
                      .data(data)
                      .build()
                      .toBuilder()
                      .build()
                      .cloneObj();
    }
}