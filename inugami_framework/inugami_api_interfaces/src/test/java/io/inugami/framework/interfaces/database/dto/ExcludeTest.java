package io.inugami.framework.interfaces.database.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class ExcludeTest {
    @Test
    void exclude() {
        assertDto(AssertDtoContext.<Exclude>builder()
                                  .objectClass(Exclude.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(Exclude::new)
                                  .fullArgConstructor(this::buildExclude)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/database/dto/exclude/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/database/dto/exclude/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/database/dto/exclude/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(Exclude instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().exclude(null).build());
        assertThat(instance.toBuilder().exclude(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().exclude(OTHER).build());
        assertThat(instance.toBuilder().exclude(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().exclude(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().exclude(OTHER).build().hashCode());

    }

    private Exclude buildExclude() {
        return Exclude.builder()
                        .exclude("all")
                        .build()
                        .toBuilder()
                        .build();

    }
}