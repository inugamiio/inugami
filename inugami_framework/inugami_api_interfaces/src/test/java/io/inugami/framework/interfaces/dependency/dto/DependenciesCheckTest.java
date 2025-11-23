package io.inugami.framework.interfaces.dependency.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class DependenciesCheckTest {
    @Test
    void dependenciesCheck() {
        assertDto(AssertDtoContext.<DependenciesCheck>builder()
                                  .objectClass(DependenciesCheck.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(DependenciesCheck::new)
                                  .fullArgConstructor(this::buildDependenciesCheck)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/dependency/dto/dependenciesCheck/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/dependency/dto/dependenciesCheck/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/dependency/dto/dependenciesCheck/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(DependenciesCheck instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        final var other = DependencyRule.builder()
                                        .groupId("inugami.io")
                                        .artifactId("plugin-other")
                                        .build();

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().deprecated(null).build());
        assertThat(instance).isNotEqualTo(instance.toBuilder().deprecated(List.of()).build());
        assertThat(instance.toBuilder().deprecated(null).build()).isNotEqualTo(instance);
        assertThat(instance.toBuilder().deprecated(List.of()).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().deprecated(List.of(other)).build());
        assertThat(instance.toBuilder().deprecated(List.of(other)).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().deprecated(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().deprecated(List.of()).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .deprecated(List.of(other))
                                                             .build()
                                                             .hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().ban(null).build());
        assertThat(instance).isNotEqualTo(instance.toBuilder().ban(List.of()).build());
        assertThat(instance.toBuilder().ban(null).build()).isNotEqualTo(instance);
        assertThat(instance.toBuilder().ban(List.of()).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().ban(List.of(other)).build());
        assertThat(instance.toBuilder().ban(List.of(other)).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().ban(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().ban(List.of()).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .ban(List.of(other))
                                                             .build()
                                                             .hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().securityIssue(null).build());
        assertThat(instance).isNotEqualTo(instance.toBuilder().securityIssue(List.of()).build());
        assertThat(instance.toBuilder().securityIssue(null).build()).isNotEqualTo(instance);
        assertThat(instance.toBuilder().securityIssue(List.of()).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().securityIssue(List.of(other)).build());
        assertThat(instance.toBuilder().securityIssue(List.of(other)).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().securityIssue(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().securityIssue(List.of()).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .securityIssue(List.of(other))
                                                             .build()
                                                             .hashCode());
    }

    private DependenciesCheck buildDependenciesCheck() {
        return DependenciesCheck.builder()
                                .deprecated(List.of(buildDependencyRule()))
                                .ban(List.of(buildDependencyRule()))
                                .securityIssue(List.of(buildDependencyRule()))
                                .build()
                                .toBuilder()
                                .build();

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
                             .build();
    }
}