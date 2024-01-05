package io.inugami.framework.interfaces.exceptions;

import io.inugami.framework.interfaces.commons.AssertDtoContext;
import io.inugami.framework.interfaces.commons.UnitTestData;
import io.inugami.framework.interfaces.commons.UnitTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FieldErrorTest {

    @Test
    void fieldError() {
        UnitTestHelper.assertDto(new AssertDtoContext<FieldError>().toBuilder()
                                                                   .objectClass(FieldError.class)
                                                                   .fullArgConstructorRefPath("io/inugami/framework/interfaces/exceptions/fieldError/model.json")
                                                                   .getterRefPath("io/inugami/framework/interfaces/exceptions/fieldError/getter.json")
                                                                   .toStringRefPath("io/inugami/framework/interfaces/exceptions/fieldError/toString.txt")
                                                                   .cloneFunction(instance -> instance.toBuilder()
                                                                                                      .build())
                                                                   .noArgConstructor(FieldError::new)
                                                                   .fullArgConstructor(this::buildDataSet)
                                                                   .noEqualsFunction(this::notEquals)
                                                                   .checkSetters(false)
                                                                   .checkSerialization(false)
                                                                   .build());
    }


    void notEquals(final FieldError instance) {
        Assertions.assertThat(instance).isNotEqualTo(instance.toBuilder());
        Assertions.assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashCode());

        //
        Assertions.assertThat(instance).isNotEqualTo(instance.toBuilder().name(null).build());
        Assertions.assertThat(instance.toBuilder().name(null).build()).isNotEqualTo(instance);
        Assertions.assertThat(instance).isNotEqualTo(instance.toBuilder().name(UnitTestData.OTHER).build());
        Assertions.assertThat(instance.toBuilder().name(UnitTestData.OTHER).build()).isNotEqualTo(instance);
        Assertions.assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().name(null).build().hashCode());
        Assertions.assertThat(instance.toBuilder().name(UnitTestData.OTHER).build().hashCode())
                  .isNotEqualTo(instance.hashCode());
        //
        Assertions.assertThat(instance).isNotEqualTo(instance.toBuilder().errorCode(null).build());
        Assertions.assertThat(instance.toBuilder().errorCode(null).build()).isNotEqualTo(instance);
        Assertions.assertThat(instance).isNotEqualTo(instance.toBuilder().errorCode(UnitTestData.OTHER).build());
        Assertions.assertThat(instance.toBuilder().errorCode(UnitTestData.OTHER).build()).isNotEqualTo(instance);
        Assertions.assertThat(instance.hashCode())
                  .isNotEqualTo(instance.toBuilder().errorCode(null).build().hashCode());
        Assertions.assertThat(instance.toBuilder().errorCode(UnitTestData.OTHER).build().hashCode())
                  .isNotEqualTo(instance.hashCode());
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