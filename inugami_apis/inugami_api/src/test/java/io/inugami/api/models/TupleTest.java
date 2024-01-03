package io.inugami.api.models;

import io.inugami.api.tools.unit.test.UnitTestData;
import io.inugami.api.tools.unit.test.dto.AssertDtoContext;
import io.inugami.interfaces.models.Tuple;
import org.junit.jupiter.api.Test;

import static io.inugami.api.tools.unit.test.UnitTestData.OTHER;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class TupleTest {

    public static final String KEY = "key";

    @Test
    void tuple() {
        assertDto(new AssertDtoContext<Tuple>().toBuilder()
                                               .objectClass(Tuple.class)
                                               .fullArgConstructorRefPath("api/models/tuple/model.json")
                                               .getterRefPath("api/models/tuple/getter.json")
                                               .toStringRefPath("api/models/tuple/toString.txt")
                                               .cloneFunction(instance -> instance.toBuilder().build())
                                               .noArgConstructor(Tuple::new)
                                               .fullArgConstructor(this::buildDataSet)
                                               .noEqualsFunction(this::notEquals)
                                               .checkSetters(false)
                                               .checkSerialization(false)
                                               .build());
    }

    void notEquals(final Tuple<String, String> instance) {
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashCode());

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().key(null).build());
        assertThat(instance.toBuilder().key(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().key(OTHER).build());
        assertThat(instance.toBuilder().key(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().key(null).build().hashCode());
        assertThat(instance.toBuilder().key(OTHER).build().hashCode()).isNotEqualTo(instance.hashCode());
    }

    private Tuple<String, String> buildDataSet() {
        return new Tuple<String, String>().toBuilder()
                                          .key(KEY)
                                          .value(UnitTestData.LOREM_IPSUM)
                                          .build();
    }
}