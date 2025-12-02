package io.inugami.framework.interfaces.models.basic;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class JsonStringArrayTest {
    @Test
    void jsonStringArray() {
        assertDto(AssertDtoContext.<JsonStringArray>builder()
                                  .objectClass(JsonStringArray.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(JsonStringArray::new)
                                  .fullArgConstructor(this::buildJsonStringArray)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/models/basic/jsonStringArray/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/models/basic/jsonStringArray/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/models/basic/jsonStringArray/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(JsonStringArray instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        String[] other = {OTHER};
        assertThat(instance).isNotEqualTo(instance.toBuilder().data(null).build());
        assertThat(instance.toBuilder().data(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().data(other).build());
        assertThat(instance.toBuilder().data(other).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().data(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().data(other).build().hashCode());

    }

    private JsonStringArray buildJsonStringArray() {
        return JsonStringArray.builder()
                              .data(new String[]{"value"})
                              .build()
                              .toBuilder()
                              .build();
    }
}