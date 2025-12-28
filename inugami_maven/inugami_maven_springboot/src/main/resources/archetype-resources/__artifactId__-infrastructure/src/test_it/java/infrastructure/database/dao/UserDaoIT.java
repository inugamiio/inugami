package ${package}.infrastructure.database.dao;

import ${package}.api.domain.user.dto.UserDTO;
import ${package}.api.domain.user.dto.UserDTOSearchRequestDTO;
import ${package}.api.domain.user.dto.UserFilters;
import ${package}.infrastructure.spring.SpringBootIntegrationRunner;
import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.api.LocalDateTimeLineMatcher;
import io.inugami.commons.test.api.UuidLineMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.commons.test.UnitTestHelper.assertTextIntegration;
import static org.assertj.core.api.Assertions.assertThat;

class UserDaoIT extends SpringBootIntegrationRunner {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Autowired
    private UserDao userDao;

    //==================================================================================================================
    // CRUD
    //==================================================================================================================
    @Test
    void crud_nominal() {
        //--------------------------------------------------------------------------------------------------------------
        // CREATE
        //--------------------------------------------------------------------------------------------------------------
        final var user = userDao.create(List.of(buildUserDTO())).stream().findFirst().orElse(null);
        assertText(user,
                   """
                           {
                             "audit" : {
                               "createdBy" : "system",
                               "createdDate" : "2025-12-28T14:55:06.818512023",
                               "version" : 1
                             },
                             "email" : "john.smith@mock.org",
                             "firstName" : "John",
                             "lastName" : "Smith",
                             "uid" : "8cb82298-0dd4-46ab-a427-587147ebd09a"
                           }
                           """,
                   LocalDateTimeLineMatcher.of(3),
                   UuidLineMatcher.of(9));

        //--------------------------------------------------------------------------------------------------------------
        // READ
        //--------------------------------------------------------------------------------------------------------------
        assertTextIntegration(userDao.getById(user.getUid(), false),
                              "infrastructure/database/dao/userDaoIT/getById.nominal.json",
                              LocalDateTimeLineMatcher.of(3),
                              UuidLineMatcher.of(9));
        assertTextIntegration(userDao.getById(user.getUid(), true),
                              "infrastructure/database/dao/userDaoIT/getById.nominal.json",
                              LocalDateTimeLineMatcher.of(3),
                              UuidLineMatcher.of(9));

        assertThat(userDao.contains(List.of(user.getUid()))).isTrue();

        assertTextIntegration(userDao.getByIds(List.of(user.getUid())),
                              "infrastructure/database/dao/userDaoIT/getByIds.nominal.json",
                              LocalDateTimeLineMatcher.of(3),
                              UuidLineMatcher.of(9));

        assertTextIntegration(userDao.getByIds(List.of(user.getUid())),
                              "infrastructure/database/dao/userDaoIT/getByIds.nominal.json",
                              LocalDateTimeLineMatcher.of(3),
                              UuidLineMatcher.of(9));

        assertTextIntegration(userDao.search(UserDTOSearchRequestDTO.builder().build(), UserFilters.FILTERS),
                              "infrastructure/database/dao/userDaoIT/search.nominal.json",
                              LocalDateTimeLineMatcher.of(4),
                              UuidLineMatcher.of(10));

        assertTextIntegration(userDao.search(UserDTOSearchRequestDTO.builder()
                                                                    .firstName("Jo")
                                                                    .lastName("Sm")
                                                                    .email("john.smith@mock.org")
                                                                    .build(), UserFilters.FILTERS),
                              "infrastructure/database/dao/userDaoIT/search.nominal.json",
                              LocalDateTimeLineMatcher.of(4),
                              UuidLineMatcher.of(10));
        //--------------------------------------------------------------------------------------------------------------
        // UPDATE
        //--------------------------------------------------------------------------------------------------------------
        userDao.update(List.of(user.toBuilder()
                                   .firstName(UnitTestData.USER_1.getFirstName())
                                   .lastName(UnitTestData.USER_1.getLastName())
                                   .email(UnitTestData.USER_1.getEmail())
                                   .build()));

        assertTextIntegration(userDao.getById(user.getUid(), false),
                              "infrastructure/database/dao/userDaoIT/update.nominal.json",
                              LocalDateTimeLineMatcher.of(3, 5),
                              UuidLineMatcher.of(11));
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    private UserDTO buildUserDTO() {
        return UserDTO.builder()
                      .firstName(UnitTestData.FIRSTNAME)
                      .lastName(UnitTestData.LASTNAME)
                      .email(UnitTestData.EMAIL)
                      .build();
    }
}