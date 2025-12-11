package io.inugami.framework.interfaces.dependency.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class HtmlAttributeTest {
    @Test
    void htmlAttribute() {
        assertDto(AssertDtoContext.<HtmlAttribute>builder()
                                  .objectClass(HtmlAttribute.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(HtmlAttribute::new)
                                  .fullArgConstructor(this::buildIHtmlAttribute)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/dependency/dto/htmlAttribute/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/dependency/dto/htmlAttribute/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/dependency/dto/htmlAttribute/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(HtmlAttribute instance) {
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
        assertThat(instance).isNotEqualTo(instance.toBuilder().value(null).build());
        assertThat(instance.toBuilder().value(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().value(OTHER).build());
        assertThat(instance.toBuilder().value(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().value(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().value(OTHER).build().hashCode());
    }

    private HtmlAttribute buildIHtmlAttribute() {
        return HtmlAttribute.builder()
                            .name("src")
                            .value("script.js")
                            .build()
                            .toBuilder()
                            .build();

    }
}