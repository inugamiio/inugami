package io.inugami.framework.interfaces.models.basic;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class JsonTest {

    @Test
    void json() {
        assertDto(AssertDtoContext.<Json>builder()
                                  .objectClass(Json.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(Json::new)
                                  .fullArgConstructor(this::buildJson)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/models/basic/json/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/models/basic/json/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/models/basic/json/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(Json instance) {
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

    private Json buildJson() {
        return Json.builder()
                   .value("value")
                   .build()
                   .toBuilder()
                   .build();
    }
}