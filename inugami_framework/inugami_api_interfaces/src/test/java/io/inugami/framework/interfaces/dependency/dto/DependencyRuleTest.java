package io.inugami.framework.interfaces.dependency.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class DependencyRuleTest {
    @Test
    void dependencyRule() {
        assertDto(AssertDtoContext.<DependencyRule>builder()
                                  .objectClass(DependencyRule.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(DependencyRule::new)
                                  .fullArgConstructor(this::buildDependencyRule)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/dependency/dto/dependencyRule/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/dependency/dto/dependencyRule/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/dependency/dto/dependencyRule/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(DependencyRule instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().groupId(null).build());
        assertThat(instance.toBuilder().groupId(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().groupId(OTHER).build());
        assertThat(instance.toBuilder().groupId(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().groupId(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().groupId(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().artifactId(null).build());
        assertThat(instance.toBuilder().artifactId(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().artifactId(OTHER).build());
        assertThat(instance.toBuilder().artifactId(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().artifactId(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().artifactId(OTHER).build().hashCode());
        //
        final var other = VersionRules.builder().build();
        assertThat(instance).isNotEqualTo(instance.toBuilder().rules(null).build());
        assertThat(instance.toBuilder().rules(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().rules(other).build());
        assertThat(instance.toBuilder().rules(other).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().rules(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().rules(other).build().hashCode());

    }

    private DependencyRule buildDependencyRule() {
        return DependencyRule.builder()
                             .groupId("inugami.io")
                             .artifactId("plugin")
                             .rules(VersionRules.builder()
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
                                                .build())
                             .comment("lorem ipsum")
                             .link("http://mock.url")
                             .level(Level.medium)
                             .build()
                             .toBuilder()
                             .build();

    }
}