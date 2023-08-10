package io.inugami.api.tools;

import io.inugami.api.tools.unit.test.dto.AssertDtoContext;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static io.inugami.api.tools.unit.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"java:S5838"})
class FieldGetterSetterTest {

    @Test
    void fieldGetterSetter() {
        assertDto(new AssertDtoContext<FieldGetterSetter>().toBuilder()
                                                           .objectClass(FieldGetterSetter.class)
                                                           .fullArgConstructorRefPath("api/tools/fieldGetterSetterTest/model.json")
                                                           .toStringRefPath("api/tools/fieldGetterSetterTest/toStringRefPath.txt")
                                                           .cloneFunction(instance -> instance.toBuilder().build())
                                                           .noArgConstructor(() -> new FieldGetterSetter())
                                                           .fullArgConstructor(this::buildDataSet)
                                                           .noEqualsFunction(this::notEquals)
                                                           .checkSetters(false)
                                                           .checkSerialization(false)
                                                           .build());
    }

    void notEquals(final FieldGetterSetter instance) {
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashCode());

        Field  otherField  = ReflectionUtils.getField("email", SomeDTO.class);
        Method otherMethod = ReflectionUtils.searchMethodByName(SomeDTO.class, "setEmail");


        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().field(null).build());
        assertThat(instance.toBuilder().field(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().field(otherField).build());
        assertThat(instance.toBuilder().field(otherField).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().field(null).build().hashCode());
        assertThat(instance.toBuilder().field(otherField).build().hashCode()).isNotEqualTo(instance.hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().getter(null).build());
        assertThat(instance.toBuilder().getter(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().getter(otherMethod).build());
        assertThat(instance.toBuilder().getter(otherMethod).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().getter(null).build().hashCode());
        assertThat(instance.toBuilder().getter(otherMethod).build().hashCode()).isNotEqualTo(instance.hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().setter(null).build());
        assertThat(instance.toBuilder().setter(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().setter(otherMethod).build());
        assertThat(instance.toBuilder().setter(otherMethod).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().setter(null).build().hashCode());
        assertThat(instance.toBuilder().setter(otherMethod).build().hashCode()).isNotEqualTo(instance.hashCode());
    }

    private FieldGetterSetter buildDataSet() {
        return FieldGetterSetter.builder()
                                .field(ReflectionUtils.getField("name", SomeDTO.class))
                                .value("Joe")
                                .getter(ReflectionUtils.searchMethodByName(SomeDTO.class, "getName"))
                                .setter(ReflectionUtils.searchMethodByName(SomeDTO.class, "setName"))
                                .build();
    }


    @Test
    void compareTo_nominal() {
        assertThat(buildDataSet().toBuilder().field(null).build().compareTo(buildDataSet())).isEqualTo(-1);
        assertThat(buildDataSet().compareTo(null)).isEqualTo(1);
        assertThat(buildDataSet().compareTo(buildDataSet())).isEqualTo(0);
        assertThat(buildDataSet().compareTo(buildDataSet().toBuilder().field(null).build())).isEqualTo(1);
    }


    @Setter
    @Getter
    private static class SomeDTO {
        private String name;
        private String email;
    }


}