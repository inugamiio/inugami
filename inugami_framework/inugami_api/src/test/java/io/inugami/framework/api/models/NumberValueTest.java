package io.inugami.framework.api.models;

import io.inugami.framework.api.tools.unit.test.dto.AssertDtoContext;
import io.inugami.framework.interfaces.models.number.NumberValue;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class NumberValueTest {


    public static final double VALUE = 20.0;

    @Test
    void numberValue() {
        assertDto(new AssertDtoContext<NumberValue>().toBuilder()
                                                     .objectClass(NumberValue.class)
                                                     .fullArgConstructorRefPath("io/inugami/framework/api/models/numberValue/model.json")
                                                     .getterRefPath("io/inugami/framework/api/models/numberValue/getter.json")
                                                     .toStringRefPath("io/inugami/framework/api/models/numberValue/toString.txt")
                                                     .cloneFunction(instance -> instance.toBuilder().build())
                                                     .noArgConstructor(NumberValue::new)
                                                     .fullArgConstructor(this::buildDataSet)
                                                     .noEqualsFunction(this::notEquals)
                                                     .checkSetters(false)
                                                     .checkSerialization(false)
                                                     .build());
    }

    void notEquals(final NumberValue instance) {
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashCode());

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().last(VALUE).build());
        assertThat(instance.toBuilder().last(VALUE).build()).isNotEqualTo(instance);
        assertThat(instance.toBuilder().last(VALUE).build().hashCode()).isNotEqualTo(instance.hashCode());

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().current(VALUE).build());
        assertThat(instance.toBuilder().current(VALUE).build()).isNotEqualTo(instance);
        assertThat(instance.toBuilder().current(VALUE).build().hashCode()).isNotEqualTo(instance.hashCode());
    }

    private NumberValue buildDataSet() {
        return NumberValue.builder()
                          .last(1.0)
                          .current(5.0)
                          .build();
    }
}