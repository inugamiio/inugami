package ${package}.interfaces.api.domain.user.dto;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.dto.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;
class UserAPITest {
    @Test
    void userAPI() {
        assertDto(AssertDtoContext.<UserAPI>builder()
                                  .objectClass(UserAPI.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(UserAPI::new)
                                  .fullArgConstructor(this::buildUserDTO)
                                  .fullArgConstructorRefPath("interfaces/api/domain/user/dto/userAPI/model.json")
                                  .getterRefPath("interfaces/api/domain/user/dto/userAPI/getter.json")
                                  .toStringRefPath("interfaces/api/domain/user/dto/userAPI/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .checkSetters(true)
                                  .build());
    }

    private void notEquals(final UserAPI instance) {
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

    private UserAPI buildUserDTO() {
        return UserAPI.builder()
                      .uid(UnitTestData.UID)
                      .firstName(UnitTestData.FIRSTNAME)
                      .lastName(UnitTestData.LASTNAME)
                      .email(UnitTestData.EMAIL)
                      .build()
                      .toBuilder()
                      .build();
    }
}