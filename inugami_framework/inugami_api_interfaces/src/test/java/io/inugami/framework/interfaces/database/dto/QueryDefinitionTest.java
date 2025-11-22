package io.inugami.framework.interfaces.database.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class QueryDefinitionTest {
    @Test
    void queryDefinition() {
        assertDto(AssertDtoContext.<QueryDefinition>builder()
                                  .objectClass(QueryDefinition.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(QueryDefinition::new)
                                  .fullArgConstructor(this::buildQueryDefinition)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/database/dto/queryDefinition/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/database/dto/queryDefinition/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/database/dto/queryDefinition/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(QueryDefinition instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().name(null).build());
        assertThat(instance.toBuilder().name(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().name(OTHER).build());
        assertThat(instance.toBuilder().name(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().name(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().name(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().type(null).build());
        assertThat(instance.toBuilder().type(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().type(OTHER).build());
        assertThat(instance.toBuilder().type(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().type(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().type(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().path(null).build());
        assertThat(instance.toBuilder().path(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().path(OTHER).build());
        assertThat(instance.toBuilder().path(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().path(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().path(OTHER).build().hashCode());

    }

    private QueryDefinition buildQueryDefinition() {
        return QueryDefinition.builder()
                              .name("simple")
                              .type("nodes")
                              .path("some/path")
                              .description("lorem ipsum")
                              .parameters(List.of("params"))
                              .build()
                              .toBuilder()
                              .build();
    }
}