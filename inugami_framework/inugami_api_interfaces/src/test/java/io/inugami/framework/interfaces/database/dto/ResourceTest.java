package io.inugami.framework.interfaces.database.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class ResourceTest {
    @Test
    void resource() {
        assertDto(AssertDtoContext.<Resource>builder()
                                  .objectClass(Resource.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(Resource::new)
                                  .fullArgConstructor(this::buildResource)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/database/dto/resource/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/database/dto/resource/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/database/dto/resource/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(Resource instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().target(null).build());
        assertThat(instance.toBuilder().target(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().target(OTHER).build());
        assertThat(instance.toBuilder().target(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().target(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().target(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().path(null).build());
        assertThat(instance.toBuilder().path(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().path(OTHER).build());
        assertThat(instance.toBuilder().path(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().path(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().path(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().gav(null).build());
        assertThat(instance.toBuilder().gav(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().gav(OTHER).build());
        assertThat(instance.toBuilder().gav(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().gav(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().gav(OTHER).build().hashCode());


    }

    private Resource buildResource() {
        return Resource.builder()
                       .target("2940bf84-22ed-4dca-baa8-c10111847c9e")
                       .path("/some/path")
                       .gav("inugami.io:plugin:4.0.0")
                       .includes(List.of(Include.builder()
                                                .include("all")
                                                .build()))
                       .excludes(List.of(Exclude.builder()
                                                .exclude("*.txt")
                                                .build()))
                       .property("date", "2025-11-20")
                       .build()
                       .toBuilder()
                       .build();

    }
}