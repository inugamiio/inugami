package io.inugami.api.alertings;

import io.inugami.api.tools.unit.test.dto.AssertDtoContext;
import io.inugami.interfaces.alertings.AlerteLevels;
import io.inugami.interfaces.alertings.DynamicAlertingLevel;
import org.junit.jupiter.api.Test;

import static io.inugami.api.tools.unit.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class DynamicAlertingLevelTest {

    @Test
    void dynamicAlertingLevel() {
        assertDto(new AssertDtoContext<DynamicAlertingLevel>().toBuilder()
                                                              .objectClass(DynamicAlertingLevel.class)
                                                              .fullArgConstructorRefPath("api/alertings/dynamicAlertingLevel/model.json")
                                                              .getterRefPath("api/alertings/dynamicAlertingLevel/getter.json")
                                                              .toStringRefPath("api/alertings/dynamicAlertingLevel/toString.txt")
                                                              .cloneFunction(instance -> instance.toBuilder().build())
                                                              .noArgConstructor(() -> new DynamicAlertingLevel())
                                                              .fullArgConstructor(this::buildDataSet)
                                                              .noEqualsFunction(this::notEquals)
                                                              .checkSetters(false)
                                                              .checkSerialization(false)
                                                              .build());
    }

    void notEquals(final DynamicAlertingLevel instance) {
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashCode());

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().level(null).build());
        assertThat(instance.toBuilder().level(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().level(AlerteLevels.INFO).build());
        assertThat(instance.toBuilder().level(AlerteLevels.INFO).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().level(null).build().hashCode());
        assertThat(instance.toBuilder().level(AlerteLevels.INFO).build().hashCode()).isNotEqualTo(instance.hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().threshold(5.0).build());
        assertThat(instance.toBuilder().threshold(5.0).build()).isNotEqualTo(instance);
        assertThat(instance.toBuilder().threshold(5.0).build().hashCode()).isNotEqualTo(instance.hashCode());
    }

    private DynamicAlertingLevel buildDataSet() {
        return DynamicAlertingLevel.builder()
                                   .name("alert1")
                                   .addLevel(100000.0)
                                   .threshold(10.0)
                                   .activationDelay(5)
                                   .duration(1)
                                   .nominal("1.0")
                                   .unit("HIT")
                                   .service("search")
                                   .component("app")
                                   .inverse(true)
                                   .build()
                                   .toBuilder()
                                   .build();
    }

}