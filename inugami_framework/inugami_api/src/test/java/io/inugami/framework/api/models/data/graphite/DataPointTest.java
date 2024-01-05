package io.inugami.framework.api.models.data.graphite;

import io.inugami.framework.api.tools.unit.test.dto.AssertDtoContext;
import io.inugami.framework.interfaces.models.number.DataPoint;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class DataPointTest {
    @Test
    void dataPoint() {
        assertDto(new AssertDtoContext<DataPoint>().toBuilder()
                                                   .objectClass(DataPoint.class)
                                                   .fullArgConstructorRefPath("io/inugami/framework/api/models/data/graphite/dataPoint/model.json")
                                                   .getterRefPath("io/inugami/framework/api/models/data/graphite/dataPoint/getter.json")
                                                   .toStringRefPath("io/inugami/framework/api/models/data/graphite/dataPoint/toString.txt")
                                                   .cloneFunction(instance -> instance.toBuilder().build())
                                                   .noArgConstructor(DataPoint::new)
                                                   .fullArgConstructor(this::buildDataSet)
                                                   .noEqualsFunction(this::notEquals)
                                                   .checkSetters(false)
                                                   .checkSerialization(false)
                                                   .build());
    }

    void notEquals(final DataPoint instance) {
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashCode());

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().value(10.0).build());
        assertThat(instance.toBuilder().value(10.0).build()).isNotEqualTo(instance);
        assertThat(instance.toBuilder().value(10.0).build().hashCode()).isNotEqualTo(instance.hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().timestamp(2L).build());
        assertThat(instance.toBuilder().timestamp(2L).build()).isNotEqualTo(instance);
        assertThat(instance.toBuilder().timestamp(2L).build().hashCode()).isNotEqualTo(instance.hashCode());
    }

    private DataPoint buildDataSet() {
        return DataPoint.builder()
                        .value(1.0)
                        .timestamp(1L)
                        .build();
    }
}