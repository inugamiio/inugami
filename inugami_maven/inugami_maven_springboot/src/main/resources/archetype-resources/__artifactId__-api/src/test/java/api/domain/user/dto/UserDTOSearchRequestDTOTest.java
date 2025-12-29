package ${package}.api.domain.user.dto;


import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.dto.AssertDtoContext;
import io.inugami.framework.interfaces.models.search.SortOrder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertDto;

class UserDTOSearchRequestDTOTest {
    @Test
    void userDTOSearchRequestDTO() {
        assertDto(AssertDtoContext.<UserDTOSearchRequestDTO>builder()
                                  .objectClass(UserDTOSearchRequestDTO.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(UserDTOSearchRequestDTO::new)
                                  .fullArgConstructor(this::buildUserDTOSearchRequestDTO)
                                  .fullArgConstructorRefPath("api/domain/user/dto/userDTOSearchRequestDTO/model.json")
                                  .getterRefPath("api/domain/user/dto/userDTOSearchRequestDTO/getter.json")
                                  .toStringRefPath("api/domain/user/dto/userDTOSearchRequestDTO/toString.txt")
                                  .checkEquals(false)
                                  .checkSetters(true)
                                  .build());
    }


    private UserDTOSearchRequestDTO buildUserDTOSearchRequestDTO() {
        return UserDTOSearchRequestDTO.builder()
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