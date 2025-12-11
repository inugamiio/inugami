package io.inugami.framework.interfaces.database.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class IncludeTest {
    @Test
    void include() {
        assertDto(AssertDtoContext.<Include>builder()
                                  .objectClass(Include.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(Include::new)
                                  .fullArgConstructor(this::buildInclude)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/database/dto/include/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/database/dto/include/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/database/dto/include/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(Include instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().include(null).build());
        assertThat(instance.toBuilder().include(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().include(OTHER).build());
        assertThat(instance.toBuilder().include(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().include(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().include(OTHER).build().hashCode());

    }

    private Include buildInclude() {
        return Include.builder()
                      .include("all")
                      .build()
                      .toBuilder()
                      .build();

    }
}