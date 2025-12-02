package io.inugami.framework.interfaces.models.basic;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class StringJsonTest {
    @Test
    void stringJson() {
        assertDto(AssertDtoContext.<StringJson>builder()
                                  .objectClass(StringJson.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(StringJson::new)
                                  .fullArgConstructor(this::buildStringJson)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/models/basic/stringJson/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/models/basic/stringJson/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/models/basic/stringJson/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(StringJson instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().value(null).build());
        assertThat(instance.toBuilder().value(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().value(OTHER).build());
        assertThat(instance.toBuilder().value(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().value(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().value(OTHER).build().hashCode());

    }

    private StringJson buildStringJson() {
        return StringJson.builder()
                              .value("value")
                              .build()
                              .toBuilder()
                              .build();
    }
}