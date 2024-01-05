package io.inugami.commons.test;

import io.inugami.commons.test.dto.AssertDtoContext;
import lombok.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UnitTestHelperDtoTest {

    @Test
    void assertDto() {
        UnitTestHelperDto.assertDto(new AssertDtoContext<SimpleDto>()
                                            .toBuilder()
                                            .objectClass(SimpleDto.class)
                                            .cloneFunction(instance -> instance.toBuilder().build())
                                            .noArgConstructor(() -> new SimpleDto())
                                            .fullArgConstructor(UnitTestHelperDtoTest::buildData)
                                            .fullArgConstructorRefPath("test/unitTestHelperDtoTest/fullArgConstructorRefPath.json")
                                            .getterRefPath("test/unitTestHelperDtoTest/getters.json")
                                            .toStringRefPath("test/unitTestHelperDtoTest/toString.json")
                                            .noEqualsFunction(UnitTestHelperDtoTest::noEquals)
                                            .equalsFunction(UnitTestHelperDtoTest::testEquals)
                                            .checkSetters(true)
                                            .build());
    }

    private static void testEquals(final SimpleDto instance) {
        assertThat(instance).isEqualTo(instance.toBuilder().build());
    }

    private static void noEquals(final SimpleDto instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        assertThat(instance).isNotEqualTo(instance.toBuilder()
                                                  .id(2L)
                                                  .build());
    }

    private static SimpleDto buildData() {
        return SimpleDto.builder()
                        .id(1L)
                        .name("john")
                        .email("john@inugami.io")
                        .build();
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    private final static class SimpleDto {
        private Long   id;
        private String name;
        private String email;
    }
}