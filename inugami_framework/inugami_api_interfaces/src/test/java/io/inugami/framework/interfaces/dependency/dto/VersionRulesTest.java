package io.inugami.framework.interfaces.dependency.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class VersionRulesTest {
    @Test
    void versionRules() {
        assertDto(AssertDtoContext.<VersionRules>builder()
                                  .objectClass(VersionRules.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(VersionRules::new)
                                  .fullArgConstructor(this::buildVersionRules)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/dependency/dto/versionRules/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/dependency/dto/versionRules/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/dependency/dto/versionRules/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(VersionRules instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        final var other = Rule.builder().build();

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().major(null).build());
        assertThat(instance.toBuilder().major(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().major(other).build());
        assertThat(instance.toBuilder().major(other).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().major(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().major(other).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().minor(null).build());
        assertThat(instance.toBuilder().minor(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().minor(other).build());
        assertThat(instance.toBuilder().minor(other).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().minor(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().minor(other).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().patch(null).build());
        assertThat(instance.toBuilder().patch(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().patch(other).build());
        assertThat(instance.toBuilder().patch(other).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().patch(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().patch(other).build().hashCode());

    }

    private VersionRules buildVersionRules() {
        return VersionRules.builder()
                           .major(Rule.builder()
                                      .version(4)
                                      .ruleType(RuleType.HIGHER_EQUALS)
                                      .build())
                           .minor(Rule.builder()
                                      .version(1)
                                      .ruleType(RuleType.HIGHER_EQUALS)
                                      .build())
                           .patch(Rule.builder()
                                      .version(0)
                                      .ruleType(RuleType.HIGHER_EQUALS)
                                      .build())
                           .build()
                           .toBuilder()
                           .build();

    }
}