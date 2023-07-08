package io.inugami.commons.test.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.inugami.commons.test.UnitTestData.OTHER;
import static io.inugami.commons.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class UserDataDTOTest {

    @Test
    void userDataDTO() {
        assertDto(new AssertDtoContext<UserDataDTO>()
                          .toBuilder()
                          .objectClass(UserDataDTO.class)
                          .fullArgConstructorRefPath("test/dto/userDataDTO/model.json")
                          .getterRefPath("test/dto/userDataDTO/getters.json")
                          .toStringRefPath("test/dto/userDataDTO/toString.txt")
                          .cloneFunction(instance -> instance.toBuilder().build())
                          .noArgConstructor(() -> new UserDataDTO())
                          .fullArgConstructor(this::buildDataSet)
                          .noEqualsFunction(this::notEquals)
                          .checkSetters(true)
                          .build());
    }


    void notEquals(final UserDataDTO value) {
        assertThat(value).isNotEqualTo(value.toBuilder());
        assertThat(value.hashCode()).isNotEqualTo(value.toBuilder().hashCode());

        assertThat(value).isNotEqualTo(value.toBuilder().email(null).build());
        assertThat(value.hashCode()).isNotEqualTo(value.toBuilder().email(null).build().hashCode());
        assertThat(value).isNotEqualTo(value.toBuilder().email(OTHER).build());
        assertThat(value.hashCode()).isNotEqualTo(value.toBuilder().email(OTHER).build().hashCode());
    }

    UserDataDTO buildDataSet() {
        return UserDataDTO.builder()
                          .id(3L)
                          .sex(UserDataDTO.Sex.MALE)
                          .firstName("Théodore")
                          .lastName("Lalonde")
                          .email("theodore.lalonde@mock.org")
                          .phoneNumber("0624455065")
                          .old(38)
                          .birthday(LocalDate.of(1985, 7, 10))
                          .socialId("7564971247732")
                          .nationality("CH")
                          .canton("VD")
                          .streetName("du Château")
                          .streetNumber("10")
                          .streetType("Chem.")
                          .zipCode("1033")
                          .city("Cheseaux-sur-Lausanne")
                          .deviceIdentifier("45fa3dd1-714e-4887-b27d-9792b327ad56")
                          .build()
                          .toBuilder()
                          .build();
    }
}