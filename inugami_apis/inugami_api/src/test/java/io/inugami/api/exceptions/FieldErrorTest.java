package io.inugami.api.exceptions;

import io.inugami.api.tools.unit.test.dto.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.api.tools.unit.test.UnitTestData.OTHER;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class FieldErrorTest {

    @Test
    void fieldError() {
        assertDto(new AssertDtoContext<FieldError>().toBuilder()
                                                    .objectClass(FieldError.class)
                                                    .fullArgConstructorRefPath("api/exceptions/fieldError/model.json")
                                                    .getterRefPath("api/exceptions/fieldError/getter.json")
                                                    .toStringRefPath("api/exceptions/fieldError/toString.txt")
                                                    .cloneFunction(instance -> instance.toBuilder().build())
                                                    .noArgConstructor(FieldError::new)
                                                    .fullArgConstructor(this::buildDataSet)
                                                    .noEqualsFunction(this::notEquals)
                                                    .checkSetters(false)
                                                    .checkSerialization(false)
                                                    .build());
    }


    void notEquals(final FieldError instance) {
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashCode());

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().name(null).build());
        assertThat(instance.toBuilder().name(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().name(OTHER).build());
        assertThat(instance.toBuilder().name(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().name(null).build().hashCode());
        assertThat(instance.toBuilder().name(OTHER).build().hashCode()).isNotEqualTo(instance.hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().errorCode(null).build());
        assertThat(instance.toBuilder().errorCode(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().errorCode(OTHER).build());
        assertThat(instance.toBuilder().errorCode(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().errorCode(null).build().hashCode());
        assertThat(instance.toBuilder().errorCode(OTHER).build().hashCode()).isNotEqualTo(instance.hashCode());
    }

    private FieldError buildDataSet() {
        return FieldError.builder()
                         .name("user.id")
                         .errorCode("ERR-1")
                         .message("invalid user id")
                         .messageDetail("user id XXXX is invalid")
                         .errorType("functional")
                         .errorCategory("GET")
                         .build();
    }
}