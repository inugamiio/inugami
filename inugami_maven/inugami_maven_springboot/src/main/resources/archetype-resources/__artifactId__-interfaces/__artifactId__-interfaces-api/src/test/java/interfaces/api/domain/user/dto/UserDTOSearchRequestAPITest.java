package ${package}.interfaces.api.domain.user.dto;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.dto.AssertDtoContext;
import io.inugami.framework.interfaces.models.search.SortOrder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertDto;

class UserDTOSearchRequestAPITest {
    @Test
    void userDTOSearchRequestAPI() {
        assertDto(AssertDtoContext.<UserDTOSearchRequestAPI>builder()
                                  .objectClass(UserDTOSearchRequestAPI.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(UserDTOSearchRequestAPI::new)
                                  .fullArgConstructor(this::buildUserDTOSearchRequestAPI)
                                  .fullArgConstructorRefPath("interfaces/api/domain/user/dto/userDTOSearchRequestAPI/model.json")
                                  .getterRefPath("interfaces/api/domain/user/dto/userDTOSearchRequestAPI/getter.json")
                                  .toStringRefPath("interfaces/api/domain/user/dto/userDTOSearchRequestAPI/toString.txt")
                                  .checkEquals(false)
                                  .checkSetters(true)
                                  .build());
    }


    private UserDTOSearchRequestAPI buildUserDTOSearchRequestAPI() {
        return UserDTOSearchRequestAPI.builder()
                                      .page(0)
                                      .pageSize(10)
                                      .sortFields("name")
                                      .sortOrder(SortOrder.ASC)
                                      .createdBy("system")
                                      .clearCreatedBy()
                                      .createdBy(List.of("system"))
                                      .createdDate(UnitTestData.DATE_TIME.minusDays(5))
                                      .clearCreatedDate()
                                      .createdDate(List.of(UnitTestData.DATE_TIME.minusDays(5)))
                                      .lastModifiedBy("system2")
                                      .clearLastModifiedBy()
                                      .lastModifiedBy(List.of("system2"))
                                      .lastModifiedDate(UnitTestData.DATE_TIME)
                                      .clearLastModifiedDate()
                                      .lastModifiedDate(List.of(UnitTestData.DATE_TIME))
                                      //------------------------------------------------------------------------------
                                      .uid(UnitTestData.UID)
                                      .clearUid()
                                      .uid(List.of(UnitTestData.UID))

                                      .firstName(UnitTestData.FIRSTNAME)
                                      .clearFirstName()
                                      .firstName(List.of(UnitTestData.FIRSTNAME))

                                      .lastName(UnitTestData.LASTNAME)
                                      .clearLastName()
                                      .lastName(List.of(UnitTestData.LASTNAME))

                                      .email(UnitTestData.EMAIL)
                                      .clearEmail()
                                      .email(List.of(UnitTestData.EMAIL))

                                      //------------------------------------------------------------------------------
                                      .build()
                                      .toBuilder()
                                      .build();
    }
}