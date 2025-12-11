package io.inugami.framework.interfaces.dependency.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class RuleTest {
    @Test
    void rule() {
        assertDto(AssertDtoContext.<Rule>builder()
                                  .objectClass(Rule.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(Rule::new)
                                  .fullArgConstructor(this::buildRule)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/dependency/dto/rule/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/dependency/dto/rule/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/dependency/dto/rule/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(Rule instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        final var other = Rule.builder().build();

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().version(1).build());
        assertThat(instance.toBuilder().version(1).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().version(1).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().ruleType(null).build());
        assertThat(instance.toBuilder().ruleType(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().ruleType(RuleType.LESS_EQUALS).build());
        assertThat(instance.toBuilder().ruleType(RuleType.LESS_EQUALS).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().ruleType(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .ruleType(RuleType.LESS_EQUALS)
                                                             .build()
                                                             .hashCode());

    }

    private Rule buildRule() {
        return Rule.builder()
                   .version(4)
                   .ruleType(RuleType.HIGHER_EQUALS)
                   .build()
                   .toBuilder()
                   .build();

    }
}