package ${package}.api.domain.user.dto;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.dto.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class UserDTOTest {

    @Test
    void userDTO() {
        assertDto(AssertDtoContext.<UserDTO>builder()
                                  .objectClass(UserDTO.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(UserDTO::new)
                                  .fullArgConstructor(this::buildUserDTO)
                                  .fullArgConstructorRefPath("api/domain/user/dto/userDTO/model.json")
                                  .getterRefPath("api/domain/user/dto/userDTO/getter.json")
                                  .toStringRefPath("api/domain/user/dto/userDTO/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .checkSetters(true)
                                  .build());
    }

    private void notEquals(final UserDTO instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().email(null).build());
        assertThat(instance.toBuilder().email(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().email(UnitTestData.OTHER).build());
        assertThat(instance.toBuilder().email(UnitTestData.OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().email(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .email(UnitTestData.OTHER)
                                                             .build()
                                                             .hashCode());
    }

    private UserDTO buildUserDTO() {
        return UserDTO.builder()
                      .uid(UnitTestData.UID)
                      .firstName(UnitTestData.FIRSTNAME)
                      .lastName(UnitTestData.LASTNAME)
                      .email(UnitTestData.EMAIL)
                      .build()
                      .toBuilder()
                      .build();
    }

}