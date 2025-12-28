package ${package}.core.domain.user;

import ${package}.api.domain.user.IUserDao;
import ${package}.api.domain.user.dto.UserDTO;
import io.inugami.commons.test.UnitTestData;
import io.inugami.framework.interfaces.models.basic.AuditDTO;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ${package}.api.domain.user.exception.UserErrors.*;
import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.commons.test.UnitTestHelper.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Mock
    private IUserDao    userDao;
    @InjectMocks
    private UserService userService;

    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    @Test
    void create_nominal() {
        when(userDao.create(any())).thenReturn(List.of(buildUserDTO()));
        assertText(userService.create(List.of(buildUserDTO().toBuilder()
                                                            .uid(null)
                                                            .audit(null)
                                                            .build())),
                   """
                           [ {
                             "audit" : {
                               "createdBy" : "system",
                               "createdDate" : "2023-05-27T12:00:00",
                               "lastModifiedBy" : "user-1",
                               "lastModifiedDate" : "2023-06-01T12:00:00",
                               "version" : 1
                             },
                             "email" : "john.smith@mock.org",
                             "firstName" : "John",
                             "lastName" : "Smith",
                             "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                           } ]
                           """);
    }

    @Test
    void create_withEmptyData() {
        assertThrows(CREATE_INVALID_DATA, () -> userService.create(null));
        assertThrows(CREATE_INVALID_DATA, () -> userService.create(List.of()));
    }

    @Test
    void create_withErrors() {
        assertThrows(CREATE_UID_FORBIDDEN, () -> userService.create(List.of(buildUserDTO())));
        assertThrows(CREATE_FIRSTNAME_INVALID, () -> userService.create(List.of(buildUserDTO().toBuilder()
                                                                                              .uid(null)
                                                                                              .firstName(null)
                                                                                              .build())));

        assertThrows(CREATE_LASTNAME_INVALID, () -> userService.create(List.of(buildUserDTO().toBuilder()
                                                                                             .uid(null)
                                                                                             .lastName(null)
                                                                                             .build())));
        assertThrows(CREATE_EMAIL_INVALID, () -> userService.create(List.of(buildUserDTO().toBuilder()
                                                                                          .uid(null)
                                                                                          .email("bad_email")
                                                                                          .build())));
    }

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Test
    void search_nominal() {
        when(userDao.search(any(), any())).thenReturn(SearchResponse.<UserDTO>builder()
                                                                    .next(false)
                                                                    .previous(false)
                                                                    .nbFoundItems(1L)
                                                                    .totalPages(1)
                                                                    .page(0)
                                                                    .pageSize(10)
                                                                    .data(List.of(buildUserDTO()))
                                                                    .build());
        assertText(userService.search(null),
                   """
                           {
                             "data" : [ {
                               "audit" : {
                                 "createdBy" : "system",
                                 "createdDate" : "2023-05-27T12:00:00",
                                 "lastModifiedBy" : "user-1",
                                 "lastModifiedDate" : "2023-06-01T12:00:00",
                                 "version" : 1
                               },
                               "email" : "john.smith@mock.org",
                               "firstName" : "John",
                               "lastName" : "Smith",
                               "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                             } ],
                             "nbFoundItems" : 1,
                             "next" : false,
                             "page" : 0,
                             "pageSize" : 10,
                             "previous" : false,
                             "totalPages" : 1
                           }
                           """);
    }

    @Test
    void getById_nominal() {
        when(userDao.getById(UnitTestData.UID, true)).thenReturn(buildUserDTO());
        assertText(userService.getById(UnitTestData.UID, true),
                   """
                           {
                             "audit" : {
                               "createdBy" : "system",
                               "createdDate" : "2023-05-27T12:00:00",
                               "lastModifiedBy" : "user-1",
                               "lastModifiedDate" : "2023-06-01T12:00:00",
                               "version" : 1
                             },
                             "email" : "john.smith@mock.org",
                             "firstName" : "John",
                             "lastName" : "Smith",
                             "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                           }
                           """);
    }

    @Test
    void getById_withErrors() {
        assertThrows(READ_USER_UID_REQUIRED, () -> userService.getById(UnitTestData.OTHER, true));
        assertThrows(READ_USER_NOT_FOUND, () -> userService.getById(UnitTestData.UID, true));
    }

    @Test
    void contains_nominal() {
        when(userDao.contains(List.of(UnitTestData.UID))).thenReturn(true);

        assertThat(userService.contains(List.of(UnitTestData.UID))).isTrue();

        assertThat(userService.contains(List.of(UnitTestData.UID))).isTrue();
        assertThat(userService.contains(List.of("b97eb035-8c06-4602-ba44-0b642e460250"))).isFalse();
        assertThat(userService.contains(List.of())).isFalse();
        assertThat(userService.contains(null)).isFalse();
    }

    @Test
    void getByIds_nominal() {
        when(userDao.getByIds(List.of(UnitTestData.UID))).thenReturn(List.of(buildUserDTO()));
        assertText(userService.getByIds(List.of(UnitTestData.UID)),
                   """
                           [ {
                             "audit" : {
                               "createdBy" : "system",
                               "createdDate" : "2023-05-27T12:00:00",
                               "lastModifiedBy" : "user-1",
                               "lastModifiedDate" : "2023-06-01T12:00:00",
                               "version" : 1
                             },
                             "email" : "john.smith@mock.org",
                             "firstName" : "John",
                             "lastName" : "Smith",
                             "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                           } ]
                           """);
    }

    @Test
    void getByIds_withoutData() {
        assertThat(userService.getByIds(null)).isEmpty();
        assertThat(userService.getByIds(List.of())).isEmpty();
    }

    //==================================================================================================================
    // UPDATE
    //==================================================================================================================
    @Test
    void update_force_nominal() {
        when(userDao.contains(List.of(UnitTestData.UID))).thenReturn(true);
        when(userDao.getByIds(List.of(UnitTestData.UID))).thenReturn(List.of(buildUserDTO()));
        when(userDao.update(any())).thenAnswer(answer -> answer.getArgument(0));

        assertText(userService.update(List.of(UserDTO.builder()
                                                     .uid(UnitTestData.UID)
                                                     .firstName(UnitTestData.USER_1.getFirstName())
                                                     .lastName(UnitTestData.USER_1.getLastName())
                                                     .email(UnitTestData.USER_1.getEmail())
                                                     .build()), true),
                   """
                           [ {
                             "audit" : {
                               "createdBy" : "system",
                               "createdDate" : "2023-05-27T12:00:00",
                               "lastModifiedBy" : "user-1",
                               "lastModifiedDate" : "2023-06-01T12:00:00",
                               "version" : 1
                             },
                             "email" : "emilie.lalonde@mock.org",
                             "firstName" : "Émilie",
                             "lastName" : "Lalonde",
                             "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                           } ]
                           """);
    }

    @Test
    void update_soft_nominal() {
        when(userDao.contains(List.of(UnitTestData.UID))).thenReturn(true);
        when(userDao.getByIds(List.of(UnitTestData.UID))).thenReturn(List.of(buildUserDTO()));
        when(userDao.update(any())).thenAnswer(answer -> answer.getArgument(0));

        assertText(userService.update(List.of(UserDTO.builder()
                                                     .uid(UnitTestData.UID)
                                                     .firstName(UnitTestData.USER_2.getFirstName())
                                                     .lastName(UnitTestData.USER_2.getLastName())
                                                     .email(UnitTestData.USER_2.getEmail())
                                                     .build()), false),
                   """
                           [ {
                             "audit" : {
                               "createdBy" : "system",
                               "createdDate" : "2023-05-27T12:00:00",
                               "lastModifiedBy" : "user-1",
                               "lastModifiedDate" : "2023-06-01T12:00:00",
                               "version" : 1
                             },
                             "email" : "jessamine.lalonde@mock.org",
                             "firstName" : "Jessamine",
                             "lastName" : "Lalonde",
                             "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                           } ]
                           """);
    }

    @Test
    void update_withError() {
        lenient().when(userDao.contains(List.of(UnitTestData.UID))).thenReturn(true);
        lenient().when(userDao.getByIds(List.of(UnitTestData.UID))).thenReturn(List.of(buildUserDTO()));

        assertThrows(UPDATE_INVALID_DATA, () -> userService.update(null, true));
        assertThrows(UPDATE_INVALID_DATA, () -> userService.update(List.of(), true));

        assertThrows(UPDATE_UID_REQUIRED, () -> userService.update(List.of(buildUserDTO().toBuilder()
                                                                                         .uid(UnitTestData.OTHER)
                                                                                         .build()), true));

        assertThrows(UPDATE_FIRSTNAME_INVALID, () -> userService.update(List.of(buildUserDTO().toBuilder()
                                                                                              .firstName(null)
                                                                                              .build()), true));

        assertThrows(UPDATE_LASTNAME_INVALID, () -> userService.update(List.of(buildUserDTO().toBuilder()
                                                                                             .lastName(null)
                                                                                             .build()), true));

        assertThrows(UPDATE_EMAIL_INVALID, () -> userService.update(List.of(buildUserDTO().toBuilder()
                                                                                          .email(null)
                                                                                          .build()), true));
    }

    //==================================================================================================================
    // DELETE
    //==================================================================================================================
    @Test
    void delete_nominal() {
        when(userDao.contains(List.of(UnitTestData.UID))).thenReturn(true);
        userService.delete(List.of(UnitTestData.UID));
        verify(userDao).delete(List.of(UnitTestData.UID));
    }

    @Test
    void delete_withError() {
        when(userDao.contains(any())).thenReturn(false);

        assertThrows(DELETE_USER_UID_REQUIRED, () -> userService.delete(null));
        assertThrows(DELETE_USER_UID_REQUIRED, () -> userService.delete(List.of()));

        assertThrows(DELETE_USERS_NOT_FOUND, () -> userService.delete(List.of("b97eb035-8c06-4602-ba44-0b642e460250")));


        verify(userDao, never()).delete(any());
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    private UserDTO buildUserDTO() {
        return UserDTO.builder()
                      .uid(UnitTestData.UID)
                      .firstName(UnitTestData.FIRSTNAME)
                      .lastName(UnitTestData.LASTNAME)
                      .email(UnitTestData.EMAIL)
                      .audit(AuditDTO.builder()
                                     .createdBy("system")
                                     .createdDate(UnitTestData.DATE_TIME.minusDays(5))
                                     .lastModifiedBy("user-1")
                                     .lastModifiedDate(UnitTestData.DATE_TIME)
                                     .version(1L)
                                     .build())
                      .build();
    }

}